package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import com.google.android.material.R.attr;
import com.google.android.material.R.styleable;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;

final class CalendarStyle {
   final CalendarItemStyle day;
   final CalendarItemStyle invalidDay;
   final Paint rangeFill;
   final CalendarItemStyle selectedDay;
   final CalendarItemStyle selectedYear;
   final CalendarItemStyle todayDay;
   final CalendarItemStyle todayYear;
   final CalendarItemStyle year;

   CalendarStyle(Context var1) {
      TypedArray var2 = var1.obtainStyledAttributes(MaterialAttributes.resolveOrThrow(var1, attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName()), styleable.MaterialCalendar);
      this.day = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_dayStyle, 0));
      this.invalidDay = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_dayInvalidStyle, 0));
      this.selectedDay = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_daySelectedStyle, 0));
      this.todayDay = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_dayTodayStyle, 0));
      ColorStateList var3 = MaterialResources.getColorStateList(var1, var2, styleable.MaterialCalendar_rangeFillColor);
      this.year = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_yearStyle, 0));
      this.selectedYear = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_yearSelectedStyle, 0));
      this.todayYear = CalendarItemStyle.create(var1, var2.getResourceId(styleable.MaterialCalendar_yearTodayStyle, 0));
      Paint var4 = new Paint();
      this.rangeFill = var4;
      var4.setColor(var3.getDefaultColor());
      var2.recycle();
   }
}
