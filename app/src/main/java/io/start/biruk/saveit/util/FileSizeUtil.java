package io.start.biruk.saveit.util;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.data.ResourceType;

/**
 * Created by biruk on 26/09/18.
 */

public class FileSizeUtil {

    public static Single<Long> computeFileSize(String path) {
        return getFiles(path)
                .filter(File::isFile)
                .map(File::length)
                .reduce((size1, size2) -> size1 + size2)
                .toSingle();
    }

    protected static Observable<File> getFiles(String path) {
        String mainPath = path + File.separator;

        File cssDir = new File(mainPath + ResourceType.css);
        File jsDir = new File(mainPath + ResourceType.js);
        File imgDir = new File(mainPath + ResourceType.img);

        return Observable.merge(
                Observable.fromArray(cssDir.listFiles()),
                Observable.fromArray(jsDir.listFiles()),
                Observable.fromArray(imgDir.listFiles())
        );
    }

    public static String sizeFormater(long size) {
        long sizeByte = 1;
        long sizeKb = sizeByte * 1024;
        long sizeMb = sizeKb * 1024;

        if (size >= sizeMb) {
            return String.format("%s MB", size / sizeMb);
        } else if (size >= sizeKb && size<sizeMb){
            return String.format("%s KB", size / sizeKb);
        }else{
            return String.format("%s byte", size);
        }
    }
}
