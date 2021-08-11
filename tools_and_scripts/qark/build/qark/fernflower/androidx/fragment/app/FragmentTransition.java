package androidx.fragment.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
   private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};
   private static final FragmentTransitionImpl PLATFORM_IMPL;
   private static final FragmentTransitionImpl SUPPORT_IMPL;

   static {
      FragmentTransitionCompat21 var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = new FragmentTransitionCompat21();
      } else {
         var0 = null;
      }

      PLATFORM_IMPL = var0;
      SUPPORT_IMPL = resolveSupportImpl();
   }

   private FragmentTransition() {
   }

   private static void addSharedElementsWithMatchingNames(ArrayList var0, ArrayMap var1, Collection var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         View var4 = (View)var1.valueAt(var3);
         if (var2.contains(ViewCompat.getTransitionName(var4))) {
            var0.add(var4);
         }
      }

   }

   private static void addToFirstInLastOut(BackStackRecord var0, FragmentTransaction.class_11 var1, SparseArray var2, boolean var3, boolean var4) {
      Fragment var18 = var1.mFragment;
      if (var18 != null) {
         int var13 = var18.mContainerId;
         if (var13 != 0) {
            int var5;
            if (var3) {
               var5 = INVERSE_OPS[var1.mCmd];
            } else {
               var5 = var1.mCmd;
            }

            boolean var6;
            boolean var7;
            boolean var14;
            boolean var20;
            label160: {
               boolean var11;
               boolean var12;
               label154: {
                  var14 = false;
                  boolean var8 = false;
                  var6 = false;
                  var7 = false;
                  boolean var9 = false;
                  boolean var10 = false;
                  var12 = false;
                  var11 = false;
                  boolean var16 = false;
                  boolean var15 = false;
                  if (var5 != 1) {
                     if (var5 == 3) {
                        break label154;
                     }

                     if (var5 == 4) {
                        if (var4) {
                           var20 = var9;
                           if (var18.mHiddenChanged) {
                              var20 = var9;
                              if (var18.mAdded) {
                                 var20 = var9;
                                 if (var18.mHidden) {
                                    var20 = true;
                                 }
                              }
                           }

                           var6 = var20;
                        } else {
                           var20 = var10;
                           if (var18.mAdded) {
                              var20 = var10;
                              if (!var18.mHidden) {
                                 var20 = true;
                              }
                           }

                           var6 = var20;
                        }

                        var20 = true;
                        break label160;
                     }

                     if (var5 == 5) {
                        if (var4) {
                           var14 = var15;
                           if (var18.mHiddenChanged) {
                              var14 = var15;
                              if (!var18.mHidden) {
                                 var14 = var15;
                                 if (var18.mAdded) {
                                    var14 = true;
                                 }
                              }
                           }
                        } else {
                           var14 = var18.mHidden;
                        }

                        var7 = true;
                        var20 = var8;
                        break label160;
                     }

                     if (var5 == 6) {
                        break label154;
                     }

                     if (var5 != 7) {
                        var20 = var8;
                        break label160;
                     }
                  }

                  if (var4) {
                     var14 = var18.mIsNewlyAdded;
                  } else {
                     var14 = var16;
                     if (!var18.mAdded) {
                        var14 = var16;
                        if (!var18.mHidden) {
                           var14 = true;
                        }
                     }
                  }

                  var7 = true;
                  var20 = var8;
                  break label160;
               }

               if (!var4) {
                  var20 = var11;
                  if (var18.mAdded) {
                     var20 = var11;
                     if (!var18.mHidden) {
                        var20 = true;
                     }
                  }

                  var6 = var20;
               } else {
                  if (!var18.mAdded && var18.mView != null && var18.mView.getVisibility() == 0 && var18.mPostponedAlpha >= 0.0F) {
                     var20 = true;
                  } else {
                     var20 = var12;
                  }

                  var6 = var20;
               }

               var20 = true;
            }

            FragmentTransition.FragmentContainerTransition var17 = (FragmentTransition.FragmentContainerTransition)var2.get(var13);
            FragmentTransition.FragmentContainerTransition var19 = var17;
            if (var14) {
               var19 = ensureContainer(var17, var2, var13);
               var19.lastIn = var18;
               var19.lastInIsPop = var3;
               var19.lastInTransaction = var0;
            }

            if (!var4 && var7) {
               if (var19 != null && var19.firstOut == var18) {
                  var19.firstOut = null;
               }

               FragmentManager var21 = var0.mManager;
               if (var18.mState < 1 && var21.mCurState >= 1 && !var0.mReorderingAllowed) {
                  var21.makeActive(var18);
                  var21.moveToState(var18, 1);
               }
            }

            var17 = var19;
            if (var6) {
               label116: {
                  if (var19 != null) {
                     var17 = var19;
                     if (var19.firstOut != null) {
                        break label116;
                     }
                  }

                  var17 = ensureContainer(var19, var2, var13);
                  var17.firstOut = var18;
                  var17.firstOutIsPop = var3;
                  var17.firstOutTransaction = var0;
               }
            }

            if (!var4 && var20 && var17 != null && var17.lastIn == var18) {
               var17.lastIn = null;
            }

         }
      }
   }

   public static void calculateFragments(BackStackRecord var0, SparseArray var1, boolean var2) {
      int var4 = var0.mOps.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         addToFirstInLastOut(var0, (FragmentTransaction.class_11)var0.mOps.get(var3), var1, false, var2);
      }

   }

   private static ArrayMap calculateNameOverrides(int var0, ArrayList var1, ArrayList var2, int var3, int var4) {
      ArrayMap var10 = new ArrayMap();
      --var4;

      for(; var4 >= var3; --var4) {
         BackStackRecord var11 = (BackStackRecord)var1.get(var4);
         if (var11.interactsWith(var0)) {
            boolean var7 = (Boolean)var2.get(var4);
            if (var11.mSharedElementSourceNames != null) {
               int var6 = var11.mSharedElementSourceNames.size();
               ArrayList var8;
               ArrayList var9;
               if (var7) {
                  var8 = var11.mSharedElementSourceNames;
                  var9 = var11.mSharedElementTargetNames;
               } else {
                  var9 = var11.mSharedElementSourceNames;
                  var8 = var11.mSharedElementTargetNames;
               }

               for(int var5 = 0; var5 < var6; ++var5) {
                  String var14 = (String)var9.get(var5);
                  String var12 = (String)var8.get(var5);
                  String var13 = (String)var10.remove(var12);
                  if (var13 != null) {
                     var10.put(var14, var13);
                  } else {
                     var10.put(var14, var12);
                  }
               }
            }
         }
      }

      return var10;
   }

   public static void calculatePopFragments(BackStackRecord var0, SparseArray var1, boolean var2) {
      if (var0.mManager.mContainer.onHasView()) {
         for(int var3 = var0.mOps.size() - 1; var3 >= 0; --var3) {
            addToFirstInLastOut(var0, (FragmentTransaction.class_11)var0.mOps.get(var3), var1, true, var2);
         }

      }
   }

   static void callSharedElementStartEnd(Fragment var0, Fragment var1, boolean var2, ArrayMap var3, boolean var4) {
      SharedElementCallback var8;
      if (var2) {
         var8 = var1.getEnterTransitionCallback();
      } else {
         var8 = var0.getEnterTransitionCallback();
      }

      if (var8 != null) {
         ArrayList var9 = new ArrayList();
         ArrayList var7 = new ArrayList();
         int var5;
         if (var3 == null) {
            var5 = 0;
         } else {
            var5 = var3.size();
         }

         for(int var6 = 0; var6 < var5; ++var6) {
            var7.add(var3.keyAt(var6));
            var9.add(var3.valueAt(var6));
         }

         if (var4) {
            var8.onSharedElementStart(var7, var9, (List)null);
            return;
         }

         var8.onSharedElementEnd(var7, var9, (List)null);
      }

   }

   private static boolean canHandleAll(FragmentTransitionImpl var0, List var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         if (!var0.canHandle(var1.get(var2))) {
            return false;
         }
      }

      return true;
   }

   static ArrayMap captureInSharedElements(FragmentTransitionImpl var0, ArrayMap var1, Object var2, FragmentTransition.FragmentContainerTransition var3) {
      Fragment var6 = var3.lastIn;
      View var7 = var6.getView();
      if (!var1.isEmpty() && var2 != null && var7 != null) {
         ArrayMap var5 = new ArrayMap();
         var0.findNamedViews(var5, var7);
         BackStackRecord var8 = var3.lastInTransaction;
         ArrayList var9;
         SharedElementCallback var10;
         if (var3.lastInIsPop) {
            var10 = var6.getExitTransitionCallback();
            var9 = var8.mSharedElementSourceNames;
         } else {
            var10 = var6.getEnterTransitionCallback();
            var9 = var8.mSharedElementTargetNames;
         }

         if (var9 != null) {
            var5.retainAll(var9);
            var5.retainAll(var1.values());
         }

         if (var10 != null) {
            var10.onMapSharedElements(var9, var5);

            for(int var4 = var9.size() - 1; var4 >= 0; --var4) {
               String var11 = (String)var9.get(var4);
               View var12 = (View)var5.get(var11);
               if (var12 == null) {
                  String var13 = findKeyForValue(var1, var11);
                  if (var13 != null) {
                     var1.remove(var13);
                  }
               } else if (!var11.equals(ViewCompat.getTransitionName(var12))) {
                  var11 = findKeyForValue(var1, var11);
                  if (var11 != null) {
                     var1.put(var11, ViewCompat.getTransitionName(var12));
                  }
               }
            }

            return var5;
         } else {
            retainValues(var1, var5);
            return var5;
         }
      } else {
         var1.clear();
         return null;
      }
   }

   private static ArrayMap captureOutSharedElements(FragmentTransitionImpl var0, ArrayMap var1, Object var2, FragmentTransition.FragmentContainerTransition var3) {
      if (!var1.isEmpty() && var2 != null) {
         Fragment var8 = var3.firstOut;
         ArrayMap var5 = new ArrayMap();
         var0.findNamedViews(var5, var8.requireView());
         BackStackRecord var6 = var3.firstOutTransaction;
         ArrayList var7;
         SharedElementCallback var9;
         if (var3.firstOutIsPop) {
            var9 = var8.getEnterTransitionCallback();
            var7 = var6.mSharedElementTargetNames;
         } else {
            var9 = var8.getExitTransitionCallback();
            var7 = var6.mSharedElementSourceNames;
         }

         if (var7 != null) {
            var5.retainAll(var7);
         }

         if (var9 != null) {
            var9.onMapSharedElements(var7, var5);

            for(int var4 = var7.size() - 1; var4 >= 0; --var4) {
               String var10 = (String)var7.get(var4);
               View var11 = (View)var5.get(var10);
               if (var11 == null) {
                  var1.remove(var10);
               } else if (!var10.equals(ViewCompat.getTransitionName(var11))) {
                  var10 = (String)var1.remove(var10);
                  var1.put(ViewCompat.getTransitionName(var11), var10);
               }
            }

            return var5;
         } else {
            var1.retainAll(var5.keySet());
            return var5;
         }
      } else {
         var1.clear();
         return null;
      }
   }

   private static FragmentTransitionImpl chooseImpl(Fragment var0, Fragment var1) {
      ArrayList var2 = new ArrayList();
      Object var4;
      if (var0 != null) {
         Object var3 = var0.getExitTransition();
         if (var3 != null) {
            var2.add(var3);
         }

         var3 = var0.getReturnTransition();
         if (var3 != null) {
            var2.add(var3);
         }

         var4 = var0.getSharedElementReturnTransition();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      if (var1 != null) {
         var4 = var1.getEnterTransition();
         if (var4 != null) {
            var2.add(var4);
         }

         var4 = var1.getReenterTransition();
         if (var4 != null) {
            var2.add(var4);
         }

         var4 = var1.getSharedElementEnterTransition();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      if (var2.isEmpty()) {
         return null;
      } else {
         FragmentTransitionImpl var5 = PLATFORM_IMPL;
         if (var5 != null && canHandleAll(var5, var2)) {
            return PLATFORM_IMPL;
         } else {
            var5 = SUPPORT_IMPL;
            if (var5 != null && canHandleAll(var5, var2)) {
               return SUPPORT_IMPL;
            } else if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
               return null;
            } else {
               throw new IllegalArgumentException("Invalid Transition types");
            }
         }
      }
   }

   static ArrayList configureEnteringExitingViews(FragmentTransitionImpl var0, Object var1, Fragment var2, ArrayList var3, View var4) {
      ArrayList var5 = null;
      if (var1 != null) {
         ArrayList var6 = new ArrayList();
         View var7 = var2.getView();
         if (var7 != null) {
            var0.captureTransitioningViews(var6, var7);
         }

         if (var3 != null) {
            var6.removeAll(var3);
         }

         var5 = var6;
         if (!var6.isEmpty()) {
            var6.add(var4);
            var0.addTargets(var1, var6);
            var5 = var6;
         }
      }

      return var5;
   }

   private static Object configureSharedElementsOrdered(final FragmentTransitionImpl var0, ViewGroup var1, final View var2, final ArrayMap var3, final FragmentTransition.FragmentContainerTransition var4, final ArrayList var5, final ArrayList var6, final Object var7, Object var8) {
      final Fragment var12 = var4.lastIn;
      final Fragment var13 = var4.firstOut;
      if (var12 != null) {
         if (var13 == null) {
            return null;
         } else {
            final boolean var9 = var4.lastInIsPop;
            final Object var10;
            if (var3.isEmpty()) {
               var10 = null;
            } else {
               var10 = getSharedElementTransition(var0, var12, var13, var9);
            }

            ArrayMap var14 = captureOutSharedElements(var0, var3, var10, var4);
            if (var3.isEmpty()) {
               var10 = null;
            } else {
               var5.addAll(var14.values());
            }

            if (var7 == null && var8 == null && var10 == null) {
               return null;
            } else {
               callSharedElementStartEnd(var12, var13, var9, var14, true);
               final Rect var15;
               if (var10 != null) {
                  Rect var11 = new Rect();
                  var0.setSharedElementTargets(var10, var2, var5);
                  setOutEpicenter(var0, var10, var8, var14, var4.firstOutIsPop, var4.firstOutTransaction);
                  if (var7 != null) {
                     var0.setEpicenter(var7, var11);
                  }

                  var15 = var11;
               } else {
                  var15 = null;
               }

               OneShotPreDrawListener.add(var1, new Runnable() {
                  public void run() {
                     ArrayMap var1 = FragmentTransition.captureInSharedElements(var0, var3, var10, var4);
                     if (var1 != null) {
                        var6.addAll(var1.values());
                        var6.add(var2);
                     }

                     FragmentTransition.callSharedElementStartEnd(var12, var13, var9, var1, false);
                     Object var2x = var10;
                     if (var2x != null) {
                        var0.swapSharedElementTargets(var2x, var5, var6);
                        View var3x = FragmentTransition.getInEpicenterView(var1, var4, var7, var9);
                        if (var3x != null) {
                           var0.getBoundsOnScreen(var3x, var15);
                        }
                     }

                  }
               });
               return var10;
            }
         }
      } else {
         return null;
      }
   }

   private static Object configureSharedElementsReordered(final FragmentTransitionImpl var0, ViewGroup var1, View var2, ArrayMap var3, FragmentTransition.FragmentContainerTransition var4, ArrayList var5, ArrayList var6, Object var7, Object var8) {
      final Fragment var11 = var4.lastIn;
      final Fragment var12 = var4.firstOut;
      if (var11 != null) {
         var11.requireView().setVisibility(0);
      }

      if (var11 != null) {
         if (var12 == null) {
            return null;
         } else {
            final boolean var9 = var4.lastInIsPop;
            Object var10;
            if (var3.isEmpty()) {
               var10 = null;
            } else {
               var10 = getSharedElementTransition(var0, var11, var12, var9);
            }

            ArrayMap var14 = captureOutSharedElements(var0, var3, var10, var4);
            final ArrayMap var13 = captureInSharedElements(var0, var3, var10, var4);
            Object var16;
            if (var3.isEmpty()) {
               if (var14 != null) {
                  var14.clear();
               }

               if (var13 != null) {
                  var13.clear();
               }

               var16 = null;
            } else {
               addSharedElementsWithMatchingNames(var5, var14, var3.keySet());
               addSharedElementsWithMatchingNames(var6, var13, var3.values());
               var16 = var10;
            }

            if (var7 == null && var8 == null && var16 == null) {
               return null;
            } else {
               callSharedElementStartEnd(var11, var12, var9, var14, true);
               final Rect var15;
               final View var17;
               if (var16 != null) {
                  var6.add(var2);
                  var0.setSharedElementTargets(var16, var2, var5);
                  setOutEpicenter(var0, var16, var8, var14, var4.firstOutIsPop, var4.firstOutTransaction);
                  var15 = new Rect();
                  var17 = getInEpicenterView(var13, var4, var7, var9);
                  if (var17 != null) {
                     var0.setEpicenter(var7, var15);
                  }
               } else {
                  var15 = null;
                  var17 = null;
               }

               OneShotPreDrawListener.add(var1, new Runnable() {
                  public void run() {
                     FragmentTransition.callSharedElementStartEnd(var11, var12, var9, var13, false);
                     View var1 = var17;
                     if (var1 != null) {
                        var0.getBoundsOnScreen(var1, var15);
                     }

                  }
               });
               return var16;
            }
         }
      } else {
         return null;
      }
   }

   private static void configureTransitionsOrdered(FragmentManager var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4, final FragmentTransition.Callback var5) {
      ViewGroup var17;
      if (var0.mContainer.onHasView()) {
         var17 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var17 = null;
      }

      if (var17 != null) {
         Fragment var9 = var2.lastIn;
         final Fragment var14 = var2.firstOut;
         FragmentTransitionImpl var10 = chooseImpl(var14, var9);
         if (var10 != null) {
            boolean var6 = var2.lastInIsPop;
            boolean var7 = var2.firstOutIsPop;
            Object var11 = getEnterTransition(var10, var9, var6);
            Object var8 = getExitTransition(var10, var14, var7);
            ArrayList var16 = new ArrayList();
            ArrayList var12 = new ArrayList();
            Object var13 = configureSharedElementsOrdered(var10, var17, var3, var4, var2, var16, var12, var11, var8);
            if (var11 != null || var13 != null || var8 != null) {
               ArrayList var15 = configureEnteringExitingViews(var10, var8, var14, var16, var3);
               if (var15 == null || var15.isEmpty()) {
                  var8 = null;
               }

               var10.addTarget(var11, var3);
               Object var18 = mergeTransitions(var10, var11, var8, var13, var9, var2.lastInIsPop);
               if (var14 != null && var15 != null && (var15.size() > 0 || var16.size() > 0)) {
                  final CancellationSignal var20 = new CancellationSignal();
                  var5.onStart(var14, var20);
                  var10.setListenerForTransitionEnd(var14, var18, var20, new Runnable() {
                     public void run() {
                        var5.onComplete(var14, var20);
                     }
                  });
               }

               if (var18 != null) {
                  ArrayList var19 = new ArrayList();
                  var10.scheduleRemoveTargets(var18, var11, var19, var8, var15, var13, var12);
                  scheduleTargetChange(var10, var17, var9, var3, var12, var11, var19, var8, var15);
                  var10.setNameOverridesOrdered(var17, var12, var4);
                  var10.beginDelayedTransition(var17, var18);
                  var10.scheduleNameReset(var17, var12, var4);
               }
            }
         }
      }
   }

   private static void configureTransitionsReordered(FragmentManager var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4, final FragmentTransition.Callback var5) {
      ViewGroup var17;
      if (var0.mContainer.onHasView()) {
         var17 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var17 = null;
      }

      if (var17 != null) {
         Fragment var15 = var2.lastIn;
         final Fragment var13 = var2.firstOut;
         FragmentTransitionImpl var9 = chooseImpl(var13, var15);
         if (var9 != null) {
            boolean var6 = var2.lastInIsPop;
            boolean var7 = var2.firstOutIsPop;
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();
            Object var12 = getEnterTransition(var9, var15, var6);
            Object var8 = getExitTransition(var9, var13, var7);
            Object var14 = configureSharedElementsReordered(var9, var17, var3, var4, var2, var11, var10, var12, var8);
            if (var12 != null || var14 != null || var8 != null) {
               Object var18 = var8;
               ArrayList var21 = configureEnteringExitingViews(var9, var8, var13, var11, var3);
               ArrayList var19 = configureEnteringExitingViews(var9, var12, var15, var10, var3);
               setViewVisibility(var19, 4);
               Object var22 = mergeTransitions(var9, var12, var18, var14, var15, var6);
               if (var13 != null && var21 != null && (var21.size() > 0 || var11.size() > 0)) {
                  final CancellationSignal var16 = new CancellationSignal();
                  var5.onStart(var13, var16);
                  var9.setListenerForTransitionEnd(var13, var22, var16, new Runnable() {
                     public void run() {
                        var5.onComplete(var13, var16);
                     }
                  });
               }

               if (var22 != null) {
                  replaceHide(var9, var18, var13, var21);
                  ArrayList var20 = var9.prepareSetNameOverridesReordered(var10);
                  var9.scheduleRemoveTargets(var22, var12, var19, var18, var21, var14, var10);
                  var9.beginDelayedTransition(var17, var22);
                  var9.setNameOverridesReordered(var17, var11, var10, var20, var4);
                  setViewVisibility(var19, 0);
                  var9.swapSharedElementTargets(var14, var11, var10);
               }
            }
         }
      }
   }

   private static FragmentTransition.FragmentContainerTransition ensureContainer(FragmentTransition.FragmentContainerTransition var0, SparseArray var1, int var2) {
      FragmentTransition.FragmentContainerTransition var3 = var0;
      if (var0 == null) {
         var3 = new FragmentTransition.FragmentContainerTransition();
         var1.put(var2, var3);
      }

      return var3;
   }

   private static String findKeyForValue(ArrayMap var0, String var1) {
      int var3 = var0.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var1.equals(var0.valueAt(var2))) {
            return (String)var0.keyAt(var2);
         }
      }

      return null;
   }

   private static Object getEnterTransition(FragmentTransitionImpl var0, Fragment var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         Object var3;
         if (var2) {
            var3 = var1.getReenterTransition();
         } else {
            var3 = var1.getEnterTransition();
         }

         return var0.cloneTransition(var3);
      }
   }

   private static Object getExitTransition(FragmentTransitionImpl var0, Fragment var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         Object var3;
         if (var2) {
            var3 = var1.getReturnTransition();
         } else {
            var3 = var1.getExitTransition();
         }

         return var0.cloneTransition(var3);
      }
   }

   static View getInEpicenterView(ArrayMap var0, FragmentTransition.FragmentContainerTransition var1, Object var2, boolean var3) {
      BackStackRecord var4 = var1.lastInTransaction;
      if (var2 != null && var0 != null && var4.mSharedElementSourceNames != null && !var4.mSharedElementSourceNames.isEmpty()) {
         String var5;
         if (var3) {
            var5 = (String)var4.mSharedElementSourceNames.get(0);
         } else {
            var5 = (String)var4.mSharedElementTargetNames.get(0);
         }

         return (View)var0.get(var5);
      } else {
         return null;
      }
   }

   private static Object getSharedElementTransition(FragmentTransitionImpl var0, Fragment var1, Fragment var2, boolean var3) {
      if (var1 != null && var2 != null) {
         Object var4;
         if (var3) {
            var4 = var2.getSharedElementReturnTransition();
         } else {
            var4 = var1.getSharedElementEnterTransition();
         }

         return var0.wrapTransitionInSet(var0.cloneTransition(var4));
      } else {
         return null;
      }
   }

   private static Object mergeTransitions(FragmentTransitionImpl var0, Object var1, Object var2, Object var3, Fragment var4, boolean var5) {
      boolean var7 = true;
      boolean var6 = var7;
      if (var1 != null) {
         var6 = var7;
         if (var2 != null) {
            var6 = var7;
            if (var4 != null) {
               if (var5) {
                  var5 = var4.getAllowReturnTransitionOverlap();
               } else {
                  var5 = var4.getAllowEnterTransitionOverlap();
               }

               var6 = var5;
            }
         }
      }

      return var6 ? var0.mergeTransitionsTogether(var2, var1, var3) : var0.mergeTransitionsInSequence(var2, var1, var3);
   }

   private static void replaceHide(FragmentTransitionImpl var0, Object var1, Fragment var2, final ArrayList var3) {
      if (var2 != null && var1 != null && var2.mAdded && var2.mHidden && var2.mHiddenChanged) {
         var2.setHideReplaced(true);
         var0.scheduleHideFragmentView(var1, var2.getView(), var3);
         OneShotPreDrawListener.add(var2.mContainer, new Runnable() {
            public void run() {
               FragmentTransition.setViewVisibility(var3, 4);
            }
         });
      }

   }

   private static FragmentTransitionImpl resolveSupportImpl() {
      try {
         FragmentTransitionImpl var0 = (FragmentTransitionImpl)Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor().newInstance();
         return var0;
      } catch (Exception var1) {
         return null;
      }
   }

   private static void retainValues(ArrayMap var0, ArrayMap var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (!var1.containsKey((String)var0.valueAt(var2))) {
            var0.removeAt(var2);
         }
      }

   }

   private static void scheduleTargetChange(final FragmentTransitionImpl var0, ViewGroup var1, final Fragment var2, final View var3, final ArrayList var4, final Object var5, final ArrayList var6, final Object var7, final ArrayList var8) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            Object var1 = var5;
            ArrayList var2x;
            if (var1 != null) {
               var0.removeTarget(var1, var3);
               var2x = FragmentTransition.configureEnteringExitingViews(var0, var5, var2, var4, var3);
               var6.addAll(var2x);
            }

            if (var8 != null) {
               if (var7 != null) {
                  var2x = new ArrayList();
                  var2x.add(var3);
                  var0.replaceTargets(var7, var8, var2x);
               }

               var8.clear();
               var8.add(var3);
            }

         }
      });
   }

   private static void setOutEpicenter(FragmentTransitionImpl var0, Object var1, Object var2, ArrayMap var3, boolean var4, BackStackRecord var5) {
      if (var5.mSharedElementSourceNames != null && !var5.mSharedElementSourceNames.isEmpty()) {
         String var6;
         if (var4) {
            var6 = (String)var5.mSharedElementTargetNames.get(0);
         } else {
            var6 = (String)var5.mSharedElementSourceNames.get(0);
         }

         View var7 = (View)var3.get(var6);
         var0.setEpicenter(var1, var7);
         if (var2 != null) {
            var0.setEpicenter(var2, var7);
         }
      }

   }

   static void setViewVisibility(ArrayList var0, int var1) {
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            ((View)var0.get(var2)).setVisibility(var1);
         }

      }
   }

   static void startTransitions(FragmentManager var0, ArrayList var1, ArrayList var2, int var3, int var4, boolean var5, FragmentTransition.Callback var6) {
      if (var0.mCurState >= 1) {
         SparseArray var10 = new SparseArray();

         int var7;
         for(var7 = var3; var7 < var4; ++var7) {
            BackStackRecord var11 = (BackStackRecord)var1.get(var7);
            if ((Boolean)var2.get(var7)) {
               calculatePopFragments(var11, var10, var5);
            } else {
               calculateFragments(var11, var10, var5);
            }
         }

         if (var10.size() != 0) {
            View var14 = new View(var0.mHost.getContext());
            var7 = var10.size();

            for(int var8 = 0; var8 < var7; ++var8) {
               int var9 = var10.keyAt(var8);
               ArrayMap var12 = calculateNameOverrides(var9, var1, var2, var3, var4);
               FragmentTransition.FragmentContainerTransition var13 = (FragmentTransition.FragmentContainerTransition)var10.valueAt(var8);
               if (var5) {
                  configureTransitionsReordered(var0, var9, var13, var14, var12, var6);
               } else {
                  configureTransitionsOrdered(var0, var9, var13, var14, var12, var6);
               }
            }
         }

      }
   }

   static boolean supportsTransition() {
      return PLATFORM_IMPL != null || SUPPORT_IMPL != null;
   }

   interface Callback {
      void onComplete(Fragment var1, CancellationSignal var2);

      void onStart(Fragment var1, CancellationSignal var2);
   }

   static class FragmentContainerTransition {
      public Fragment firstOut;
      public boolean firstOutIsPop;
      public BackStackRecord firstOutTransaction;
      public Fragment lastIn;
      public boolean lastInIsPop;
      public BackStackRecord lastInTransaction;
   }
}
