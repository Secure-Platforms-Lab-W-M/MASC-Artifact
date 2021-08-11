/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.silk;

import static org.atalk.impl.neomedia.codec.audio.silk.Macros.*;

/**
 * Convert input to a linear scale.
 *
 * @author Jing Dai
 * @author Dingxin Xu
 */
public class Log2lin
{
	/**
	 * Approximation of 2^() (very close inverse of Silk_lin2log.SKP_Silk_lin2log()) Convert input
	 * to a linear scale.
	 *
	 * @param inLog_Q7
	 *        Input on log scale
	 * @return
	 */
	static int SKP_Silk_log2lin(final int inLog_Q7) /* I: Input on log scale */
	{
		int out, frac_Q7;

		if (inLog_Q7 < 0) {
			return 0;
		}

		out = (1 << (inLog_Q7 >> 7));

		frac_Q7 = inLog_Q7 & 0x7F;
		if (inLog_Q7 < 2048) {
			/* Piece-wise parabolic approximation */
			out = SigProcFIX.SKP_ADD_RSHIFT(
				out,
				SigProcFIX.SKP_MUL(out,
					SKP_SMLAWB(frac_Q7, SigProcFIX.SKP_MUL(frac_Q7, 128 - frac_Q7), -174)), 7);
		}
		else {
			/* Piece-wise parabolic approximation */
			out = SigProcFIX.SKP_MLA(out, SigProcFIX.SKP_RSHIFT(out, 7),
				SKP_SMLAWB(frac_Q7, SigProcFIX.SKP_MUL(frac_Q7, 128 - frac_Q7), -174));
		}
		return out;
	}
}
