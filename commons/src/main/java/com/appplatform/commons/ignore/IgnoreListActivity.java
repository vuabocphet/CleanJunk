package com.appplatform.commons.ignore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.appplatform.commons.AppPlatformManager;
import com.appplatform.commons.R;
import com.appplatform.commons.task.AbOnTaskLoadRunningAppListener;
import com.appplatform.commons.task.AppInfo;
import com.appplatform.commons.task.CacheManager;
import com.appplatform.commons.task.TaskHandler;
import com.appplatform.commons.views.EmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangSon on 1/22/2018.
 */

public class IgnoreListActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, IgnoreListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private ListView listView;
    private TaskHandler taskHandler;
    private IgnoreListAdapter adapter;
    private List<String> ignoreList;
    private View loadingView;
    private SearchView mSearchView;
    private List<AppInfo> currentList = new ArrayList<>();
    private CacheManager cacheManager;
    private EmptyView emptyLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }


    private void init() {
        this.initView();
        this.initTask();
    }


    private void initView() {

        setContentView(R.layout.activity_ignore_list);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        this.listView = this.findViewById(R.id.ignore_list_view);
        this.loadingView = this.findViewById(R.id.ignore_loading_progressBar);
        this.emptyLayout = this.findViewById(R.id.empty_layout);
        this.emptyLayout.setEmptyMessage(getString(R.string.commons_no_app_found));

        this.adapter = new IgnoreListAdapter(this);
        this.listView.setAdapter(adapter);
        this.mSearchView = new SearchView(this);
        this.mSearchView.setQueryHint(getResources().getString(R.string.menu_search));
        this.mSearchView.setMaxWidth(1200);
        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText, emptyLayout);
                }
                return false;
            }
        });

    }


    private void initTask() {
        this.cacheManager = AppPlatformManager.getInstance().getIgnoreList();
        this.ignoreList = cacheManager.getCachedList();
        this.taskHandler = TaskHandler.get();
        this.taskHandler.loadListInstalledApps(this, new AbOnTaskLoadRunningAppListener() {
            @Override
            public void onTaskLoaded(List<AppInfo> list) {
                if (list.isEmpty()) {
                    loadingView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    return;
                }
                emptyLayout.setVisibility(View.GONE);
                currentList.clear();
                currentList.addAll(list);
                sortList(currentList);
                adapter.setIgnoreList(IgnoreListActivity.this.ignoreList);
                adapter.setCurrentList(currentList);
                adapter.addAll(currentList);
                adapter.sort();
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    private void sortList(List<AppInfo> list) {
        int N = list.size();
        int j = 0;
        for (int i = 0; i < N; i++) {
            AppInfo appInfo = list.get(i);
            if (IgnoreListActivity.this.ignoreList.contains(appInfo.getPackageName())) {
                list.set(i, list.get(j));
                list.set(j, appInfo);
                j++;
                if (j == IgnoreListActivity.this.ignoreList.size()) {
                    break;
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(R.string.menu_search).setIcon(R.drawable.ic_search_black_24dp).setActionView(this.mSearchView);
        MenuItemCompat.setOnActionExpandListener(mi, new MenuItemCompat.OnActionExpandListener() {
            public boolean onMenuItemActionExpand(MenuItem item) {
                IgnoreListActivity.this.getSupportActionBar().setIcon(R.drawable.pic_diaphaneity_rectangle);
                return true;
            }

            public boolean onMenuItemActionCollapse(MenuItem item) {
                IgnoreListActivity.this.getSupportActionBar().setIcon(R.drawable.pic_diaphaneity_actionbar);
                if (adapter != null && TextUtils.isEmpty(mSearchView.getQuery())) {
                    adapter.filter("", emptyLayout);
                }
                return true;
            }
        });
        MenuItemCompat.setShowAsAction(mi, 10);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        try {
            if (this.adapter != null) {
                List<String> ignoreList = adapter.getIgnoreList();
                SharedPreferences sharedPreferences = this.getSharedPreferences("ignore_list_pref", Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("ignore_list").apply();
                StringBuilder stringBuilder = new StringBuilder();
                for (String packageName : ignoreList) {
                    if (stringBuilder.length() == 0) {
                        stringBuilder.append(packageName);
                        continue;
                    }
                    stringBuilder.append("|");
                    stringBuilder.append(packageName);
                }
                sharedPreferences.edit().putString("ignore_list", stringBuilder.toString()).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.taskHandler.releaseTask();
        super.onDestroy();
    }
}
