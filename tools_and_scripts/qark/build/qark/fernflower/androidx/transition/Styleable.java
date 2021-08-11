package androidx.transition;

class Styleable {
   static final int[] ARC_MOTION = new int[]{16843901, 16843902, 16843903};
   static final int[] CHANGE_BOUNDS = new int[]{16843983};
   static final int[] CHANGE_TRANSFORM = new int[]{16843964, 16843965};
   static final int[] FADE = new int[]{16843745};
   static final int[] PATTERN_PATH_MOTION = new int[]{16843978};
   static final int[] SLIDE = new int[]{16843824};
   static final int[] TRANSITION = new int[]{16843073, 16843160, 16843746, 16843855};
   static final int[] TRANSITION_MANAGER = new int[]{16843741, 16843742, 16843743};
   static final int[] TRANSITION_SET = new int[]{16843744};
   static final int[] TRANSITION_TARGET = new int[]{16842799, 16843740, 16843841, 16843842, 16843853, 16843854};
   static final int[] VISIBILITY_TRANSITION = new int[]{16843900};

   private Styleable() {
   }

   interface ArcMotion {
      int MAXIMUM_ANGLE = 2;
      int MINIMUM_HORIZONTAL_ANGLE = 0;
      int MINIMUM_VERTICAL_ANGLE = 1;
   }

   interface ChangeBounds {
      int RESIZE_CLIP = 0;
   }

   interface ChangeTransform {
      int REPARENT = 0;
      int REPARENT_WITH_OVERLAY = 1;
   }

   interface Fade {
      int FADING_MODE = 0;
   }

   interface PatternPathMotion {
      int PATTERN_PATH_DATA = 0;
   }

   interface Slide {
      int SLIDE_EDGE = 0;
   }

   interface Transition {
      int DURATION = 1;
      int INTERPOLATOR = 0;
      int MATCH_ORDER = 3;
      int START_DELAY = 2;
   }

   interface TransitionManager {
      int FROM_SCENE = 0;
      int TO_SCENE = 1;
      int TRANSITION = 2;
   }

   interface TransitionSet {
      int TRANSITION_ORDERING = 0;
   }

   interface TransitionTarget {
      int EXCLUDE_CLASS = 3;
      int EXCLUDE_ID = 2;
      int EXCLUDE_NAME = 5;
      int TARGET_CLASS = 0;
      int TARGET_ID = 1;
      int TARGET_NAME = 4;
   }

   interface VisibilityTransition {
      int TRANSITION_VISIBILITY_MODE = 0;
   }
}
