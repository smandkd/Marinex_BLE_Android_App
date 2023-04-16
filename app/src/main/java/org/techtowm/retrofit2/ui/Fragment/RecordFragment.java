package org.techtowm.retrofit2.ui.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.IBinder;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.techtowm.retrofit2.Data.UserData;
import org.techtowm.retrofit2.Dialog.CheckModeDialog;
import org.techtowm.retrofit2.Dialog.StoreRecordDialog;
import org.techtowm.retrofit2.NetworkState;
import org.techtowm.retrofit2.R;
import org.techtowm.retrofit2.TextUtil;
import org.techtowm.retrofit2.ui.SerialListener;
import org.techtowm.retrofit2.ui.SerialService;
import org.techtowm.retrofit2.ui.SerialSocket;

import java.math.BigDecimal;

public class RecordFragment extends Fragment
        implements CheckModeDialog.DialogListener, ServiceConnection, SerialListener
{
    // show dialog that store record on database
    private StoreRecordDialog storeRecordDialog;
    // check mode dialog
    CheckModeDialog checkModeDialog;
    // Toolbar section
    Menu menu;
    // for login session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String KEY_SIMPLE_DATA = "data";
    SharedPreferences sharedPreferences;
    // would be sent to url including below
    private String id;
    private Integer mode;

    // for record's UI
    private Button btn_start;
    private Button btn_stop;
    private Button btn_reset;
    private Button btn_store;
    private TextView tv_distance;
    private TextView tv_speed;

    // record distance, speed, time for Performance Monitor
    private Chronometer chronometer;

    int[] stop_distance = {0};

    String stop_speed;

    long stopTime = 0;
    int millisecond;
    long time;
    String distance = "-- ";
    String Speed;
    int distance_int;
    double distance_double;
   // float lc_right;
   // float lc_left;
   // float speed_float;
    double speed;
    private String newline = TextUtil.newline_crlf;

    private enum Connected { False, Pending, True }

    private SerialService service;
    private String deviceAddress;
    private String deviceName;
    private Connected connected = Connected.False;
    private boolean hexEnabled = false;
    private boolean initialStart = true;

    @SuppressWarnings("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("Lifetime" , "onAttach");
        super.onAttach(context);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Lifetime" , "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Log.d("Sang", "RecordFragment onCreate " + connected.toString() );

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                deviceName = result.getString("MyString");
                deviceAddress = result.getString("device");
                Log.d("Sang",deviceName + "  " + deviceAddress);
            }
        });
    }

    // onCreateView()

    @Override
    public void onStart() {
        Log.d("Lifetime" , "onStart");
        super.onStart();

        if(service != null) {
            service.attach(this);
        }
        else {
            getActivity().startService(new Intent(getActivity(), SerialService.class));
            // prevents service destroy on unbind from recreated activity caused by orientation change
        }
    }

    @Override
    public void onResume() {
        Log.d("Lifetime" , "onResume");
        super.onResume();
        /*
        if(initialStart && service != null) {
            initialStart = false;

            getActivity().runOnUiThread(this::connect);
        }

         */

    }

    @Override
    public void onStop() {
        Log.d("Lifetime" , "onStop");
/*
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
*/
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("Lifetime" , "onDestroy");

        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));


        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("Lifetime" , "onDetach");
        /*
        try { getActivity().unbindService(this); } catch(Exception ignored) {}

         */
        super.onDetach();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recordfragment_top_app_bar, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mode_1000 : {
                setMode(1000);
                Toast.makeText(getActivity(), "1000m 주행모드입니다. ", Toast.LENGTH_SHORT).show();
                Log.d("Sang", "RecordFragment : " + mode);
                return true;
            }
            case R.id.mode_500: {
                setMode(500);
                Toast.makeText(getActivity(), "500m 주행모드입니다. ", Toast.LENGTH_SHORT).show();
                Log.d("Sang", "RecordFragment : " + mode);
                return true;
            }
            case R.id.mode_free: {
                Toast.makeText(getActivity(), "자유 주행모드입니다. ", Toast.LENGTH_SHORT).show();
                setMode(0);
                return true;
            }
            default : return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifetime" , "onCreateView");

        try{
            getActivity().runOnUiThread(this::connect);
            connected = Connected.True;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Log.d("SANG", "Enum Connected : " + connected );

        View view = inflater.inflate(R.layout.fragment_record_2, container, false);

        // check mode dialog
        checkModeDialog = new CheckModeDialog(getContext());
        checkModeDialog.setDialogListener(new CheckModeDialog.DialogListener() {
            @Override
            public void checkModeFinished(Integer mode) {
                setMode(mode);
            }
        });
        checkModeDialog.setCancelable(false);
        checkModeDialog.show(getChildFragmentManager(), "dialog");

        // record store dialog
        storeRecordDialog = new StoreRecordDialog();

        // get Bundle data that includes an id.
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        UserData data = bundle.getParcelable(KEY_SIMPLE_DATA);
        id = data.userId;

        // reference about record's buttons, chronometer and textview.
        btn_start = view.findViewById(R.id.btn_start);
        btn_stop = view.findViewById(R.id.btn_stop);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_store = view.findViewById(R.id.btn_store);
        tv_distance = view.findViewById(R.id.distance);
        tv_speed = view.findViewById(R.id.speed);
        chronometer = view.findViewById(R.id.time);

        // start, stop, reset, store buttons
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mode == null ) {
                    return;
                }
                startRecording();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mode == null ) {
                    return;
                }

                stopRecording();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mode == null ) {
                    return;
                }
                resetRecoring();
                Integer num = 1;
                send(String.valueOf(num));
            }
        });

        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mode == null ) {
                    return;
                }
                store_record();
            }
        });


        // check connection of network.( lte, wifi etc. )
        NetworkState networkState = new NetworkState();
        if(networkState.isConnected(getActivity())) {}
        else {
            Toast.makeText(getActivity(), "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            return view;
        }

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = (SystemClock.elapsedRealtime() - chronometer.getBase())/1000;
            }
        });

        tv_distance.setText("-- m");

        if( distance == null ) {
            tv_speed.setText("-- m/s");

        }
        return view;
    }

    private void setMode(int i) {
        // if mode would be changed, then stop recording and reset.
        mode = i;
        stopRecording();
        resetRecoring();
    }

    private void stopRecording() {

        stop_distance[0] = distance_int;

        chronometer.stop();
        btn_start.setEnabled(true);
        btn_stop.setEnabled(false);
        btn_store.setEnabled(true);

        stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        millisecond = (int) (((-1) * stopTime)/1000);

        time = millisecond;
        stop_speed = String.valueOf((double)(  stop_distance[0]/millisecond ));
        status_distance(String.valueOf( stop_distance[0]), stop_speed);
        Log.d("Sang", "millisecond : " + millisecond + " stop distance : " +  stop_distance[0] );
    }

    private void startRecording() {
        btn_start.setEnabled(false);
        btn_stop.setEnabled(true);
        btn_store.setEnabled(false);

        try {
            Thread.sleep(3000);
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
            distance = "0 ";
            time = chronometer.getBase();
            Log.d("Time", String.valueOf(time));
        }catch( InterruptedException e ) {

        }
    }

    // reset recording
    private void resetRecoring() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        stopTime = 0;
        //tv_speed.setText(0 + "m/s");
        status_distance("-- ","-- ");
        btn_stop.setEnabled(true);
        btn_start.setEnabled(true);
        btn_store.setEnabled(true);
    }

    // store recording. use retrofit and send to marinexsport.com with queries( distance, speed, time, id, mode);
    private void store_record() {
        // if mode is null, then toast below message on the app
        if( mode == null ) {
            Toast.makeText(getActivity(), "주행모드를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // put datas to bundle
        Bundle bundle = new Bundle();
        bundle.putLong("distance", distance_int);
        bundle.putInt("time", millisecond);
        bundle.putDouble("speed", speed);
        bundle.putInt("mode", mode);
        bundle.putString("id", id);
        storeRecordDialog.setArguments(bundle);
        // start StoreRecordDialog
        storeRecordDialog.show(getChildFragmentManager(), "dialog");
    }



    private void connect() {
        Log.d("Sang", "RecordFragment bluetooth connecting");
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

            connected = Connected.Pending;

            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);

            //status("connected to " + "  " + deviceAddress);
            Log.d("Sang", "RecordFragment bluetooth connected");

        } catch (Exception e) {
            Log.d("SANG", "connect() error : " + e);
        }
    }

    private void disconnect() {
        Log.d("Sang", "RecordFragment Bluetooth Disconnect");
        connected = Connected.False;
        service.disconnect();
    }

    private void send(String str) {

        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("SANG", "send str : " + str);

        try {
            String msg;
            byte[] data;
            if(hexEnabled) {
                StringBuilder sb = new StringBuilder();
                TextUtil.toHexString(sb, TextUtil.fromHexString(str));
                TextUtil.toHexString(sb, newline.getBytes());
                msg = sb.toString();
                data = TextUtil.fromHexString(msg);
            } else {
               // msg = str;
               // data = (str + newline).getBytes();
                data = (str + newline).getBytes();
            }
            //SpannableStringBuilder spn = new SpannableStringBuilder(msg + '\n');
            //spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //receiveText.append(spn);
            Log.d("SANG", "send() service.write before");
            service.write(data);
            Log.d("SANG", "send() service.write after");
            Log.d("SANG", "send data : " + data);
        } catch (Exception e) {
            Log.d("SANG", "send error : " + e);
            e.printStackTrace();
            onSerialIoError(e);
        }
    }

    private void receive(byte[] data) {
        String msg = new String(data);


        if( newline.equals(TextUtil.newline_crlf) && (msg.length() > 0) ) {
            msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);

            String data1 = msg.replaceAll("\n", ",");
            String datann = data1.replaceAll("     ", "");
            String dataFin = datann.replace(" ", "");
            int data_fir_len = dataFin.length();

            if (data_fir_len < 10) {
               // left_power = dataFin.replace(",", "");
                //lc_left = Float.parseFloat(left_power);
            } else if( dataFin.contains("S") ||  dataFin.contains("T")) {
                Log.d("Sang", "Arduino Message : " + dataFin );
            } else {
                try{
                    String[] splitData = dataFin.split(",");
                    //right_power = splitData[0];
                    //lc_right = Float.parseFloat(right_power);

                    distance = splitData[1];
                    distance_double = Double.parseDouble(distance);
                    distance_int = ((int)distance_double)/10;

                    if( distance_int == mode ){
                        if( mode == 500 || mode == 1000 ) {
                            stopRecording();
                            status_distance(String.valueOf(mode), Speed);
                            Toast.makeText( getActivity(), "주행을 완료했습니다.", Toast.LENGTH_SHORT ).show();
                        }
                    }

                }
                catch(Exception e) {
                    Log.d("Sang", "Arduino Android Connection Exception : " + e.toString() );
                }
            }

            if( time == 0 ) {
                speed = 0;
            }
            else {
                speed = (float)(distance_int)/time;
            }
            
            Speed = String.format("%.2f", speed);
           
            status_distance(String.valueOf(distance_int), Speed);
            Log.d("SANG",  distance_int + "  " + time + " " + speed);
            Log.d("distance", "distance1 : " + distance_int + " speed : " + speed);
        }
    }

    private void status_distance(String dis, String sp) {
        //SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        //spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv_distance.setText(dis + "m");
        tv_speed.setText(sp + "m/s");


    }

    @Override
    public void onSerialConnect() {
        connected = Connected.True;
        Toast.makeText(getActivity(), deviceName + " 와 연결되었습니다." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSerialConnectError(Exception e) {
        disconnect();
        Toast.makeText(getActivity(), "onSerialConnectError 블루투스 기기를 연결하십시오. " , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSerialRead(byte[] data) { receive(data); }

    @Override
    public void onSerialIoError(Exception e) {
        disconnect();
        Toast.makeText(getActivity(), "onSerialIoError 블루투스 기기를 연결하십시오. " , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkModeFinished(Integer mode) {
        this.mode = mode;
    }
}
/*
private void receive(byte[] data) {
        String msg = new String(data);

        if( newline.equals(TextUtil.newline_crlf) && (msg.length() > 0) ) {
            msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);

            String data1 = msg.replaceAll("\n", ",");
            String datann = data1.replaceAll("     ", "");
            String dataFin = datann.replace(" ", "");
            int data_fir_len = dataFin.length();
            // Log.d("Sang", data1 + "  " + datann + "  " + data_fir_len);

            if (data_fir_len < 10) {
               // left_power = dataFin.replace(",", "");
                //lc_left = Float.parseFloat(left_power);
            } else if( dataFin.contains("S") ||  dataFin.contains("T")) {
                Log.d("Sang", "Arduino Message : " + dataFin );
            } else {
                try{
                    String[] splitData = dataFin.split(",");
                    //right_power = splitData[0];
                    //lc_right = Float.parseFloat(right_power);

                    distance = splitData[1];
                    distance_int = Integer.parseInt(distance);
                    //speed_float = Float.parseFloat(speed);
                }
                catch(Exception e) {
                    Log.d("Sang", "Arduino Android Connection Exception : " + e.toString() );
                }

            }

            speed = (double)(distance_int)/time;

            if( Double.isInfinite(speed) || Double.isNaN(speed)) {
                Speed = "-- ";
                distance = "-- ";
            }
            else {
                Speed = String.format("%.2f", (double)(distance_int)/time);
            }

            if( distance == null ) {
                distance = "0 ";
            } else if(distance_int == 5) {
                chronometer.stop();
            }
            else {

            }

            status_distance(distance, Speed);

            Log.d("Time", "Speed : " + Speed);
            Log.d("SANG",  distance_int + "  " + time + " " + Speed);
        }
    }
 */