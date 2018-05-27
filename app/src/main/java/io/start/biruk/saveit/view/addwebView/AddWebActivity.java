package io.start.biruk.saveit.view.addwebView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.start.biruk.saveit.presenter.AddWebPresenter;
import io.start.biruk.saveit.view.MainActivity;

/**
 * Created by biruk on 5/12/2018.
 */
public class AddWebActivity extends AppCompatActivity implements AddWebView{

    private static final String TAG = "AddWebActivity";
    AddWebPresenter addWebPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addWebPresenter=new AddWebPresenter(this);

        String text = getIntent().getStringExtra(Intent.EXTRA_TEXT).trim();
        addWebPresenter.addContent(text);

        Log.d(TAG, text);
    }

    @Override
    public void launchMainView(String action) {
        Intent launchMainView=new Intent(this, MainActivity.class);
        launchMainView.setAction(action);
        startActivity(launchMainView);
    }
}
