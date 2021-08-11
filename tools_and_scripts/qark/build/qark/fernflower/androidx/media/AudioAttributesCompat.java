package androidx.media;

import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AudioAttributesCompat implements VersionedParcelable {
   static final String AUDIO_ATTRIBUTES_CONTENT_TYPE = "androidx.media.audio_attrs.CONTENT_TYPE";
   static final String AUDIO_ATTRIBUTES_FLAGS = "androidx.media.audio_attrs.FLAGS";
   static final String AUDIO_ATTRIBUTES_FRAMEWORKS = "androidx.media.audio_attrs.FRAMEWORKS";
   static final String AUDIO_ATTRIBUTES_LEGACY_STREAM_TYPE = "androidx.media.audio_attrs.LEGACY_STREAM_TYPE";
   static final String AUDIO_ATTRIBUTES_USAGE = "androidx.media.audio_attrs.USAGE";
   public static final int CONTENT_TYPE_MOVIE = 3;
   public static final int CONTENT_TYPE_MUSIC = 2;
   public static final int CONTENT_TYPE_SONIFICATION = 4;
   public static final int CONTENT_TYPE_SPEECH = 1;
   public static final int CONTENT_TYPE_UNKNOWN = 0;
   static final int FLAG_ALL = 1023;
   static final int FLAG_ALL_PUBLIC = 273;
   public static final int FLAG_AUDIBILITY_ENFORCED = 1;
   static final int FLAG_BEACON = 8;
   static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
   static final int FLAG_BYPASS_MUTE = 128;
   static final int FLAG_DEEP_BUFFER = 512;
   public static final int FLAG_HW_AV_SYNC = 16;
   static final int FLAG_HW_HOTWORD = 32;
   static final int FLAG_LOW_LATENCY = 256;
   static final int FLAG_SCO = 4;
   static final int FLAG_SECURE = 2;
   static final int INVALID_STREAM_TYPE = -1;
   private static final int[] SDK_USAGES;
   private static final int SUPPRESSIBLE_CALL = 2;
   private static final int SUPPRESSIBLE_NOTIFICATION = 1;
   private static final SparseIntArray SUPPRESSIBLE_USAGES;
   private static final String TAG = "AudioAttributesCompat";
   public static final int USAGE_ALARM = 4;
   public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
   public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
   public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
   public static final int USAGE_ASSISTANT = 16;
   public static final int USAGE_GAME = 14;
   public static final int USAGE_MEDIA = 1;
   public static final int USAGE_NOTIFICATION = 5;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
   public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
   public static final int USAGE_NOTIFICATION_EVENT = 10;
   public static final int USAGE_NOTIFICATION_RINGTONE = 6;
   public static final int USAGE_UNKNOWN = 0;
   private static final int USAGE_VIRTUAL_SOURCE = 15;
   public static final int USAGE_VOICE_COMMUNICATION = 2;
   public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
   static boolean sForceLegacyBehavior;
   AudioAttributesImpl mImpl;

   static {
      SparseIntArray var0 = new SparseIntArray();
      SUPPRESSIBLE_USAGES = var0;
      var0.put(5, 1);
      SUPPRESSIBLE_USAGES.put(6, 2);
      SUPPRESSIBLE_USAGES.put(7, 2);
      SUPPRESSIBLE_USAGES.put(8, 1);
      SUPPRESSIBLE_USAGES.put(9, 1);
      SUPPRESSIBLE_USAGES.put(10, 1);
      SDK_USAGES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
   }

   AudioAttributesCompat() {
   }

   AudioAttributesCompat(AudioAttributesImpl var1) {
      this.mImpl = var1;
   }

   public static AudioAttributesCompat fromBundle(Bundle var0) {
      AudioAttributesImpl var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = AudioAttributesImplApi21.fromBundle(var0);
      } else {
         var1 = AudioAttributesImplBase.fromBundle(var0);
      }

      return var1 == null ? null : new AudioAttributesCompat(var1);
   }

   public static void setForceLegacyBehavior(boolean var0) {
      sForceLegacyBehavior = var0;
   }

   static int toVolumeStreamType(boolean var0, int var1, int var2) {
      if ((var1 & 1) == 1) {
         return var0 ? 1 : 7;
      } else if ((var1 & 4) == 4) {
         return var0 ? 0 : 6;
      } else {
         var1 = 3;
         switch(var2) {
         case 0:
            if (var0) {
               var1 = Integer.MIN_VALUE;
            }

            return var1;
         case 1:
         case 12:
         case 14:
         case 16:
            return 3;
         case 2:
            return 0;
         case 3:
            if (var0) {
               return 0;
            }

            return 8;
         case 4:
            return 4;
         case 5:
         case 7:
         case 8:
         case 9:
         case 10:
            return 5;
         case 6:
            return 2;
         case 11:
            return 10;
         case 13:
            return 1;
         case 15:
         default:
            if (!var0) {
               return 3;
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown usage value ");
               var3.append(var2);
               var3.append(" in audio attributes");
               throw new IllegalArgumentException(var3.toString());
            }
         }
      }
   }

   static int toVolumeStreamType(boolean var0, AudioAttributesCompat var1) {
      return toVolumeStreamType(var0, var1.getFlags(), var1.getUsage());
   }

   static int usageForStreamType(int var0) {
      switch(var0) {
      case 0:
         return 2;
      case 1:
      case 7:
         return 13;
      case 2:
         return 6;
      case 3:
         return 1;
      case 4:
         return 4;
      case 5:
         return 5;
      case 6:
         return 2;
      case 8:
         return 3;
      case 9:
      default:
         return 0;
      case 10:
         return 11;
      }
   }

   static String usageToString(int var0) {
      switch(var0) {
      case 0:
         return "USAGE_UNKNOWN";
      case 1:
         return "USAGE_MEDIA";
      case 2:
         return "USAGE_VOICE_COMMUNICATION";
      case 3:
         return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
      case 4:
         return "USAGE_ALARM";
      case 5:
         return "USAGE_NOTIFICATION";
      case 6:
         return "USAGE_NOTIFICATION_RINGTONE";
      case 7:
         return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
      case 8:
         return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
      case 9:
         return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
      case 10:
         return "USAGE_NOTIFICATION_EVENT";
      case 11:
         return "USAGE_ASSISTANCE_ACCESSIBILITY";
      case 12:
         return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
      case 13:
         return "USAGE_ASSISTANCE_SONIFICATION";
      case 14:
         return "USAGE_GAME";
      case 15:
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("unknown usage ");
         var1.append(var0);
         return var1.toString();
      case 16:
         return "USAGE_ASSISTANT";
      }
   }

   public static AudioAttributesCompat wrap(Object var0) {
      if (VERSION.SDK_INT >= 21 && !sForceLegacyBehavior) {
         AudioAttributesImplApi21 var2 = new AudioAttributesImplApi21((AudioAttributes)var0);
         AudioAttributesCompat var1 = new AudioAttributesCompat();
         var1.mImpl = var2;
         return var1;
      } else {
         return null;
      }
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof AudioAttributesCompat;
      boolean var2 = false;
      if (!var3) {
         return false;
      } else {
         AudioAttributesCompat var5 = (AudioAttributesCompat)var1;
         AudioAttributesImpl var4 = this.mImpl;
         if (var4 == null) {
            if (var5.mImpl == null) {
               var2 = true;
            }

            return var2;
         } else {
            return var4.equals(var5.mImpl);
         }
      }
   }

   public int getContentType() {
      return this.mImpl.getContentType();
   }

   public int getFlags() {
      return this.mImpl.getFlags();
   }

   public int getLegacyStreamType() {
      return this.mImpl.getLegacyStreamType();
   }

   int getRawLegacyStreamType() {
      return this.mImpl.getRawLegacyStreamType();
   }

   public int getUsage() {
      return this.mImpl.getUsage();
   }

   public int getVolumeControlStream() {
      return this.mImpl.getVolumeControlStream();
   }

   public int hashCode() {
      return this.mImpl.hashCode();
   }

   public Bundle toBundle() {
      return this.mImpl.toBundle();
   }

   public String toString() {
      return this.mImpl.toString();
   }

   public Object unwrap() {
      return this.mImpl.getAudioAttributes();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface AttributeContentType {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface AttributeUsage {
   }

   abstract static class AudioManagerHidden {
      public static final int STREAM_ACCESSIBILITY = 10;
      public static final int STREAM_BLUETOOTH_SCO = 6;
      public static final int STREAM_SYSTEM_ENFORCED = 7;
      public static final int STREAM_TTS = 9;

      private AudioManagerHidden() {
      }
   }

   public static class Builder {
      private int mContentType = 0;
      private int mFlags = 0;
      private int mLegacyStream = -1;
      private int mUsage = 0;

      public Builder() {
      }

      public Builder(AudioAttributesCompat var1) {
         this.mUsage = var1.getUsage();
         this.mContentType = var1.getContentType();
         this.mFlags = var1.getFlags();
         this.mLegacyStream = var1.getRawLegacyStreamType();
      }

      public AudioAttributesCompat build() {
         Object var2;
         if (!AudioAttributesCompat.sForceLegacyBehavior && VERSION.SDK_INT >= 21) {
            android.media.AudioAttributes.Builder var3 = (new android.media.AudioAttributes.Builder()).setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
            int var1 = this.mLegacyStream;
            if (var1 != -1) {
               var3.setLegacyStreamType(var1);
            }

            var2 = new AudioAttributesImplApi21(var3.build(), this.mLegacyStream);
         } else {
            var2 = new AudioAttributesImplBase(this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream);
         }

         return new AudioAttributesCompat((AudioAttributesImpl)var2);
      }

      public AudioAttributesCompat.Builder setContentType(int var1) {
         if (var1 != 0 && var1 != 1 && var1 != 2 && var1 != 3 && var1 != 4) {
            this.mUsage = 0;
            return this;
         } else {
            this.mContentType = var1;
            return this;
         }
      }

      public AudioAttributesCompat.Builder setFlags(int var1) {
         this.mFlags |= var1 & 1023;
         return this;
      }

      AudioAttributesCompat.Builder setInternalLegacyStreamType(int var1) {
         switch(var1) {
         case 0:
            this.mContentType = 1;
            break;
         case 2:
            this.mContentType = 4;
            break;
         case 3:
            this.mContentType = 2;
            break;
         case 4:
            this.mContentType = 4;
            break;
         case 5:
            this.mContentType = 4;
            break;
         case 6:
            this.mContentType = 1;
            this.mFlags |= 4;
            break;
         case 7:
            this.mFlags |= 1;
         case 1:
            this.mContentType = 4;
            break;
         case 8:
            this.mContentType = 4;
            break;
         case 9:
            this.mContentType = 4;
            break;
         case 10:
            this.mContentType = 1;
            break;
         default:
            StringBuilder var2 = new StringBuilder();
            var2.append("Invalid stream type ");
            var2.append(var1);
            var2.append(" for AudioAttributesCompat");
            Log.e("AudioAttributesCompat", var2.toString());
         }

         this.mUsage = AudioAttributesCompat.usageForStreamType(var1);
         return this;
      }

      public AudioAttributesCompat.Builder setLegacyStreamType(int var1) {
         if (var1 != 10) {
            this.mLegacyStream = var1;
            return VERSION.SDK_INT >= 21 ? this.setInternalLegacyStreamType(var1) : this;
         } else {
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
         }
      }

      public AudioAttributesCompat.Builder setUsage(int var1) {
         switch(var1) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
            this.mUsage = var1;
            return this;
         case 16:
            if (!AudioAttributesCompat.sForceLegacyBehavior && VERSION.SDK_INT > 25) {
               this.mUsage = var1;
               return this;
            }

            this.mUsage = 12;
            return this;
         default:
            this.mUsage = 0;
            return this;
         }
      }
   }
}
