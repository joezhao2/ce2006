package com.example.gmodsv1;

import androidx.annotation.NonNull;

public class moduleclass {
    private String coursecode;
    private String name;
    private int AU;

    public moduleclass() {
    }

    public moduleclass(int AU,String coursecode,String name) {
        this.coursecode = coursecode;
        this.name = name;
        this.AU = AU;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoursecode() {
        return coursecode;
    }

    public void setCoursecode(String coursecode) {
        this.coursecode = coursecode;
    }
    public int getAU(){
        return AU;
    }
    public void setAU (int AU){this.AU=AU;}

    @NonNull
    @Override
    public String toString() {
        return this.coursecode +" "+ this.name;
        //overriding toString
    }
}