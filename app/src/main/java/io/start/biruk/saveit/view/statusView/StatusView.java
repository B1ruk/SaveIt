package io.start.biruk.saveit.view.statusView;

/**
 * Created by biruk on 02/10/18.
 */

public interface StatusView {
    void displayTotalSize(String msg);

    void displayArticleCount(String msg);

    void displayError(String msg,Throwable e);
}
