/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.silk;

import static org.atalk.impl.neomedia.codec.audio.silk.Define.*;
import static org.atalk.impl.neomedia.codec.audio.silk.Macros.*;

/**
 * Convert NLSF parameters to stable AR prediction filter coefficients.
 *
 * @author Jing Dai
 * @author Dingxin Xu
 */
public class NLSF2AStable
{
	/**
	 * Convert NLSF parameters to stable AR prediction filter coefficients.
	 * 
	 * @param pAR_Q12
	 *        Stabilized AR coefs [LPC_order].
	 * @param pNLSF
	 *        NLSF vector [LPC_order].
	 * @param LPC_order
	 *        LPC/LSF order.
	 */
	static void SKP_Silk_NLSF2A_stable(short pAR_Q12[], /* O Stabilized AR coefs [LPC_order] */
		int pNLSF[], /* I NLSF vector [LPC_order] */
		final int LPC_order /* I LPC/LSF order */
	)
	{
		int i;
		int invGain_Q30;
		int invGain_Q30_ptr[] = new int[1];
		NLSF2A.SKP_Silk_NLSF2A(pAR_Q12, pNLSF, LPC_order);

		/* Ensure stable LPCs */
		for (i = 0; i < MAX_LPC_STABILIZE_ITERATIONS; i++) {
			if (LPCInvPredGain.SKP_Silk_LPC_inverse_pred_gain(invGain_Q30_ptr, pAR_Q12, LPC_order) == 1) {
				invGain_Q30 = invGain_Q30_ptr[0];
				Bwexpander.SKP_Silk_bwexpander(pAR_Q12, LPC_order, 65536 - SKP_SMULBB(66, i)); /*
																								 * 66_Q16
																								 * =
																								 * 0.001
																								 */
			}
			else {
				invGain_Q30 = invGain_Q30_ptr[0];
				break;
			}
		}

		/* Reached the last iteration */
		if (i == MAX_LPC_STABILIZE_ITERATIONS) {
			for (i = 0; i < LPC_order; i++) {
				pAR_Q12[i] = 0;
			}
		}
	}
}
