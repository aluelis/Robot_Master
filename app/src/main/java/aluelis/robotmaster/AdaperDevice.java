package aluelis.robotmaster;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by szvetlintanyi on 02/05/16.
 */
public class AdaperDevice extends ArrayAdapter<BluetoothDevice> {

    public AdaperDevice(Context context, List<BluetoothDevice> objects) {
        super(context, R.layout.row_devicelist, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View root = convertView;

        if (root == null) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.row_devicelist, parent, false);
        }
        BluetoothDevice device = getItem(position);
        ViewHolder vh;

        if (root.getTag() != null) {
            vh = (ViewHolder) root.getTag();
        } else {
            vh = new ViewHolder(root);
        }

        vh.Name.setText(device.getName());
        vh.Address.setText(device.getAddress());

        root.setTag(vh);
        return root;
    }



    private static class ViewHolder {
        TextView Name, Address;

        public ViewHolder(View root) {
            Name = (TextView) root.findViewById(R.id.tvName);
            Address = (TextView) root.findViewById(R.id.tvAddress);
        }
    }
}
