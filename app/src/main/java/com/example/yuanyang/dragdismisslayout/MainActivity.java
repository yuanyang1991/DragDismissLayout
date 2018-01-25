package com.example.yuanyang.dragdismisslayout;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.reflect.TypeVariable;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private static String [] imgs = new String[]{
            "http://img.hb.aicdn.com/4249a09ba63a1442fccbc085db11c55c67741c0620ffe9-OEoIvu_fw658",
            "http://img.hb.aicdn.com/2919d89bd91874c6d8679c95c8f7ac210a60d06d22d84-Udh660_fw658",
            "http://img.hb.aicdn.com/e6a4d8e966a0f4c80668ac40910edc0a8a88fbce1731af-ctgiwU_fw658"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView iv1 = findViewById(R.id.iv1);
        final ImageView iv2 = findViewById(R.id.iv2);
        final ImageView iv3 = findViewById(R.id.iv3);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDisplayActivity.show((int)iv1.getX(),(int)iv1.getY(),dp2px(100),dp2px(200),imgs[0],MainActivity.this);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDisplayActivity.show((int)iv2.getX(),(int)iv2.getY(),dp2px(200),dp2px(200),imgs[1],MainActivity.this);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDisplayActivity.show((int)iv3.getX(),(int)iv3.getY(),dp2px(200),dp2px(100),imgs[2],MainActivity.this);
            }
        });
        Glide.with(this).load(Uri.parse(imgs[0])).into(iv1);
        Glide.with(this).load(Uri.parse(imgs[1])).into(iv2);
        Glide.with(this).load(Uri.parse(imgs[2])).into(iv3);
    }

    private int dp2px(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,getResources().getDisplayMetrics());
    }
}
