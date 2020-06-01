package com.appplatform.commons.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by hoangsondev on 6/7/2014.
 */
public final class ViewHolder {
    public static <T extends View> T getView(View view, int viewId) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(viewId);
        if (childView == null) {
            childView = view.findViewById(viewId);
            viewHolder.put(viewId, childView);
        }
        return (T) childView;
    }
}
