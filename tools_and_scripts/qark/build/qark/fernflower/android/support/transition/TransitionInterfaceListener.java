package android.support.transition;

interface TransitionInterfaceListener {
   void onTransitionCancel(TransitionInterface var1);

   void onTransitionEnd(TransitionInterface var1);

   void onTransitionPause(TransitionInterface var1);

   void onTransitionResume(TransitionInterface var1);

   void onTransitionStart(TransitionInterface var1);
}
