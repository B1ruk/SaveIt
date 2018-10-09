package io.start.biruk.saveit.model.articleFetcher;

import java.io.IOException;
import java.util.Arrays;

import io.reactivex.Observable;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by biruk on 5/12/2018.
 * Fetches the index page
 */
public class ArticleFetcher {

    public Observable<String> fetchIndexPage(String url) {
        String mainUrl = url;

        return Observable.fromCallable(() -> getIndexPage(url))
                .onErrorResumeNext(Observable.fromCallable(() -> getIndexPageUsingHttps(url)))
                .filter(Response::isSuccessful)
                .map(response -> response.body().string())
                .retry(3);

    }

    private Response getIndexPage(String url) throws IOException {
    return new OkHttpClient().newCall(buildRequest(url)).execute();
    }


    private Response getIndexPageUsingHttps(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS,ConnectionSpec.COMPATIBLE_TLS))
                .build();

        return okHttpClient.newCall(buildRequest(url)).execute();

    }

    public Request buildRequest(String url) {
        return new Request.Builder()
                .url(parseUrl(url))
                .build();
    }

    private HttpUrl parseUrl(String url) {
        return HttpUrl.parse(url);
    }


}
