package aluelis.robotmaster;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import mehdi.sakout.fancybuttons.FancyButton;

public class ActivityMain extends AppCompatActivity implements OnClickListener {
    static private FancyButton btnConnect, btnAutonomous, btnButton, btnTouch;
    static boolean connected = false;
    public static IBluetooth bt = null;
    public static boolean isAutonomous = false;
    static final int REQUEST_ENABLE_BT = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnConnect = (FancyButton) findViewById(R.id.btnConnect);
        if (btnConnect != null) {
            btnConnect.setOnClickListener(this);
        }

        btnAutonomous = (FancyButton) findViewById(R.id.btnAutonomous);
        if (btnAutonomous != null) {
            btnAutonomous.setOnClickListener(this);
        }

        btnButton = (FancyButton) findViewById(R.id.btnButtonControl);
        if (btnButton != null) {
            btnButton.setOnClickListener(this);
        }

        btnTouch = (FancyButton) findViewById(R.id.btnTouchControl);
        if (btnTouch != null) {
            btnTouch.setOnClickListener(this);
        }


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
                btnConnect.setText(bt.getContext().getString(R.string.connected));
                btnConnect.setBackgroundColor(bt.getContext().getResources().getColor(R.color.android_green));
                btnAutonomous.setVisibility(View.VISIBLE);
                btnButton.setVisibility(View.VISIBLE);
                btnTouch.setVisibility(View.VISIBLE);
                isAutonomous = false;
                btnAutonomous.setText(bt.getContext().getString(R.string.autonomous));
                btnAutonomous.setBackgroundColor(bt.getContext().getResources().getColor(R.color.primary_color));
                btnButton.setVisibility(View.VISIBLE);
                btnTouch.setVisibility(View.VISIBLE);
            } else if (status == IBluetooth.DISCONNECTED) {
                connected = false;
                btnConnect.setText(bt.getContext().getString(R.string.disconnected));
                btnConnect.setBackgroundColor(bt.getContext().getResources().getColor(R.color.primary_color));
                btnAutonomous.setVisibility(View.GONE);
                btnButton.setVisibility(View.GONE);
                btnTouch.setVisibility(View.GONE);
            }
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                if (!connected) {
                    startActivity(new Intent(this, ActivityDeviceList.class));
                } else {
                    bt.disconnect();
                }
                break;
            case R.id.btnAutonomous:
                bt.sendData("A");
                if (!isAutonomous) {
                    isAutonomous = true;
                    btnAutonomous.setText(getString(R.string.manual));
                    btnAutonomous.setBackgroundColor(bt.getContext().getResources().getColor(R.color.android_green));
                    btnButton.setVisibility(View.GONE);
                    btnTouch.setVisibility(View.GONE);
                } else {
                    isAutonomous = false;
                    btnAutonomous.setText(getString(R.string.autonomous));
                    btnAutonomous.setBackgroundColor(bt.getContext().getResources().getColor(R.color.primary_color));
                    btnButton.setVisibility(View.VISIBLE);
                    btnTouch.setVisibility(View.VISIBLE);
                }
                break;
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
