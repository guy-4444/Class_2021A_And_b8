package com.classy.class_2021a_and_b8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkContactsPermission();
    }

    private void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("pttt", "No Permission granted");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Log.d("pttt", "shouldShowRequestPermissionRationale");
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 123);
            } else {
                openSettingsToManuallyPermission();
                Log.d("pttt", "No shouldShowRequestPermissionRationale");
            }
        } else {
            readContacts();
        }
    }

    private void openSettingsToManuallyPermission() {
            String message = getString(R.string.message_permission_disabled);
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, 0)
                            .setMessage(message)
                            .setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivityForResult(intent, 126);
                                            dialog.cancel();
                                        }
                                    })
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show();
            alertDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("pttt", "onActivityResult");

        if (requestCode == 126) {
            checkContactsPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("pttt", "onRequestPermissionsResult");
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("pttt", "PERMISSION_GRANTED");
                    readContacts();
                } else {
                    Log.d("pttt", "PERMISSION_DENIED");
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void readContacts() {
        Log.d("pttt", "readContacts");


    }
}