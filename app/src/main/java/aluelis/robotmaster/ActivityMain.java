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
import android.widget.Toast;

public class ActivityMain extends Activity implements OnClickListener {
    static private Button btnConnect, btnAutonomous, btnButton, btnTouch;
    static boolean connected = false;
    public static IBluetooth bt = null;
    private boolean isAutonomous = false;
    static final int REQUEST_ENABLE_BT = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);

        btnAutonomous = (Button) findViewById(R.id.btnAutonomous);
        btnAutonomous.setOnClickListener(this);

        btnButton = (Button) findViewById(R.id.btnButtonControl);
        btnButton.setOnClickListener(this);

        btnTouch = (Button) findViewById(R.id.btnTouchControl);
        btnTouch.setOnClickListener(this);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            finish();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bt = new IBluetooth(new handlerStatus(), ActivityMain.this);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent moreData) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt = new IBluetooth(new handlerStatus(), ActivityMain.this);
            } else {
                Toast.makeText(this, "Enabled Bluetooth is required to use the app.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

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
                btnButton.setVisibility(View.VISIBLE);
                btnTouch.setVisibility(View.VISIBLE);
            } else if (status == IBluetooth.DISCONNECTED) {
                connected = false;
                btnConnect.setText(R.string.disconnected);
                btnAutonomous.setVisibility(View.GONE);
                btnButton.setVisibility(View.GONE);
                btnTouch.setVisibility(View.GONE);
                Log.d("STATE", "Disconnected");
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
                if (!isAutonomous) {
                    isAutonomous = true;
                    btnAutonomous.setText(R.string.manual);
                } else {
                    isAutonomous = false;
                    btnAutonomous.setText(R.string.autonomous);
                }
            case R.id.btnButtonControl:
                startActivity(new Intent(this, ActivityButton.class));
                break;
            case R.id.btnTouchControl:
                startActivity(new Intent(this, ActivityTouch.class));
                break;
            default:
                break;

        }
    }
}
