package com.github.chuross.recyclerviewadapters.internal;

import android.view.View;

import androidx.annotation.NonNull;

public interface EventExecutor {

    void execute(@NonNull View view, int position);
}
