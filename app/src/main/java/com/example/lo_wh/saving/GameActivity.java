package com.example.lo_wh.saving;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class GameActivity extends AppCompatActivity {

    ImageView mImage_in;
    ImageView mImage_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mImage_in = findViewById(R.id.inside_imageview);
        mImage_out = findViewById(R.id.outside_imageview);

        mImage_in.setAdjustViewBounds(true);
        mImage_in.setScaleType(ImageView.ScaleType.CENTER_CROP);

        findViewById(R.id.outside_imageview).setOnTouchListener(new dragTouchListener());
        findViewById(R.id.outside_imageview).setOnDragListener(new dragEventListener());
    }

    private final class dragTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            } else {
                return false;
            }
        }

    }

    protected class dragEventListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event){
            int x_cord;
            int y_cord;

            final int action = event.getAction();

            switch(action){
                case DragEvent.ACTION_DRAG_ENTERED:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    System.out.println("Entered at " + x_cord + "," + y_cord);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    System.out.println("Exited at " + x_cord + "," + y_cord);
                    break;
                case DragEvent.ACTION_DROP:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    System.out.println("Dropped at " + x_cord + "," + y_cord);
            }
            return true;
        }
    }
}
