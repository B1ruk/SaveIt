package io.start.biruk.saveit.model.articleFetcher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;

import io.reactivex.Single;
import io.start.biruk.saveit.model.data.ResourceType;
import io.start.biruk.saveit.util.FileUtil;
import io.start.biruk.saveit.util.HashUtil;
import okio.BufferedSink;
import okio.Okio;

import static io.start.biruk.saveit.model.data.ResourceType.*;
import static io.start.biruk.saveit.model.data.ResourceType.img;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleSaver {


    public Single<String> createArticleSingle(String url, String responseBody) {

        return Single.fromCallable(() -> createArticle(url, responseBody))
                .filter(path -> !path.isEmpty())
                .toSingle();
    }


    private String createArticle(String url, String responseBody) throws IOException {

        String articlePath = FileUtil.getMainPath() + File.separator + HashUtil.getHash(url);
        File articleDir = new File(articlePath);

        if (!articleDir.exists()) {
            articleDir.mkdir();
            createSubDirectory(articleDir.getPath());
        }
        createIndexPage(articleDir.getPath(), responseBody);

        return articleDir.getPath();
    }


    //creates directories within the path specified
    private boolean createSubDirectory(String mainPath) {
        List<ResourceType> subFolders = Arrays.asList(js, css, img);
        File subDir;

        for (ResourceType path : subFolders) {
            subDir = new File(mainPath + File.separator + path);
            subDir.mkdir();
        }

        //checks if the contents within the dir equals the no of subfolders
        boolean status = new File(mainPath).list().length == subFolders.size();

        return status;
    }

    private boolean createIndexPage(String path, String responseBody) throws IOException {
        String indexPagePath = path + File.separator + "index.html";
        File indexPage = new File(indexPagePath);

        if (indexPage.createNewFile() || indexPage.exists()) {
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(indexPage));
            bufferedSink.writeUtf8(responseBody);
            bufferedSink.close();
        }
        return indexPage.exists();
    }
}
