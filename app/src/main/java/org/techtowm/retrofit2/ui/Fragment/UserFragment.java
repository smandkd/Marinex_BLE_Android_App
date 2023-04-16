
package org.techtowm.retrofit2.ui.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.techtowm.retrofit2.NetworkState;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.Data.UserData;
import org.techtowm.retrofit2.ui.UserTabFragment.UserTabFragment1;
import org.techtowm.retrofit2.ui.UserTabFragment.UserTabFragment2;

public class UserFragment extends Fragment {
    // for login session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String KEY_SIMPLE_DATA = "data";
    SharedPreferences sharedPreferences;

    // Record table
    TableLayout tableLayout;
    TableLayout tableLayout_user;
    TableRow tableRow;

    // show user's info at the top of monitor
    TextView userid;

    // show progress dialog during set record table
    ProgressDialog progressDialog;

    // tablayout
    TabLayout tabLayout;
    UserTabFragment1 userTabFragment1;
    UserTabFragment2 userTabFragment2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // reference text view
        userid = (TextView) view.findViewById(R.id.tv_id);

        // get data from sharedpreference
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        UserData data = bundle.getParcelable(KEY_SIMPLE_DATA);

        if( intent != null ) {

            if(intent != null) {
                Log.d("Sang", "LoginActivity : intent is not null");
                Log.d("Sang", "LoginActivity : " + "\n"
                        + " userid : " + data.userId
                        + " userName : " + data.userName
                        + " userIdx : " + data.userIdx);
                userid.setText(data.userName + "님 환영합니다. ");

            }
        }

        // tablayout
        tabLayout = view.findViewById(R.id.tab_layout);
        userTabFragment1 = new UserTabFragment1();
        userTabFragment2 = new UserTabFragment2();
        getChildFragmentManager().beginTransaction().replace(R.id.user_framelayout, userTabFragment1).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                Fragment selected = null;
                if(pos == 0) {
                    selected = userTabFragment1;
                }
                else if(pos == 1) {
                    selected = userTabFragment2;
                }

                getChildFragmentManager().beginTransaction().replace(R.id.user_framelayout, selected).commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // check network state
        NetworkState networkState = new NetworkState();
        if(networkState.isConnected(getActivity())) {

        }
        else {
            Toast.makeText(getActivity(), "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();

            return view;
        }

        return view;
    }
}