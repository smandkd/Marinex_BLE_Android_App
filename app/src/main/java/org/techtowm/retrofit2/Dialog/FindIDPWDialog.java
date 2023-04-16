package org.techtowm.retrofit2.Dialog;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIDPWDialog extends DialogFragment {
    private TextView ed_name;
    private TextView ed_id;
    private TextView tv_ID;
    private TextView tv_PW;
    private Button btn_findID;
    private Button btn_findPW;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.find_id_pw_dialog_layout,container,false);

        btn_findID = view.findViewById(R.id.btn_ID);
        btn_findPW = view.findViewById(R.id.btn_findpw);
        ed_name = view.findViewById(R.id.ed_name);
        ed_id = view.findViewById(R.id.edtv_PW);
        tv_ID = view.findViewById(R.id.tv_ID);
        tv_PW = view.findViewById(R.id.tv_PW);

        btn_findID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("Sang", "FindIDPWDialog : ed_findID : " + ed_name.getText().toString());

                getUserID();
            }
        });

        btn_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserPassword();
            }
        });
        return view;
    }

    private void getUserPassword() {
        String id = ed_id.getText().toString().trim();

        if( id == null ) {
            tv_PW.setText("다시 입력하세요");
            tv_PW.setTextColor(Color.RED);

            return;
        }

        APIService apiInterface = APIClient.getClient().create(APIService.class);
        apiInterface.getPW(id).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                UserDTO data = response.body();

                Log.d("Sang", "FindIDPWDialog : " + response.code() + "  " + data.getPassword());

                if( response.isSuccessful() )
                {
                    if( data.getPassword() == null) {
                        tv_PW.setText("다시 입력하세요");
                        tv_PW.setTextColor(Color.RED);
                    }
                    else {
                        tv_PW.setText( "비밀번호 : " + data.getPassword() );
                        tv_PW.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("Sang", "FindIDPWDialog : " + t.toString() );
            }
        });
    }

    private void getUserID() {
        String name = ed_name.getText().toString().trim();

        if( name == null ) {
            tv_ID.setText("다시 입력하세요");
            tv_ID.setTextColor(Color.RED);

            return;
        }

        APIService apiInterface = APIClient.getClient().create(APIService.class);

        apiInterface.getID(name).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                UserDTO data = response.body();

                Log.d("Sang", "FindIDPWDialog : " + response.code() + "  " + data.getID() );

                if(response.isSuccessful()) {
                    if(data.getID() == null ) {
                        tv_ID.setText("다시 입력하세요");
                        tv_ID.setTextColor(Color.RED);
                    }
                    else {
                        tv_ID.setText("아이디 : " + data.getID());
                        tv_ID.setTextColor(Color.RED);
                    }
                }
                else {
                    Log.d("Sang", "FindIDPWDialog : failed" );
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("Sang", "FindIDPWDialog : failed  " + t.toString() );
            }
        });
    }
}
