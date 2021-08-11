package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Deprecated
public class RequestManagerFragment extends Fragment {
   private static final String TAG = "RMFragment";
   private final Set childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private Fragment parentFragmentHint;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private RequestManagerFragment rootRequestManagerFragment;

   public RequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   RequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new RequestManagerFragment.FragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   private Fragment getParentFragmentUsingHint() {
      Fragment var1;
      if (VERSION.SDK_INT >= 17) {
         var1 = this.getParentFragment();
      } else {
         var1 = null;
      }

      return var1 != null ? var1 : this.parentFragmentHint;
   }

   private boolean isDescendant(Fragment var1) {
      Fragment var2 = this.getParentFragment();

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

   private void registerFragmentWithRoot(Activity var1) {
      this.unregisterFragmentWithRoot();
      RequestManagerFragment var2 = Glide.get(var1).getRequestManagerRetriever().getRequestManagerFragment(var1);
      this.rootRequestManagerFragment = var2;
      if (!this.equals(var2)) {
         this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
      }

   }

   private void removeChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.remove(var1);
   }

   private void unregisterFragmentWithRoot() {
      RequestManagerFragment var1 = this.rootRequestManagerFragment;
      if (var1 != null) {
         var1.removeChildRequestManagerFragment(this);
         this.rootRequestManagerFragment = null;
      }

   }

   Set getDescendantRequestManagerFragments() {
      if (this.equals(this.rootRequestManagerFragment)) {
         return Collections.unmodifiableSet(this.childRequestManagerFragments);
      } else if (this.rootRequestManagerFragment != null && VERSION.SDK_INT >= 17) {
         HashSet var1 = new HashSet();
         Iterator var2 = this.rootRequestManagerFragment.getDescendantRequestManagerFragments().iterator();

         while(var2.hasNext()) {
            RequestManagerFragment var3 = (RequestManagerFragment)var2.next();
            if (this.isDescendant(var3.getParentFragment())) {
               var1.add(var3);
            }
         }

         return Collections.unmodifiableSet(var1);
      } else {
         return Collections.emptySet();
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

   public void onAttach(Activity var1) {
      super.onAttach(var1);

      try {
         this.registerFragmentWithRoot(var1);
      } catch (IllegalStateException var2) {
         if (Log.isLoggable("RMFragment", 5)) {
            Log.w("RMFragment", "Unable to register fragment with root", var2);
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
      if (var1 != null && var1.getActivity() != null) {
         this.registerFragmentWithRoot(var1.getActivity());
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

   private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      FragmentRequestManagerTreeNode() {
      }

      public Set getDescendants() {
         Set var2 = RequestManagerFragment.this.getDescendantRequestManagerFragments();
         HashSet var1 = new HashSet(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            RequestManagerFragment var3 = (RequestManagerFragment)var4.next();
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
         var1.append(RequestManagerFragment.this);
         var1.append("}");
         return var1.toString();
      }
   }
}
