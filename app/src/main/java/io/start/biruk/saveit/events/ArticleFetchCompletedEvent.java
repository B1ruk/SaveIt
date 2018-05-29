package io.start.biruk.saveit.events;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleFetchCompletedEvent {

    private String msg;

    public ArticleFetchCompletedEvent(String msg) {
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }
}
