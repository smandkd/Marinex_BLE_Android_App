package org.techtowm.retrofit2.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteRecordDialog extends DialogFragment {
    // array list of bundle data
    private ArrayList<Integer> nolist;

    //  btn yes, no
    Button btnYes;
    Button btnNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.delete_record_dialog_layout,container,false);

        // get bundle from UserTabFragment1, and put data into nolist
        Bundle bundle = getArguments();
        if( bundle != null ) {
            nolist = bundle.getIntegerArrayList("recordnolist");

            for(Integer t : nolist) {
                Log.d("Sang", "DeleteRecordDialog : " + nolist.toString());
            }
        }

        // buttons
        btnYes = view.findViewById(R.id.btn_yes);
        btnNo = view.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDelete(nolist);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Sang", "DeleteRecordDialog is canceled " );
                dismiss();
            }
        });

        return view;
    }

    private void recordDelete(ArrayList<Integer> nolist) {
        ArrayList<Integer> recordNoList = nolist;

        APIService apiInterface = APIClient.getClient().create(APIService.class);

        for( Integer t : recordNoList ) {
            Log.d("Sang", "DeleteRecordDialog : " + t.toString());

            apiInterface.deleteRecordChecked(Integer.parseInt(t.toString())).enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if(response.isSuccessful() ) {
                        Log.d("Sang", "DeleteRecordDialog : delete success");
                        Toast.makeText(getActivity(), "기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else {
                        Log.d("Sang", "DeleteRecordDialog : delete Failed");
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.d("Sang", "DeleteRecordDialog : " + t.toString() );
                }
            });
        }
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
