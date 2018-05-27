package io.start.biruk.saveit.model.articleFetcher;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by biruk on 5/12/2018.
 * Fetches the index page
 */
public class ArticleFetcher {

    public Single<String> fetchIndexPage(String url) {

        return Single.fromCallable(() -> getIndexPage(url))
                .filter(Response::isSuccessful)
                .retry(7)
                .map(response -> response.body().string())
                .toSingle();

    }

    private Response getIndexPage(String url) throws IOException {
        Request request = new Request.Builder()
                .url(parseUrl(url))
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        return response;
    }

    private HttpUrl parseUrl(String url) {
        return HttpUrl.parse(url);
    }


}
