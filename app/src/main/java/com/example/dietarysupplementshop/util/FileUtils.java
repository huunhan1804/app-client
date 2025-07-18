// Trong utils/FileUtils.java
package com.example.dietarysupplementshop.util;

import android.content.Context;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static File getFileFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        String scheme = uri.getScheme();
        if (scheme == null) {
            return null;
        }

        if ("file".equals(scheme)) {
            return new File(uri.getPath());
        } else if ("content".equals(scheme)) {
            String fileName = getFileName(context, uri);
            if (fileName == null) {
                fileName = "temp_file"; // Fallback name
            }

            File tempFile = new File(context.getCacheDir(), fileName);
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {

                if (inputStream != null) {
                    byte[] buffer = new byte[4 * 1024]; // 4KB buffer
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                    return tempFile;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error copying file from URI: " + e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}