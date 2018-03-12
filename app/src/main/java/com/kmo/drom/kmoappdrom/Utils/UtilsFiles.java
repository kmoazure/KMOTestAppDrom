package com.kmo.drom.kmoappdrom.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by kmoaz on 12.03.2018.
 */

public class UtilsFiles {
    private static String defaultFile = "login.txt";
    private static String TAG = "Utils files";

    public static void writeFile (@NonNull Context context, @NonNull String login) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(defaultFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(login);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG + " Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFile (@NonNull Context context) {
        String data = "";

        try {
            InputStream inputStream = context.openFileInput(defaultFile);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                data = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return data;
    }
}
