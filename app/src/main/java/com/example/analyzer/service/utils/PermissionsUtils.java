package com.example.analyzer.service.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    public static final String TAG = "PermissionsUtilsTag";
    private static int requestCode = 0;
    private static final String REQUIRED_PERMISSION_MESSAGE = "This app required permission to run: ";

    public static boolean checkAndRequestPermissions(@NonNull Activity activity, String premission) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                premission);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, premission)) {
                Toast.makeText(activity, REQUIRED_PERMISSION_MESSAGE + premission,
                        Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(activity, new String[]{premission}, requestCode++);
            return false;
        }

        return true;
    }
}
