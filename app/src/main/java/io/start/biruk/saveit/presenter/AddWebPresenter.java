package io.start.biruk.saveit.presenter;

import org.greenrobot.eventbus.EventBus;

import io.start.biruk.saveit.events.FetchArticleEvent;
import io.start.biruk.saveit.util.HttpUtil;
import io.start.biruk.saveit.view.addwebView.AddWebView;

/**
 * Created by biruk on 5/12/2018.
 */
public class AddWebPresenter {
    private AddWebView addWebView;

    public AddWebPresenter(AddWebView addWebView) {
        this.addWebView = addWebView;
    }

    public void addContent(String text) {

        String resMsg;
        if (HttpUtil.isValid(text)) {
            EventBus.getDefault().post(new FetchArticleEvent(text));
            resMsg="added "+text+" to queue";
        }else {
            resMsg="unable to parse content";
        }
        addWebView.launchMainView(resMsg);

    }

}
