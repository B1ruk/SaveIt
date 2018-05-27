package io.start.biruk.saveit.model.articleFetcher.responseFetcher;

import android.net.UrlQuerySanitizer;
import android.util.Log;

import com.annimon.stream.Stream;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.data.ResourceLink;
import io.start.biruk.saveit.model.data.ResourceType;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import static io.start.biruk.saveit.model.data.ResourceType.*;

/**
 * Created by biruk on 5/14/2018.
 */
public class ResourceFetcher {

    private static final String TAG = "ResourceFetcher";

    private String url;
    private String path;
    private Document responseDoc;
    private ResourcesLinkFetcher resLinkFetcher;


    public Single<Boolean> saveResponsesToStorage(String url, Document responseDoc, String articlePath) {
        init(url, responseDoc, articlePath);
        resLinkFetcher = new ResourcesLinkFetcher();

        return Observable.merge(saveImg(), saveCss(), saveJs())
                .toList()
                .map(resourceLinks -> replaceIndexPage(resourceLinks));

    }

    private boolean replaceIndexPage(List<ResourceLink> resourceLinks) throws IOException {
        String rawHtml = responseDoc.toString();
        Log.d(TAG, String.format("\t  before \t\n %s", rawHtml));

        for (ResourceLink resLink : resourceLinks) {
            rawHtml = rawHtml.replace(resLink.getOldLinkPath(), resLink.getNewLinkPath());
        }

        return createIndexPage(rawHtml);

    }


    private boolean createIndexPage(String responseBody) throws IOException {
        String indexPagePath = path + File.separator + "index.html";
        File indexPage = new File(indexPagePath);

        if (indexPage.createNewFile() || indexPage.exists()) {
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(indexPage));
            bufferedSink.writeUtf8(responseBody);
            bufferedSink.close();
        }
        return indexPage.exists();
    }

    private void init(String url, Document responseDoc, String articlePath) {
        this.url = url;
        this.responseDoc = responseDoc;
        this.path = articlePath;
    }

    private Observable<ResourceLink> saveJs() {

        List<String> allJsLinks = resLinkFetcher.getAllJsLinks(responseDoc);

        return Observable.defer(() -> Observable.fromIterable(allJsLinks))
                .flatMap(link -> Observable.just(saveToStorage(link, js))
                        .retry(7));

    }

    private Observable<ResourceLink> saveCss() {

        List<String> allCssLinks = resLinkFetcher.getAllCssLinks(responseDoc);

        return Observable.defer(() -> Observable.fromIterable(allCssLinks))
                .flatMap(link -> Observable.just(saveToStorage(link, css))
                        .retry(4));

    }

    private Observable<ResourceLink> saveImg() {

        List<String> allImgLinks = resLinkFetcher.getAllImgLinks(responseDoc);

        return Observable.defer(() -> Observable.fromIterable(allImgLinks))
                .flatMap(link -> Observable.just(saveToStorage(link, img))
                        .retry(4));

    }

    private ResourceLink saveToStorage(String link, ResourceType resourceType) throws IOException {

        File resFile;
        String newLink = resourceType + File.separator + getFileName(link);
        String resFilePath = path + File.separator + newLink;
        resFile = new File(resFilePath);

        Response response = fetchFile(link);
        resFile.createNewFile();

        if (resFile.exists()) {
            writeToFile(response, resFile);
        }

        return new ResourceLink(link, newLink);

    }

    private void writeToFile(Response rawResponse, File resFile) throws IOException {
        BufferedSource bufferedSource = rawResponse.body().source();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(resFile));
        bufferedSink.writeAll(bufferedSource);
        bufferedSink.close();
    }

    private Response fetchFile(String link) throws IOException {
        Request request = new Request.Builder()
                .url(getAbsolutePath(link))
                .build();

        Response response = new OkHttpClient().newCall(request).execute();
        return response;
    }

    private HttpUrl getAbsolutePath(String link) {
        if (link.startsWith("http") || link.startsWith("https")){
            return HttpUrl.parse(link);
        }

        if (url.endsWith("/") && link.startsWith("/")){
            String fullPath=url+link.substring(1,link.length());
            return HttpUrl.parse(fullPath);
        }
        else if (!url.endsWith("/") && !link.startsWith("/")){
            return HttpUrl.parse(url + "/" + link);
        }
        else
            return HttpUrl.parse(url+link);
    }

    private String getFileName(String link) {
        String[] names = link.split("/");
        return names[names.length - 1];       //returns the last name
    }


}
