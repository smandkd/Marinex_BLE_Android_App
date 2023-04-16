package org.techtowm.retrofit2.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Retrofit.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreRecordDialog extends DialogFragment {
    // store_record_dialog_layout's views
    private TextView tvDialog;
    private Button btnYes;
    private Button btnNo;

    // record
    String id;
    Double speed ;
    Integer time = 0;
    Integer distance = 20;
    Integer mode = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.store_record_dialog_layout, container, false);

        tvDialog  = view.findViewById(R.id.tv_dialog);
        btnYes = view.findViewById(R.id.btn_yes);
        btnNo = view.findViewById(R.id.btn_no);

        // get bundle data from UserRecord
        Bundle bundle = getArguments();
        if( bundle != null ) {
            Log.d("Sang", "StoreRecordFragment : " + speed + " " + time + " " + distance + " " + mode +  "  " + id);

            speed = bundle.getDouble("speed");
            time = bundle.getInt("time");
            distance = bundle.getInt("distance");
            mode = bundle.getInt("mode");
            id = bundle.getString("id");
        }

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // upload record
                Log.d("Sang", "RecordFragment : store record dialog btn yes");
                store_record(speed, time, distance, mode);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // back to UserFragment
                Log.d("Sang", "RecordFragment : store record dialog btn no");
                dismiss();
            }
        });


        return view;
    }

    private void store_record(Double speed, Integer time, Integer distance, Integer mode) {

        String user_distance = String.valueOf(distance);
        String user_speed = String.valueOf(speed);
        String user_time = String.valueOf(time);
        Integer user_mode = mode;

        Log.d("Sang", "RecordFragment : " + "\n" +
                "user_distance : " + user_distance + "\n" +
                "user_speed : " + user_speed + "\n" +
                "user_time : " + user_time + "\n" +
                "mode : " + mode);

        // retrofit
        Call<List<UserDTO>> call = APIClient.getInstance().getApiInterface().addrecord(user_distance, user_speed, user_time, id, user_mode);
        call.enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {

                if(response.isSuccessful()) {
                    Log.d("Sang", "StoreRecordDialog : update user_record succeed" );
                    Toast.makeText(getActivity(), "기록이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    // if retrofit is succed, then go back to UserFragment
                    dismiss();

                }
                else {
                    Log.d("Sang", "StoreRecordDialog : responsecode " + response.code() );
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Log.d("Sang", "StoreRecordDialog : Fail" + t.toString());
            }
        });
    }

    // dimiss method
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        Fragment parentFragment = getParentFragment();
        if( parentFragment instanceof DialogInterface.OnDismissListener ) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
    }
}
