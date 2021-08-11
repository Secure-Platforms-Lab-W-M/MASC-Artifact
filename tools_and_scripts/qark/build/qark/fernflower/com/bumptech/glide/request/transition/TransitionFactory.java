package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public interface TransitionFactory {
   Transition build(DataSource var1, boolean var2);
}
