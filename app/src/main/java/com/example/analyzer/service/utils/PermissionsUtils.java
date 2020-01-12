package com.example.analyzer.service.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {
    public static final String TAG = "PermissionsUtilsTag";
    private static int requestCode = 0;
    private static final String REQUIRED_PERMISSION_MESSAGE = "This app required permission to run: ";
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static int checkAndRequestPermissions(String[] permissions) {
        List<String> remainPermissions = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    Toast.makeText(mActivity, REQUIRED_PERMISSION_MESSAGE + permission, Toast.LENGTH_SHORT).show();
                }
                remainPermissions.add(permission);
            }
        }

        if(remainPermissions.size() > 0) {
            ActivityCompat.requestPermissions(mActivity, remainPermissions.toArray(new String[0]), requestCode++);
            return -1;
        }
        return PackageManager.PERMISSION_GRANTED;
    }
}