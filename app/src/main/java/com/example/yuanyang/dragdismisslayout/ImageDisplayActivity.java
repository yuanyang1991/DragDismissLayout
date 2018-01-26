package com.example.yuanyang.dragdismisslayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.lib.DragDismissLayout;
import com.example.lib.Location;
import com.example.lib.LocationUtil;
import com.github.chrisbanes.photoview.PhotoView;

import org.xml.sax.helpers.LocatorImpl;

public class ImageDisplayActivity extends AppCompatActivity {

    public static void show(View view,String imageUrl ,Context context){
        Location location = LocationUtil.getRawLocation(view);
        show(location.x,location.y,view.getMeasuredWidth(),view.getMeasuredHeight(),imageUrl,context);
    }

    public static void show(int x, int y, int width, int height, String imageUrl,Context context){
        Intent intent = new Intent(context,ImageDisplayActivity.class);
        intent.putExtra("x",x);
        intent.putExtra("url",imageUrl);
        intent.putExtra("y",y);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DragDismissLayout pullDownLayout = new DragDismissLayout(this);
        pullDownLayout.attachTo(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        PhotoView photoView = findViewById(R.id.photoView);
        int x =getIntent().getIntExtra("x",-1);
        int y =getIntent().getIntExtra("y",-1);
        int width = getIntent().getIntExtra("width",-1);
        int height = getIntent().getIntExtra("height",-1);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(photoView);
        pullDownLayout.setTargetData(x,y,width,height);
    }
}
