package android.support.transition;

interface TransitionSetImpl {
   TransitionSetImpl addTransition(TransitionImpl var1);

   int getOrdering();

   TransitionSetImpl removeTransition(TransitionImpl var1);

   TransitionSetImpl setOrdering(int var1);
}
