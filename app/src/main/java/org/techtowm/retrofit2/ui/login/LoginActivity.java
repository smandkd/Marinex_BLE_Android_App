
package org.techtowm.retrofit2.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.techtowm.retrofit2.Dialog.FindIDPWDialog;
import org.techtowm.retrofit2.NetworkState;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Data.UserData;
import org.techtowm.retrofit2.databinding.ActivityLoginBinding;
import org.techtowm.retrofit2.ui.UserActivity;
import org.techtowm.retrofit2.ui.join.MainActivity;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    // sharedpreference
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "email_key";
    public static final String NAME_KEY = "name_key";
    private String userId;
    private Integer userIdx;
    private String userName;

    // UserData
    public static final int REQUEST_CODE_USER = 101;
    public static final String KEY_SIMPLE_DATA = "data";

    private View view;

    // FindIDPWDialog
    private FindIDPWDialog FindPWDialog;

    // join, findid, findpw textview
    private TextView join;
    private TextView tv_findID;
    private TextView tv_findPW;
    private TextView login_result;

    // login button
    private Button login;

    // input id, password
    private EditText et_id, et_password;

    // toolbar
    Toolbar toolbar;

    // progressbar
    ProgressBar progressBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //setContentView(R.layout.activity_login);

        view = binding.layout;

        // toolbar, add backpressed button
        toolbar = view.findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = binding.progressBar;

        // find id, pw dialog
        FindPWDialog = new FindIDPWDialog();

        et_id = binding.etId;
        et_password = binding.etPassword;
        login = binding.LOGIN;
        join = binding.JOIN;
        tv_findID = binding.tvFindID;
        tv_findPW = binding.tvFindPW;
        login_result = binding.loginResult;

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        LoginViewModel loginViewModel = new LoginViewModel();

        loginViewModel.getProgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                progressBar.setVisibility(i);
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                login_result.setText(s);
                login_result.setTextColor(Color.RED);
            }
        });

        loginViewModel.getToken().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    LoginModel data = loginViewModel.getLoginModel();

                    editor.putString(ID_KEY, data.getId());
                    editor.putString(NAME_KEY, data.getName());
                    editor.apply();

                    userId = data.getId();
                    userIdx = data.getIdx();
                    userName = data.getName();

                    // start UserActivity
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    UserData userData = new UserData(userId, userIdx, userName);
                    intent.putExtra(KEY_SIMPLE_DATA, userData);
                    startActivityForResult(intent, REQUEST_CODE_USER);
                } else {
                    return;
                }
            }
        });

        // confirm id, pw in database that is existing
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.login(et_id.getText().toString().trim(), et_password.getText().toString().trim());
                //userLogin();
            }
        });

        // start join activity
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startjoin();
            }
        });

        // close findidpwdialog when monitor touched
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });

        // find id
        tv_findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPWDialog.show(getSupportFragmentManager(), "dialog");
            }
        });
        // find pw
        tv_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPWDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        // check network
        NetworkState networkState = new NetworkState();
        if (networkState.isConnected(getApplicationContext())) {

        } else {
            Toast.makeText(this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();

            return;
        }
    }

    // start join activity method
    private void startjoin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
