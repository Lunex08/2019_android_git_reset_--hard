package com.example.analyzer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    public static final String TAG = "PermissionsUtilsTag";
    public static final int REQUEST_PERMISSION_READ_CALL_LOG = 0;
    private static final String REQUIRED_PERMISSION_MESSAGE = "This app required permission to run: ";

    public static boolean checkAndRequestPermissions(@NonNull Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_CALL_LOG);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)) {
                Toast.makeText(activity, REQUIRED_PERMISSION_MESSAGE + Manifest.permission.READ_CALL_LOG,
                        Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_PERMISSION_READ_CALL_LOG);
            return false;
        }

        return true;
    }
}
