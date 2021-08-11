package dnsfilter.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

public class FilterConfig implements OnClickListener, OnKeyListener {
   static String ALL_ACTIVE = "All Active Filters";
   static String ALL_CATEGORIES = "All Categories";
   static String INVALID_URL = "<invalid URL!>";
   static String NEW_ITEM = "<new>";
   Button categoryDown;
   TextView categoryField;
   TreeMap categoryMap;
   Button categoryUp;
   TableLayout configTable;
   Button editCancel;
   Button editDelete;
   Dialog editDialog;
   Button editOk;
   TableRow editedRow;
   FilterConfig.FilterConfigEntry[] filterEntries;
   boolean loaded = false;

   public FilterConfig(TableLayout var1, Button var2, Button var3, TextView var4) {
      this.configTable = var1;
      this.editDialog = new Dialog(var1.getContext(), 2131034112);
      this.editDialog.setOnKeyListener(this);
      this.editDialog.setContentView(2130903043);
      this.editDialog.setTitle(var1.getContext().getResources().getString(2130968590));
      this.editOk = (Button)this.editDialog.findViewById(2130837538);
      this.editDelete = (Button)this.editDialog.findViewById(2130837537);
      this.editCancel = (Button)this.editDialog.findViewById(2130837536);
      this.editOk.setOnClickListener(this);
      this.editDelete.setOnClickListener(this);
      this.editCancel.setOnClickListener(this);
      this.categoryUp = var2;
      this.categoryDown = var3;
      this.categoryField = var4;
      var4.setText(ALL_ACTIVE);
      this.categoryMap = new TreeMap();
   }

   private void addEmptyEndItem() {
      this.addItem(new FilterConfig.FilterConfigEntry(false, NEW_ITEM, NEW_ITEM, NEW_ITEM));
   }

   private void addItem(FilterConfig.FilterConfigEntry var1) {
      TableRow var2 = (TableRow)LayoutInflater.from(this.configTable.getContext()).inflate(2130903042, (ViewGroup)null);
      this.configTable.addView(var2);
      View[] var3 = this.getContentCells(var2);
      ((CheckBox)var3[0]).setChecked(var1.active);
      ((TextView)var3[1]).setText(var1.category);
      ((TextView)var3[2]).setText(var1.field_8);
      ((TextView)var3[3]).setText(var1.url);
      var3[4].setOnClickListener(this);
      this.setVisibility(var2);
   }

   private View[] getContentCells(TableRow var1) {
      View[] var3 = new View[5];

      for(int var2 = 0; var2 < 5; ++var2) {
         var3[var2] = var1.getChildAt(var2);
      }

      var3[2] = ((ViewGroup)var3[2]).getChildAt(0);
      var3[3] = ((ViewGroup)var3[3]).getChildAt(0);
      return var3;
   }

   private void handleCategoryChange(Button var1) {
      String var2 = this.categoryField.getText().toString();
      String var3;
      if (!this.categoryMap.containsKey(var2)) {
         var3 = ALL_ACTIVE;
      } else if (var1 == this.categoryUp) {
         var2 = (String)this.categoryMap.higherKey(var2);
         var3 = var2;
         if (var2 == null) {
            var3 = (String)this.categoryMap.firstKey();
         }
      } else {
         var2 = (String)this.categoryMap.lowerKey(var2);
         var3 = var2;
         if (var2 == null) {
            var3 = (String)this.categoryMap.lastKey();
         }
      }

      this.categoryField.setText(var3);
      this.updateView();
   }

   private void handleEditDialogEvent(View var1) {
      if (var1 == this.editCancel) {
         this.editDialog.dismiss();
         this.editDialog.findViewById(2130837534).setVisibility(8);
      } else {
         View[] var5 = this.getContentCells(this.editedRow);
         boolean var2 = ((TextView)var5[2]).getText().toString().equals(NEW_ITEM);
         String var11;
         Integer var13;
         if (var1 == this.editDelete) {
            if (!var2) {
               this.editedRow.getChildAt(3).setOnClickListener((OnClickListener)null);
               this.configTable.removeView(this.editedRow);
               var11 = ((TextView)var5[1]).getText().toString();
               var13 = (Integer)this.categoryMap.get(var11);
               if (var13 == 1) {
                  this.categoryMap.remove(var11);
               } else {
                  this.categoryMap.put(var11, new Integer(var13 - 1));
               }
            }

            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
         } else if (var1 == this.editOk) {
            View[] var10 = new View[]{this.editDialog.findViewById(2130837511), this.editDialog.findViewById(2130837535), this.editDialog.findViewById(2130837539), this.editDialog.findViewById(2130837541)};

            try {
               this.validateContent(var10);
            } catch (Exception var9) {
               TextView var4 = (TextView)this.editDialog.findViewById(2130837534);
               var4.setVisibility(0);
               var4.setText(var9.getMessage());
               return;
            }

            boolean var3 = ((CheckBox)var10[0]).isChecked();
            String var6 = ((TextView)var10[1]).getText().toString();
            String var7 = ((TextView)var10[2]).getText().toString();
            String var8 = ((TextView)var10[3]).getText().toString();
            var11 = ((TextView)var5[1]).getText().toString();
            if (!var11.equals(var6)) {
               if (!var11.equals(NEW_ITEM)) {
                  var13 = (Integer)this.categoryMap.get(var11);
                  if (var13 == 1) {
                     this.categoryMap.remove(var11);
                  } else {
                     this.categoryMap.put(var11, new Integer(var13 - 1));
                  }
               }

               var13 = (Integer)this.categoryMap.get(var6);
               Integer var12 = var13;
               if (var13 == null) {
                  var12 = new Integer(0);
               }

               this.categoryMap.put(var6, new Integer(var12 + 1));
            }

            ((CheckBox)var5[0]).setChecked(var3);
            ((TextView)var5[1]).setText(var6);
            ((TextView)var5[2]).setText(var7);
            ((TextView)var5[3]).setText(var8);
            if (var2) {
               this.newItem(this.editedRow);
            }

            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
         }
      }
   }

   private void newItem(TableRow var1) {
      this.addEmptyEndItem();
   }

   private void setVisibility(TableRow var1) {
      String var5 = this.categoryField.getText().toString();
      boolean var3 = true;
      String var6 = ((TextView)var1.getVirtualChildAt(1)).getText().toString();
      boolean var4 = ((CheckBox)var1.getVirtualChildAt(0)).isChecked();
      boolean var2 = var3;
      if (!var5.equals(ALL_CATEGORIES)) {
         label21: {
            if (var5.equals(ALL_ACTIVE)) {
               var2 = var3;
               if (var4) {
                  break label21;
               }
            }

            var2 = var3;
            if (!var6.equals(NEW_ITEM)) {
               if (var6.equals(var5)) {
                  var2 = var3;
               } else {
                  var2 = false;
               }
            }
         }
      }

      if (var2) {
         var1.setVisibility(0);
      } else {
         var1.setVisibility(8);
      }
   }

   private void showEditDialog(TableRow var1) {
      this.editedRow = var1;
      View[] var2 = this.getContentCells(this.editedRow);
      ((CheckBox)this.editDialog.findViewById(2130837511)).setChecked(((CheckBox)var2[0]).isChecked());
      ((TextView)this.editDialog.findViewById(2130837535)).setText(((TextView)var2[1]).getText().toString());
      ((TextView)this.editDialog.findViewById(2130837539)).setText(((TextView)var2[2]).getText().toString());
      ((TextView)this.editDialog.findViewById(2130837541)).setText(((TextView)var2[3]).getText().toString());
      this.editDialog.show();
      LayoutParams var3 = this.editDialog.getWindow().getAttributes();
      var3.width = (int)((double)this.configTable.getContext().getResources().getDisplayMetrics().widthPixels * 1.0D);
      this.editDialog.getWindow().setAttributes(var3);
   }

   private void updateView() {
      int var2 = this.configTable.getChildCount();

      for(int var1 = 0; var1 < var2 - 2; ++var1) {
         this.setVisibility((TableRow)this.configTable.getChildAt(var1 + 1));
      }

   }

   private void validateContent(View[] var1) throws Exception {
      try {
         String var2 = ((TextView)var1[3]).getText().toString();
         String var3;
         if (!var2.startsWith("file://")) {
            URL var6 = new URL(var2);
            var3 = ((TextView)var1[2]).getText().toString().trim();
            if (var3.equals(NEW_ITEM) || var3.equals("")) {
               ((TextView)var1[2]).setText(var6.getHost());
            }

            var3 = ((TextView)var1[1]).getText().toString().trim();
            if (var3.equals(NEW_ITEM) || var3.equals("")) {
               ((TextView)var1[1]).setText(var6.getHost());
            }

         } else {
            File var5 = new File(var2.substring(7));
            var3 = ((TextView)var1[2]).getText().toString().trim();
            if (var3.equals(NEW_ITEM) || var3.equals("")) {
               ((TextView)var1[2]).setText(var5.getName());
            }

            var3 = ((TextView)var1[1]).getText().toString().trim();
            if (var3.equals(NEW_ITEM) || var3.equals("")) {
               ((TextView)var1[1]).setText(var5.getName());
            }

         }
      } catch (MalformedURLException var4) {
         throw var4;
      }
   }

   public void clear() {
      this.filterEntries = this.getFilterEntries();

      for(int var1 = this.configTable.getChildCount() - 1; var1 > 0; --var1) {
         TableRow var2 = (TableRow)this.configTable.getChildAt(var1);
         var2.getChildAt(4).setOnClickListener((OnClickListener)null);
         this.configTable.removeView(var2);
      }

      this.categoryDown.setOnClickListener((OnClickListener)null);
      this.categoryUp.setOnClickListener((OnClickListener)null);
      this.loaded = false;
   }

   public String getCurrentCategory() {
      return this.categoryField.getText().toString();
   }

   public FilterConfig.FilterConfigEntry[] getFilterEntries() {
      if (!this.loaded) {
         return this.filterEntries;
      } else {
         int var2 = this.configTable.getChildCount() - 2;
         FilterConfig.FilterConfigEntry[] var3 = new FilterConfig.FilterConfigEntry[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            View[] var4 = this.getContentCells((TableRow)this.configTable.getChildAt(var1 + 1));
            var3[var1] = new FilterConfig.FilterConfigEntry(((CheckBox)var4[0]).isChecked(), ((TextView)var4[1]).getText().toString().trim(), ((TextView)var4[2]).getText().toString().trim(), ((TextView)var4[3]).getText().toString().trim());
         }

         return var3;
      }
   }

   public void load() {
      if (!this.loaded) {
         for(int var1 = 0; var1 < this.filterEntries.length; ++var1) {
            this.addItem(this.filterEntries[var1]);
         }

         this.addEmptyEndItem();
         this.categoryDown.setOnClickListener(this);
         this.categoryUp.setOnClickListener(this);
         this.loaded = true;
      }
   }

   public void onClick(View var1) {
      if (var1 != this.editOk && var1 != this.editDelete && var1 != this.editCancel) {
         if (var1 != this.categoryUp && var1 != this.categoryDown) {
            this.showEditDialog((TableRow)var1.getParent());
         } else {
            this.handleCategoryChange((Button)var1);
         }
      } else {
         this.handleEditDialogEvent(var1);
      }
   }

   public boolean onKey(DialogInterface var1, int var2, KeyEvent var3) {
      if (var2 == 4 || var2 == 3) {
         var1.dismiss();
      }

      return false;
   }

   public void setCurrentCategory(String var1) {
      this.categoryField.setText(var1);
   }

   public void setEntries(FilterConfig.FilterConfigEntry[] var1) {
      this.filterEntries = var1;
      this.categoryMap.clear();
      this.categoryMap.put(ALL_ACTIVE, new Integer(0));
      this.categoryMap.put(ALL_CATEGORIES, new Integer(0));

      for(int var2 = 0; var2 < this.filterEntries.length; ++var2) {
         Integer var3 = (Integer)this.categoryMap.get(this.filterEntries[var2].category);
         Integer var4 = var3;
         if (var3 == null) {
            var4 = new Integer(0);
         }

         var4 = new Integer(var4 + 1);
         this.categoryMap.put(this.filterEntries[var2].category, var4);
      }

   }

   public static class FilterConfigEntry {
      boolean active;
      String category;
      // $FF: renamed from: id java.lang.String
      String field_8;
      String url;

      public FilterConfigEntry(boolean var1, String var2, String var3, String var4) {
         this.active = var1;
         this.category = var2;
         this.field_8 = var3;
         this.url = var4;
      }
   }
}
