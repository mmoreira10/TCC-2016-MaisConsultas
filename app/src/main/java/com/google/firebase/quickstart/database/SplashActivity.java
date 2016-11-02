package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;

        private final static int TIME_SPLASH = 2500;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);

            progressBar = (ProgressBar) findViewById(R.id.pBar);
            progressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent login = new Intent(SplashActivity.this, EscolhaLoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }, TIME_SPLASH);
        }
}
