package com.epam.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.epam.widget.SquareImagesLayout;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] a =
                {
                        //R.mipmap.ic_launcher,
                        //R.mipmap.ic_launcher,
                        //R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher
                };
        final SquareImagesLayout s = (SquareImagesLayout) findViewById(R.id.sil);
        s.setImages(a);
        s.setContentPadding(0);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                s.setContentPadding(random.nextInt(100) + 10);
                s.setNumberOfColumns(random.nextInt(7) + 1);
            }
        });
    }
}
