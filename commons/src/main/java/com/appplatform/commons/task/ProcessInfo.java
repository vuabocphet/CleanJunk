package com.appplatform.commons.task;


import android.content.pm.ResolveInfo;

public class ProcessInfo {
    private int pId;
    private String processName;
    private int memory = 0;
    private ResolveInfo resolveInfo;
private boolean isChecked = true;
    public ProcessInfo(int processId, String processName, ResolveInfo resolveInfo) {
        this.pId = processId;
        this.processName = processName;
        this.resolveInfo = resolveInfo;
    }

    public ProcessInfo(int pId, String processName) {
        this.pId = pId;
        this.processName = processName;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
