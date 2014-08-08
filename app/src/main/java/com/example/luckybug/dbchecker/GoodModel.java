package com.example.luckybug.dbchecker;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LuckyBug on 06.08.2014.
 */
public class GoodModel implements Serializable {

    private String description;
    private Bitmap image = null;
    private String check;

    public static ArrayList<GoodModel> defaultGoods = new ArrayList<GoodModel>(Arrays.asList(
            new GoodModel("Светов. короб светится, без повреж, чист."),
            new GoodModel("Витрины б/мех. повреждений, чист."),
            new GoodModel("Вх. дверь б/мех. повреждений, чист."),
            new GoodModel("Пол при вх, за касс. зон. чист, мусора нет"),
            new GoodModel("Налич. правильн. офор. вывес. РЕЖИМ РАБОТЫ")
    ));

    public GoodModel(String description) {
        this.description = description;
        check = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}