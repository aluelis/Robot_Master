package aluelis.robotmaster;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
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
        ivForward.setOnClickListener(this);
        ImageView ivBackward = (ImageView) findViewById(R.id.ivDown);
        ivBackward.setOnClickListener(this);
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setOnClickListener(this);
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setOnClickListener(this);
        ImageView ivStop = (ImageView) findViewById(R.id.ivStop);
        ivStop.setOnClickListener(this);

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
            case R.id.ivUp:
                bt.sendData("F");
                break;
            case R.id.ivDown:
                bt.sendData("B");
                break;
            case R.id.ivLeft:
                bt.sendData("L");
                break;
            case R.id.ivRight:
                bt.sendData("R");
                break;
            case R.id.ivStop:
                bt.sendData("S");
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
}
