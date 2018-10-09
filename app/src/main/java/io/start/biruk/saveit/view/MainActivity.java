package io.start.biruk.saveit.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.service.ArticleFetcherService;
import io.start.biruk.saveit.util.FileUtil;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;
import io.start.biruk.saveit.view.dialog.AddUrlDialog;
import io.start.biruk.saveit.view.dialog.StatusDialog;
import io.start.biruk.saveit.view.searchView.SearchActivity;
import io.start.biruk.saveit.view.settingsView.SettingsActivity;
import io.start.biruk.saveit.view.tagsView.TagFragment;

public class MainActivity extends BaseThemeActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.main_view) View mainView;
    @Bind(R.id.main_toolbar) Toolbar mainToolbar;
    @Bind(R.id.fragment_container) View fragmentContainer;
    @Bind(R.id.bottom_nav) BottomNavigationView bottomNav;
    @Bind(R.id.launch_add_url) FloatingActionButton launchAddUrlFab;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent) {
        displayInfo("Page saved successfully");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        permissionCheck();


    }

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DialogOnDeniedPermissionListener storagePermission = DialogOnDeniedPermissionListener.Builder
                    .withContext(this)
                    .withTitle("Storage Permission")
                    .withButtonText(android.R.string.ok)
                    .withIcon(R.mipmap.ic_launcher)
                    .build();

            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Log.d(TAG,"storage permission success");
                            checkStorage();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Log.d(TAG,"storage permission deinied");
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    })
                    .check();
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Log.d(TAG,"internet permission success");
                            initViews();
                            initArticleFetcherService();

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Log.d(TAG,"internet permission deinied");
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    })
                    .check();


        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            checkStorage();
            initArticleFetcherService();
            initViews();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String action = getIntent().getAction();
        if (action != null) {
            if (!action.startsWith("android")) {
                displayInfo(action);
            }
        }
    }

    private void checkStorage() {
        FileUtil.createMainDirectory();
    }


    public void initArticleFetcherService() {
        Intent startArticleService = new Intent(this, ArticleFetcherService.class);
        startService(startArticleService);
    }


    private void initViews() {
        setSupportActionBar(mainToolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setDefaultView();
        setUpBottomNav();
        launchAddUrlFab.setOnClickListener(v -> launchAddUrlDialog());
    }

    public void setDefaultView() {
        bindFragment(ArticleFragment.newInstance(1));
    }

    public void launchAddUrlDialog() {

        AddUrlDialog addUrlDialog = new AddUrlDialog();
        addUrlDialog.show(getSupportFragmentManager(), TAG);
    }

    private void displayInfo(String msg) {
        Snackbar.make(mainView, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchBar = menu.findItem(R.id.main_search_bar);
        MenuItem settings = menu.findItem(R.id.menu_settings);
        MenuItem status = menu.findItem(R.id.menu_status);

        status.setOnMenuItemClickListener(item -> {
            StatusDialog statusDialog = new StatusDialog();
            statusDialog.show(getSupportFragmentManager(), TAG);
            return true;
        });

        settings.setOnMenuItemClickListener(item -> {

            Intent stAct = new Intent(this, SettingsActivity.class);
            startActivity(stAct);
            return true;
        });

        searchBar.setOnMenuItemClickListener(item -> {
            launchSearchView();
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void launchSearchView() {
        Intent launchSearchResults = new Intent(this, SearchActivity.class);
        startActivity(launchSearchResults);
    }


    public void setUpBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_article:
                    bindFragment(ArticleFragment.newInstance(1));
                    break;
                case R.id.bottom_nav_tag:
                    bindFragment(new TagFragment());
                    break;
                case R.id.bottom_nav_fav:
                    bindFragment(ArticleFragment.newInstance(2));
                    break;
            }
            return true;
        });
    }

    public void bindFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
