package com.jay.six.ui.widget.recyclerview.interfaces;

import com.jay.six.ui.widget.recyclerview.ViewHolder;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public interface OnSwipeMenuClickListener<T> {
    void onSwipMenuClick(ViewHolder viewHolder, T data, int position);
}
