package com.epam.sample.models;

import android.net.Uri;

import java.util.ArrayList;

public class DayImages {
    private String dayDate;
    private ArrayList<Uri> uris;

    public DayImages() {
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public void addUri(Uri uri) {
        if (null == uri) {
            return;
        }

        if (null == uris) {
            uris = new ArrayList<>();
        }

        uris.add(uri);
    }

    public Uri getUriAt(int index) {
        if (null == uris) {
            return null;
        }

        return uris.get(index);
    }

    public int getUriCount() {
        if (null == uris) {
            return 0;
        }

        return uris.size();
    }

    public ArrayList<Uri> getUris() {
        return uris;
    }

    public void setUris(ArrayList<Uri> uris) {
        this.uris = uris;
    }
}
