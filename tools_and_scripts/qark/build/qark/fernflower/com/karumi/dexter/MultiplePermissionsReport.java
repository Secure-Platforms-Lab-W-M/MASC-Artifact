package com.karumi.dexter;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class MultiplePermissionsReport {
   private final List deniedPermissionResponses = new LinkedList();
   private final List grantedPermissionResponses = new LinkedList();

   MultiplePermissionsReport() {
   }

   boolean addDeniedPermissionResponse(PermissionDeniedResponse var1) {
      return this.deniedPermissionResponses.add(var1);
   }

   boolean addGrantedPermissionResponse(PermissionGrantedResponse var1) {
      return this.grantedPermissionResponses.add(var1);
   }

   public boolean areAllPermissionsGranted() {
      return this.deniedPermissionResponses.isEmpty();
   }

   void clear() {
      this.grantedPermissionResponses.clear();
      this.deniedPermissionResponses.clear();
   }

   public List getDeniedPermissionResponses() {
      return this.deniedPermissionResponses;
   }

   public List getGrantedPermissionResponses() {
      return this.grantedPermissionResponses;
   }

   public boolean isAnyPermissionPermanentlyDenied() {
      Iterator var1 = this.deniedPermissionResponses.iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((PermissionDeniedResponse)var1.next()).isPermanentlyDenied());

      return true;
   }
}
