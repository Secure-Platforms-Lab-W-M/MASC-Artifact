/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
#include "org_atalk_impl_neomedia_codec_audio_speex_Speex.h"

#include <speex/speex.h>
#include <speex/speex_resampler.h>
#include <stdint.h>
#include <stdlib.h>
#define debug(...) fprintf (stderr, __VA_ARGS__)


JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1destroy
    (JNIEnv *jniEnv, jclass clazz, jlong bits)
{
    SpeexBits *bitsPtr = (SpeexBits *) (intptr_t) bits;

    speex_bits_destroy(bitsPtr);
    free(bitsPtr);
}

JNIEXPORT jlong JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1init
    (JNIEnv *jniEnv, jclass clazz)
{
    SpeexBits *bits = malloc(sizeof(SpeexBits));

    if (bits)
        speex_bits_init(bits);
    return (jlong) (intptr_t) bits;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1nbytes
    (JNIEnv *jniEnv, jclass clazz, jlong bits)
{
    return speex_bits_nbytes((SpeexBits *) (intptr_t) bits);
}

JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1read_1from
    (JNIEnv *jniEnv, jclass clazz,
    jlong bits, jbyteArray bytes, jint bytesOffset, jint len)
{
    jbyte *bytesPtr = (*jniEnv)->GetPrimitiveArrayCritical(jniEnv, bytes, NULL);

    if (bytesPtr)
    {
        speex_bits_read_from(
            (SpeexBits *) (intptr_t) bits,
            (char *) (bytesPtr + bytesOffset),
            len);
        (*jniEnv)->ReleasePrimitiveArrayCritical(
                jniEnv,
                bytes,
                bytesPtr,
                JNI_ABORT);
    }
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1remaining
    (JNIEnv *jniEnv, jclass clazz, jlong bits)
{
    return speex_bits_remaining((SpeexBits *) (intptr_t) bits);
}

JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1reset
    (JNIEnv *jniEnv, jclass clazz, jlong bits)
{
    speex_bits_reset((SpeexBits *) (intptr_t) bits);
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1write
    (JNIEnv *jniEnv, jclass clazz,
    jlong bits, jbyteArray bytes, jint bytesOffset, jint max_len)
{
    jbyte *bytesPtr = (*jniEnv)->GetPrimitiveArrayCritical(jniEnv, bytes, NULL);
    jint ret;

    if (bytesPtr)
    {
        ret
            = speex_bits_write(
                (SpeexBits *) (intptr_t) bits,
                (char *) (bytesPtr + bytesOffset),
                max_len);
        (*jniEnv)->ReleasePrimitiveArrayCritical(jniEnv, bytes, bytesPtr, 0);
    }
    else
        ret = 0;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1decode_1int
    (JNIEnv *jniEnv, jclass clazz,
    jlong state, jlong bits, jbyteArray out, jint outOffset)
{
    jbyte *outPtr = (*jniEnv)->GetByteArrayElements(jniEnv, out, NULL);
    jint ret;

    if (outPtr)
    {
        ret
            = speex_decode_int(
                (void *) (intptr_t) state,
                (SpeexBits *) (intptr_t) bits,
                (spx_int16_t *) (outPtr + outOffset));
        (*jniEnv)->ReleaseByteArrayElements(jniEnv, out, outPtr, 0);
    }
    else
        ret = -2;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1ctl__JI
    (JNIEnv *jniEnv, jclass clazz, jlong state, jint request)
{
    int ret;
    int value = 0;

    ret = speex_decoder_ctl((void *) (intptr_t) state, request, &value);
    if (ret == 0)
        ret = value;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1ctl__JII
    (JNIEnv *jniEnv, jclass clazz, jlong state, jint request, jint value)
{
    return speex_decoder_ctl((void *) (intptr_t) state, request, &value);
}

JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1destroy
    (JNIEnv *jniEnv, jclass clazz, jlong state)
{
    speex_decoder_destroy((void *) (intptr_t) state);
}

JNIEXPORT jlong JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1init
    (JNIEnv *jniEnv, jclass clazz, jlong mode)
{
    return (jlong)  (intptr_t) speex_decoder_init((SpeexMode *) (intptr_t) mode);
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1encode_1int
    (JNIEnv *jniEnv, jclass clazz,
    jlong state, jbyteArray in, jint inOffset, jlong bits)
{
    jbyte *inPtr = (*jniEnv)->GetByteArrayElements(jniEnv, in, NULL);
    jint ret;

    if (inPtr)
    {
        ret
            = speex_encode_int(
                (void *) (intptr_t) state,
                (spx_int16_t *) (inPtr + inOffset),
                (SpeexBits *) (intptr_t) bits);
        (*jniEnv)->ReleaseByteArrayElements(jniEnv, in, inPtr, JNI_ABORT);
    }
    else
        ret = 0;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1ctl__JI
    (JNIEnv *jniEnv, jclass clazz, jlong state, jint request)
{
    int ret;
    int value = 0;

    ret = speex_encoder_ctl((void *) (intptr_t) state, request, &value);
    if (ret == 0)
        ret = value;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1ctl__JII
    (JNIEnv *jniEnv, jclass clazz, jlong state, jint request, jint value)
{
    return speex_encoder_ctl((void *) (intptr_t) state, request, &value);
}

JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1destroy
    (JNIEnv *jniEnv, jclass clazz, jlong state)
{
    speex_encoder_destroy((void *) (intptr_t) state);
}

JNIEXPORT jlong JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1init
    (JNIEnv *jniEnv, jclass clazz, jlong mode)
{
    return (jlong) (intptr_t) speex_encoder_init((SpeexMode *) (intptr_t) mode);
}

JNIEXPORT jlong JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1lib_1get_1mode
    (JNIEnv *jniEnv, jclass clazz, jint mode)
{
    return (jlong) (intptr_t) speex_lib_get_mode(mode);
}

JNIEXPORT void JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1destroy
    (JNIEnv *jniENv, jclass clazz, jlong state)
{
    speex_resampler_destroy((SpeexResamplerState *) (intptr_t) state);
}

JNIEXPORT jlong JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1init
    (JNIEnv *jniEnv, jclass clazz,
    jint nb_channels, jint in_rate, jint out_rate, jint quality, jlong err)
{
    return
        (jlong)
        (intptr_t)
            speex_resampler_init(
                nb_channels,
                in_rate, out_rate,
                quality,
                (int *) (intptr_t) err);
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1process_1interleaved_1int
    (JNIEnv *jniEnv, jclass clazz,
    jlong state,
    jbyteArray in, jint inOffset, jint in_len,
    jbyteArray out, jint outOffset, jint out_len)
{
    jbyte *inPtr = (*jniEnv)->GetPrimitiveArrayCritical(jniEnv, in, NULL);
    jint ret;

    if (inPtr)
    {
        jbyte *outPtr = (*jniEnv)->GetPrimitiveArrayCritical(jniEnv, out, NULL);

        if (outPtr)
        {
            spx_uint32_t _in_len = in_len;
            spx_uint32_t _out_len = out_len;

            ret
                = speex_resampler_process_interleaved_int(
                    (SpeexResamplerState *) (intptr_t) state,
                    (spx_int16_t *) (inPtr + inOffset),
                    &_in_len,
                    (spx_int16_t *) (outPtr + outOffset),
                    &_out_len);
            (*jniEnv)->ReleasePrimitiveArrayCritical(jniEnv, out, outPtr, 0);

            /*
             * speex_resampler_process_interleaved_int is supposed to return the
             * number of samples which have been written but it doesn't seem to
             * do it and instead returns zero.
             */
            ret = _out_len;
        }
        else
            ret = 0;
        (*jniEnv)->ReleasePrimitiveArrayCritical(jniEnv, in, inPtr, JNI_ABORT);
    }
    else
        ret = 0;
    return ret;
}

JNIEXPORT jint JNICALL
Java_org_atalk_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1set_1rate
    (JNIEnv *jniEnv, jclass clazz, jlong state, jint in_rate, jint out_rate)
{
    return
        speex_resampler_set_rate(
            (SpeexResamplerState *) (intptr_t) state,
            in_rate, out_rate);
}
