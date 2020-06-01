package com.appplatform.commons.ignore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appplatform.commons.R;
import com.appplatform.commons.adapter.BaseItemAdapter;
import com.appplatform.commons.task.AppInfo;
import com.appplatform.commons.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangSon on 1/22/2018.
 */

public class IgnoreListAdapter extends BaseItemAdapter<AppInfo> {
    public IgnoreListAdapter(Context context) {
        super(context, R.layout.adapter_ignore_list);

    }


    private List<String> ignoreList = new ArrayList<>();
    private List<AppInfo> currentList = new ArrayList<>();


    public void setIgnoreList(List<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    public List<String> getIgnoreList() {
        return ignoreList;
    }

    public void setCurrentList(List<AppInfo> currentList) {
        this.currentList = currentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(getResource(), null, false);
        }

        final AppInfo appInfo = getItem(position);

        TextView appName = ViewHolder.getView(convertView, R.id.ignore_task_title);
        ImageView appChecked = ViewHolder.getView(convertView, R.id.ignore_task_select);
        ImageView appIcon = ViewHolder.getView(convertView, R.id.ignore_task_icon);

        appIcon.setImageBitmap(appInfo.getAppIcon());
        appName.setText(appInfo.getAppName());
        if (ignoreList.contains(appInfo.getPackageName())) {
            appChecked.setBackgroundResource(R.drawable.ignorelist_ignore);
            appInfo.setSelected(true);
        } else {
            appChecked.setBackgroundResource(R.drawable.ignorelist_unignore);
            appInfo.setSelected(false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ignoreList.contains(appInfo.getPackageName())) {
                    ignoreList.remove(appInfo.getPackageName());
                    Toast.makeText(getContext(), R.string.ignore_list_toast_remove, Toast.LENGTH_SHORT).show();
                } else {
                    ignoreList.add(appInfo.getPackageName());
                    Toast.makeText(getContext(), R.string.ignore_list_toast_add, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }

        });

        return convertView;
    }


    public void filter(String keyword, View emptyView) {
        synchronized (this) {
            clear();
            addAll(this.currentList);
            for (int i = getCount() - 1; i >= 0; i--) {
                if (!(getItem(i).getAppName().toLowerCase().contains(keyword.toLowerCase()))) {
                    remove(i);
                }
            }
            emptyView.setVisibility(getItems().size() == 0 ? View.VISIBLE : View.GONE);
            notifyDataSetChanged();
        }
    }

    public void sort() {
        int size = ignoreList.size();
        int N = getCount();
        for (int i = size; i < N - 1; i++) {
            for (int j = N - 2; j >= i; j--) {
                if (compare(getItem(j), getItem(j + 1))) {
                    swap(getItems(), j, j + 1);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void swap(List list, int idx1, int idx2) {
        Object obj1 = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, obj1);
    }

    private boolean compare(AppInfo appInfo1, AppInfo appInfo2) {
        return appInfo1.getAppName().compareToIgnoreCase(appInfo2.getAppName()) > 0;
    }


}
