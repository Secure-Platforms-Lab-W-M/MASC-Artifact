package android.support.v4.internal.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface SupportMenuItem extends MenuItem {
   int SHOW_AS_ACTION_ALWAYS = 2;
   int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
   int SHOW_AS_ACTION_IF_ROOM = 1;
   int SHOW_AS_ACTION_NEVER = 0;
   int SHOW_AS_ACTION_WITH_TEXT = 4;

   boolean collapseActionView();

   boolean expandActionView();

   View getActionView();

   int getAlphabeticModifiers();

   CharSequence getContentDescription();

   ColorStateList getIconTintList();

   Mode getIconTintMode();

   int getNumericModifiers();

   ActionProvider getSupportActionProvider();

   CharSequence getTooltipText();

   boolean isActionViewExpanded();

   MenuItem setActionView(int var1);

   MenuItem setActionView(View var1);

   MenuItem setAlphabeticShortcut(char var1, int var2);

   SupportMenuItem setContentDescription(CharSequence var1);

   MenuItem setIconTintList(ColorStateList var1);

   MenuItem setIconTintMode(Mode var1);

   MenuItem setNumericShortcut(char var1, int var2);

   MenuItem setShortcut(char var1, char var2, int var3, int var4);

   void setShowAsAction(int var1);

   MenuItem setShowAsActionFlags(int var1);

   SupportMenuItem setSupportActionProvider(ActionProvider var1);

   SupportMenuItem setTooltipText(CharSequence var1);
}
