package aluelis.robotmaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class IBluetooth {

    private Context context;
    private BluetoothSocket socket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private OutputStream sendStream = null;

    Handler handlerStatus;

    public static int CONNECTED = 1;
    public static int DISCONNECTED = 2;

    public IBluetooth(Handler status, Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handlerStatus = status;
        this.context = context;
    }

    public void connect() {

        Set<BluetoothDevice> setPairedDevices = mBluetoothAdapter.getBondedDevices();
        BluetoothDevice[] pairedDevices = setPairedDevices.toArray(new BluetoothDevice[setPairedDevices.size()]);

        for (BluetoothDevice pairedDevice : pairedDevices) {
            if (pairedDevice.getName().contains("HC-05")) {
                try {
                    socket = pairedDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    InputStream receiveStream = socket.getInputStream();
                    BufferedReader receiveReader = new BufferedReader(new InputStreamReader(receiveStream));
                    sendStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    socket.connect();

                    Message msg = handlerStatus.obtainMessage();
                    msg.arg1 = CONNECTED;
                    handlerStatus.sendMessage(msg);

                } catch (IOException e) {
                    handlerStatus.post(new Runnable() {
                        public void run() {
                            Toast.makeText(context, R.string.conn_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void disconnect() {
        try {
            socket.close();

            Message msg = handlerStatus.obtainMessage();
            msg.arg1 = DISCONNECTED;
            handlerStatus.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        try {
            sendStream.write(data.getBytes());
            sendStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
