package aluelis.robotmaster;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by szvetlintanyi on 09/04/16.
 */
public class ActivityButton extends Activity implements View.OnTouchListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        ImageView ivForward = (ImageView) findViewById(R.id.ivUp);
        ivForward.setOnTouchListener(this);
        ImageView ivBackward = (ImageView) findViewById(R.id.ivDown);
        ivBackward.setOnTouchListener(this);
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setOnTouchListener(this);
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setOnTouchListener(this);

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ivUp:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    ActivityMain.bt.sendData("F");
                    Log.d("sendData", "F");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ActivityMain.bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivDown:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    ActivityMain.bt.sendData("B");
                    Log.d("sendData", "B");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ActivityMain.bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivLeft:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    ActivityMain.bt.sendData("L");
                    Log.d("sendData", "FL");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ActivityMain.bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;
            case R.id.ivRight:
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    ActivityMain.bt.sendData("R");
                    Log.d("sendData", "R");
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ActivityMain.bt.sendData("S");
                    Log.d("sendData", "S");
                }
                break;

            default:
                break;

        }
        return true;
    }
}
