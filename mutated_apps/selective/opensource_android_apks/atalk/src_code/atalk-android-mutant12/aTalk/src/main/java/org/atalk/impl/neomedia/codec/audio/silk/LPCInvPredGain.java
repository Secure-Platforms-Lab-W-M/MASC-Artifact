/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.silk;

import static org.atalk.impl.neomedia.codec.audio.silk.Macros.*;
import static org.atalk.impl.neomedia.codec.audio.silk.Typedef.*;

/**
 * Compute inverse of LPC prediction gain, and test if LPC coefficients are stable (all poles within
 * unit circle)
 *
 * @author Jing Dai
 * @author Dingxin Xu
 */
public class LPCInvPredGain
{
	static final int QA = 16;
	static final int A_LIMIT = 65520;

	/**
	 * Compute inverse of LPC prediction gain, and test if LPC coefficients are stable (all poles
	 * within unit circle).
	 * 
	 * @param invGain_Q30
	 *        Inverse prediction gain, Q30 energy domain
	 * @param A_Q12
	 *        Prediction coefficients, Q12 [order]
	 * @param order
	 *        Prediction order
	 * @return Returns 1 if unstable, otherwise 0
	 */
	static int SKP_Silk_LPC_inverse_pred_gain( /* O: Returns 1 if unstable, otherwise 0 */
	int[] invGain_Q30, /* O: Inverse prediction gain, Q30 energy domain */
		short[] A_Q12, /* I: Prediction coefficients, Q12 [order] */
		final int order /* I: Prediction order */
	)
	{
		int k, n, headrm;
		int rc_Q31, rc_mult1_Q30, rc_mult2_Q16;
		int[][] Atmp_QA = new int[2][SigProcFIX.SKP_Silk_MAX_ORDER_LPC];
		int tmp_QA;
		int[] Aold_QA, Anew_QA;

		Anew_QA = Atmp_QA[order & 1];
		/* Increase Q domain of the AR coefficients */
		for (k = 0; k < order; k++) {
			Anew_QA[k] = (A_Q12[k] << (QA - 12));
		}

		invGain_Q30[0] = (1 << 30);

		for (k = order - 1; k > 0; k--) {
			/* Check for stability */
			if ((Anew_QA[k] > A_LIMIT) || (Anew_QA[k] < -A_LIMIT)) {
				return 1;
			}

			/* Set RC equal to negated AR coef */
			rc_Q31 = -(Anew_QA[k] << (31 - QA));

			/* rc_mult1_Q30 range: [ 1 : 2^30-1 ] */
			rc_mult1_Q30 = (SKP_int32_MAX >> 1) - SigProcFIX.SKP_SMMUL(rc_Q31, rc_Q31);
			SKP_assert(rc_mult1_Q30 > (1 << 15)); /* reduce A_LIMIT if fails */
			SKP_assert(rc_mult1_Q30 < (1 << 30));

			/* rc_mult2_Q16 range: [ 2^16 : SKP_int32_MAX ] */
			rc_mult2_Q16 = Inlines.SKP_INVERSE32_varQ(rc_mult1_Q30, 46); /* 16 = 46 - 30 */

			/* Update inverse gain */
			/* invGain_Q30 range: [ 0 : 2^30 ] */
			invGain_Q30[0] = (SigProcFIX.SKP_SMMUL(invGain_Q30[0], rc_mult1_Q30) << 2);

			SKP_assert(invGain_Q30[0] >= 0);
			SKP_assert(invGain_Q30[0] <= (1 << 30));

			/* Swap pointers */
			Aold_QA = Anew_QA;
			Anew_QA = Atmp_QA[k & 1];

			/* Update AR coefficient */
			headrm = SKP_Silk_CLZ32(rc_mult2_Q16) - 1;
			rc_mult2_Q16 = (rc_mult2_Q16 << headrm); /* Q: 16 + headrm */
			for (n = 0; n < k; n++) {
				tmp_QA = Aold_QA[n] - (SigProcFIX.SKP_SMMUL(Aold_QA[k - n - 1], rc_Q31) << 1);
				Anew_QA[n] = (SigProcFIX.SKP_SMMUL(tmp_QA, rc_mult2_Q16) << (16 - headrm));
			}
		}

		/* Check for stability */
		if ((Anew_QA[0] > A_LIMIT) || (Anew_QA[0] < -A_LIMIT)) {
			return 1;
		}

		/* Set RC equal to negated AR coef */
		rc_Q31 = -(Anew_QA[0] << (31 - QA));

		/* Range: [ 1 : 2^30 ] */
		rc_mult1_Q30 = (SKP_int32_MAX >> 1) - SigProcFIX.SKP_SMMUL(rc_Q31, rc_Q31);

		/* Update inverse gain */
		/* Range: [ 0 : 2^30 ] */
		invGain_Q30[0] = (SigProcFIX.SKP_SMMUL(invGain_Q30[0], rc_mult1_Q30) << 2);
		SKP_assert(invGain_Q30[0] >= 0);
		SKP_assert(invGain_Q30[0] <= 1 << 30);

		return 0;
	}

	/**
	 * For input in Q13 domain.
	 * 
	 * @param invGain_Q30
	 *        Inverse prediction gain, Q30 energy domain.
	 * @param A_Q13
	 *        Prediction coefficients, Q13 [order].
	 * @param order
	 *        Prediction order.
	 * @return Returns 1 if unstable, otherwise 0.
	 */
	static int SKP_Silk_LPC_inverse_pred_gain_Q13( /* O: Returns 1 if unstable, otherwise 0 */
	int[] invGain_Q30, /* O: Inverse prediction gain, Q30 energy domain */
		short[] A_Q13, /* I: Prediction coefficients, Q13 [order] */
		final int order /* I: Prediction order */
	)
	{
		int k, n, headrm;
		int rc_Q31, rc_mult1_Q30, rc_mult2_Q16;
		int[][] Atmp_QA = new int[2][SigProcFIX.SKP_Silk_MAX_ORDER_LPC];
		int tmp_QA;

		int[] Aold_QA, Anew_QA;

		Anew_QA = Atmp_QA[order & 1];
		/* Increase Q domain of the AR coefficients */
		for (k = 0; k < order; k++) {
			Anew_QA[k] = (A_Q13[k] << (QA - 13));
		}

		invGain_Q30[0] = (1 << 30);
		for (k = order - 1; k > 0; k--) {
			/* Check for stability */
			if ((Anew_QA[k] > A_LIMIT) || (Anew_QA[k] < -A_LIMIT)) {
				return 1;
			}

			/* Set RC equal to negated AR coef */
			rc_Q31 = -(Anew_QA[k] << (31 - QA));

			/* rc_mult1_Q30 range: [ 1 : 2^30-1 ] */
			rc_mult1_Q30 = (SKP_int32_MAX >> 1) - SigProcFIX.SKP_SMMUL(rc_Q31, rc_Q31);
			assert (rc_mult1_Q30 > (1 << 15)); /* reduce A_LIMIT if fails */
			assert (rc_mult1_Q30 < (1 << 30));

			/* rc_mult2_Q16 range: [ 2^16 : SKP_int32_MAX ] */
			rc_mult2_Q16 = Inlines.SKP_INVERSE32_varQ(rc_mult1_Q30, 46); /* 16 = 46 - 30 */

			/* Update inverse gain */
			/* invGain_Q30 range: [ 0 : 2^30 ] */
			invGain_Q30[0] = (SigProcFIX.SKP_SMMUL(invGain_Q30[0], rc_mult1_Q30) << 2);
			SKP_assert(invGain_Q30[0] >= 0);
			SKP_assert(invGain_Q30[0] <= 1 << 30);

			/* Swap pointers */
			Aold_QA = Anew_QA;
			Anew_QA = Atmp_QA[k & 1];

			/* Update AR coefficient */
			headrm = SKP_Silk_CLZ32(rc_mult2_Q16) - 1;
			rc_mult2_Q16 = (rc_mult2_Q16 << headrm); /* Q: 16 + headrm */
			for (n = 0; n < k; n++) {
				tmp_QA = Aold_QA[n] - (SigProcFIX.SKP_SMMUL(Aold_QA[k - n - 1], rc_Q31) << 1);
				Anew_QA[n] = (SigProcFIX.SKP_SMMUL(tmp_QA, rc_mult2_Q16) << (16 - headrm));
			}
		}

		/* Check for stability */
		if ((Anew_QA[0] > A_LIMIT) || (Anew_QA[0] < -A_LIMIT)) {
			return 1;
		}

		/* Set RC equal to negated AR coef */
		rc_Q31 = -(Anew_QA[0] << (31 - QA));

		/* Range: [ 1 : 2^30 ] */
		rc_mult1_Q30 = (SKP_int32_MAX >> 1) - SigProcFIX.SKP_SMMUL(rc_Q31, rc_Q31);

		/* Update inverse gain */
		/* Range: [ 0 : 2^30 ] */
		invGain_Q30[0] = (SigProcFIX.SKP_SMMUL(invGain_Q30[0], rc_mult1_Q30) << 2);
		SKP_assert(invGain_Q30[0] >= 0);
		SKP_assert(invGain_Q30[0] <= 1 << 30);

		return 0;
	}
}
