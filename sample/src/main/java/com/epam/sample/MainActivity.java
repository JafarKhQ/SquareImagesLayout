package com.epam.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.epam.widget.SquareImagesLayout;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    SquareImagesLayout s;
    final static int[] a =
            {
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        s = (SquareImagesLayout) findViewById(R.id.sil);
        s.setImages(a);

        findViewById(R.id.ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setNumberOfColumns(s.getNumberOfColumns() + 1);
                s.setImages(a);
            }
        });

        findViewById(R.id.ar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setNumberOfRows(s.getNumberOfRows() + 1);
                s.setImages(a);
            }
        });

        findViewById(R.id.rc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = s.getNumberOfColumns() - 1;
                s.setNumberOfColumns(res > 0 ? res : 1);
            }
        });

        findViewById(R.id.rr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = s.getNumberOfRows() - 1;
                s.setNumberOfRows(res > 0 ? res : 1);
            }
        });

        findViewById(R.id.ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setContentPadding(s.getContentPadding() + 10);
            }
        });

        findViewById(R.id.dp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setContentPadding(s.getContentPadding() - 10);
            }
        });

        findViewById(R.id.st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                s.setScaleType(ST[r.nextInt(ST.length)]);
            }
        });
    }

    static final ImageView.ScaleType[] ST = ImageView.ScaleType.values();
}
