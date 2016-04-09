package aluelis.robotmaster;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener {
    static private Button btnConnect, btnAutonomous;
    static boolean connected = false;
    static RelativeLayout control;
    private IBluetooth bt = null;
    private boolean isAutonomous = false;
    static final int REQUEST_ENABLE_BT = 3;


    static class handlerStatus extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int status = msg.arg1;
            if (status == IBluetooth.CONNECTED) {
                connected = true;
                Log.d("STATE", "Connected");
                btnConnect.setText(R.string.connected);
                btnAutonomous.setVisibility(View.VISIBLE);
                control.setVisibility(View.VISIBLE);
            } else if (status == IBluetooth.DISCONNECTED) {
                connected = false;
                btnConnect.setText(R.string.disconnected);
                btnAutonomous.setVisibility(View.GONE);
                control.setVisibility(View.GONE);
                Log.d("STATE", "Disconnected");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        control = (RelativeLayout)findViewById(R.id.controlLayout);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);

        btnAutonomous = (Button)findViewById(R.id.btnAutonomous);
        btnAutonomous.setOnClickListener(this);

        ImageView ivForward = (ImageView) findViewById(R.id.ivUp);
        ivForward.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("F");
                    Log.d("sendData", "F");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                return true;
            }
        });
        ImageView ivBackward = (ImageView) findViewById(R.id.ivDown);
        ivBackward.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("B");
                    Log.d("sendData", "B");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                return true;
            }
        });
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("R");
                    Log.d("sendData", "R");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                return true;
            }
        });
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("L");
                    Log.d("sendData", "L");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                return true;
            }
        });
        ImageView ivStop = (ImageView) findViewById(R.id.ivStop);
        ivStop.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                return true;
            }
        });

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            finish();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bt = new IBluetooth(new handlerStatus(), MainActivity.this);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent moreData) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt = new IBluetooth(new handlerStatus(), MainActivity.this);
            }else{
                Toast.makeText(this, "Enabled Bluetooth is required to use the app.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                if (!connected) {
                    bt.connect();
                } else {
                    bt.disconnect();
                }
                break;
            case R.id.btnAutonomous:
                bt.sendData("A");
                if(!isAutonomous){
                    isAutonomous = true;
                    btnAutonomous.setText(R.string.manual);
                    control.setVisibility(View.GONE);
                }else{
                    isAutonomous = false;
                    btnAutonomous.setText(R.string.autonomous);
                    control.setVisibility(View.VISIBLE);
                }
            default:
                break;

        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ivUp:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("F");
                    Log.d("sendData", "F");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivDown:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("B");
                    Log.d("sendData", "B");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivLeft:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("L");
                    Log.d("sendData", "FL");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivRight:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("R");
                    Log.d("sendData", "R");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivStop:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.btnAutonomous:
                bt.sendData("A");
                if (!isAutonomous) {
                    isAutonomous = true;
                    btnAutonomous.setText(R.string.manual);
                    control.setVisibility(View.GONE);
                } else {
                    isAutonomous = false;
                    btnAutonomous.setText(R.string.autonomous);
                    control.setVisibility(View.VISIBLE);
                }
            default:
                break;

        }
        return false;
    }
}
