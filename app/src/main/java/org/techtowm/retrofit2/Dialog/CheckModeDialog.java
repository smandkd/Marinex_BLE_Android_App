package org.techtowm.retrofit2.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.techtowm.retrofit2.R;

public class CheckModeDialog extends DialogFragment {

    private Context context;
    private View view;

    private RadioGroup radioGroup;

    private Button btnCheck;

    private DialogListener dialogListener;

    Integer mode;

    public CheckModeDialog(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.check_mode_dialog, container, false);
        radioGroup = view.findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i) {
                    case R.id.mode_500:
                        mode = 500;
                        Log.d("Sang", "CheckModeDialog : " + mode);


                        break;
                    case R.id.mode_1000:
                        mode = 1000;
                        Log.d("Sang", "CheckModeDialog : " + mode);


                        break;
                    case R.id.mode_free:
                        mode = 0;
                        Log.d("Sang", "CheckModeDialog : " + mode);

                        break;
                }

            }

        });



        btnCheck = view.findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mode == null ) {
                    Toast.makeText(getContext(), "모드를 선택해주십시오." , Toast.LENGTH_SHORT).show();

                    return;
                }

                dialogListener.checkModeFinished(mode);
                dismiss();
            }
        });

        return view;
    }

    public interface DialogListener {
        void checkModeFinished(Integer mode);
    }
    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
