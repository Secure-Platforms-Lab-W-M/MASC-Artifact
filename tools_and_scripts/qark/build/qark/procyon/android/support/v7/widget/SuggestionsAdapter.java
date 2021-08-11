// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.net.Uri$Builder;
import android.view.ViewGroup;
import java.util.List;
import android.content.res.Resources;
import android.view.View;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v4.content.ContextCompat;
import java.io.InputStream;
import java.io.IOException;
import android.content.res.Resources$NotFoundException;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.ComponentName;
import android.text.style.TextAppearanceSpan;
import android.text.SpannableString;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.database.Cursor;
import android.content.res.ColorStateList;
import android.app.SearchableInfo;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnClickListener;
import android.support.v4.widget.ResourceCursorAdapter;

class SuggestionsAdapter extends ResourceCursorAdapter implements View$OnClickListener
{
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed;
    private final int mCommitIconResId;
    private int mFlagsCol;
    private int mIconName1Col;
    private int mIconName2Col;
    private final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement;
    private final SearchManager mSearchManager;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col;
    private int mText2Col;
    private int mText2UrlCol;
    private ColorStateList mUrlColor;
    
    public SuggestionsAdapter(final Context mProviderContext, final SearchView mSearchView, final SearchableInfo mSearchable, final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache) {
        super(mProviderContext, mSearchView.getSuggestionRowLayout(), null, true);
        this.mClosed = false;
        this.mQueryRefinement = 1;
        this.mText1Col = -1;
        this.mText2Col = -1;
        this.mText2UrlCol = -1;
        this.mIconName1Col = -1;
        this.mIconName2Col = -1;
        this.mFlagsCol = -1;
        this.mSearchManager = (SearchManager)this.mContext.getSystemService("search");
        this.mSearchView = mSearchView;
        this.mSearchable = mSearchable;
        this.mCommitIconResId = mSearchView.getSuggestionCommitIconResId();
        this.mProviderContext = mProviderContext;
        this.mOutsideDrawablesCache = mOutsideDrawablesCache;
    }
    
    private Drawable checkIconCache(final String s) {
        final Drawable$ConstantState drawable$ConstantState = this.mOutsideDrawablesCache.get(s);
        if (drawable$ConstantState == null) {
            return null;
        }
        return drawable$ConstantState.newDrawable();
    }
    
    private CharSequence formatUrl(final CharSequence charSequence) {
        if (this.mUrlColor == null) {
            final TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(typedValue.resourceId);
        }
        final SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan((Object)new TextAppearanceSpan((String)null, 0, 0, this.mUrlColor, (ColorStateList)null), 0, charSequence.length(), 33);
        return (CharSequence)spannableString;
    }
    
    private Drawable getActivityIcon(final ComponentName componentName) {
        final PackageManager packageManager = this.mContext.getPackageManager();
        try {
            final ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 128);
            final int iconResource = activityInfo.getIconResource();
            if (iconResource == 0) {
                return null;
            }
            final Drawable drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo);
            if (drawable == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid icon resource ");
                sb.append(iconResource);
                sb.append(" for ");
                sb.append(componentName.flattenToShortString());
                Log.w("SuggestionsAdapter", sb.toString());
                return null;
            }
            return drawable;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.w("SuggestionsAdapter", ex.toString());
            return null;
        }
    }
    
    private Drawable getActivityIconWithCache(final ComponentName componentName) {
        final String flattenToShortString = componentName.flattenToShortString();
        final boolean containsKey = this.mOutsideDrawablesCache.containsKey(flattenToShortString);
        final Drawable$ConstantState drawable$ConstantState = null;
        if (!containsKey) {
            final Drawable activityIcon = this.getActivityIcon(componentName);
            Drawable$ConstantState constantState;
            if (activityIcon == null) {
                constantState = drawable$ConstantState;
            }
            else {
                constantState = activityIcon.getConstantState();
            }
            this.mOutsideDrawablesCache.put(flattenToShortString, constantState);
            return activityIcon;
        }
        final Drawable$ConstantState drawable$ConstantState2 = this.mOutsideDrawablesCache.get(flattenToShortString);
        if (drawable$ConstantState2 == null) {
            return null;
        }
        return drawable$ConstantState2.newDrawable(this.mProviderContext.getResources());
    }
    
    public static String getColumnString(final Cursor cursor, final String s) {
        return getStringOrNull(cursor, cursor.getColumnIndex(s));
    }
    
    private Drawable getDefaultIcon1(final Cursor cursor) {
        final Drawable activityIconWithCache = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (activityIconWithCache != null) {
            return activityIconWithCache;
        }
        return this.mContext.getPackageManager().getDefaultActivityIcon();
    }
    
    private Drawable getDrawable(final Uri uri) {
        try {
            if ("android.resource".equals(uri.getScheme())) {
                try {
                    return this.getDrawableFromResourceUri(uri);
                }
                catch (Resources$NotFoundException ex3) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Resource does not exist: ");
                    sb.append(uri);
                    throw new FileNotFoundException(sb.toString());
                }
            }
            final InputStream openInputStream = this.mProviderContext.getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                try {
                    final Drawable fromStream = Drawable.createFromStream(openInputStream, (String)null);
                    try {
                        openInputStream.close();
                        return fromStream;
                    }
                    catch (IOException openInputStream) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error closing icon stream for ");
                        sb2.append(uri);
                        Log.e("SuggestionsAdapter", sb2.toString(), (Throwable)openInputStream);
                        return fromStream;
                    }
                }
                finally {
                    try {
                        openInputStream.close();
                    }
                    catch (IOException ex) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Error closing icon stream for ");
                        sb3.append(uri);
                        Log.e("SuggestionsAdapter", sb3.toString(), (Throwable)ex);
                    }
                }
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to open ");
            sb4.append(uri);
            throw new FileNotFoundException(sb4.toString());
        }
        catch (FileNotFoundException ex2) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Icon not found: ");
            sb5.append(uri);
            sb5.append(", ");
            sb5.append(ex2.getMessage());
            Log.w("SuggestionsAdapter", sb5.toString());
            return null;
        }
    }
    
    private Drawable getDrawableFromResourceValue(final String s) {
        if (s != null && !s.isEmpty()) {
            if ("0".equals(s)) {
                return null;
            }
            try {
                final int int1 = Integer.parseInt(s);
                final StringBuilder sb = new StringBuilder();
                sb.append("android.resource://");
                sb.append(this.mProviderContext.getPackageName());
                sb.append("/");
                sb.append(int1);
                final String string = sb.toString();
                final Drawable checkIconCache = this.checkIconCache(string);
                if (checkIconCache != null) {
                    return checkIconCache;
                }
                final Drawable drawable = ContextCompat.getDrawable(this.mProviderContext, int1);
                this.storeInIconCache(string, drawable);
                return drawable;
            }
            catch (Resources$NotFoundException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Icon resource not found: ");
                sb2.append(s);
                Log.w("SuggestionsAdapter", sb2.toString());
                return null;
            }
            catch (NumberFormatException ex2) {
                final Drawable checkIconCache2 = this.checkIconCache(s);
                if (checkIconCache2 != null) {
                    return checkIconCache2;
                }
                final Drawable drawable2 = this.getDrawable(Uri.parse(s));
                this.storeInIconCache(s, drawable2);
                return drawable2;
            }
        }
        return null;
    }
    
    private Drawable getIcon1(final Cursor cursor) {
        final int mIconName1Col = this.mIconName1Col;
        if (mIconName1Col == -1) {
            return null;
        }
        final Drawable drawableFromResourceValue = this.getDrawableFromResourceValue(cursor.getString(mIconName1Col));
        if (drawableFromResourceValue != null) {
            return drawableFromResourceValue;
        }
        return this.getDefaultIcon1(cursor);
    }
    
    private Drawable getIcon2(final Cursor cursor) {
        final int mIconName2Col = this.mIconName2Col;
        if (mIconName2Col == -1) {
            return null;
        }
        return this.getDrawableFromResourceValue(cursor.getString(mIconName2Col));
    }
    
    private static String getStringOrNull(final Cursor cursor, final int n) {
        if (n == -1) {
            return null;
        }
        try {
            return cursor.getString(n);
        }
        catch (Exception ex) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", (Throwable)ex);
            return null;
        }
    }
    
    private void setViewDrawable(final ImageView imageView, final Drawable imageDrawable, final int visibility) {
        imageView.setImageDrawable(imageDrawable);
        if (imageDrawable == null) {
            imageView.setVisibility(visibility);
            return;
        }
        imageView.setVisibility(0);
        imageDrawable.setVisible(false, false);
        imageDrawable.setVisible(true, false);
    }
    
    private void setViewText(final TextView textView, final CharSequence text) {
        textView.setText(text);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
    }
    
    private void storeInIconCache(final String s, final Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(s, drawable.getConstantState());
        }
    }
    
    private void updateSpinnerState(final Cursor cursor) {
        Bundle extras;
        if (cursor != null) {
            extras = cursor.getExtras();
        }
        else {
            extras = null;
        }
        if (extras == null) {
            return;
        }
        if (extras.getBoolean("in_progress")) {
            return;
        }
    }
    
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ChildViewCache childViewCache = (ChildViewCache)view.getTag();
        int int1 = 0;
        final int mFlagsCol = this.mFlagsCol;
        if (mFlagsCol != -1) {
            int1 = cursor.getInt(mFlagsCol);
        }
        if (childViewCache.mText1 != null) {
            this.setViewText(childViewCache.mText1, getStringOrNull(cursor, this.mText1Col));
        }
        if (childViewCache.mText2 != null) {
            final String stringOrNull = getStringOrNull(cursor, this.mText2UrlCol);
            CharSequence charSequence;
            if (stringOrNull != null) {
                charSequence = this.formatUrl(stringOrNull);
            }
            else {
                charSequence = getStringOrNull(cursor, this.mText2Col);
            }
            if (TextUtils.isEmpty(charSequence)) {
                if (childViewCache.mText1 != null) {
                    childViewCache.mText1.setSingleLine(false);
                    childViewCache.mText1.setMaxLines(2);
                }
            }
            else if (childViewCache.mText1 != null) {
                childViewCache.mText1.setSingleLine(true);
                childViewCache.mText1.setMaxLines(1);
            }
            this.setViewText(childViewCache.mText2, charSequence);
        }
        if (childViewCache.mIcon1 != null) {
            this.setViewDrawable(childViewCache.mIcon1, this.getIcon1(cursor), 4);
        }
        if (childViewCache.mIcon2 != null) {
            this.setViewDrawable(childViewCache.mIcon2, this.getIcon2(cursor), 8);
        }
        final int mQueryRefinement = this.mQueryRefinement;
        if (mQueryRefinement != 2 && (mQueryRefinement != 1 || (int1 & 0x1) == 0x0)) {
            childViewCache.mIconRefine.setVisibility(8);
            return;
        }
        childViewCache.mIconRefine.setVisibility(0);
        childViewCache.mIconRefine.setTag((Object)childViewCache.mText1.getText());
        childViewCache.mIconRefine.setOnClickListener((View$OnClickListener)this);
    }
    
    public void changeCursor(final Cursor cursor) {
        if (this.mClosed) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        try {
            super.changeCursor(cursor);
            if (cursor != null) {
                this.mText1Col = cursor.getColumnIndex("suggest_text_1");
                this.mText2Col = cursor.getColumnIndex("suggest_text_2");
                this.mText2UrlCol = cursor.getColumnIndex("suggest_text_2_url");
                this.mIconName1Col = cursor.getColumnIndex("suggest_icon_1");
                this.mIconName2Col = cursor.getColumnIndex("suggest_icon_2");
                this.mFlagsCol = cursor.getColumnIndex("suggest_flags");
            }
        }
        catch (Exception ex) {
            Log.e("SuggestionsAdapter", "error changing cursor and caching columns", (Throwable)ex);
        }
    }
    
    public void close() {
        this.changeCursor(null);
        this.mClosed = true;
    }
    
    public CharSequence convertToString(final Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        final String columnString = getColumnString(cursor, "suggest_intent_query");
        if (columnString != null) {
            return columnString;
        }
        if (this.mSearchable.shouldRewriteQueryFromData()) {
            final String columnString2 = getColumnString(cursor, "suggest_intent_data");
            if (columnString2 != null) {
                return columnString2;
            }
        }
        if (!this.mSearchable.shouldRewriteQueryFromText()) {
            return null;
        }
        final String columnString3 = getColumnString(cursor, "suggest_text_1");
        if (columnString3 != null) {
            return columnString3;
        }
        return null;
    }
    
    Drawable getDrawableFromResourceUri(final Uri uri) throws FileNotFoundException {
        final String authority = uri.getAuthority();
        if (!TextUtils.isEmpty((CharSequence)authority)) {
            try {
                final Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(authority);
                final List pathSegments = uri.getPathSegments();
                if (pathSegments == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No path: ");
                    sb.append(uri);
                    throw new FileNotFoundException(sb.toString());
                }
                final int size = pathSegments.size();
                int n = 0;
                Label_0136: {
                    if (size == 1) {
                        try {
                            n = Integer.parseInt(pathSegments.get(0));
                            break Label_0136;
                        }
                        catch (NumberFormatException ex) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Single path segment is not a resource ID: ");
                            sb2.append(uri);
                            throw new FileNotFoundException(sb2.toString());
                        }
                    }
                    if (size != 2) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("More than two path segments: ");
                        sb3.append(uri);
                        throw new FileNotFoundException(sb3.toString());
                    }
                    n = resourcesForApplication.getIdentifier((String)pathSegments.get(1), (String)pathSegments.get(0), authority);
                }
                if (n != 0) {
                    return resourcesForApplication.getDrawable(n);
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("No resource found for: ");
                sb4.append(uri);
                throw new FileNotFoundException(sb4.toString());
            }
            catch (PackageManager$NameNotFoundException ex2) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("No package found for authority: ");
                sb5.append(uri);
                throw new FileNotFoundException(sb5.toString());
            }
        }
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("No authority: ");
        sb6.append(uri);
        throw new FileNotFoundException(sb6.toString());
    }
    
    public View getDropDownView(final int n, View dropDownView, final ViewGroup viewGroup) {
        try {
            dropDownView = super.getDropDownView(n, dropDownView, viewGroup);
            return dropDownView;
        }
        catch (RuntimeException ex) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", (Throwable)ex);
            final View dropDownView2 = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            if (dropDownView2 != null) {
                ((ChildViewCache)dropDownView2.getTag()).mText1.setText((CharSequence)ex.toString());
                return dropDownView2;
            }
            return dropDownView2;
        }
    }
    
    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }
    
    Cursor getSearchManagerSuggestions(final SearchableInfo searchableInfo, final String s, final int n) {
        if (searchableInfo == null) {
            return null;
        }
        final String suggestAuthority = searchableInfo.getSuggestAuthority();
        if (suggestAuthority == null) {
            return null;
        }
        final Uri$Builder fragment = new Uri$Builder().scheme("content").authority(suggestAuthority).query("").fragment("");
        final String suggestPath = searchableInfo.getSuggestPath();
        if (suggestPath != null) {
            fragment.appendEncodedPath(suggestPath);
        }
        fragment.appendPath("search_suggest_query");
        final String suggestSelection = searchableInfo.getSuggestSelection();
        String[] array;
        if (suggestSelection != null) {
            array = new String[] { s };
        }
        else {
            fragment.appendPath(s);
            array = null;
        }
        if (n > 0) {
            fragment.appendQueryParameter("limit", String.valueOf(n));
        }
        return this.mContext.getContentResolver().query(fragment.build(), (String[])null, suggestSelection, array, (String)null);
    }
    
    public View getView(final int n, View view, final ViewGroup viewGroup) {
        try {
            view = super.getView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException ex) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", (Throwable)ex);
            final View view2 = this.newView(this.mContext, this.mCursor, viewGroup);
            if (view2 != null) {
                ((ChildViewCache)view2.getTag()).mText1.setText((CharSequence)ex.toString());
                return view2;
            }
            return view2;
        }
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        final View view = super.newView(context, cursor, viewGroup);
        view.setTag((Object)new ChildViewCache(view));
        ((ImageView)view.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return view;
    }
    
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void onClick(final View view) {
        final Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence)tag);
        }
    }
    
    public Cursor runQueryOnBackgroundThread(final CharSequence charSequence) {
        String string;
        if (charSequence == null) {
            string = "";
        }
        else {
            string = charSequence.toString();
        }
        if (this.mSearchView.getVisibility() == 0) {
            if (this.mSearchView.getWindowVisibility() != 0) {
                return null;
            }
            try {
                final Cursor searchManagerSuggestions = this.getSearchManagerSuggestions(this.mSearchable, string, 50);
                if (searchManagerSuggestions != null) {
                    searchManagerSuggestions.getCount();
                    return searchManagerSuggestions;
                }
                return null;
            }
            catch (RuntimeException ex) {
                Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", (Throwable)ex);
                return null;
            }
        }
        return null;
    }
    
    public void setQueryRefinement(final int mQueryRefinement) {
        this.mQueryRefinement = mQueryRefinement;
    }
    
    private static final class ChildViewCache
    {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;
        
        public ChildViewCache(final View view) {
            this.mText1 = (TextView)view.findViewById(16908308);
            this.mText2 = (TextView)view.findViewById(16908309);
            this.mIcon1 = (ImageView)view.findViewById(16908295);
            this.mIcon2 = (ImageView)view.findViewById(16908296);
            this.mIconRefine = (ImageView)view.findViewById(R.id.edit_query);
        }
    }
}
