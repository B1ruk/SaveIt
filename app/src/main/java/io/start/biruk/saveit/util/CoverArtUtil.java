package io.start.biruk.saveit.util;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 19/07/18.
 */

public class CoverArtUtil {

    public static List<String> getCoverArtPath(List<ArticleModel> articleModels) {
        return Stream.of(articleModels)
                .map(ArticleModel::getPath)
                .filter(path -> new File(path + File.separator + "sc.png").exists())
                .toList();
    }

    public static boolean coverArtExists(String path) {
        File scPath = new File(path + File.separator + "sc.png");
        return scPath.exists();
    }

}
