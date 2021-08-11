/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.silk;

import static org.atalk.impl.neomedia.codec.audio.silk.Define.*;

/**
 *
 * @author Jing Dai
 * @author Dingxin Xu
 *
 */
public class TablesOtherFLP
{
	static float[] SKP_Silk_HarmShapeFIR_FLP = { 16384.0f / 65536.0f, 32767.0f / 65536.0f,
		16384.0f / 65536.0f };

	float[][] SKP_Silk_Quantization_Offsets = {
		{ OFFSET_VL_Q10 / 1024.0f, OFFSET_VH_Q10 / 1024.0f },
		{ OFFSET_UVL_Q10 / 1024.0f, OFFSET_UVH_Q10 / 1024.0f } };
}
