package org.techtowm.retrofit2.ui.UserTabFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.Data.UserData;
import org.techtowm.retrofit2.Dialog.DeleteRecordDialog;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;
import org.techtowm.retrofit2.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTabFragment1 extends Fragment implements DialogInterface.OnDismissListener{
    // for login session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String KEY_SIMPLE_DATA = "data";
    SharedPreferences sharedPreferences;
    String userID;

    View view;

    // Record table
    TableLayout tableLayout;
    TableLayout tableLayout_user;
    TableRow tableRow;
    TextView tvNoRecord;

    // show progress dialog during set record table
    ProgressDialog progressDialog;

    // record delete button
    Button btnDelete;

    // checkbox
    //CheckBox box;
    MaterialCheckBox box;
    LinearLayout linearLayout;

    // column number that is checked of record table
    Integer recordNo;
    ArrayList<Integer> recordNoList;

    // delete record dialog
    DeleteRecordDialog deleteRecordDialog;

    // button logout
    Button logout;

    // meterial checkbox
    MaterialCheckBox checkbox;
    

    public static UserTabFragment1 newInstance() {
        return new UserTabFragment1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_tab1, container, false);

        //meterial checkbox
        //checkbox = view.findViewById(R.id.checkbox);
        /*
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if( isChecked ) {
                    box.setChecked(true);
                }
                else {
                    box.setChecked(false);
                }
            }
        });

         */

        // reference logout button
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // delete record dialog
        deleteRecordDialog = new DeleteRecordDialog();

        // record number list
        recordNoList = new ArrayList<>();

        // progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("데이터를 받아오는 중입니다.");
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar);
        progressDialog.show();

        // reference table layout
        tableLayout_user = (TableLayout) view.findViewById(R.id.user_tablelist);
        tvNoRecord = (TextView) view.findViewById(R.id.tv_norecord);
        tvNoRecord.setVisibility(view.GONE);
        tableLayout_user.setVisibility(view.GONE);

        // get data from sharedpreference
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        UserData data = bundle.getParcelable(KEY_SIMPLE_DATA);
        userID = data.userId;

        // show user's record
        showMyRecord(userID);

        // record delete button
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnDelete.setVisibility(view.GONE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDelete();
            }
        });

        return view;
    }

    // show user's record
    private void showMyRecord(String id) {
        APIService apiInterface = APIClient.getClient().create(APIService.class);

        apiInterface.showMyRecord(id).enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                List<UserDTO> data = response.body();
                Integer count = 0;

                if(response.isSuccessful()) {
                    progressDialog.dismiss();

                    if( data.isEmpty() ) {
                        tvNoRecord.setVisibility(view.VISIBLE);
                        tableLayout_user.setVisibility(view.GONE);

                        return;
                    }
                    else {
                        tvNoRecord.setVisibility(view.GONE);
                        tableLayout_user.setVisibility(view.VISIBLE);
                    }

                    for (UserDTO field : data) {
                        Log.d("Sang", "field record num : " + field.getRecordNo());

                        tableRow = new TableRow(getActivity());
                        tableRow.setId(100+count);
                        tableRow.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        tableRow.setBackgroundColor(Color.WHITE);
                        //tableRow.setPadding(0,0,0,3);

                        //checkbox
                        box = new MaterialCheckBox(getActivity());
                        box.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                        box.setGravity(Gravity.CENTER);
                        box.setBackgroundColor(Color.WHITE);
                        tableRow.addView(box);

                        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    btnDelete.setVisibility(view.VISIBLE);
                                    addListRecordNoChecked(field.getRecordNo());
                                }
                                else {
                                    removeListNo(field.getRecordNo());
                                    btnDelete.setVisibility(view.VISIBLE);

                                    if( recordNoList.isEmpty() ) {
                                        btnDelete.setVisibility(view.GONE);
                                    }
                                }
                            }
                        });

                        TextView table_column_id = new TextView(getActivity());
                        TextView table_column_date = new TextView(getActivity());
                        TextView table_column_time = new TextView(getActivity());
                        TextView table_column_mode = new TextView(getActivity());

                        table_column_date.setId(200+count);
                        table_column_date.setText(String.valueOf(field.getDate()));
                        table_column_date.setGravity(Gravity.CENTER);
                        table_column_date.setTextColor(Color.BLACK);
                        table_column_date.setBackgroundColor(Color.WHITE);
                        tableRow.addView(table_column_date);

                        table_column_id.setId(200+count);
                        table_column_id.setGravity(Gravity.CENTER);
                        table_column_id.setText(field.getID());
                        table_column_id.setTextColor(Color.BLACK);
                        table_column_id.setBackgroundColor(Color.WHITE);
                        tableRow.addView(table_column_id);

                        table_column_time.setId(200+count);
                        table_column_time.setText(getTimeFormat(field.getTime()));
                        table_column_time.setGravity(Gravity.CENTER);
                        table_column_time.setTextColor(Color.BLACK);
                        table_column_time.setBackgroundColor(Color.WHITE);
                        tableRow.addView(table_column_time);

                        table_column_mode.setId(200+count);
                        if( field.getMode() == 0) {
                            table_column_mode.setText("자유모드");
                        }
                        else {
                            table_column_mode.setText(String.valueOf(field.getMode()) + "m");
                        }
                        table_column_mode.setGravity(Gravity.CENTER);
                        table_column_mode.setTextColor(Color.BLACK);
                        table_column_mode.setBackgroundColor(Color.WHITE);
                        tableRow.addView(table_column_mode);

                        tableLayout_user.addView(tableRow, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.WRAP_CONTENT,
                                TableLayout.LayoutParams.WRAP_CONTENT
                        ));
                        count++;
                    }
                }
                else {
                    Log.d("Sang", "UserActivity : showMyRecord is failed");
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Log.d("Sang", "UserActivity : showmyrecord : " + t.toString() );
            }
        });
    }

    // add record index to recordNoList
    private void addListRecordNoChecked(Integer num) {
        Integer recordno = num;
        recordNoList.add(recordno);
        // Observable
    }

    private void removeListNo(Integer num) {
        Log.d("Sang", "UserTabFragment1 record num :  " + num);
        recordNoList.remove(num);
    }

    // delete specific user's record single or multiple
    private void recordDelete() {
        // bundle that include array list of record no
        for( Integer t : recordNoList ) {
            Log.d("Sang", "UserTabFragment1 record index " + t);
        }

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("recordnolist", recordNoList);
        deleteRecordDialog.setArguments(bundle);
        deleteRecordDialog.show(getChildFragmentManager(), "dialog");
    }

    public static String getTimeFormat(Integer time) {
        String min = String.valueOf(time/60);
        String sec = String.valueOf(time%60);

        String out_time = min + " : " + sec ;
        return out_time;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    // logout
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Log.d("Sang", "UserActivity : Logout");
        Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}