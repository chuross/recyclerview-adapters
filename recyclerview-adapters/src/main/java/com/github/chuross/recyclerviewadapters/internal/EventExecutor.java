package com.github.chuross.recyclerviewadapters.internal;

import android.support.annotation.NonNull;
import android.view.View;

public interface EventExecutor {

    void execute(@NonNull View view, int position);
}
