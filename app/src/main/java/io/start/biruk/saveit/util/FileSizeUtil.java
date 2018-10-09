package io.start.biruk.saveit.util;


import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by biruk on 26/09/18.
 */

public class FileSizeUtil {

    public static Single<Long> computeFolderSizeSingle(String path) {
        return Single.just(path)
                .map(File::new)
                .map(FileSizeUtil::computeFolderSize);
    }

    private static long computeFolderSize(File folder) {
        File[] files = folder.listFiles();

        long folderSize = 0;

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                folderSize += files[i].length();
            } else {
                folderSize += computeFolderSize(files[i]);
            }
        }
        return folderSize;
    }


    public static String sizeFormater(long size) {
        double sizeByte = 1;
        double sizeKb = sizeByte * 1024;
        double sizeMb = sizeKb * 1024;

        if (size >= sizeMb) {
            double sizeInMB = size / sizeMb;
            if (sizeInMB > 100) {
                return String.format("%.5s MB", sizeInMB);
            } else {
                return String.format("%.4s MB", sizeInMB);
            }
        } else if (size >= sizeKb && size < sizeMb) {
            return String.format("%.5s KB", size / sizeKb);
        } else {
            return String.format("%.3s byte", size);
        }
    }
}
