package com.appplatform.commons.adapter;

/**
 * Created by Duydq on 28/10/2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Code4LifeVn on 1/13/2016.
 */
public abstract class BaseRecycleAdapter<Item, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = BaseRecycleAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Item> items = null;

    public BaseRecycleAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = (items == null) ? new ArrayList<Item>(0) : items;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected void startActivity(Intent intent) {
        this.getContext().startActivity(intent);
    }

    protected void startActivity(Intent intent, Bundle extra) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.getContext().startActivity(intent, extra);
        }

    }

    public Context getContext() {
        return this.context;
    }

    /**
     * Get total items
     */
    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    /**
     * Get Item ID
     *
     * @param position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        try {
            View view = viewHolder.itemView;
            final Item object = this.items.get(position);
            this.populateViewHolder(viewHolder, object, position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(viewHolder, v, object, position);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClick(viewHolder, view, object, position);
                    return true;
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Create View Holder
     *
     * @param viewGroup
     * @param position
     */
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        return this.getViewHolder(this.layoutInflater.inflate(this.getViewId(position), viewGroup, false), position);
    }

    /**
     * Declare Layout/View Id
     */
    public abstract int getViewId(int position);

    /**
     * Get View Holder
     *
     * @param view
     */
    protected abstract ViewHolder getViewHolder(View view, int position);

    /**
     * Trigger item's event
     *
     * @param item
     */
    protected abstract void onItemClick(ViewHolder viewHolder, View view, Item item, int position);

    protected abstract void onItemLongClick(ViewHolder viewHolder, View view, Item item, int position);

    /**
     * Populate View Holder View
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    protected abstract void populateViewHolder(ViewHolder viewHolder, Item item, int position);

    /**
     * Set items
     *
     * @param items
     */
    public void setItems(List<Item> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    //=> override if needed
    public void specifyPadding(int measure) {

    }

    /**
     * Add elements array into the list.
     *
     * @param objects
     */
    public BaseRecycleAdapter<Item, ViewHolder> addAll(List<Item> objects) {
        if (items != null && !items.isEmpty()) {
            items.clear();
        }
        if (objects != null && !this.items.containsAll(objects)) {
            this.items.addAll(objects);
            this.notifyDataSetChanged();
        }
        return this;
    }


    public BaseRecycleAdapter<Item, ViewHolder> addAllNotClearObjects(List<Item> objects) {
        if (objects != null && !this.items.containsAll(objects)) {
            this.items.addAll(objects);
            this.notifyDataSetChanged();
        }
        return this;
    }


    public BaseRecycleAdapter<Item, ViewHolder> addAllNotChange(List<Item> objects) {
        if (items != null && !items.isEmpty()) {
            items.clear();
        }
        if (objects != null && !this.items.containsAll(objects)) {
            this.items.addAll(objects);
        }
        return this;
    }

    /**
     * Add elements array into the list.
     *
     * @param items
     */
    @TargetApi(11)
    public BaseRecycleAdapter<Item, ViewHolder> addAll(Item... items) {
        if (this.items != null && !this.items.isEmpty()) {
            this.items.clear();
        }
        this.addAll(Arrays.asList(items));
        this.notifyDataSetChanged();
        return this;
    }

    /**
     * Add an object into the list
     *
     * @param object
     */
    public BaseRecycleAdapter<Item, ViewHolder> add(Item object) {
        if (object != null) {
            this.items.add(object);
            this.notifyDataSetChanged();
        }
        return this;
    }

    public BaseRecycleAdapter<Item, ViewHolder> addNotChange(Item object) {
        if (object != null) {
            this.items.add(object);
        }
        return this;
    }

    /**
     * Get all items
     *
     * @return List items
     */
    public List<Item> getItems() {
        return this.items;
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(Item object, int index) {
        this.items.add(index, object);
        this.notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(Item object) {
        this.items.remove(object);
        this.notifyDataSetChanged();
    }

    /**
     * Remove a element at position
     *
     * @param position Position that want to remove
     */
    public void remove(int position) {
        this.items.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (this.items != null) {
            this.items.clear();
            this.notifyDataSetChanged();
        }
    }

    /**
     * Sort all elements.
     *
     * @param comparator Comparator
     */

    public void sort(Comparator<Item> comparator) {
        Collections.sort(this.items, comparator);
        this.notifyDataSetChanged();
    }
}
