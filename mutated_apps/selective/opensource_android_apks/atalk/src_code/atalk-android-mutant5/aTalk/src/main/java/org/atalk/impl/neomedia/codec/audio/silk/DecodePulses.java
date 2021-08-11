/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.silk;

import static org.atalk.impl.neomedia.codec.audio.silk.Define.*;
import static org.atalk.impl.neomedia.codec.audio.silk.Macros.*;

import java.util.*;

/**
 * Decode quantization indices of excitation.
 *
 * @author Jing Dai
 * @author Dingxin Xu
 */
public class DecodePulses
{
	/**
	 * Decode quantization indices of excitation.
	 * 
	 * @param psRC
	 *        Range coder state.
	 * @param psDecCtrl
	 *        Decoder control.
	 * @param q
	 *        Excitation signal.
	 * @param frame_length
	 *        Frame length (preliminary).
	 */
	static void SKP_Silk_decode_pulses(SKP_Silk_range_coder_state psRC, /* I/O Range coder state */
		SKP_Silk_decoder_control psDecCtrl, /* I/O Decoder control */
		int q[], /* O Excitation signal */
		final int frame_length /* I Frame length (preliminary) */
	)
	{
		int i, j, k, iter, abs_q, nLS, bit;
		int[] sum_pulses = new int[Define.MAX_NB_SHELL_BLOCKS];
		int[] nLshifts = new int[Define.MAX_NB_SHELL_BLOCKS];
		int[] pulses_ptr;
		int pulses_ptr_offset;
		int[] cdf_ptr;

		/*********************/
		/* Decode rate level */
		/*********************/
		int RateLevelIndex_ptr[] = new int[1];
		RateLevelIndex_ptr[0] = psDecCtrl.RateLevelIndex;
		RangeCoder.SKP_Silk_range_decoder(RateLevelIndex_ptr, 0, psRC,
			TablesPulsesPerBlock.SKP_Silk_rate_levels_CDF[psDecCtrl.sigtype], 0,
			TablesPulsesPerBlock.SKP_Silk_rate_levels_CDF_offset);
		psDecCtrl.RateLevelIndex = RateLevelIndex_ptr[0];

		/* Calculate number of shell blocks */
		iter = frame_length / Define.SHELL_CODEC_FRAME_LENGTH;

		/***************************************************/
		/* Sum-Weighted-Pulses Decoding */
		/***************************************************/
		cdf_ptr = TablesPulsesPerBlock.SKP_Silk_pulses_per_block_CDF[psDecCtrl.RateLevelIndex];

		for (i = 0; i < iter; i++) {
			nLshifts[i] = 0;
			RangeCoder.SKP_Silk_range_decoder(sum_pulses, i, psRC, cdf_ptr, 0,
				TablesPulsesPerBlock.SKP_Silk_pulses_per_block_CDF_offset);

			/* LSB indication */
			while (sum_pulses[i] == (Define.MAX_PULSES + 1)) {
				nLshifts[i]++;
				RangeCoder.SKP_Silk_range_decoder(sum_pulses, i, psRC,
					TablesPulsesPerBlock.SKP_Silk_pulses_per_block_CDF[Define.N_RATE_LEVELS - 1], 0,
					TablesPulsesPerBlock.SKP_Silk_pulses_per_block_CDF_offset);
			}
		}

		/***************************************************/
		/* Shell decoding */
		/***************************************************/
		for (i = 0; i < iter; i++) {
			if (sum_pulses[i] > 0) {
				ShellCoder.SKP_Silk_shell_decoder(q, SKP_SMULBB(i, Define.SHELL_CODEC_FRAME_LENGTH), psRC,
					sum_pulses[i]);
			}
			else {
				Arrays.fill(q, (SKP_SMULBB(i, Define.SHELL_CODEC_FRAME_LENGTH)),
					((SKP_SMULBB(i, Define.SHELL_CODEC_FRAME_LENGTH)) + Define.SHELL_CODEC_FRAME_LENGTH), 0);
			}
		}

		/***************************************************/
		/* LSB Decoding */
		/***************************************************/
		for (i = 0; i < iter; i++) {
			if (nLshifts[i] > 0) {
				nLS = nLshifts[i];
				pulses_ptr = q;
				pulses_ptr_offset = SKP_SMULBB(i, Define.SHELL_CODEC_FRAME_LENGTH);

				for (k = 0; k < Define.SHELL_CODEC_FRAME_LENGTH; k++) {
					abs_q = pulses_ptr[pulses_ptr_offset + k];
					for (j = 0; j < nLS; j++) {
						abs_q = abs_q << 1;
						int bit_ptr[] = new int[1];
						RangeCoder.SKP_Silk_range_decoder(bit_ptr, 0, psRC,
							TablesOther.SKP_Silk_lsb_CDF, 0, 1);
						bit = bit_ptr[0];
						abs_q += bit;
					}
					pulses_ptr[pulses_ptr_offset + k] = abs_q;
				}
			}
		}

		/****************************************/
		/* Decode and add signs to pulse signal */
		/****************************************/
		CodeSigns.SKP_Silk_decode_signs(psRC, q, frame_length, psDecCtrl.sigtype,
			psDecCtrl.QuantOffsetType, psDecCtrl.RateLevelIndex);
	}
}
