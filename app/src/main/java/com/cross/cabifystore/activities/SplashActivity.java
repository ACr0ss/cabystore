package com.cross.cabifystore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.cross.cabifystore.R;
import com.cross.cabifystore.data.ApiManager;
import com.cross.cabifystore.data.PreferencesManager;
import com.cross.cabifystore.utils.NetworkUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    private Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        splashScreen.setKeepOnScreenCondition(() -> true );

        compositeDisposable = new CompositeDisposable();
        startApp();
    }

    private void startApp() {
        Disposable disposable = NetworkUtils.hasInternetConnection().subscribe((hasInternet) -> {
            if (hasInternet) {
                ApiManager.getData(SplashActivity.this);
                closeActivity();
            } else if (new PreferencesManager(SplashActivity.this).getLastTimestamp() > 0) {
                Toast.makeText(SplashActivity.this, getString(R.string.no_internet_warning), Toast.LENGTH_LONG).show();
                launchStore();
            } else {
                Toast.makeText(SplashActivity.this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                closeActivity();
            }
        });

        compositeDisposable.add(disposable);
    }

    private void closeActivity() {
        handler.postDelayed(() -> {
            finishAffinity();
        }, 2000);
    }

    public void launchStore() {
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, CartActivity.class));
            finish();
        }, 500);
    }

}
