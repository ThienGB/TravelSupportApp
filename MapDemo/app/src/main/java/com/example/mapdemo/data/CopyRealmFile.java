package com.example.mapdemo.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
public class CopyRealmFile {
    public static void copyRealmFile(Context context) {
        // Tên file Realm
        String realmFileName = "qlphongban1.realm";
        // Đường dẫn file Realm
        File realmFile = new File(context.getFilesDir(), realmFileName);
        // Đường dẫn đích để sao chép file Realm
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File destinationFile = new File(downloadsDirectory, realmFileName);
        if (realmFile.exists()) {
            try {
                copyFile(realmFile, destinationFile);
                Log.d("Realm", "Realm file copied to: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e("Realm", "Error copying Realm file", e);
            }
        } else {
            Log.e("Realm", "Realm file not found");
        }
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream fis = new FileInputStream(sourceFile);
        FileOutputStream fos = new FileOutputStream(destinationFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }

        fis.close();
        fos.close();
    }
}
