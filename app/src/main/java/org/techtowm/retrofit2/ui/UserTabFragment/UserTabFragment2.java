package org.techtowm.retrofit2.ui.UserTabFragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.techtowm.retrofit2.Adapter.UTF2RecyclerViewAdapter;
import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTabFragment2 extends Fragment {
    View view;

    UTF2RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<UserDTO> allRecordList;

    // show progress dialog during set record table
    ProgressDialog progressDialog;

    // spinner
    Spinner spinner;

    public static UserTabFragment2 newInstance() {
        return new UserTabFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_tab2, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.usertab2_recycler_view);

        showASC(1000);
        prepareRecyclerView();

        //spinner
        spinner = (Spinner) view.findViewById(R.id.spinner_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.mode_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Sang", "UserTabFragment2 : " + spinner.getItemAtPosition(i));

                if(spinner.getItemAtPosition(i).toString().equals("1000m")) {
                    Log.d("Sang", "UserTabFragment2 : selected '1000m' mode" );
                    if( allRecordList == null ) {

                    }
                    else {
                        allRecordList.clear();
                    }

                    showASC(1000);
                }
                else {
                    Log.d("Sang", "UserTabFragment2 : selected '500m' mode" );
                    if( allRecordList == null ) {

                    }
                    else {
                        allRecordList.clear();
                    }
                    showASC( 500);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("데이터를 받아오는 중입니다.");
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar);
        progressDialog.show();

        // reference table layout
        return view;
    }

    // show all user's record
    private void showASC(Integer mode) {
        APIService apiInterface = APIClient.getClient().create(APIService.class);

        apiInterface.showReByMode(mode).enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if(response.isSuccessful()) {
                    progressDialog.dismiss();
                    allRecordList = response.body();
                    prepareAdapter(allRecordList);
                } else {
                    Log.d("Sang", "UserTabFragment2 Failure : " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Log.d("Sang", "UserTabFragment2 Failure : " + t.toString());
                t.printStackTrace();
            }
        });

    }

    public void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void prepareAdapter(List<UserDTO> list) {
        adapter = new UTF2RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);
    }


}
