package com.classy.class_2021a_and_b8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
            readAndWriteContactsToFile();
        }
    }

    private void openContactsPermissionInfoDialog() {
        String message = getString(R.string.permission_rationale);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, 0)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkContactsPermission();
                            }
                        })
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
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
                    readAndWriteContactsToFile();
                } else {
                    Log.d("pttt", "PERMISSION_DENIED");
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        openContactsPermissionInfoDialog();
                    } else {
                        openSettingsToManuallyPermission();
                    }
                }
                return;
            }
        }
    }

    private void readAndWriteContactsToFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            checkContactsPermission();
            return;
        }

        ArrayList<Contact> contacts = MyUtils.readContacts(this);

        if (contacts == null) {
            Toast.makeText(this, "Contacts Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }

        ContactsDB contactsDB = new ContactsDB().setUserName("Guy").setDate(System.currentTimeMillis());
        contactsDB.setContacts(contacts);

        String json = new Gson().toJson(contactsDB);
        writeFileToDevice(json, "my_contacts.txt");


        String jsonFromFile = readFile("my_contacts.txt");
        if (jsonFromFile != null) {
            ContactsDB cb = new Gson().fromJson(jsonFromFile, ContactsDB.class);
            int x = 0;
            int y = x + 5;
        }
    }

    private void writeFileToDevice(String dataToWrite, String fileNameWithExtension) {
        File folder = new File(getFilesDir() + File.separator + "MyFiles" + File.separator + "Contact_Files");

        boolean isExists = folder.exists();
        boolean isSuccess = folder.mkdirs();

        File file = new File(folder.getPath() + File.separator + fileNameWithExtension);
        PrintWriter writer = null;

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.print(dataToWrite);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            Log.d("pttt", "writeToFile FileNotFoundException" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("pttt", "writeToFile IOException" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String readFile(String fileNameWithExtension) {
        Log.d("pttt", "readFile");
        try {
            File folder = new File(getExternalFilesDir(null)
                    + File.separator + "MyFiles" + File.separator + "Contact_Files");
            File file = new File(folder.getPath() + File.separator + fileNameWithExtension);

            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String read;
            StringBuilder stringBuilder = new StringBuilder("");

            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read);
            }
            Log.d("Output", stringBuilder.toString());
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.d("pttt", "readFile FileNotFoundException" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("pttt", "readFile IOException" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}