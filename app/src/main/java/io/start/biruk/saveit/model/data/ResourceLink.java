package io.start.biruk.saveit.model.data;

/**
 * Created by biruk on 5/14/2018.
 */
public class ResourceLink {
    private String oldLinkPath;
    private String newLinkPath;

    public ResourceLink(String oldLinkPath, String newLinkPath) {
        this.oldLinkPath = oldLinkPath;
        this.newLinkPath = newLinkPath;
    }

    public String getOldLinkPath() {
        return oldLinkPath;
    }

    public String getNewLinkPath() {
        return newLinkPath;
    }
}
