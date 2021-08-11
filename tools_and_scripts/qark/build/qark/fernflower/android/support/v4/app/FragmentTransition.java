package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
   private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8};

   private static void addSharedElementsWithMatchingNames(ArrayList var0, ArrayMap var1, Collection var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         View var4 = (View)var1.valueAt(var3);
         if (var2.contains(ViewCompat.getTransitionName(var4))) {
            var0.add(var4);
         }
      }

   }

   private static void addToFirstInLastOut(BackStackRecord var0, BackStackRecord.class_20 var1, SparseArray var2, boolean var3, boolean var4) {
      Fragment var15 = var1.fragment;
      if (var15 != null) {
         int var10 = var15.mContainerId;
         if (var10 != 0) {
            int var5;
            if (var3) {
               var5 = INVERSE_OPS[var1.cmd];
            } else {
               var5 = var1.cmd;
            }

            boolean var6;
            boolean var7;
            boolean var11;
            boolean var17;
            label155: {
               var6 = false;
               var7 = false;
               boolean var9 = false;
               boolean var8 = false;
               boolean var13 = false;
               boolean var12 = false;
               if (var5 != 1) {
                  label156: {
                     if (var5 != 3) {
                        if (var5 == 4) {
                           if (var4) {
                              var17 = var6;
                              if (var15.mHiddenChanged) {
                                 var17 = var6;
                                 if (var15.mAdded) {
                                    var17 = var6;
                                    if (var15.mHidden) {
                                       var17 = true;
                                    }
                                 }
                              }
                           } else {
                              var17 = var7;
                              if (var15.mAdded) {
                                 var17 = var7;
                                 if (!var15.mHidden) {
                                    var17 = true;
                                 }
                              }
                           }

                           var11 = false;
                           var6 = true;
                           var7 = var17;
                           var17 = false;
                           break label155;
                        }

                        if (var5 == 5) {
                           if (var4) {
                              var11 = var12;
                              if (var15.mHiddenChanged) {
                                 var11 = var12;
                                 if (!var15.mHidden) {
                                    var11 = var12;
                                    if (var15.mAdded) {
                                       var11 = true;
                                    }
                                 }
                              }
                           } else {
                              var11 = var15.mHidden;
                           }

                           var6 = false;
                           var7 = false;
                           var17 = true;
                           break label155;
                        }

                        if (var5 != 6) {
                           if (var5 != 7) {
                              var11 = false;
                              var6 = false;
                              var7 = false;
                              var17 = false;
                              break label155;
                           }
                           break label156;
                        }
                     }

                     if (var4) {
                        if (!var15.mAdded && var15.mView != null && var15.mView.getVisibility() == 0 && var15.mPostponedAlpha >= 0.0F) {
                           var17 = true;
                        } else {
                           var17 = var9;
                        }
                     } else {
                        var17 = var8;
                        if (var15.mAdded) {
                           var17 = var8;
                           if (!var15.mHidden) {
                              var17 = true;
                           }
                        }
                     }

                     var11 = false;
                     var6 = true;
                     var7 = var17;
                     var17 = false;
                     break label155;
                  }
               }

               if (var4) {
                  var11 = var15.mIsNewlyAdded;
               } else {
                  var11 = var13;
                  if (!var15.mAdded) {
                     var11 = var13;
                     if (!var15.mHidden) {
                        var11 = true;
                     }
                  }
               }

               var6 = false;
               var7 = false;
               var17 = true;
            }

            FragmentTransition.FragmentContainerTransition var16 = (FragmentTransition.FragmentContainerTransition)var2.get(var10);
            if (var11) {
               var16 = ensureContainer(var16, var2, var10);
               var16.lastIn = var15;
               var16.lastInIsPop = var3;
               var16.lastInTransaction = var0;
            }

            if (!var4 && var17) {
               if (var16 != null && var16.firstOut == var15) {
                  var16.firstOut = null;
               }

               FragmentManagerImpl var14 = var0.mManager;
               if (var15.mState < 1 && var14.mCurState >= 1 && !var0.mReorderingAllowed) {
                  var14.makeActive(var15);
                  var14.moveToState(var15, 1, 0, 0, false);
               }
            }

            if (var7) {
               label112: {
                  FragmentTransition.FragmentContainerTransition var18 = var16;
                  if (var16 != null) {
                     var16 = var16;
                     if (var18.firstOut != null) {
                        break label112;
                     }
                  }

                  var16 = ensureContainer(var16, var2, var10);
                  var16.firstOut = var15;
                  var16.firstOutIsPop = var3;
                  var16.firstOutTransaction = var0;
               }
            }

            if (!var4 && var6 && var16 != null && var16.lastIn == var15) {
               var16.lastIn = null;
            }

         }
      }
   }

   public static void calculateFragments(BackStackRecord var0, SparseArray var1, boolean var2) {
      int var4 = var0.mOps.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         addToFirstInLastOut(var0, (BackStackRecord.class_20)var0.mOps.get(var3), var1, false, var2);
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
            addToFirstInLastOut(var0, (BackStackRecord.class_20)var0.mOps.get(var3), var1, true, var2);
         }

      }
   }

   private static void callSharedElementStartEnd(Fragment var0, Fragment var1, boolean var2, ArrayMap var3, boolean var4) {
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

   @RequiresApi(21)
   private static ArrayMap captureInSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      Fragment var5 = var2.lastIn;
      View var6 = var5.getView();
      if (!var0.isEmpty() && var1 != null && var6 != null) {
         ArrayMap var4 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var4, var6);
         BackStackRecord var7 = var2.lastInTransaction;
         ArrayList var8;
         SharedElementCallback var9;
         if (var2.lastInIsPop) {
            var9 = var5.getExitTransitionCallback();
            var8 = var7.mSharedElementSourceNames;
         } else {
            var9 = var5.getEnterTransitionCallback();
            var8 = var7.mSharedElementTargetNames;
         }

         if (var8 != null) {
            var4.retainAll(var8);
         }

         if (var9 != null) {
            var9.onMapSharedElements(var8, var4);

            for(int var3 = var8.size() - 1; var3 >= 0; --var3) {
               String var12 = (String)var8.get(var3);
               View var10 = (View)var4.get(var12);
               if (var10 == null) {
                  String var11 = findKeyForValue(var0, var12);
                  if (var11 != null) {
                     var0.remove(var11);
                  }
               } else if (!var12.equals(ViewCompat.getTransitionName(var10))) {
                  var12 = findKeyForValue(var0, var12);
                  if (var12 != null) {
                     var0.put(var12, ViewCompat.getTransitionName(var10));
                  }
               }
            }

            return var4;
         } else {
            retainValues(var0, var4);
            return var4;
         }
      } else {
         var0.clear();
         return null;
      }
   }

   @RequiresApi(21)
   private static ArrayMap captureOutSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      if (!var0.isEmpty() && var1 != null) {
         Fragment var5 = var2.firstOut;
         ArrayMap var4 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var4, var5.getView());
         BackStackRecord var6 = var2.firstOutTransaction;
         ArrayList var7;
         SharedElementCallback var8;
         if (var2.firstOutIsPop) {
            var8 = var5.getEnterTransitionCallback();
            var7 = var6.mSharedElementTargetNames;
         } else {
            var8 = var5.getExitTransitionCallback();
            var7 = var6.mSharedElementSourceNames;
         }

         var4.retainAll(var7);
         if (var8 != null) {
            var8.onMapSharedElements(var7, var4);

            for(int var3 = var7.size() - 1; var3 >= 0; --var3) {
               String var10 = (String)var7.get(var3);
               View var9 = (View)var4.get(var10);
               if (var9 == null) {
                  var0.remove(var10);
               } else if (!var10.equals(ViewCompat.getTransitionName(var9))) {
                  var10 = (String)var0.remove(var10);
                  var0.put(ViewCompat.getTransitionName(var9), var10);
               }
            }

            return var4;
         } else {
            var0.retainAll(var4.keySet());
            return var4;
         }
      } else {
         var0.clear();
         return null;
      }
   }

   @RequiresApi(21)
   private static ArrayList configureEnteringExitingViews(Object var0, Fragment var1, ArrayList var2, View var3) {
      ArrayList var4 = null;
      if (var0 != null) {
         ArrayList var5 = new ArrayList();
         View var6 = var1.getView();
         if (var6 != null) {
            FragmentTransitionCompat21.captureTransitioningViews(var5, var6);
         }

         if (var2 != null) {
            var5.removeAll(var2);
         }

         var4 = var5;
         if (!var5.isEmpty()) {
            var5.add(var3);
            FragmentTransitionCompat21.addTargets(var0, var5);
            var4 = var5;
         }
      }

      return var4;
   }

   @RequiresApi(21)
   private static Object configureSharedElementsOrdered(ViewGroup var0, final View var1, final ArrayMap var2, final FragmentTransition.FragmentContainerTransition var3, final ArrayList var4, final ArrayList var5, final Object var6, Object var7) {
      final Fragment var11 = var3.lastIn;
      final Fragment var12 = var3.firstOut;
      if (var11 != null) {
         if (var12 == null) {
            return null;
         } else {
            final boolean var8 = var3.lastInIsPop;
            final Object var9;
            if (var2.isEmpty()) {
               var9 = null;
            } else {
               var9 = getSharedElementTransition(var11, var12, var8);
            }

            ArrayMap var13 = captureOutSharedElements(var2, var9, var3);
            if (var2.isEmpty()) {
               var9 = null;
            } else {
               var4.addAll(var13.values());
            }

            if (var6 == null && var7 == null && var9 == null) {
               return null;
            } else {
               callSharedElementStartEnd(var11, var12, var8, var13, true);
               final Rect var14;
               if (var9 != null) {
                  Rect var10 = new Rect();
                  FragmentTransitionCompat21.setSharedElementTargets(var9, var1, var4);
                  setOutEpicenter(var9, var7, var13, var3.firstOutIsPop, var3.firstOutTransaction);
                  if (var6 != null) {
                     FragmentTransitionCompat21.setEpicenter(var6, var10);
                  }

                  var14 = var10;
               } else {
                  var14 = null;
               }

               OneShotPreDrawListener.add(var0, new Runnable() {
                  public void run() {
                     ArrayMap var1x = FragmentTransition.captureInSharedElements(var2, var9, var3);
                     if (var1x != null) {
                        var5.addAll(var1x.values());
                        var5.add(var1);
                     }

                     FragmentTransition.callSharedElementStartEnd(var11, var12, var8, var1x, false);
                     Object var2x = var9;
                     if (var2x != null) {
                        FragmentTransitionCompat21.swapSharedElementTargets(var2x, var4, var5);
                        View var3x = FragmentTransition.getInEpicenterView(var1x, var3, var6, var8);
                        if (var3x != null) {
                           FragmentTransitionCompat21.getBoundsOnScreen(var3x, var14);
                        }
                     }

                  }
               });
               return var9;
            }
         }
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static Object configureSharedElementsReordered(ViewGroup var0, View var1, ArrayMap var2, FragmentTransition.FragmentContainerTransition var3, ArrayList var4, ArrayList var5, Object var6, Object var7) {
      final Fragment var10 = var3.lastIn;
      final Fragment var11 = var3.firstOut;
      if (var10 != null) {
         var10.getView().setVisibility(0);
      }

      if (var10 != null) {
         if (var11 == null) {
            return null;
         } else {
            final boolean var8 = var3.lastInIsPop;
            Object var9;
            if (var2.isEmpty()) {
               var9 = null;
            } else {
               var9 = getSharedElementTransition(var10, var11, var8);
            }

            ArrayMap var13 = captureOutSharedElements(var2, var9, var3);
            final ArrayMap var12 = captureInSharedElements(var2, var9, var3);
            Object var15;
            if (var2.isEmpty()) {
               if (var13 != null) {
                  var13.clear();
               }

               if (var12 != null) {
                  var12.clear();
               }

               var15 = null;
            } else {
               addSharedElementsWithMatchingNames(var4, var13, var2.keySet());
               addSharedElementsWithMatchingNames(var5, var12, var2.values());
               var15 = var9;
            }

            if (var6 == null && var7 == null && var15 == null) {
               return null;
            } else {
               callSharedElementStartEnd(var10, var11, var8, var13, true);
               final Rect var14;
               final View var16;
               if (var15 != null) {
                  var5.add(var1);
                  FragmentTransitionCompat21.setSharedElementTargets(var15, var1, var4);
                  setOutEpicenter(var15, var7, var13, var3.firstOutIsPop, var3.firstOutTransaction);
                  var14 = new Rect();
                  var16 = getInEpicenterView(var12, var3, var6, var8);
                  if (var16 != null) {
                     FragmentTransitionCompat21.setEpicenter(var6, var14);
                  }
               } else {
                  var14 = null;
                  var16 = null;
               }

               OneShotPreDrawListener.add(var0, new Runnable() {
                  public void run() {
                     FragmentTransition.callSharedElementStartEnd(var10, var11, var8, var12, false);
                     View var1 = var16;
                     if (var1 != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(var1, var14);
                     }

                  }
               });
               return var15;
            }
         }
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static void configureTransitionsOrdered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var14;
      if (var0.mContainer.onHasView()) {
         var14 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var14 = null;
      }

      if (var14 != null) {
         Fragment var8 = var2.lastIn;
         Fragment var12 = var2.firstOut;
         boolean var5 = var2.lastInIsPop;
         boolean var6 = var2.firstOutIsPop;
         Object var9 = getEnterTransition(var8, var5);
         Object var7 = getExitTransition(var12, var6);
         ArrayList var13 = new ArrayList();
         ArrayList var10 = new ArrayList();
         Object var11 = configureSharedElementsOrdered(var14, var3, var4, var2, var13, var10, var9, var7);
         if (var9 != null || var11 != null || var7 != null) {
            ArrayList var15 = configureEnteringExitingViews(var7, var12, var13, var3);
            if (var15 == null || var15.isEmpty()) {
               var7 = null;
            }

            FragmentTransitionCompat21.addTarget(var9, var3);
            Object var16 = mergeTransitions(var9, var7, var11, var8, var2.lastInIsPop);
            if (var16 != null) {
               var13 = new ArrayList();
               FragmentTransitionCompat21.scheduleRemoveTargets(var16, var9, var13, var7, var15, var11, var10);
               scheduleTargetChange(var14, var8, var3, var10, var9, var13, var7, var15);
               FragmentTransitionCompat21.setNameOverridesOrdered(var14, var10, var4);
               FragmentTransitionCompat21.beginDelayedTransition(var14, var16);
               FragmentTransitionCompat21.scheduleNameReset(var14, var10, var4);
            }
         }
      }
   }

   @RequiresApi(21)
   private static void configureTransitionsReordered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var14;
      if (var0.mContainer.onHasView()) {
         var14 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var14 = null;
      }

      if (var14 != null) {
         Fragment var13 = var2.lastIn;
         Fragment var11 = var2.firstOut;
         boolean var5 = var2.lastInIsPop;
         boolean var6 = var2.firstOutIsPop;
         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();
         Object var10 = getEnterTransition(var13, var5);
         Object var7 = getExitTransition(var11, var6);
         Object var12 = configureSharedElementsReordered(var14, var3, var4, var2, var9, var8, var10, var7);
         if (var10 != null || var12 != null || var7 != null) {
            Object var16 = var7;
            ArrayList var19 = configureEnteringExitingViews(var7, var11, var9, var3);
            ArrayList var18 = configureEnteringExitingViews(var10, var13, var8, var3);
            setViewVisibility(var18, 4);
            Object var17 = mergeTransitions(var10, var16, var12, var13, var5);
            if (var17 != null) {
               replaceHide(var16, var11, var19);
               ArrayList var15 = FragmentTransitionCompat21.prepareSetNameOverridesReordered(var8);
               FragmentTransitionCompat21.scheduleRemoveTargets(var17, var10, var18, var16, var19, var12, var8);
               FragmentTransitionCompat21.beginDelayedTransition(var14, var17);
               FragmentTransitionCompat21.setNameOverridesReordered(var14, var9, var8, var15, var4);
               setViewVisibility(var18, 0);
               FragmentTransitionCompat21.swapSharedElementTargets(var12, var9, var8);
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

   @RequiresApi(21)
   private static Object getEnterTransition(Fragment var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         Object var2;
         if (var1) {
            var2 = var0.getReenterTransition();
         } else {
            var2 = var0.getEnterTransition();
         }

         return FragmentTransitionCompat21.cloneTransition(var2);
      }
   }

   @RequiresApi(21)
   private static Object getExitTransition(Fragment var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         Object var2;
         if (var1) {
            var2 = var0.getReturnTransition();
         } else {
            var2 = var0.getExitTransition();
         }

         return FragmentTransitionCompat21.cloneTransition(var2);
      }
   }

   private static View getInEpicenterView(ArrayMap var0, FragmentTransition.FragmentContainerTransition var1, Object var2, boolean var3) {
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

   @RequiresApi(21)
   private static Object getSharedElementTransition(Fragment var0, Fragment var1, boolean var2) {
      if (var0 != null && var1 != null) {
         Object var3;
         if (var2) {
            var3 = var1.getSharedElementReturnTransition();
         } else {
            var3 = var0.getSharedElementEnterTransition();
         }

         return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(var3));
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static Object mergeTransitions(Object var0, Object var1, Object var2, Fragment var3, boolean var4) {
      boolean var6 = true;
      boolean var5 = var6;
      if (var0 != null) {
         var5 = var6;
         if (var1 != null) {
            var5 = var6;
            if (var3 != null) {
               if (var4) {
                  var4 = var3.getAllowReturnTransitionOverlap();
               } else {
                  var4 = var3.getAllowEnterTransitionOverlap();
               }

               var5 = var4;
            }
         }
      }

      return var5 ? FragmentTransitionCompat21.mergeTransitionsTogether(var1, var0, var2) : FragmentTransitionCompat21.mergeTransitionsInSequence(var1, var0, var2);
   }

   @RequiresApi(21)
   private static void replaceHide(Object var0, Fragment var1, final ArrayList var2) {
      if (var1 != null && var0 != null && var1.mAdded && var1.mHidden && var1.mHiddenChanged) {
         var1.setHideReplaced(true);
         FragmentTransitionCompat21.scheduleHideFragmentView(var0, var1.getView(), var2);
         OneShotPreDrawListener.add(var1.mContainer, new Runnable() {
            public void run() {
               FragmentTransition.setViewVisibility(var2, 4);
            }
         });
      }

   }

   private static void retainValues(ArrayMap var0, ArrayMap var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (!var1.containsKey((String)var0.valueAt(var2))) {
            var0.removeAt(var2);
         }
      }

   }

   @RequiresApi(21)
   private static void scheduleTargetChange(ViewGroup var0, final Fragment var1, final View var2, final ArrayList var3, final Object var4, final ArrayList var5, final Object var6, final ArrayList var7) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            Object var1x = var4;
            ArrayList var2x;
            if (var1x != null) {
               FragmentTransitionCompat21.removeTarget(var1x, var2);
               var2x = FragmentTransition.configureEnteringExitingViews(var4, var1, var3, var2);
               var5.addAll(var2x);
            }

            if (var7 != null) {
               if (var6 != null) {
                  var2x = new ArrayList();
                  var2x.add(var2);
                  FragmentTransitionCompat21.replaceTargets(var6, var7, var2x);
               }

               var7.clear();
               var7.add(var2);
            }

         }
      });
   }

   @RequiresApi(21)
   private static void setOutEpicenter(Object var0, Object var1, ArrayMap var2, boolean var3, BackStackRecord var4) {
      if (var4.mSharedElementSourceNames != null && !var4.mSharedElementSourceNames.isEmpty()) {
         String var6;
         if (var3) {
            var6 = (String)var4.mSharedElementTargetNames.get(0);
         } else {
            var6 = (String)var4.mSharedElementSourceNames.get(0);
         }

         View var5 = (View)var2.get(var6);
         FragmentTransitionCompat21.setEpicenter(var0, var5);
         if (var1 != null) {
            FragmentTransitionCompat21.setEpicenter(var1, var5);
         }
      }

   }

   private static void setViewVisibility(ArrayList var0, int var1) {
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            ((View)var0.get(var2)).setVisibility(var1);
         }

      }
   }

   static void startTransitions(FragmentManagerImpl var0, ArrayList var1, ArrayList var2, int var3, int var4, boolean var5) {
      if (var0.mCurState >= 1) {
         if (VERSION.SDK_INT >= 21) {
            SparseArray var9 = new SparseArray();

            int var6;
            for(var6 = var3; var6 < var4; ++var6) {
               BackStackRecord var10 = (BackStackRecord)var1.get(var6);
               if ((Boolean)var2.get(var6)) {
                  calculatePopFragments(var10, var9, var5);
               } else {
                  calculateFragments(var10, var9, var5);
               }
            }

            if (var9.size() != 0) {
               View var13 = new View(var0.mHost.getContext());
               int var7 = var9.size();

               for(var6 = 0; var6 < var7; ++var6) {
                  int var8 = var9.keyAt(var6);
                  ArrayMap var11 = calculateNameOverrides(var8, var1, var2, var3, var4);
                  FragmentTransition.FragmentContainerTransition var12 = (FragmentTransition.FragmentContainerTransition)var9.valueAt(var6);
                  if (var5) {
                     configureTransitionsReordered(var0, var8, var12, var13, var11);
                  } else {
                     configureTransitionsOrdered(var0, var8, var12, var13, var11);
                  }
               }
            }
         }

      }
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
