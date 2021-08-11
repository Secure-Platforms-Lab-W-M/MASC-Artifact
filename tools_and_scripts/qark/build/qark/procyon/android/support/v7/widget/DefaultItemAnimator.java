// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v4.view.ViewCompat;
import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.NonNull;
import android.animation.ValueAnimator;
import java.util.List;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.animation.AnimatorListenerAdapter;
import java.util.ArrayList;
import android.animation.TimeInterpolator;

public class DefaultItemAnimator extends SimpleItemAnimator
{
    private static final boolean DEBUG = false;
    private static TimeInterpolator sDefaultInterpolator;
    ArrayList<ViewHolder> mAddAnimations;
    ArrayList<ArrayList<ViewHolder>> mAdditionsList;
    ArrayList<ViewHolder> mChangeAnimations;
    ArrayList<ArrayList<ChangeInfo>> mChangesList;
    ArrayList<ViewHolder> mMoveAnimations;
    ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<ViewHolder> mPendingRemovals;
    ArrayList<ViewHolder> mRemoveAnimations;
    
    public DefaultItemAnimator() {
        this.mPendingRemovals = new ArrayList<ViewHolder>();
        this.mPendingAdditions = new ArrayList<ViewHolder>();
        this.mPendingMoves = new ArrayList<MoveInfo>();
        this.mPendingChanges = new ArrayList<ChangeInfo>();
        this.mAdditionsList = new ArrayList<ArrayList<ViewHolder>>();
        this.mMovesList = new ArrayList<ArrayList<MoveInfo>>();
        this.mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
        this.mAddAnimations = new ArrayList<ViewHolder>();
        this.mMoveAnimations = new ArrayList<ViewHolder>();
        this.mRemoveAnimations = new ArrayList<ViewHolder>();
        this.mChangeAnimations = new ArrayList<ViewHolder>();
    }
    
    private void animateRemoveImpl(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        final ViewPropertyAnimator animate = itemView.animate();
        this.mRemoveAnimations.add(viewHolder);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getRemoveDuration()).alpha(0.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                animate.setListener((Animator$AnimatorListener)null);
                itemView.setAlpha(1.0f);
                DefaultItemAnimator.this.dispatchRemoveFinished(viewHolder);
                DefaultItemAnimator.this.mRemoveAnimations.remove(viewHolder);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                DefaultItemAnimator.this.dispatchRemoveStarting(viewHolder);
            }
        }).start();
    }
    
    private void endChangeAnimation(final List<ChangeInfo> list, final ViewHolder viewHolder) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final ChangeInfo changeInfo = list.get(i);
            if (this.endChangeAnimationIfNecessary(changeInfo, viewHolder)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    list.remove(changeInfo);
                }
            }
        }
    }
    
    private void endChangeAnimationIfNecessary(final ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }
    
    private boolean endChangeAnimationIfNecessary(final ChangeInfo changeInfo, final ViewHolder viewHolder) {
        boolean b = false;
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        }
        else {
            if (changeInfo.oldHolder != viewHolder) {
                return false;
            }
            changeInfo.oldHolder = null;
            b = true;
        }
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setTranslationX(0.0f);
        viewHolder.itemView.setTranslationY(0.0f);
        this.dispatchChangeFinished(viewHolder, b);
        return true;
    }
    
    private void resetAnimation(final ViewHolder viewHolder) {
        if (DefaultItemAnimator.sDefaultInterpolator == null) {
            DefaultItemAnimator.sDefaultInterpolator = new ValueAnimator().getInterpolator();
        }
        viewHolder.itemView.animate().setInterpolator(DefaultItemAnimator.sDefaultInterpolator);
        this.endAnimation(viewHolder);
    }
    
    @Override
    public boolean animateAdd(final ViewHolder viewHolder) {
        this.resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
        this.mPendingAdditions.add(viewHolder);
        return true;
    }
    
    void animateAddImpl(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        final ViewPropertyAnimator animate = itemView.animate();
        this.mAddAnimations.add(viewHolder);
        animate.alpha(1.0f).setDuration(((RecyclerView.ItemAnimator)this).getAddDuration()).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                itemView.setAlpha(1.0f);
            }
            
            public void onAnimationEnd(final Animator animator) {
                animate.setListener((Animator$AnimatorListener)null);
                DefaultItemAnimator.this.dispatchAddFinished(viewHolder);
                DefaultItemAnimator.this.mAddAnimations.remove(viewHolder);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                DefaultItemAnimator.this.dispatchAddStarting(viewHolder);
            }
        }).start();
    }
    
    @Override
    public boolean animateChange(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int n, final int n2, final int n3, final int n4) {
        if (viewHolder == viewHolder2) {
            return this.animateMove(viewHolder, n, n2, n3, n4);
        }
        final float translationX = viewHolder.itemView.getTranslationX();
        final float translationY = viewHolder.itemView.getTranslationY();
        final float alpha = viewHolder.itemView.getAlpha();
        this.resetAnimation(viewHolder);
        final int n5 = (int)(n3 - n - translationX);
        final int n6 = (int)(n4 - n2 - translationY);
        viewHolder.itemView.setTranslationX(translationX);
        viewHolder.itemView.setTranslationY(translationY);
        viewHolder.itemView.setAlpha(alpha);
        if (viewHolder2 != null) {
            this.resetAnimation(viewHolder2);
            viewHolder2.itemView.setTranslationX((float)(-n5));
            viewHolder2.itemView.setTranslationY((float)(-n6));
            viewHolder2.itemView.setAlpha(0.0f);
        }
        this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, n, n2, n3, n4));
        return true;
    }
    
    void animateChangeImpl(final ChangeInfo changeInfo) {
        final ViewHolder oldHolder = changeInfo.oldHolder;
        View itemView = null;
        View itemView2;
        if (oldHolder == null) {
            itemView2 = null;
        }
        else {
            itemView2 = oldHolder.itemView;
        }
        final ViewHolder newHolder = changeInfo.newHolder;
        if (newHolder != null) {
            itemView = newHolder.itemView;
        }
        if (itemView2 != null) {
            final ViewPropertyAnimator setDuration = itemView2.animate().setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            setDuration.translationX((float)(changeInfo.toX - changeInfo.fromX));
            setDuration.translationY((float)(changeInfo.toY - changeInfo.fromY));
            setDuration.alpha(0.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    setDuration.setListener((Animator$AnimatorListener)null);
                    itemView2.setAlpha(1.0f);
                    itemView2.setTranslationX(0.0f);
                    itemView2.setTranslationY(0.0f);
                    DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
                    DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
                }
            }).start();
        }
        if (itemView != null) {
            final ViewPropertyAnimator animate = itemView.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration()).alpha(1.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    animate.setListener((Animator$AnimatorListener)null);
                    itemView.setAlpha(1.0f);
                    itemView.setTranslationX(0.0f);
                    itemView.setTranslationY(0.0f);
                    DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
                    DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
                }
            }).start();
        }
    }
    
    @Override
    public boolean animateMove(final ViewHolder viewHolder, int n, int n2, final int n3, final int n4) {
        final View itemView = viewHolder.itemView;
        n += (int)viewHolder.itemView.getTranslationX();
        n2 += (int)viewHolder.itemView.getTranslationY();
        this.resetAnimation(viewHolder);
        final int n5 = n3 - n;
        final int n6 = n4 - n2;
        if (n5 == 0 && n6 == 0) {
            this.dispatchMoveFinished(viewHolder);
            return false;
        }
        if (n5 != 0) {
            itemView.setTranslationX((float)(-n5));
        }
        if (n6 != 0) {
            itemView.setTranslationY((float)(-n6));
        }
        this.mPendingMoves.add(new MoveInfo(viewHolder, n, n2, n3, n4));
        return true;
    }
    
    void animateMoveImpl(final ViewHolder viewHolder, int n, int n2, final int n3, final int n4) {
        final View itemView = viewHolder.itemView;
        n = n3 - n;
        n2 = n4 - n2;
        if (n != 0) {
            itemView.animate().translationX(0.0f);
        }
        if (n2 != 0) {
            itemView.animate().translationY(0.0f);
        }
        final ViewPropertyAnimator animate = itemView.animate();
        this.mMoveAnimations.add(viewHolder);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getMoveDuration()).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (n != 0) {
                    itemView.setTranslationX(0.0f);
                }
                if (n2 != 0) {
                    itemView.setTranslationY(0.0f);
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                animate.setListener((Animator$AnimatorListener)null);
                DefaultItemAnimator.this.dispatchMoveFinished(viewHolder);
                DefaultItemAnimator.this.mMoveAnimations.remove(viewHolder);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                DefaultItemAnimator.this.dispatchMoveStarting(viewHolder);
            }
        }).start();
    }
    
    @Override
    public boolean animateRemove(final ViewHolder viewHolder) {
        this.resetAnimation(viewHolder);
        this.mPendingRemovals.add(viewHolder);
        return true;
    }
    
    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
        return !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
    }
    
    void cancelAll(final List<ViewHolder> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            list.get(i).itemView.animate().cancel();
        }
    }
    
    void dispatchFinishedWhenDone() {
        if (!this.isRunning()) {
            ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
        }
    }
    
    @Override
    public void endAnimation(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        itemView.animate().cancel();
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            if (this.mPendingMoves.get(i).holder == viewHolder) {
                itemView.setTranslationY(0.0f);
                itemView.setTranslationX(0.0f);
                this.dispatchMoveFinished(viewHolder);
                this.mPendingMoves.remove(i);
            }
        }
        this.endChangeAnimation(this.mPendingChanges, viewHolder);
        if (this.mPendingRemovals.remove(viewHolder)) {
            itemView.setAlpha(1.0f);
            this.dispatchRemoveFinished(viewHolder);
        }
        if (this.mPendingAdditions.remove(viewHolder)) {
            itemView.setAlpha(1.0f);
            this.dispatchAddFinished(viewHolder);
        }
        for (int j = this.mChangesList.size() - 1; j >= 0; --j) {
            final ArrayList<ChangeInfo> list = this.mChangesList.get(j);
            this.endChangeAnimation(list, viewHolder);
            if (list.isEmpty()) {
                this.mChangesList.remove(j);
            }
        }
        for (int k = this.mMovesList.size() - 1; k >= 0; --k) {
            final ArrayList<MoveInfo> list2 = this.mMovesList.get(k);
            int l = list2.size() - 1;
            while (l >= 0) {
                if (list2.get(l).holder == viewHolder) {
                    itemView.setTranslationY(0.0f);
                    itemView.setTranslationX(0.0f);
                    this.dispatchMoveFinished(viewHolder);
                    list2.remove(l);
                    if (list2.isEmpty()) {
                        this.mMovesList.remove(k);
                        break;
                    }
                    break;
                }
                else {
                    --l;
                }
            }
        }
        for (int n = this.mAdditionsList.size() - 1; n >= 0; --n) {
            final ArrayList<ViewHolder> list3 = this.mAdditionsList.get(n);
            if (list3.remove(viewHolder)) {
                itemView.setAlpha(1.0f);
                this.dispatchAddFinished(viewHolder);
                if (list3.isEmpty()) {
                    this.mAdditionsList.remove(n);
                }
            }
        }
        this.mRemoveAnimations.remove(viewHolder);
        this.mAddAnimations.remove(viewHolder);
        this.mChangeAnimations.remove(viewHolder);
        this.mMoveAnimations.remove(viewHolder);
        this.dispatchFinishedWhenDone();
    }
    
    @Override
    public void endAnimations() {
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            final MoveInfo moveInfo = this.mPendingMoves.get(i);
            final View itemView = moveInfo.holder.itemView;
            itemView.setTranslationY(0.0f);
            itemView.setTranslationX(0.0f);
            this.dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(i);
        }
        for (int j = this.mPendingRemovals.size() - 1; j >= 0; --j) {
            this.dispatchRemoveFinished(this.mPendingRemovals.get(j));
            this.mPendingRemovals.remove(j);
        }
        for (int k = this.mPendingAdditions.size() - 1; k >= 0; --k) {
            final ViewHolder viewHolder = this.mPendingAdditions.get(k);
            viewHolder.itemView.setAlpha(1.0f);
            this.dispatchAddFinished(viewHolder);
            this.mPendingAdditions.remove(k);
        }
        for (int l = this.mPendingChanges.size() - 1; l >= 0; --l) {
            this.endChangeAnimationIfNecessary(this.mPendingChanges.get(l));
        }
        this.mPendingChanges.clear();
        if (!this.isRunning()) {
            return;
        }
        for (int n = this.mMovesList.size() - 1; n >= 0; --n) {
            final ArrayList<MoveInfo> list = this.mMovesList.get(n);
            for (int n2 = list.size() - 1; n2 >= 0; --n2) {
                final MoveInfo moveInfo2 = list.get(n2);
                final View itemView2 = moveInfo2.holder.itemView;
                itemView2.setTranslationY(0.0f);
                itemView2.setTranslationX(0.0f);
                this.dispatchMoveFinished(moveInfo2.holder);
                list.remove(n2);
                if (list.isEmpty()) {
                    this.mMovesList.remove(list);
                }
            }
        }
        for (int n3 = this.mAdditionsList.size() - 1; n3 >= 0; --n3) {
            final ArrayList<ViewHolder> list2 = this.mAdditionsList.get(n3);
            for (int n4 = list2.size() - 1; n4 >= 0; --n4) {
                final ViewHolder viewHolder2 = list2.get(n4);
                viewHolder2.itemView.setAlpha(1.0f);
                this.dispatchAddFinished(viewHolder2);
                list2.remove(n4);
                if (list2.isEmpty()) {
                    this.mAdditionsList.remove(list2);
                }
            }
        }
        for (int n5 = this.mChangesList.size() - 1; n5 >= 0; --n5) {
            final ArrayList<ChangeInfo> list3 = this.mChangesList.get(n5);
            for (int n6 = list3.size() - 1; n6 >= 0; --n6) {
                this.endChangeAnimationIfNecessary(list3.get(n6));
                if (list3.isEmpty()) {
                    this.mChangesList.remove(list3);
                }
            }
        }
        this.cancelAll(this.mRemoveAnimations);
        this.cancelAll(this.mMoveAnimations);
        this.cancelAll(this.mAddAnimations);
        this.cancelAll(this.mChangeAnimations);
        ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
    }
    
    @Override
    public boolean isRunning() {
        if (this.mPendingAdditions.isEmpty()) {
            if (this.mPendingChanges.isEmpty()) {
                if (this.mPendingMoves.isEmpty()) {
                    if (this.mPendingRemovals.isEmpty()) {
                        if (this.mMoveAnimations.isEmpty()) {
                            if (this.mRemoveAnimations.isEmpty()) {
                                if (this.mAddAnimations.isEmpty()) {
                                    if (this.mChangeAnimations.isEmpty()) {
                                        if (this.mMovesList.isEmpty()) {
                                            if (this.mAdditionsList.isEmpty()) {
                                                if (this.mChangesList.isEmpty()) {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public void runPendingAnimations() {
        final boolean b = this.mPendingRemovals.isEmpty() ^ true;
        final boolean b2 = this.mPendingMoves.isEmpty() ^ true;
        final boolean b3 = this.mPendingChanges.isEmpty() ^ true;
        final boolean b4 = this.mPendingAdditions.isEmpty() ^ true;
        if (!b && !b2 && !b4 && !b3) {
            return;
        }
        final Iterator<ViewHolder> iterator = this.mPendingRemovals.iterator();
        while (iterator.hasNext()) {
            this.animateRemoveImpl(iterator.next());
        }
        this.mPendingRemovals.clear();
        if (b2) {
            final ArrayList<MoveInfo> list = new ArrayList<MoveInfo>();
            list.addAll(this.mPendingMoves);
            this.mMovesList.add(list);
            this.mPendingMoves.clear();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (final MoveInfo moveInfo : list) {
                        DefaultItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                    }
                    list.clear();
                    DefaultItemAnimator.this.mMovesList.remove(list);
                }
            };
            if (b) {
                ViewCompat.postOnAnimationDelayed(list.get(0).holder.itemView, runnable, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
            }
            else {
                runnable.run();
            }
        }
        if (b3) {
            final ArrayList<ChangeInfo> list2 = new ArrayList<ChangeInfo>();
            list2.addAll(this.mPendingChanges);
            this.mChangesList.add(list2);
            this.mPendingChanges.clear();
            final Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    final Iterator<ChangeInfo> iterator = list2.iterator();
                    while (iterator.hasNext()) {
                        DefaultItemAnimator.this.animateChangeImpl((ChangeInfo)iterator.next());
                    }
                    list2.clear();
                    DefaultItemAnimator.this.mChangesList.remove(list2);
                }
            };
            if (b) {
                ViewCompat.postOnAnimationDelayed(list2.get(0).oldHolder.itemView, runnable2, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
            }
            else {
                runnable2.run();
            }
        }
        if (!b4) {
            return;
        }
        final ArrayList<ViewHolder> list3 = new ArrayList<ViewHolder>();
        list3.addAll(this.mPendingAdditions);
        this.mAdditionsList.add(list3);
        this.mPendingAdditions.clear();
        final Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                final Iterator<ViewHolder> iterator = list3.iterator();
                while (iterator.hasNext()) {
                    DefaultItemAnimator.this.animateAddImpl(iterator.next());
                }
                list3.clear();
                DefaultItemAnimator.this.mAdditionsList.remove(list3);
            }
        };
        if (!b && !b2 && !b3) {
            runnable3.run();
            return;
        }
        long changeDuration = 0L;
        long removeDuration;
        if (b) {
            removeDuration = ((RecyclerView.ItemAnimator)this).getRemoveDuration();
        }
        else {
            removeDuration = 0L;
        }
        long moveDuration;
        if (b2) {
            moveDuration = ((RecyclerView.ItemAnimator)this).getMoveDuration();
        }
        else {
            moveDuration = 0L;
        }
        if (b3) {
            changeDuration = ((RecyclerView.ItemAnimator)this).getChangeDuration();
        }
        ViewCompat.postOnAnimationDelayed(list3.get(0).itemView, runnable3, Math.max(moveDuration, changeDuration) + removeDuration);
    }
    
    private static class ChangeInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder newHolder;
        public ViewHolder oldHolder;
        public int toX;
        public int toY;
        
        private ChangeInfo(final ViewHolder oldHolder, final ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }
        
        ChangeInfo(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int fromX, final int fromY, final int toX, final int toY) {
            this(viewHolder, viewHolder2);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ChangeInfo{oldHolder=");
            sb.append(this.oldHolder);
            sb.append(", newHolder=");
            sb.append(this.newHolder);
            sb.append(", fromX=");
            sb.append(this.fromX);
            sb.append(", fromY=");
            sb.append(this.fromY);
            sb.append(", toX=");
            sb.append(this.toX);
            sb.append(", toY=");
            sb.append(this.toY);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class MoveInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder holder;
        public int toX;
        public int toY;
        
        MoveInfo(final ViewHolder holder, final int fromX, final int fromY, final int toX, final int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }
}
