
package org.techtowm.retrofit2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtowm.retrofit2.Dialog.CheckModeDialog;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.ui.Fragment.BluetoothFragment;
import org.techtowm.retrofit2.ui.Fragment.RecordFragment;
import org.techtowm.retrofit2.ui.Fragment.UserFragment;

public class UserActivity extends AppCompatActivity implements CheckModeDialog.DialogListener {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BluetoothFragment bluetoothFragment = new BluetoothFragment();
    private RecordFragment recordFragment = new RecordFragment();
    private UserFragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, bluetoothFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.bluetoothItem:
                        transaction.replace(R.id.framlayout, bluetoothFragment).commitAllowingStateLoss();
                        break;

                    case R.id.recordItem:
                        transaction.replace(R.id.framlayout, recordFragment).commitAllowingStateLoss();
                        break;

                    case R.id.userItem:
                        transaction.replace(R.id.framlayout, userFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void checkModeFinished(Integer mode) {

    }

}