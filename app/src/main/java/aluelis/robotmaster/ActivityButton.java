package aluelis.robotmaster;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by szvetlintanyi on 09/04/16.
 */
public class ActivityButton extends AppCompatActivity implements View.OnTouchListener {

    //+000-255
    final String FORWARD = "+255+255\n";
    final String BACK = "-255-255\n";
    final String LEFT = "-255+255\n";
    final String RIGHT = "+255-255\n";
    final String STOP = "+000+000\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }

        ImageView ivForward = (ImageView) findViewById(R.id.ivUp);
        if (ivForward != null)

        {
            ivForward.setOnTouchListener(this);
        }

        ImageView ivBackward = (ImageView) findViewById(R.id.ivDown);
        if (ivBackward != null)

        {
            ivBackward.setOnTouchListener(this);
        }

        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        if (ivRight != null)

        {
            ivRight.setOnTouchListener(this);
        }

        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        if (ivLeft != null)

        {
            ivLeft.setOnTouchListener(this);
        }

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ivUp:
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    ActivityMain.bt.sendData(FORWARD);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ActivityMain.bt.sendData(STOP);
                }
                break;
            case R.id.ivDown:
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    ActivityMain.bt.sendData(BACK);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ActivityMain.bt.sendData(STOP);
                }
                break;
            case R.id.ivLeft:
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    ActivityMain.bt.sendData(LEFT);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ActivityMain.bt.sendData(STOP);
                }
                break;
            case R.id.ivRight:
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    ActivityMain.bt.sendData(RIGHT);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ActivityMain.bt.sendData(STOP);
                }
                break;

            default:
                break;

        }
        return true;
    }
}
