package aluelis.robotmaster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by szvetlintanyi on 09/04/16.
 */
public class ActivityTouch extends Activity {

    TouchView touchView;
    final int MAX_SPEED = 255;
    final int xRperc = 60;
    private final int FINGER_CIRCLE_SIZE = 25;
    private final int BIG_CIRCLE_SIZE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touchView = new TouchView(this);
        setContentView(touchView);
    }

    public class TouchView extends View {

        Paint fingerPaint, borderPaint, textPaint;
        int width, height;
        float x, y, circX, circY, dragX, dragY;
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
            width = Math.round((this.getRight() - this.getLeft()) / 2);
            height = Math.round((this.getBottom() - this.getTop()) / 2);
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

        String directionLeft, directionRight;
        int axisX = Math.round(x * MAX_SPEED / BIG_CIRCLE_SIZE);
        int axisY = Math.round(y * MAX_SPEED / BIG_CIRCLE_SIZE);

        if (axisX + axisY > MAX_SPEED) {
            directionLeft = Integer.toString(MAX_SPEED);
        } else if (axisX + axisY < -MAX_SPEED) {
            directionLeft = Integer.toString(-MAX_SPEED);
        } else {
            directionLeft = Integer.toString(Math.min(MAX_SPEED, axisY + axisX));
        }

        if (axisY - axisX > MAX_SPEED) {
            directionRight = Integer.toString(MAX_SPEED);
        } else {
            directionRight = Integer.toString(Math.max(-MAX_SPEED, axisY - axisX));
        }

        String command = "L" + directionLeft + "R" + directionRight;
        ActivityMain.bt.sendData(command);
        return command;
    }
}