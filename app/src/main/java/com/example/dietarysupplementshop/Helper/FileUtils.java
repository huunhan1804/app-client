package com.example.dietarysupplementshop.Helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {

    private static final String TAG = "FileUtils";


    public static MultipartBody.Part prepareFilePart(Context context, Uri uri, String partName) {
        if (uri == null) {
            Log.e(TAG, "URI is null for partName: " + partName);
            return null;
        }

        File file = getFileFromUri(context, uri);
        if (file == null) {
            Log.e(TAG, "Failed to get file from URI: " + uri.toString());
            return null;
        }

        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    public static RequestBody createJsonRequestBody(String jsonString) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
    }


    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }

        if (fileName == null) {
            fileName = "temp_file_" + System.currentTimeMillis();
        }

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                file = new File(context.getCacheDir(), fileName);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating file from URI: " + e.getMessage());
            file = null;
        }
        return file;
    }


    public static List<MultipartBody.Part> prepareMultipleFileParts(Context context, List<Uri> uris, String partName) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (uris != null) {
            for (Uri uri : uris) {
                MultipartBody.Part part = prepareFilePart(context, uri, partName);
                if (part != null) {
                    parts.add(part);
                }
            }
        }
        return parts;
    }
}