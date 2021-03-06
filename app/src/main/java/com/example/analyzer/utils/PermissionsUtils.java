package com.example.analyzer.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {
    public static final String TAG = "PermissionsUtilsTag";
    private static int requestCode = 0;
    private static final String REQUIRED_PERMISSION_MESSAGE = "This app required permission to run: ";

    public static void checkAndRequestPermissions(@NonNull Activity activity, String[] permissions) {
        List<String> remainPermissions = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    Toast.makeText(activity, REQUIRED_PERMISSION_MESSAGE + permission, Toast.LENGTH_SHORT).show();
                }
                remainPermissions.add(permission);
            }
        }

        if(remainPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, remainPermissions.toArray(new String[0]), requestCode++);
        }
    }
}
