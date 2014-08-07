package com.example.luckybug.dbchecker;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by LuckyBug on 06.08.2014.
 */
public class GoodModel implements Serializable {

    private String description;
    private int selected;

    public GoodModel(String description, Context context) {
        this.description = description;
        selected = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}