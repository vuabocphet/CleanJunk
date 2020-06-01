package com.appplatform.commons;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.appplatform.commons.utils.DisplayUtil;
import com.appplatform.commons.utils.FormatterUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Locale;


/**
 * Created by Admin on 3/12/2018.
 */

public class DeviceInfoActivity extends AppCompatActivity {
    TextView txtPhone;
    TextView txtSerial;
    TextView txtBrand;
    TextView txtCPU;
    TextView txtHardware;
    TextView txtScreen;
    TextView txtCapacity;
    TextView txtTotalRam;
    TextView txtAvailableRam;
    TextView txtStorage;
    TextView txtAvailableStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_device_info_commons);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        this.txtPhone = findViewById(R.id.tvPhoneName);
        this.txtSerial = findViewById(R.id.tvPhoneSerial);
        this.txtBrand = findViewById(R.id.tvPhoneBrand);
        this.txtCPU = findViewById(R.id.tvPhoneCPU);
        this.txtHardware = findViewById(R.id.tvInfoHardware);
        this.txtScreen = findViewById(R.id.tvInfoScreen);
        this.txtCapacity = findViewById(R.id.tvInfoCapacity);
        this.txtTotalRam = findViewById(R.id.tvInfoRam);
        this.txtAvailableRam = findViewById(R.id.txtAvaiRam);
        this.txtStorage = findViewById(R.id.txtStorage);
        this.txtAvailableStorage = findViewById(R.id.txtAvaiStorage);


        this.txtPhone.setText(Build.MODEL);
        this.txtSerial.setText(Build.SERIAL);
        this.txtBrand.setText(Build.BRAND);

        String cpuTemp;
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            cpuTemp = intent.getStringExtra("cpu_temp");
        } else {
            cpuTemp = getCpuTemp();
        }

        this.txtCPU.setText(getString(R.string.common_celsius, cpuTemp));
        this.txtHardware.setText(Build.HARDWARE);
        this.txtScreen.setText(String.format(Locale.US, "%dx%d", DisplayUtil.getScreenWidth(this), DisplayUtil.getScreenHeight(this)));
        this.txtCapacity.setText(getBatteryCapacity());
        this.txtStorage.setText(getTotalInternalMemorySize());
        this.txtAvailableStorage.setText(getAvailableInternalMemorySize());
        try {
            ActivityManager.MemoryInfo memInfo = getMemInfo();
            this.txtAvailableRam.setText(FormatterUtils.formatShortFileSize2(this, memInfo.availMem));
            this.txtTotalRam.setText(FormatterUtils.formatShortFileSize2(this, memInfo.totalMem));
        } catch (Exception e) {
            e.printStackTrace();
            this.txtTotalRam.setText("N/A");
            this.txtAvailableRam.setText("N/A");
        }
    }


    private ActivityManager.MemoryInfo getMemInfo() {
        ActivityManager actManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        if (actManager != null) {
            actManager.getMemoryInfo(memInfo);
        }
        return memInfo;
    }


    private String getBatteryCapacity() {
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            Object mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(getBaseContext());
            double batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);
            return (int) batteryCapacity + "mAh";
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "N/A";
    }


    private String getTotalInternalMemorySize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize;
            long totalBlocks;
            if (Build.VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            }

            return FormatterUtils.formatShortFileSize2(this, totalBlocks * blockSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private String getAvailableInternalMemorySize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());

            long blockSize;
            long availableBlocks;
            if (Build.VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }

            return FormatterUtils.formatShortFileSize2(this, availableBlocks * blockSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }


    private String getCpuTemp() {
        try {
            Process p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;

            return String.valueOf(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
