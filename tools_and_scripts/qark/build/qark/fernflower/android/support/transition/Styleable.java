package android.support.transition;

import android.annotation.SuppressLint;
import android.support.annotation.StyleableRes;

@SuppressLint({"InlinedApi"})
class Styleable {
   @StyleableRes
   static final int[] ARC_MOTION = new int[]{16843901, 16843902, 16843903};
   @StyleableRes
   static final int[] CHANGE_BOUNDS = new int[]{16843983};
   @StyleableRes
   static final int[] CHANGE_TRANSFORM = new int[]{16843964, 16843965};
   @StyleableRes
   static final int[] FADE = new int[]{16843745};
   @StyleableRes
   static final int[] PATTERN_PATH_MOTION = new int[]{16843978};
   @StyleableRes
   static final int[] SLIDE = new int[]{16843824};
   @StyleableRes
   static final int[] TRANSITION = new int[]{16843073, 16843160, 16843746, 16843855};
   @StyleableRes
   static final int[] TRANSITION_MANAGER = new int[]{16843741, 16843742, 16843743};
   @StyleableRes
   static final int[] TRANSITION_SET = new int[]{16843744};
   @StyleableRes
   static final int[] TRANSITION_TARGET = new int[]{16842799, 16843740, 16843841, 16843842, 16843853, 16843854};
   @StyleableRes
   static final int[] VISIBILITY_TRANSITION = new int[]{16843900};

   interface ArcMotion {
      @StyleableRes
      int MAXIMUM_ANGLE = 2;
      @StyleableRes
      int MINIMUM_HORIZONTAL_ANGLE = 0;
      @StyleableRes
      int MINIMUM_VERTICAL_ANGLE = 1;
   }

   interface ChangeBounds {
      @StyleableRes
      int RESIZE_CLIP = 0;
   }

   interface ChangeTransform {
      @StyleableRes
      int REPARENT = 0;
      @StyleableRes
      int REPARENT_WITH_OVERLAY = 1;
   }

   interface Fade {
      @StyleableRes
      int FADING_MODE = 0;
   }

   interface PatternPathMotion {
      @StyleableRes
      int PATTERN_PATH_DATA = 0;
   }

   interface Slide {
      @StyleableRes
      int SLIDE_EDGE = 0;
   }

   interface Transition {
      @StyleableRes
      int DURATION = 1;
      @StyleableRes
      int INTERPOLATOR = 0;
      @StyleableRes
      int MATCH_ORDER = 3;
      @StyleableRes
      int START_DELAY = 2;
   }

   interface TransitionManager {
      @StyleableRes
      int FROM_SCENE = 0;
      @StyleableRes
      int TO_SCENE = 1;
      @StyleableRes
      int TRANSITION = 2;
   }

   interface TransitionSet {
      @StyleableRes
      int TRANSITION_ORDERING = 0;
   }

   interface TransitionTarget {
      @StyleableRes
      int EXCLUDE_CLASS = 3;
      @StyleableRes
      int EXCLUDE_ID = 2;
      @StyleableRes
      int EXCLUDE_NAME = 5;
      @StyleableRes
      int TARGET_CLASS = 0;
      @StyleableRes
      int TARGET_ID = 1;
      @StyleableRes
      int TARGET_NAME = 4;
   }

   interface VisibilityTransition {
      @StyleableRes
      int TRANSITION_VISIBILITY_MODE = 0;
   }
}
