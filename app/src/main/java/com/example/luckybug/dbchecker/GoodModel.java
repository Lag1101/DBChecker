package com.example.luckybug.dbchecker;

import java.io.Serializable;

/**
 * Created by LuckyBug on 06.08.2014.
 */
public class GoodModel implements Serializable {

    private String description;
    private boolean selected;

    public GoodModel(String description) {
        this.description = description;
        selected = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}