package com.example.luckybug.dbchecker;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vasiliy.lomanov on 05.08.2014.
 */
public class Model implements Serializable {

    private String name;
    Location location;
    ArrayList<GoodModel> list;

    private static Location createLocationByLatitudeAndLongitude(double latitude, double longitude) {
        Location location = new Location("");
        location.setAccuracy(50);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }
    public static ArrayList<Model> defaultPlaces = new ArrayList<Model>(Arrays.asList(
            new Model("М,г.Москва,пр-д Петровско-Разумовский старый,д.1/2", createLocationByLatitudeAndLongitude(55.799634, 37.566354)),
            new Model("МО,р.пос.Андреевка", createLocationByLatitudeAndLongitude(55.983696, 37.146225)),
            new Model("M,г. Москва, Петровско-Разумовский проезд, д. 28", createLocationByLatitudeAndLongitude(55.804542, 37.562458))
    ));

    public Model(String name, Location location) {
        this.name = name;
        this.location = location;
        list = GoodModel.defaultGoods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GoodModel> getList() {
        return list;
    }

    public void setList(ArrayList<GoodModel> list) {
        this.list = list;
    }

    public Location getLocation() {
        return location;
    }


}