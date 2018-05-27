package io.start.biruk.saveit.util;

import android.os.Environment;

import com.annimon.stream.Stream;

import java.io.File;

/**
 * Created by biruk on 5/10/2018.
 */
public class FileUtil {
    private static final String MAIN_STORAGE_PATH;

    static {
        MAIN_STORAGE_PATH = "save it";
    }

    public static boolean createMainDirectory() {
        File mainDir = new File(Environment.getExternalStorageDirectory(), MAIN_STORAGE_PATH);
        if (!mainDir.exists()) {
            return mainDir.mkdir();
        }
        return mainDir.exists();
    }

    public static String getMainPath() {
        File mainDir = new File(Environment.getExternalStorageDirectory(), MAIN_STORAGE_PATH);
        if (!mainDir.exists()) {
            mainDir.mkdir();
            return mainDir.getPath();
        }
        return mainDir.getPath();
    }

}
