package com.example.luckybug.dbchecker;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasiliy.lomanov on 05.08.2014.
 */
public class Model implements Serializable {

    private String name;
    Location location;
    ArrayList<GoodModel> list;

    public Model(String name, Location location) {
        this.name = name;
        this.location = location;
        list = new ArrayList<GoodModel>();
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