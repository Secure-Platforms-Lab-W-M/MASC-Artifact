package com.google.android.material.bottomnavigation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface LabelVisibilityMode {
   int LABEL_VISIBILITY_AUTO = -1;
   int LABEL_VISIBILITY_LABELED = 1;
   int LABEL_VISIBILITY_SELECTED = 0;
   int LABEL_VISIBILITY_UNLABELED = 2;
}
