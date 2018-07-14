package io.start.biruk.saveit.events;

/**
 * Created by biruk on 14/07/18.
 */

public class TagSelectEvent {
    private String tag;

    public TagSelectEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
