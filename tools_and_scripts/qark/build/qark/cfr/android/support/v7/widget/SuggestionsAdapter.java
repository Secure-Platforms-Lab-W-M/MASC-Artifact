/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.SearchManager
 *  android.app.SearchableInfo
 *  android.content.ComponentName
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.database.Cursor
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Bundle
 *  android.text.SpannableString
 *  android.text.TextUtils
 *  android.text.style.TextAppearanceSpan
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter
extends ResourceCursorAdapter
implements View.OnClickListener {
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed = false;
    private final int mCommitIconResId;
    private int mFlagsCol = -1;
    private int mIconName1Col = -1;
    private int mIconName2Col = -1;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement = 1;
    private final SearchManager mSearchManager;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col = -1;
    private int mText2Col = -1;
    private int mText2UrlCol = -1;
    private ColorStateList mUrlColor;

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchableInfo, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.mSearchManager = (SearchManager)this.mContext.getSystemService("search");
        this.mSearchView = searchView;
        this.mSearchable = searchableInfo;
        this.mCommitIconResId = searchView.getSuggestionCommitIconResId();
        this.mProviderContext = context;
        this.mOutsideDrawablesCache = weakHashMap;
    }

    private Drawable checkIconCache(String string2) {
        if ((string2 = this.mOutsideDrawablesCache.get(string2)) == null) {
            return null;
        }
        return string2.newDrawable();
    }

    private CharSequence formatUrl(CharSequence charSequence) {
        TypedValue typedValue;
        if (this.mUrlColor == null) {
            typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(typedValue.resourceId);
        }
        typedValue = new SpannableString(charSequence);
        typedValue.setSpan((Object)new TextAppearanceSpan(null, 0, 0, this.mUrlColor, null), 0, charSequence.length(), 33);
        return typedValue;
    }

    private Drawable getActivityIcon(ComponentName componentName) {
        ActivityInfo activityInfo;
        Object object = this.mContext.getPackageManager();
        try {
            activityInfo = object.getActivityInfo(componentName, 128);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.w((String)"SuggestionsAdapter", (String)nameNotFoundException.toString());
            return null;
        }
        int n = activityInfo.getIconResource();
        if (n == 0) {
            return null;
        }
        if ((object = object.getDrawable(componentName.getPackageName(), n, activityInfo.applicationInfo)) == null) {
            object = new StringBuilder();
            object.append("Invalid icon resource ");
            object.append(n);
            object.append(" for ");
            object.append(componentName.flattenToShortString());
            Log.w((String)"SuggestionsAdapter", (String)object.toString());
            return null;
        }
        return object;
    }

    private Drawable getActivityIconWithCache(ComponentName componentName) {
        String string2 = componentName.flattenToShortString();
        boolean bl = this.mOutsideDrawablesCache.containsKey(string2);
        Object var3_4 = null;
        if (bl) {
            componentName = this.mOutsideDrawablesCache.get(string2);
            if (componentName == null) {
                return null;
            }
            return componentName.newDrawable(this.mProviderContext.getResources());
        }
        Drawable drawable2 = this.getActivityIcon(componentName);
        componentName = drawable2 == null ? var3_4 : drawable2.getConstantState();
        this.mOutsideDrawablesCache.put(string2, (Drawable.ConstantState)componentName);
        return drawable2;
    }

    public static String getColumnString(Cursor cursor, String string2) {
        return SuggestionsAdapter.getStringOrNull(cursor, cursor.getColumnIndex(string2));
    }

    private Drawable getDefaultIcon1(Cursor cursor) {
        cursor = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (cursor != null) {
            return cursor;
        }
        return this.mContext.getPackageManager().getDefaultActivityIcon();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Drawable getDrawable(Uri var1_1) {
        try {
            var2_2 = "android.resource".equals(var1_1.getScheme());
            if (var2_2) {
                try {
                    return this.getDrawableFromResourceUri(var1_1);
                }
                catch (Resources.NotFoundException var3_4) {
                    var3_5 = new StringBuilder();
                    var3_5.append("Resource does not exist: ");
                    var3_5.append((Object)var1_1);
                    throw new FileNotFoundException(var3_5.toString());
                }
            }
            var4_10 = this.mProviderContext.getContentResolver().openInputStream(var1_1);
            if (var4_10 == null) ** GOTO lbl43
        }
        catch (FileNotFoundException var3_9) {
            var4_10 = new StringBuilder();
            var4_10.append("Icon not found: ");
            var4_10.append((Object)var1_1);
            var4_10.append(", ");
            var4_10.append(var3_9.getMessage());
            Log.w((String)"SuggestionsAdapter", (String)var4_10.toString());
            return null;
        }
        var3_6 = Drawable.createFromStream((InputStream)var4_10, (String)null);
        {
            catch (Throwable var3_7) {
                try {
                    var4_10.close();
                    throw var3_7;
                }
                catch (IOException var4_12) {
                    var5_14 = new StringBuilder();
                    var5_14.append("Error closing icon stream for ");
                    var5_14.append((Object)var1_1);
                    Log.e((String)"SuggestionsAdapter", (String)var5_14.toString(), (Throwable)var4_12);
                }
                throw var3_7;
            }
        }
        try {
            var4_10.close();
            return var3_6;
        }
        catch (IOException var4_11) {
            var5_13 = new StringBuilder();
            var5_13.append("Error closing icon stream for ");
            var5_13.append((Object)var1_1);
            Log.e((String)"SuggestionsAdapter", (String)var5_13.toString(), (Throwable)var4_11);
            return var3_6;
        }
lbl43: // 1 sources:
        var3_8 = new StringBuilder();
        var3_8.append("Failed to open ");
        var3_8.append((Object)var1_1);
        throw new FileNotFoundException(var3_8.toString());
    }

    private Drawable getDrawableFromResourceValue(String string2) {
        if (string2 != null && !string2.isEmpty()) {
            Drawable drawable2;
            int n;
            block7 : {
                if ("0".equals(string2)) {
                    return null;
                }
                n = Integer.parseInt(string2);
                CharSequence charSequence = new StringBuilder();
                charSequence.append("android.resource://");
                charSequence.append(this.mProviderContext.getPackageName());
                charSequence.append("/");
                charSequence.append(n);
                charSequence = charSequence.toString();
                drawable2 = this.checkIconCache((String)charSequence);
                if (drawable2 == null) break block7;
                return drawable2;
            }
            try {
                CharSequence charSequence;
                drawable2 = ContextCompat.getDrawable(this.mProviderContext, n);
                this.storeInIconCache((String)charSequence, drawable2);
                return drawable2;
            }
            catch (Resources.NotFoundException notFoundException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Icon resource not found: ");
                stringBuilder.append(string2);
                Log.w((String)"SuggestionsAdapter", (String)stringBuilder.toString());
                return null;
            }
            catch (NumberFormatException numberFormatException) {
                Drawable drawable3 = this.checkIconCache(string2);
                if (drawable3 != null) {
                    return drawable3;
                }
                drawable3 = this.getDrawable(Uri.parse((String)string2));
                this.storeInIconCache(string2, drawable3);
                return drawable3;
            }
        }
        return null;
    }

    private Drawable getIcon1(Cursor cursor) {
        int n = this.mIconName1Col;
        if (n == -1) {
            return null;
        }
        Drawable drawable2 = this.getDrawableFromResourceValue(cursor.getString(n));
        if (drawable2 != null) {
            return drawable2;
        }
        return this.getDefaultIcon1(cursor);
    }

    private Drawable getIcon2(Cursor cursor) {
        int n = this.mIconName2Col;
        if (n == -1) {
            return null;
        }
        return this.getDrawableFromResourceValue(cursor.getString(n));
    }

    private static String getStringOrNull(Cursor object, int n) {
        if (n == -1) {
            return null;
        }
        try {
            object = object.getString(n);
            return object;
        }
        catch (Exception exception) {
            Log.e((String)"SuggestionsAdapter", (String)"unexpected error retrieving valid column from cursor, did the remote process die?", (Throwable)exception);
            return null;
        }
    }

    private void setViewDrawable(ImageView imageView, Drawable drawable2, int n) {
        imageView.setImageDrawable(drawable2);
        if (drawable2 == null) {
            imageView.setVisibility(n);
            return;
        }
        imageView.setVisibility(0);
        drawable2.setVisible(false, false);
        drawable2.setVisible(true, false);
    }

    private void setViewText(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
        if (TextUtils.isEmpty((CharSequence)charSequence)) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
    }

    private void storeInIconCache(String string2, Drawable drawable2) {
        if (drawable2 != null) {
            this.mOutsideDrawablesCache.put(string2, drawable2.getConstantState());
            return;
        }
    }

    private void updateSpinnerState(Cursor cursor) {
        cursor = cursor != null ? cursor.getExtras() : null;
        if (cursor != null) {
            if (cursor.getBoolean("in_progress")) {
                return;
            }
            return;
        }
    }

    @Override
    public void bindView(View object, Context object2, Cursor cursor) {
        object2 = (ChildViewCache)object.getTag();
        int n = 0;
        int n2 = this.mFlagsCol;
        if (n2 != -1) {
            n = cursor.getInt(n2);
        }
        if (object2.mText1 != null) {
            object = SuggestionsAdapter.getStringOrNull(cursor, this.mText1Col);
            this.setViewText(object2.mText1, (CharSequence)object);
        }
        if (object2.mText2 != null) {
            object = SuggestionsAdapter.getStringOrNull(cursor, this.mText2UrlCol);
            object = object != null ? this.formatUrl((CharSequence)object) : SuggestionsAdapter.getStringOrNull(cursor, this.mText2Col);
            if (TextUtils.isEmpty((CharSequence)object)) {
                if (object2.mText1 != null) {
                    object2.mText1.setSingleLine(false);
                    object2.mText1.setMaxLines(2);
                }
            } else if (object2.mText1 != null) {
                object2.mText1.setSingleLine(true);
                object2.mText1.setMaxLines(1);
            }
            this.setViewText(object2.mText2, (CharSequence)object);
        }
        if (object2.mIcon1 != null) {
            this.setViewDrawable(object2.mIcon1, this.getIcon1(cursor), 4);
        }
        if (object2.mIcon2 != null) {
            this.setViewDrawable(object2.mIcon2, this.getIcon2(cursor), 8);
        }
        if ((n2 = this.mQueryRefinement) != 2 && (n2 != 1 || (n & 1) == 0)) {
            object2.mIconRefine.setVisibility(8);
            return;
        }
        object2.mIconRefine.setVisibility(0);
        object2.mIconRefine.setTag((Object)object2.mText1.getText());
        object2.mIconRefine.setOnClickListener((View.OnClickListener)this);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void changeCursor(Cursor cursor) {
        if (this.mClosed) {
            Log.w((String)"SuggestionsAdapter", (String)"Tried to change cursor after adapter was closed.");
            if (cursor == null) return;
            cursor.close();
            return;
        }
        try {
            super.changeCursor(cursor);
            if (cursor == null) return;
        }
        catch (Exception exception) {
            Log.e((String)"SuggestionsAdapter", (String)"error changing cursor and caching columns", (Throwable)exception);
            return;
        }
        this.mText1Col = cursor.getColumnIndex("suggest_text_1");
        this.mText2Col = cursor.getColumnIndex("suggest_text_2");
        this.mText2UrlCol = cursor.getColumnIndex("suggest_text_2_url");
        this.mIconName1Col = cursor.getColumnIndex("suggest_icon_1");
        this.mIconName2Col = cursor.getColumnIndex("suggest_icon_2");
        this.mFlagsCol = cursor.getColumnIndex("suggest_flags");
    }

    public void close() {
        this.changeCursor(null);
        this.mClosed = true;
    }

    @Override
    public CharSequence convertToString(Cursor object) {
        if (object == null) {
            return null;
        }
        String string2 = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_query");
        if (string2 != null) {
            return string2;
        }
        if (this.mSearchable.shouldRewriteQueryFromData() && (string2 = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_intent_data")) != null) {
            return string2;
        }
        if (this.mSearchable.shouldRewriteQueryFromText()) {
            if ((object = SuggestionsAdapter.getColumnString((Cursor)object, "suggest_text_1")) != null) {
                return object;
            }
            return null;
        }
        return null;
    }

    Drawable getDrawableFromResourceUri(Uri uri) throws FileNotFoundException {
        block10 : {
            block11 : {
                block12 : {
                    Resources resources;
                    String string2 = uri.getAuthority();
                    if (TextUtils.isEmpty((CharSequence)string2)) break block10;
                    try {
                        resources = this.mContext.getPackageManager().getResourcesForApplication(string2);
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("No package found for authority: ");
                        stringBuilder.append((Object)uri);
                        throw new FileNotFoundException(stringBuilder.toString());
                    }
                    List list = uri.getPathSegments();
                    if (list == null) break block11;
                    int n = list.size();
                    if (n == 1) {
                        try {
                            n = Integer.parseInt((String)list.get(0));
                        }
                        catch (NumberFormatException numberFormatException) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Single path segment is not a resource ID: ");
                            stringBuilder.append((Object)uri);
                            throw new FileNotFoundException(stringBuilder.toString());
                        }
                    }
                    if (n != 2) break block12;
                    n = resources.getIdentifier((String)list.get(1), (String)list.get(0), string2);
                    if (n != 0) {
                        return resources.getDrawable(n);
                    }
                    StringBuilder nameNotFoundException = new StringBuilder();
                    nameNotFoundException.append("No resource found for: ");
                    nameNotFoundException.append((Object)uri);
                    throw new FileNotFoundException(nameNotFoundException.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("More than two path segments: ");
                stringBuilder.append((Object)uri);
                throw new FileNotFoundException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No path: ");
            stringBuilder.append((Object)uri);
            throw new FileNotFoundException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No authority: ");
        stringBuilder.append((Object)uri);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    @Override
    public View getDropDownView(int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getDropDownView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)"SuggestionsAdapter", (String)"Search suggestions cursor threw exception.", (Throwable)runtimeException);
            viewGroup = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            if (viewGroup != null) {
                ((ChildViewCache)viewGroup.getTag()).mText1.setText((CharSequence)runtimeException.toString());
                return viewGroup;
            }
            return viewGroup;
        }
    }

    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }

    Cursor getSearchManagerSuggestions(SearchableInfo arrstring, String string2, int n) {
        if (arrstring == null) {
            return null;
        }
        String string3 = arrstring.getSuggestAuthority();
        if (string3 == null) {
            return null;
        }
        string3 = new Uri.Builder().scheme("content").authority(string3).query("").fragment("");
        String string4 = arrstring.getSuggestPath();
        if (string4 != null) {
            string3.appendEncodedPath(string4);
        }
        string3.appendPath("search_suggest_query");
        string4 = arrstring.getSuggestSelection();
        if (string4 != null) {
            arrstring = new String[]{string2};
        } else {
            string3.appendPath(string2);
            arrstring = null;
        }
        if (n > 0) {
            string3.appendQueryParameter("limit", String.valueOf(n));
        }
        string2 = string3.build();
        return this.mContext.getContentResolver().query((Uri)string2, null, string4, arrstring, null);
    }

    @Override
    public View getView(int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)"SuggestionsAdapter", (String)"Search suggestions cursor threw exception.", (Throwable)runtimeException);
            viewGroup = this.newView(this.mContext, this.mCursor, viewGroup);
            if (viewGroup != null) {
                ((ChildViewCache)viewGroup.getTag()).mText1.setText((CharSequence)runtimeException.toString());
                return viewGroup;
            }
            return viewGroup;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        context = super.newView(context, cursor, viewGroup);
        context.setTag((Object)new ChildViewCache((View)context));
        ((ImageView)context.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return context;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.updateSpinnerState(this.getCursor());
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        this.updateSpinnerState(this.getCursor());
    }

    public void onClick(View object) {
        if ((object = object.getTag()) instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence)object);
            return;
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
        charSequence = charSequence == null ? "" : charSequence.toString();
        if (this.mSearchView.getVisibility() == 0) {
            block5 : {
                if (this.mSearchView.getWindowVisibility() != 0) {
                    return null;
                }
                try {
                    charSequence = this.getSearchManagerSuggestions(this.mSearchable, (String)charSequence, 50);
                    if (charSequence == null) break block5;
                }
                catch (RuntimeException runtimeException) {
                    Log.w((String)"SuggestionsAdapter", (String)"Search suggestions query threw an exception.", (Throwable)runtimeException);
                    return null;
                }
                charSequence.getCount();
                return charSequence;
            }
            return null;
        }
        return null;
    }

    public void setQueryRefinement(int n) {
        this.mQueryRefinement = n;
    }

    private static final class ChildViewCache {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;

        public ChildViewCache(View view) {
            this.mText1 = (TextView)view.findViewById(16908308);
            this.mText2 = (TextView)view.findViewById(16908309);
            this.mIcon1 = (ImageView)view.findViewById(16908295);
            this.mIcon2 = (ImageView)view.findViewById(16908296);
            this.mIconRefine = (ImageView)view.findViewById(R.id.edit_query);
        }
    }

}

