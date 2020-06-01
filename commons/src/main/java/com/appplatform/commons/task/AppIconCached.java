package com.appplatform.commons.task;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class AppIconCached {
    private static final int INITIAL_ICON_CACHE_CAPACITY = 50;
    private final HashMap<String, CacheEntry> mCache = new HashMap<>(INITIAL_ICON_CACHE_CAPACITY);

    public void remove(String packageName) {
        synchronized (this.mCache) {
            if (packageName != null) {
                this.mCache.remove(packageName);
            }
        }
    }

    public void flush() {
        synchronized (this.mCache) {
            this.mCache.clear();
        }
    }

    public void addToCache(String packageName, String title, Bitmap icon, Drawable iconDrawable) {
        CacheEntry entry = this.mCache.get(packageName);
        if (entry == null) {
            entry = new CacheEntry();
        }
        this.mCache.put(packageName, entry);
        entry.title = title;
        entry.icon = icon;
        entry.iconDrawable = iconDrawable;
    }

    public CacheEntry getCacheEntry(String packageName) {
        CacheEntry cacheEntry;
        synchronized (this.mCache) {
            cacheEntry = this.mCache.get(packageName);
        }
        return cacheEntry;
    }

    public class CacheEntry {
        public Bitmap icon;
        public Drawable iconDrawable;
        public String title;
    }
}
