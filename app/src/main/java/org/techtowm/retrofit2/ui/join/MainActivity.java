
package org.techtowm.retrofit2.ui.join;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.databinding.ActivityMainBinding;
import org.techtowm.retrofit2.ui.login.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    JoinViewModel joinViewModel;
    ActivityMainBinding binding;

    // views
    private Button insBtn;
    private EditText etId, etPassword, etName, etAge;
    private View view;

    // toolbar
    Toolbar toolbar;

    // if back button is pressed, then return to loginActivity
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        joinViewModel = new JoinViewModel();

        toolbar = binding.joinToolbar;
        setSupportActionBar(toolbar);
        // remove title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // show back pressed button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = binding.layout;

        etId = binding.etId;
        etPassword = binding.etPassword;
        etName = binding.etName;
        etAge = binding.etAge;
        insBtn = binding.btnInsert;

        // add new user's data to server
        insBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertData();
                joinViewModel.join(etId.getText().toString(), etPassword.getText().toString(), etName.getText().toString(), etAge.getText().toString());
            }
        });

        joinViewModel.getJoinResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d("SangJoinVM", "aBoolean " + aBoolean);
                    Toast.makeText(getApplicationContext(), "회원가입이 성공했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("SangJoinVM", "회원가입 실패, line 87");
                    return;
                }
            }
        });

        // check password is valid
        TextWatcher textWatcherPw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                joinViewModel.isPasswordValid(editable.toString().trim());
            }
        };

        TextWatcher textWatcherId = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                joinViewModel.checkDuplicateId(etId.getText().toString());
            }
        };

        joinViewModel.getDuplicateResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etId.setError(s);
            }
        });

        joinViewModel.getPwValidResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etPassword.setError(s);
            }
        });

        etId.addTextChangedListener(textWatcherId);
        etPassword.addTextChangedListener(textWatcherPw);

        // if touch the screen, then keyboard disappear.
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

    }
}
    /*
    // method that add new user's data
    private void insertData() {
        String id = etId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String age = String.valueOf(etAge.getText());


        if (id.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty()) {
            Toast.makeText(getApplicationContext(), "입력란이 비었습니다.", Toast.LENGTH_SHORT).show();
            // return;
        } else //if(joinViewModel.isPasswordValid(etPassword.getText().toString().trim()))
        {
            Call<UserDTO> call = APIClient.getInstance().getApiInterface().insertdata(id, password, name, age);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {

                    Log.d("SangJoinVM", "Join : " + response.body().getToken() );

                    if (response.isSuccessful()) {
                        if (response.body().getToken() == 1) {
                            Toast.makeText(getApplicationContext(), "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.d("SangJoinVM", "Fail2 : " + t.toString());
                }
            });
        }
    }
}


// check id is duplicated
    private void checkDuplicatedID(String s) {
        exist_id = s;

        APIService apiInterface = APIClient.getClient().create(APIService.class);

        apiInterface.getData().enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                Log.d("Sang", "MainActivity( receive data ) : " + response.code() + "");
                List<UserDTO> data = response.body();

                for( int i = 0; i < data.size(); i++ ) {
                    if( exist_id.equals(data.get(i).getID()) ) {
                        tvId.setText("아이디 중복");
                        tvId.setTextColor(Color.RED);

                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Log.d("Sang", "Fail1 : " + t.toString());
            }
        });
    }
}
Call<List<UserDTO>>call = APIClient.getInstance().getApiInterface().insertdata(id, password, name, age);
    call.enqueue(new Callback<List<UserDTO>>() {
        @Override
        public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
            Log.d("Sang", "Join : " + String.valueOf(response.code()) + response.errorBody());
        }

        @Override
        public void onFailure(Call<List<UserDTO>> call, Throwable t) {
            Log.d("Sang", "Fail2 : " + t.toString());
        }
    });
- enqueue : 비동기 방식, execute : 동기 방식

Logcat 으로 server json data를 출력하고 'userID', 'userPassword', 'userIdx', 'userName', 'userAge'
를 입력하면 서버 데이터 베이스로 insert 한후 LoginActivity 로 이동한다.
 */