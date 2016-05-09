package aluelis.robotmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by szvetlintanyi on 09/04/16.
 */
public class ActivityTouch extends AppCompatActivity {

    TouchView touchView;
    LinearLayout container;
    final int MAX_SPEED = 255;
    private int FINGER_CIRCLE_SIZE;
    private int BIG_CIRCLE_SIZE;
    int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        container = (LinearLayout) findViewById(R.id.touchContainer);

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

        getScreenSize();
        if(getResources().getConfiguration().orientation == 1) {
            BIG_CIRCLE_SIZE = (int) (screenWidth * 0.45);
            FINGER_CIRCLE_SIZE = (int) (BIG_CIRCLE_SIZE * 0.1);
        }else{
            BIG_CIRCLE_SIZE = (int) (screenWidth * 0.2);
            FINGER_CIRCLE_SIZE = (int) (BIG_CIRCLE_SIZE * 0.1);
        }
        touchView = new TouchView(this);
        container.addView(touchView);
    }

    public class TouchView extends View {

        Paint fingerPaint, borderPaint, textPaint;
        int width, height;
        float x, y, circX, circY;
        String debugText;
        boolean drag = false;


        public TouchView(Context context) {
            super(context);
            fingerPaint = new Paint();
            fingerPaint.setAntiAlias(true);
            fingerPaint.setColor(Color.RED);

            borderPaint = new Paint();
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(Color.BLUE);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(6);

            textPaint = new Paint();
            textPaint.setColor(Color.GREEN);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(26);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(getResources().getConfiguration().orientation == 1) {
                width = Math.round((this.getRight() - this.getLeft()) / 2);
                height = Math.round((this.getBottom() - this.getTop()) / 2);
            }else{
                width = (int) Math.round((this.getRight() - this.getLeft()) / 4.5);
                height = 6 * Math.round((this.getBottom() - this.getTop()) / 10);
            }
            if (!drag) {
                x = width;
                y = height;
                fingerPaint.setColor(Color.RED);
            }

            canvas.drawCircle(x, y, FINGER_CIRCLE_SIZE, fingerPaint);
            canvas.drawCircle(width, height, BIG_CIRCLE_SIZE, borderPaint);

            canvas.drawText(String.valueOf("X: " + circX), 10, 20, textPaint);
            canvas.drawText(String.valueOf("Y: " + circY), 10, 40, textPaint);
            canvas.drawText("Motor: " + debugText, 10, 115, textPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float evX = event.getX();
            float evY = event.getY();

            circX = event.getX() - width;
            circY = event.getY() - height;

            float radius = (float) Math.sqrt(Math.pow(Math.abs(circX), 2) + Math.pow(Math.abs(circY), 2));

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (radius >= 0 && radius <= BIG_CIRCLE_SIZE) {
                        x = evX;
                        y = evY;
                        fingerPaint.setColor(Color.GREEN);
                        debugText = command(circX, circY);
                        invalidate();
                        drag = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (drag && radius >= 0 && radius <= BIG_CIRCLE_SIZE) {
                        x = evX;
                        y = evY;
                        debugText = command(circX, circY);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    circX = 0;
                    circY = 0;
                    drag = false;
                    debugText = command(circX, circY);
                    invalidate();
                    break;
            }
            return true;
        }
    }

    private String command(float x, float y) {
        y *= -1;
        NumberFormat nf = new DecimalFormat("000");

        String directionLeft, directionRight;
        int axisX = Math.round(x * MAX_SPEED / BIG_CIRCLE_SIZE);
        int axisY = Math.round(y * MAX_SPEED / BIG_CIRCLE_SIZE);

        if (axisX + axisY > MAX_SPEED) {
            directionLeft = "+" + Integer.toString(MAX_SPEED);
        } else if (axisX + axisY < -MAX_SPEED) {
            directionLeft = Integer.toString(-MAX_SPEED);
        } else {
            if(Math.min(MAX_SPEED, axisY + axisX) >= 0){
                directionLeft = "+" + nf.format(Math.min(MAX_SPEED, axisY + axisX));
            }else{
                directionLeft = nf.format(Math.min(MAX_SPEED, axisY + axisX));
            }

        }

        if (axisY - axisX > MAX_SPEED) {
            directionRight = "+" + Integer.toString(MAX_SPEED);
        } else {
            if(Math.max(-MAX_SPEED, axisY - axisX) >=0){
                directionRight = "+" + nf.format(Math.max(-MAX_SPEED, axisY - axisX));
            }else {
                directionRight = nf.format(Math.max(-MAX_SPEED, axisY - axisX));
            }
        }

        String command = directionLeft +directionRight +"\n";
        Log.d("command", command);
        ActivityMain.bt.sendData(command);
        return command;
    }

    private void getScreenSize(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            setWidth(size.x);
            setHeight(size.y);
        }else{
            Display display = getWindowManager().getDefaultDisplay();
            setWidth(display.getWidth());
            setHeight(display.getHeight());
        }

    }

    private void setWidth(int width){
        screenWidth = width;
    }

    private void setHeight(int height){
        screenHeight = height;
    }

}