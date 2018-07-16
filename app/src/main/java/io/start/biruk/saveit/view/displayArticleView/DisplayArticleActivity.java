package io.start.biruk.saveit.view.displayArticleView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.R;

public class DisplayArticleActivity extends AppCompatActivity {

    private static final String TAG = "DisplayArticleActivity";

    @Bind(R.id.web_view) WebView webView;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);

        ButterKnife.bind(this);

        this.preferences=PreferenceManager.getDefaultSharedPreferences(this);

        String articlePath = getIntent().getAction();
        initWebView(articlePath);
        if (!articlePath.isEmpty()) {
            String path = "file://" + articlePath + File.separator + "index.html";
            webView.loadUrl(path);
        }
    }


    private void initWebView(String articlePath) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG,"page load completed");
                takeScreenShotEx(articlePath);
                super.onPageFinished(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setBuiltInZoomControls(preferences.getBoolean(getResources().getString(R.string.zoomCtrlKey),false));
        webView.getSettings().setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        webView.setFocusable(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setFocusable(true);
    }

    private void takeScreenShotEx(String articlePath) {
        String imgPath = articlePath + File.separator + "sc.png";
        if (!articleScreenShotExist(imgPath)) {
            try {
                takeScreenShot(imgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void takeScreenShot(String imgPath) throws IOException {
        webView.setDrawingCacheEnabled(true);
        Bitmap screenShot = webView.getDrawingCache(true);
        File screenShotPath = new File(imgPath);
        screenShotPath.createNewFile();
        saveScreenShot(screenShot, imgPath);
    }

    private boolean articleScreenShotExist(String imgPath) {
        File screenShot = new File(Environment.getExternalStorageDirectory(), imgPath);
        return screenShot.exists();
    }

    private void saveScreenShot(Bitmap screenBitmap, String imgPath) {
        try {
            saveBitmap(screenBitmap, imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBitmap(Bitmap screenBitmap, String imgPath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(imgPath);
        screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
