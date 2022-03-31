package com.example.gmodsv1;

public class  ReadWrite {
    public String doB , gender , mobile;

    //Constructor that is empty to apply for snapshot of data from the database
    public ReadWrite(){}

    public ReadWrite(String doB , String gender , String mobile){
        this.doB = doB;
        this.gender = gender;
        this.mobile = mobile;
    }

}
