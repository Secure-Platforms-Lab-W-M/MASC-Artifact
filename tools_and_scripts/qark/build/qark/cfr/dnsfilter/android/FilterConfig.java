/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnKeyListener
 *  android.content.res.Resources
 *  android.util.DisplayMetrics
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.Button
 *  android.widget.CheckBox
 *  android.widget.TableLayout
 *  android.widget.TableRow
 *  android.widget.TextView
 */
package dnsfilter.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;
import java.net.URL;
import java.util.TreeMap;

public class FilterConfig
implements View.OnClickListener,
DialogInterface.OnKeyListener {
    static String ALL_ACTIVE;
    static String ALL_CATEGORIES;
    static String INVALID_URL;
    static String NEW_ITEM;
    Button categoryDown;
    TextView categoryField;
    TreeMap<String, Integer> categoryMap;
    Button categoryUp;
    TableLayout configTable;
    Button editCancel;
    Button editDelete;
    Dialog editDialog;
    Button editOk;
    TableRow editedRow;
    FilterConfigEntry[] filterEntries;
    boolean loaded = false;

    static {
        NEW_ITEM = "<new>";
        INVALID_URL = "<invalid URL!>";
        ALL_CATEGORIES = "All Categories";
        ALL_ACTIVE = "All Active Filters";
    }

    public FilterConfig(TableLayout tableLayout, Button button, Button button2, TextView textView) {
        this.configTable = tableLayout;
        this.editDialog = new Dialog(tableLayout.getContext(), 2131034112);
        this.editDialog.setOnKeyListener((DialogInterface.OnKeyListener)this);
        this.editDialog.setContentView(2130903043);
        this.editDialog.setTitle((CharSequence)tableLayout.getContext().getResources().getString(2130968590));
        this.editOk = (Button)this.editDialog.findViewById(2130837538);
        this.editDelete = (Button)this.editDialog.findViewById(2130837537);
        this.editCancel = (Button)this.editDialog.findViewById(2130837536);
        this.editOk.setOnClickListener((View.OnClickListener)this);
        this.editDelete.setOnClickListener((View.OnClickListener)this);
        this.editCancel.setOnClickListener((View.OnClickListener)this);
        this.categoryUp = button;
        this.categoryDown = button2;
        this.categoryField = textView;
        textView.setText((CharSequence)ALL_ACTIVE);
        this.categoryMap = new TreeMap();
    }

    private void addEmptyEndItem() {
        this.addItem(new FilterConfigEntry(false, NEW_ITEM, NEW_ITEM, NEW_ITEM));
    }

    private void addItem(FilterConfigEntry filterConfigEntry) {
        TableRow tableRow = (TableRow)LayoutInflater.from((Context)this.configTable.getContext()).inflate(2130903042, null);
        this.configTable.addView((View)tableRow);
        View[] arrview = this.getContentCells(tableRow);
        ((CheckBox)arrview[0]).setChecked(filterConfigEntry.active);
        ((TextView)arrview[1]).setText((CharSequence)filterConfigEntry.category);
        ((TextView)arrview[2]).setText((CharSequence)filterConfigEntry.id);
        ((TextView)arrview[3]).setText((CharSequence)filterConfigEntry.url);
        arrview[4].setOnClickListener((View.OnClickListener)this);
        this.setVisibility(tableRow);
    }

    private View[] getContentCells(TableRow tableRow) {
        View[] arrview = new View[5];
        for (int i = 0; i < 5; ++i) {
            arrview[i] = tableRow.getChildAt(i);
        }
        arrview[2] = ((ViewGroup)arrview[2]).getChildAt(0);
        arrview[3] = ((ViewGroup)arrview[3]).getChildAt(0);
        return arrview;
    }

    private void handleCategoryChange(Button object) {
        String string2 = this.categoryField.getText().toString();
        if (!this.categoryMap.containsKey(string2)) {
            object = ALL_ACTIVE;
        } else if (object == this.categoryUp) {
            string2 = this.categoryMap.higherKey(string2);
            object = string2;
            if (string2 == null) {
                object = this.categoryMap.firstKey();
            }
        } else {
            string2 = this.categoryMap.lowerKey(string2);
            object = string2;
            if (string2 == null) {
                object = this.categoryMap.lastKey();
            }
        }
        this.categoryField.setText((CharSequence)object);
        this.updateView();
    }

    private void handleEditDialogEvent(View object) {
        if (object == this.editCancel) {
            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
            return;
        }
        View[] arrview = this.getContentCells(this.editedRow);
        boolean bl = ((TextView)arrview[2]).getText().toString().equals(NEW_ITEM);
        if (object == this.editDelete) {
            if (!bl) {
                this.editedRow.getChildAt(3).setOnClickListener(null);
                this.configTable.removeView((View)this.editedRow);
                object = ((TextView)arrview[1]).getText().toString();
                Integer n = this.categoryMap.get(object);
                if (n == 1) {
                    this.categoryMap.remove(object);
                } else {
                    this.categoryMap.put((String)object, new Integer(n - 1));
                }
            }
            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
            return;
        }
        if (object == this.editOk) {
            object = new View[]{this.editDialog.findViewById(2130837511), this.editDialog.findViewById(2130837535), this.editDialog.findViewById(2130837539), this.editDialog.findViewById(2130837541)};
            try {
                this.validateContent((View[])object);
            }
            catch (Exception exception) {
                TextView textView = (TextView)this.editDialog.findViewById(2130837534);
                textView.setVisibility(0);
                textView.setText((CharSequence)exception.getMessage());
                return;
            }
            boolean bl2 = ((CheckBox)object[0]).isChecked();
            String string2 = ((TextView)object[1]).getText().toString();
            String string3 = ((TextView)object[2]).getText().toString();
            String string4 = ((TextView)object[3]).getText().toString();
            object = ((TextView)arrview[1]).getText().toString();
            if (!object.equals(string2)) {
                Integer n;
                if (!object.equals(NEW_ITEM)) {
                    n = this.categoryMap.get(object);
                    if (n == 1) {
                        this.categoryMap.remove(object);
                    } else {
                        this.categoryMap.put((String)object, new Integer(n - 1));
                    }
                }
                n = this.categoryMap.get(string2);
                object = n;
                if (n == null) {
                    object = new Integer(0);
                }
                this.categoryMap.put(string2, new Integer(object.intValue() + 1));
            }
            ((CheckBox)arrview[0]).setChecked(bl2);
            ((TextView)arrview[1]).setText((CharSequence)string2);
            ((TextView)arrview[2]).setText((CharSequence)string3);
            ((TextView)arrview[3]).setText((CharSequence)string4);
            if (bl) {
                this.newItem(this.editedRow);
            }
            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
            return;
        }
    }

    private void newItem(TableRow tableRow) {
        this.addEmptyEndItem();
    }

    private void setVisibility(TableRow tableRow) {
        boolean bl;
        block5 : {
            String string2;
            boolean bl2;
            String string3;
            block6 : {
                string2 = this.categoryField.getText().toString();
                bl2 = true;
                string3 = ((TextView)tableRow.getVirtualChildAt(1)).getText().toString();
                boolean bl3 = ((CheckBox)tableRow.getVirtualChildAt(0)).isChecked();
                bl = bl2;
                if (string2.equals(ALL_CATEGORIES)) break block5;
                if (!string2.equals(ALL_ACTIVE)) break block6;
                bl = bl2;
                if (bl3) break block5;
            }
            bl = bl2;
            if (!string3.equals(NEW_ITEM)) {
                bl = string3.equals(string2) ? bl2 : false;
            }
        }
        if (bl) {
            tableRow.setVisibility(0);
            return;
        }
        tableRow.setVisibility(8);
    }

    private void showEditDialog(TableRow layoutParams) {
        this.editedRow = layoutParams;
        layoutParams = this.getContentCells(this.editedRow);
        ((CheckBox)this.editDialog.findViewById(2130837511)).setChecked(((CheckBox)layoutParams[0]).isChecked());
        ((TextView)this.editDialog.findViewById(2130837535)).setText((CharSequence)((TextView)layoutParams[1]).getText().toString());
        ((TextView)this.editDialog.findViewById(2130837539)).setText((CharSequence)((TextView)layoutParams[2]).getText().toString());
        ((TextView)this.editDialog.findViewById(2130837541)).setText((CharSequence)((TextView)layoutParams[3]).getText().toString());
        this.editDialog.show();
        layoutParams = this.editDialog.getWindow().getAttributes();
        layoutParams.width = (int)((double)this.configTable.getContext().getResources().getDisplayMetrics().widthPixels * 1.0);
        this.editDialog.getWindow().setAttributes(layoutParams);
    }

    private void updateView() {
        int n = this.configTable.getChildCount();
        for (int i = 0; i < n - 2; ++i) {
            this.setVisibility((TableRow)this.configTable.getChildAt(i + 1));
        }
    }

    private void validateContent(View[] arrview) throws Exception {
        block7 : {
            Object object = ((TextView)arrview[3]).getText().toString();
            if (!object.startsWith("file://")) {
                object = new URL((String)object);
                String string2 = ((TextView)arrview[2]).getText().toString().trim();
                if (string2.equals(NEW_ITEM) || string2.equals("")) {
                    ((TextView)arrview[2]).setText((CharSequence)object.getHost());
                }
                if ((string2 = ((TextView)arrview[1]).getText().toString().trim()).equals(NEW_ITEM) || string2.equals("")) {
                    ((TextView)arrview[1]).setText((CharSequence)object.getHost());
                }
                break block7;
            }
            object = new File(object.substring(7));
            String string3 = ((TextView)arrview[2]).getText().toString().trim();
            if (string3.equals(NEW_ITEM) || string3.equals("")) {
                ((TextView)arrview[2]).setText((CharSequence)object.getName());
            }
            if ((string3 = ((TextView)arrview[1]).getText().toString().trim()).equals(NEW_ITEM) || string3.equals("")) {
                ((TextView)arrview[1]).setText((CharSequence)object.getName());
            }
            return;
        }
    }

    public void clear() {
        this.filterEntries = this.getFilterEntries();
        for (int i = this.configTable.getChildCount() - 1; i > 0; --i) {
            TableRow tableRow = (TableRow)this.configTable.getChildAt(i);
            tableRow.getChildAt(4).setOnClickListener(null);
            this.configTable.removeView((View)tableRow);
        }
        this.categoryDown.setOnClickListener(null);
        this.categoryUp.setOnClickListener(null);
        this.loaded = false;
    }

    public String getCurrentCategory() {
        return this.categoryField.getText().toString();
    }

    public FilterConfigEntry[] getFilterEntries() {
        if (!this.loaded) {
            return this.filterEntries;
        }
        int n = this.configTable.getChildCount() - 2;
        FilterConfigEntry[] arrfilterConfigEntry = new FilterConfigEntry[n];
        for (int i = 0; i < n; ++i) {
            View[] arrview = this.getContentCells((TableRow)this.configTable.getChildAt(i + 1));
            arrfilterConfigEntry[i] = new FilterConfigEntry(((CheckBox)arrview[0]).isChecked(), ((TextView)arrview[1]).getText().toString().trim(), ((TextView)arrview[2]).getText().toString().trim(), ((TextView)arrview[3]).getText().toString().trim());
        }
        return arrfilterConfigEntry;
    }

    public void load() {
        if (this.loaded) {
            return;
        }
        for (int i = 0; i < this.filterEntries.length; ++i) {
            this.addItem(this.filterEntries[i]);
        }
        this.addEmptyEndItem();
        this.categoryDown.setOnClickListener((View.OnClickListener)this);
        this.categoryUp.setOnClickListener((View.OnClickListener)this);
        this.loaded = true;
    }

    public void onClick(View view) {
        if (view != this.editOk && view != this.editDelete && view != this.editCancel) {
            if (view != this.categoryUp && view != this.categoryDown) {
                this.showEditDialog((TableRow)view.getParent());
                return;
            }
            this.handleCategoryChange((Button)view);
            return;
        }
        this.handleEditDialogEvent(view);
    }

    public boolean onKey(DialogInterface dialogInterface, int n, KeyEvent keyEvent) {
        if (n == 4 || n == 3) {
            dialogInterface.dismiss();
        }
        return false;
    }

    public void setCurrentCategory(String string2) {
        this.categoryField.setText((CharSequence)string2);
    }

    public void setEntries(FilterConfigEntry[] object) {
        this.filterEntries = object;
        this.categoryMap.clear();
        this.categoryMap.put(ALL_ACTIVE, new Integer(0));
        this.categoryMap.put(ALL_CATEGORIES, new Integer(0));
        for (int i = 0; i < this.filterEntries.length; ++i) {
            Integer n = this.categoryMap.get(this.filterEntries[i].category);
            object = n;
            if (n == null) {
                object = new Integer(0);
            }
            object = new Integer(object.intValue() + 1);
            this.categoryMap.put(this.filterEntries[i].category, (Integer)object);
        }
    }

    public static class FilterConfigEntry {
        boolean active;
        String category;
        String id;
        String url;

        public FilterConfigEntry(boolean bl, String string2, String string3, String string4) {
            this.active = bl;
            this.category = string2;
            this.id = string3;
            this.url = string4;
        }
    }

}

