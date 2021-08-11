// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import android.view.KeyEvent;
import android.content.DialogInterface;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import android.view.WindowManager$LayoutParams;
import android.widget.CheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TableRow;
import android.app.Dialog;
import android.widget.TableLayout;
import java.util.TreeMap;
import android.widget.TextView;
import android.widget.Button;
import android.content.DialogInterface$OnKeyListener;
import android.view.View$OnClickListener;

public class FilterConfig implements View$OnClickListener, DialogInterface$OnKeyListener
{
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
    boolean loaded;
    
    static {
        FilterConfig.NEW_ITEM = "<new>";
        FilterConfig.INVALID_URL = "<invalid URL!>";
        FilterConfig.ALL_CATEGORIES = "All Categories";
        FilterConfig.ALL_ACTIVE = "All Active Filters";
    }
    
    public FilterConfig(final TableLayout configTable, final Button categoryUp, final Button categoryDown, final TextView categoryField) {
        this.loaded = false;
        this.configTable = configTable;
        (this.editDialog = new Dialog(configTable.getContext(), 2131034112)).setOnKeyListener((DialogInterface$OnKeyListener)this);
        this.editDialog.setContentView(2130903043);
        this.editDialog.setTitle((CharSequence)configTable.getContext().getResources().getString(2130968590));
        this.editOk = (Button)this.editDialog.findViewById(2130837538);
        this.editDelete = (Button)this.editDialog.findViewById(2130837537);
        this.editCancel = (Button)this.editDialog.findViewById(2130837536);
        this.editOk.setOnClickListener((View$OnClickListener)this);
        this.editDelete.setOnClickListener((View$OnClickListener)this);
        this.editCancel.setOnClickListener((View$OnClickListener)this);
        this.categoryUp = categoryUp;
        this.categoryDown = categoryDown;
        (this.categoryField = categoryField).setText((CharSequence)FilterConfig.ALL_ACTIVE);
        this.categoryMap = new TreeMap<String, Integer>();
    }
    
    private void addEmptyEndItem() {
        this.addItem(new FilterConfigEntry(false, FilterConfig.NEW_ITEM, FilterConfig.NEW_ITEM, FilterConfig.NEW_ITEM));
    }
    
    private void addItem(final FilterConfigEntry filterConfigEntry) {
        final TableRow visibility = (TableRow)LayoutInflater.from(this.configTable.getContext()).inflate(2130903042, (ViewGroup)null);
        this.configTable.addView((View)visibility);
        final View[] contentCells = this.getContentCells(visibility);
        ((CheckBox)contentCells[0]).setChecked(filterConfigEntry.active);
        ((TextView)contentCells[1]).setText((CharSequence)filterConfigEntry.category);
        ((TextView)contentCells[2]).setText((CharSequence)filterConfigEntry.id);
        ((TextView)contentCells[3]).setText((CharSequence)filterConfigEntry.url);
        contentCells[4].setOnClickListener((View$OnClickListener)this);
        this.setVisibility(visibility);
    }
    
    private View[] getContentCells(final TableRow tableRow) {
        final View[] array = new View[5];
        for (int i = 0; i < 5; ++i) {
            array[i] = tableRow.getChildAt(i);
        }
        array[2] = ((ViewGroup)array[2]).getChildAt(0);
        array[3] = ((ViewGroup)array[3]).getChildAt(0);
        return array;
    }
    
    private void handleCategoryChange(final Button button) {
        final String string = this.categoryField.getText().toString();
        String all_ACTIVE;
        if (!this.categoryMap.containsKey(string)) {
            all_ACTIVE = FilterConfig.ALL_ACTIVE;
        }
        else if (button == this.categoryUp) {
            if ((all_ACTIVE = this.categoryMap.higherKey(string)) == null) {
                all_ACTIVE = this.categoryMap.firstKey();
            }
        }
        else if ((all_ACTIVE = this.categoryMap.lowerKey(string)) == null) {
            all_ACTIVE = this.categoryMap.lastKey();
        }
        this.categoryField.setText((CharSequence)all_ACTIVE);
        this.updateView();
    }
    
    private void handleEditDialogEvent(final View view) {
        if (view == this.editCancel) {
            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
            return;
        }
        final View[] contentCells = this.getContentCells(this.editedRow);
        final boolean equals = ((TextView)contentCells[2]).getText().toString().equals(FilterConfig.NEW_ITEM);
        if (view == this.editDelete) {
            if (!equals) {
                this.editedRow.getChildAt(3).setOnClickListener((View$OnClickListener)null);
                this.configTable.removeView((View)this.editedRow);
                final String string = ((TextView)contentCells[1]).getText().toString();
                final Integer n = this.categoryMap.get(string);
                if (n == 1) {
                    this.categoryMap.remove(string);
                }
                else {
                    this.categoryMap.put(string, new Integer(n - 1));
                }
            }
            this.editDialog.dismiss();
            this.editDialog.findViewById(2130837534).setVisibility(8);
            return;
        }
        if (view == this.editOk) {
            final View[] array = { this.editDialog.findViewById(2130837511), this.editDialog.findViewById(2130837535), this.editDialog.findViewById(2130837539), this.editDialog.findViewById(2130837541) };
            try {
                this.validateContent(array);
                final boolean checked = ((CheckBox)array[0]).isChecked();
                final String string2 = ((TextView)array[1]).getText().toString();
                final String string3 = ((TextView)array[2]).getText().toString();
                final String string4 = ((TextView)array[3]).getText().toString();
                final String string5 = ((TextView)contentCells[1]).getText().toString();
                if (!string5.equals(string2)) {
                    if (!string5.equals(FilterConfig.NEW_ITEM)) {
                        final Integer n2 = this.categoryMap.get(string5);
                        if (n2 == 1) {
                            this.categoryMap.remove(string5);
                        }
                        else {
                            this.categoryMap.put(string5, new Integer(n2 - 1));
                        }
                    }
                    Integer n3;
                    if ((n3 = this.categoryMap.get(string2)) == null) {
                        n3 = new Integer(0);
                    }
                    this.categoryMap.put(string2, new Integer(n3 + 1));
                }
                ((CheckBox)contentCells[0]).setChecked(checked);
                ((TextView)contentCells[1]).setText((CharSequence)string2);
                ((TextView)contentCells[2]).setText((CharSequence)string3);
                ((TextView)contentCells[3]).setText((CharSequence)string4);
                if (equals) {
                    this.newItem(this.editedRow);
                }
                this.editDialog.dismiss();
                this.editDialog.findViewById(2130837534).setVisibility(8);
            }
            catch (Exception ex) {
                final TextView textView = (TextView)this.editDialog.findViewById(2130837534);
                textView.setVisibility(0);
                textView.setText((CharSequence)ex.getMessage());
            }
        }
    }
    
    private void newItem(final TableRow tableRow) {
        this.addEmptyEndItem();
    }
    
    private void setVisibility(final TableRow tableRow) {
        final String string = this.categoryField.getText().toString();
        final boolean b = true;
        final String string2 = ((TextView)tableRow.getVirtualChildAt(1)).getText().toString();
        final boolean checked = ((CheckBox)tableRow.getVirtualChildAt(0)).isChecked();
        boolean b2 = b;
        Label_0108: {
            if (!string.equals(FilterConfig.ALL_CATEGORIES)) {
                if (string.equals(FilterConfig.ALL_ACTIVE)) {
                    b2 = b;
                    if (checked) {
                        break Label_0108;
                    }
                }
                b2 = b;
                if (!string2.equals(FilterConfig.NEW_ITEM)) {
                    b2 = (string2.equals(string) && b);
                }
            }
        }
        if (b2) {
            tableRow.setVisibility(0);
            return;
        }
        tableRow.setVisibility(8);
    }
    
    private void showEditDialog(final TableRow editedRow) {
        this.editedRow = editedRow;
        final View[] contentCells = this.getContentCells(this.editedRow);
        ((CheckBox)this.editDialog.findViewById(2130837511)).setChecked(((CheckBox)contentCells[0]).isChecked());
        ((TextView)this.editDialog.findViewById(2130837535)).setText((CharSequence)((TextView)contentCells[1]).getText().toString());
        ((TextView)this.editDialog.findViewById(2130837539)).setText((CharSequence)((TextView)contentCells[2]).getText().toString());
        ((TextView)this.editDialog.findViewById(2130837541)).setText((CharSequence)((TextView)contentCells[3]).getText().toString());
        this.editDialog.show();
        final WindowManager$LayoutParams attributes = this.editDialog.getWindow().getAttributes();
        attributes.width = (int)(this.configTable.getContext().getResources().getDisplayMetrics().widthPixels * 1.0);
        this.editDialog.getWindow().setAttributes(attributes);
    }
    
    private void updateView() {
        for (int childCount = this.configTable.getChildCount(), i = 0; i < childCount - 2; ++i) {
            this.setVisibility((TableRow)this.configTable.getChildAt(i + 1));
        }
    }
    
    private void validateContent(final View[] array) throws Exception {
        try {
            final String string = ((TextView)array[3]).getText().toString();
            if (string.startsWith("file://")) {
                final File file = new File(string.substring(7));
                final String trim = ((TextView)array[2]).getText().toString().trim();
                if (trim.equals(FilterConfig.NEW_ITEM) || trim.equals("")) {
                    ((TextView)array[2]).setText((CharSequence)file.getName());
                }
                final String trim2 = ((TextView)array[1]).getText().toString().trim();
                if (trim2.equals(FilterConfig.NEW_ITEM) || trim2.equals("")) {
                    ((TextView)array[1]).setText((CharSequence)file.getName());
                }
                return;
            }
            final URL url = new URL(string);
            final String trim3 = ((TextView)array[2]).getText().toString().trim();
            if (trim3.equals(FilterConfig.NEW_ITEM) || trim3.equals("")) {
                ((TextView)array[2]).setText((CharSequence)url.getHost());
            }
            final String trim4 = ((TextView)array[1]).getText().toString().trim();
            if (trim4.equals(FilterConfig.NEW_ITEM) || trim4.equals("")) {
                ((TextView)array[1]).setText((CharSequence)url.getHost());
            }
        }
        catch (MalformedURLException ex) {
            throw ex;
        }
    }
    
    public void clear() {
        this.filterEntries = this.getFilterEntries();
        for (int i = this.configTable.getChildCount() - 1; i > 0; --i) {
            final TableRow tableRow = (TableRow)this.configTable.getChildAt(i);
            tableRow.getChildAt(4).setOnClickListener((View$OnClickListener)null);
            this.configTable.removeView((View)tableRow);
        }
        this.categoryDown.setOnClickListener((View$OnClickListener)null);
        this.categoryUp.setOnClickListener((View$OnClickListener)null);
        this.loaded = false;
    }
    
    public String getCurrentCategory() {
        return this.categoryField.getText().toString();
    }
    
    public FilterConfigEntry[] getFilterEntries() {
        if (!this.loaded) {
            return this.filterEntries;
        }
        final int n = this.configTable.getChildCount() - 2;
        final FilterConfigEntry[] array = new FilterConfigEntry[n];
        for (int i = 0; i < n; ++i) {
            final View[] contentCells = this.getContentCells((TableRow)this.configTable.getChildAt(i + 1));
            array[i] = new FilterConfigEntry(((CheckBox)contentCells[0]).isChecked(), ((TextView)contentCells[1]).getText().toString().trim(), ((TextView)contentCells[2]).getText().toString().trim(), ((TextView)contentCells[3]).getText().toString().trim());
        }
        return array;
    }
    
    public void load() {
        if (this.loaded) {
            return;
        }
        for (int i = 0; i < this.filterEntries.length; ++i) {
            this.addItem(this.filterEntries[i]);
        }
        this.addEmptyEndItem();
        this.categoryDown.setOnClickListener((View$OnClickListener)this);
        this.categoryUp.setOnClickListener((View$OnClickListener)this);
        this.loaded = true;
    }
    
    public void onClick(final View view) {
        if (view == this.editOk || view == this.editDelete || view == this.editCancel) {
            this.handleEditDialogEvent(view);
            return;
        }
        if (view != this.categoryUp && view != this.categoryDown) {
            this.showEditDialog((TableRow)view.getParent());
            return;
        }
        this.handleCategoryChange((Button)view);
    }
    
    public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
        if (n == 4 || n == 3) {
            dialogInterface.dismiss();
        }
        return false;
    }
    
    public void setCurrentCategory(final String text) {
        this.categoryField.setText((CharSequence)text);
    }
    
    public void setEntries(final FilterConfigEntry[] filterEntries) {
        this.filterEntries = filterEntries;
        this.categoryMap.clear();
        this.categoryMap.put(FilterConfig.ALL_ACTIVE, new Integer(0));
        this.categoryMap.put(FilterConfig.ALL_CATEGORIES, new Integer(0));
        for (int i = 0; i < this.filterEntries.length; ++i) {
            Integer n;
            if ((n = this.categoryMap.get(this.filterEntries[i].category)) == null) {
                n = new Integer(0);
            }
            this.categoryMap.put(this.filterEntries[i].category, new Integer(n + 1));
        }
    }
    
    public static class FilterConfigEntry
    {
        boolean active;
        String category;
        String id;
        String url;
        
        public FilterConfigEntry(final boolean active, final String category, final String id, final String url) {
            this.active = active;
            this.category = category;
            this.id = id;
            this.url = url;
        }
    }
}
