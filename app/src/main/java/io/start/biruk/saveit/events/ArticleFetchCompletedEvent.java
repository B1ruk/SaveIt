package io.start.biruk.saveit.events;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleFetchCompletedEvent {

    private String url;

    public ArticleFetchCompletedEvent(String url) {
        this.url=url;
    }

    public String getUrl() {
        return url;
    }
}
