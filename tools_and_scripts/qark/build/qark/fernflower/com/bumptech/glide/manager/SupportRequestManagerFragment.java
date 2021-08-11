package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SupportRequestManagerFragment extends Fragment {
   private static final String TAG = "SupportRMFragment";
   private final Set childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private Fragment parentFragmentHint;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private SupportRequestManagerFragment rootRequestManagerFragment;

   public SupportRequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   public SupportRequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new SupportRequestManagerFragment.SupportFragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(SupportRequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   private Fragment getParentFragmentUsingHint() {
      Fragment var1 = this.getParentFragment();
      return var1 != null ? var1 : this.parentFragmentHint;
   }

   private static FragmentManager getRootFragmentManager(Fragment var0) {
      while(var0.getParentFragment() != null) {
         var0 = var0.getParentFragment();
      }

      return var0.getFragmentManager();
   }

   private boolean isDescendant(Fragment var1) {
      Fragment var2 = this.getParentFragmentUsingHint();

      while(true) {
         Fragment var3 = var1.getParentFragment();
         if (var3 == null) {
            return false;
         }

         if (var3.equals(var2)) {
            return true;
         }

         var1 = var1.getParentFragment();
      }
   }

   private void registerFragmentWithRoot(Context var1, FragmentManager var2) {
      this.unregisterFragmentWithRoot();
      SupportRequestManagerFragment var3 = Glide.get(var1).getRequestManagerRetriever().getSupportRequestManagerFragment(var1, var2);
      this.rootRequestManagerFragment = var3;
      if (!this.equals(var3)) {
         this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
      }

   }

   private void removeChildRequestManagerFragment(SupportRequestManagerFragment var1) {
      this.childRequestManagerFragments.remove(var1);
   }

   private void unregisterFragmentWithRoot() {
      SupportRequestManagerFragment var1 = this.rootRequestManagerFragment;
      if (var1 != null) {
         var1.removeChildRequestManagerFragment(this);
         this.rootRequestManagerFragment = null;
      }

   }

   Set getDescendantRequestManagerFragments() {
      SupportRequestManagerFragment var1 = this.rootRequestManagerFragment;
      if (var1 == null) {
         return Collections.emptySet();
      } else if (this.equals(var1)) {
         return Collections.unmodifiableSet(this.childRequestManagerFragments);
      } else {
         HashSet var4 = new HashSet();
         Iterator var2 = this.rootRequestManagerFragment.getDescendantRequestManagerFragments().iterator();

         while(var2.hasNext()) {
            SupportRequestManagerFragment var3 = (SupportRequestManagerFragment)var2.next();
            if (this.isDescendant(var3.getParentFragmentUsingHint())) {
               var4.add(var3);
            }
         }

         return Collections.unmodifiableSet(var4);
      }
   }

   ActivityFragmentLifecycle getGlideLifecycle() {
      return this.lifecycle;
   }

   public RequestManager getRequestManager() {
      return this.requestManager;
   }

   public RequestManagerTreeNode getRequestManagerTreeNode() {
      return this.requestManagerTreeNode;
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      FragmentManager var3 = getRootFragmentManager(this);
      if (var3 == null) {
         if (Log.isLoggable("SupportRMFragment", 5)) {
            Log.w("SupportRMFragment", "Unable to register fragment with root, ancestor detached");
         }

      } else {
         try {
            this.registerFragmentWithRoot(this.getContext(), var3);
         } catch (IllegalStateException var2) {
            if (Log.isLoggable("SupportRMFragment", 5)) {
               Log.w("SupportRMFragment", "Unable to register fragment with root", var2);
            }

         }
      }
   }

   public void onDestroy() {
      super.onDestroy();
      this.lifecycle.onDestroy();
      this.unregisterFragmentWithRoot();
   }

   public void onDetach() {
      super.onDetach();
      this.parentFragmentHint = null;
      this.unregisterFragmentWithRoot();
   }

   public void onStart() {
      super.onStart();
      this.lifecycle.onStart();
   }

   public void onStop() {
      super.onStop();
      this.lifecycle.onStop();
   }

   void setParentFragmentHint(Fragment var1) {
      this.parentFragmentHint = var1;
      if (var1 != null) {
         if (var1.getContext() != null) {
            FragmentManager var2 = getRootFragmentManager(var1);
            if (var2 != null) {
               this.registerFragmentWithRoot(var1.getContext(), var2);
            }
         }
      }
   }

   public void setRequestManager(RequestManager var1) {
      this.requestManager = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("{parent=");
      var1.append(this.getParentFragmentUsingHint());
      var1.append("}");
      return var1.toString();
   }

   private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      SupportFragmentRequestManagerTreeNode() {
      }

      public Set getDescendants() {
         Set var2 = SupportRequestManagerFragment.this.getDescendantRequestManagerFragments();
         HashSet var1 = new HashSet(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            SupportRequestManagerFragment var3 = (SupportRequestManagerFragment)var4.next();
            if (var3.getRequestManager() != null) {
               var1.add(var3.getRequestManager());
            }
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append("{fragment=");
         var1.append(SupportRequestManagerFragment.this);
         var1.append("}");
         return var1.toString();
      }
   }
}
