package io.start.biruk.saveit.events;

/**
 * Created by biruk on 5/12/2018.
 */
public class FetchArticleEvent {
    private String url;

    public FetchArticleEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
