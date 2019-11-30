package com.adaptive.exoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Moive_activity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moive);
        Intent intent = getIntent();
        String string = intent.getStringExtra("movie");
        String url = intent.getStringExtra("url");
        String movie_url = intent.getStringExtra("movie_url");
        textView = (TextView) findViewById(R.id.movie_name);
        imageView = (ImageView) findViewById(R.id.movie_cover);
        textView.setText(string);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .apply(requestOptions)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Moive_activity.this, PlayerActivity.class);
                i.putExtra("movie_url",movie_url);
                startActivity(i);
            }
        });
    }
}
