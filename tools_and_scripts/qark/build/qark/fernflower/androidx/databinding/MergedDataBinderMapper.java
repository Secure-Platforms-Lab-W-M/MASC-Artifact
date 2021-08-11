package androidx.databinding;

import android.util.Log;
import android.view.View;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class MergedDataBinderMapper extends DataBinderMapper {
   private static final String TAG = "MergedDataBinderMapper";
   private Set mExistingMappers = new HashSet();
   private List mFeatureBindingMappers = new CopyOnWriteArrayList();
   private List mMappers = new CopyOnWriteArrayList();

   private boolean loadFeatures() {
      boolean var1 = false;
      Iterator var3 = this.mFeatureBindingMappers.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         boolean var2;
         label49: {
            label48: {
               label47: {
                  StringBuilder var6;
                  IllegalAccessException var15;
                  label46: {
                     InstantiationException var10000;
                     label57: {
                        Class var5;
                        boolean var10001;
                        try {
                           var5 = Class.forName(var4);
                        } catch (ClassNotFoundException var10) {
                           var10001 = false;
                           break label47;
                        } catch (IllegalAccessException var11) {
                           var15 = var11;
                           var10001 = false;
                           break label46;
                        } catch (InstantiationException var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label57;
                        }

                        var2 = var1;

                        try {
                           if (!DataBinderMapper.class.isAssignableFrom(var5)) {
                              break label49;
                           }

                           this.addMapper((DataBinderMapper)var5.newInstance());
                           this.mFeatureBindingMappers.remove(var4);
                           break label48;
                        } catch (ClassNotFoundException var7) {
                           var10001 = false;
                           break label47;
                        } catch (IllegalAccessException var8) {
                           var15 = var8;
                           var10001 = false;
                           break label46;
                        } catch (InstantiationException var9) {
                           var10000 = var9;
                           var10001 = false;
                        }
                     }

                     InstantiationException var13 = var10000;
                     var6 = new StringBuilder();
                     var6.append("unable to add feature mapper for ");
                     var6.append(var4);
                     Log.e("MergedDataBinderMapper", var6.toString(), var13);
                     continue;
                  }

                  IllegalAccessException var14 = var15;
                  var6 = new StringBuilder();
                  var6.append("unable to add feature mapper for ");
                  var6.append(var4);
                  Log.e("MergedDataBinderMapper", var6.toString(), var14);
                  var2 = var1;
                  break label49;
               }

               var2 = var1;
               break label49;
            }

            var2 = true;
         }

         var1 = var2;
      }

      return var1;
   }

   public void addMapper(DataBinderMapper var1) {
      Class var2 = var1.getClass();
      if (this.mExistingMappers.add(var2)) {
         this.mMappers.add(var1);
         Iterator var3 = var1.collectDependencies().iterator();

         while(var3.hasNext()) {
            this.addMapper((DataBinderMapper)var3.next());
         }
      }

   }

   protected void addMapper(String var1) {
      List var2 = this.mFeatureBindingMappers;
      StringBuilder var3 = new StringBuilder();
      var3.append(var1);
      var3.append(".DataBinderMapperImpl");
      var2.add(var3.toString());
   }

   public String convertBrIdToString(int var1) {
      Iterator var2 = this.mMappers.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            if (this.loadFeatures()) {
               return this.convertBrIdToString(var1);
            }

            return null;
         }

         var3 = ((DataBinderMapper)var2.next()).convertBrIdToString(var1);
      } while(var3 == null);

      return var3;
   }

   public ViewDataBinding getDataBinder(DataBindingComponent var1, View var2, int var3) {
      Iterator var4 = this.mMappers.iterator();

      ViewDataBinding var5;
      do {
         if (!var4.hasNext()) {
            if (this.loadFeatures()) {
               return this.getDataBinder(var1, var2, var3);
            }

            return null;
         }

         var5 = ((DataBinderMapper)var4.next()).getDataBinder(var1, var2, var3);
      } while(var5 == null);

      return var5;
   }

   public ViewDataBinding getDataBinder(DataBindingComponent var1, View[] var2, int var3) {
      Iterator var4 = this.mMappers.iterator();

      ViewDataBinding var5;
      do {
         if (!var4.hasNext()) {
            if (this.loadFeatures()) {
               return this.getDataBinder(var1, var2, var3);
            }

            return null;
         }

         var5 = ((DataBinderMapper)var4.next()).getDataBinder(var1, var2, var3);
      } while(var5 == null);

      return var5;
   }

   public int getLayoutId(String var1) {
      Iterator var3 = this.mMappers.iterator();

      int var2;
      do {
         if (!var3.hasNext()) {
            if (this.loadFeatures()) {
               return this.getLayoutId(var1);
            }

            return 0;
         }

         var2 = ((DataBinderMapper)var3.next()).getLayoutId(var1);
      } while(var2 == 0);

      return var2;
   }
}
