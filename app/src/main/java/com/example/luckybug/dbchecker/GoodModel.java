package com.example.luckybug.dbchecker;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LuckyBug on 06.08.2014.
 */
public class GoodModel implements Serializable {

    private String description;
    private int selected;

    public static ArrayList<GoodModel> defaultGoods = new ArrayList<GoodModel>(Arrays.asList(
            new GoodModel("Светов. короб светится, без повреж, чист."),
            new GoodModel("Витрины б/мех. повреждений, чист."),
            new GoodModel("Вх. дверь б/мех. повреждений, чист."),
            new GoodModel("Пол при вх, за касс. зон. чист, мусора нет"),
            new GoodModel("Налич. правильн. офор. вывес. РЕЖИМ РАБОТЫ")
    ));

    public GoodModel(String description) {
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