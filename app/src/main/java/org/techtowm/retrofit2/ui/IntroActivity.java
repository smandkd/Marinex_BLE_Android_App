
package org.techtowm.retrofit2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.ui.login.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}