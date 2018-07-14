package io.start.biruk.saveit.events;

/**
 * Created by biruk on 14/07/18.
 */

public class UrlFromClipboardEvent {
    private String url;

    public UrlFromClipboardEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
