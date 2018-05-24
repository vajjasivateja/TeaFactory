package com.example.vajjasivateja.teafactory.Model;

public class User {
    private String Phone;
    private String Name;
    private String Birthdate;
    private String Address;
    private String error_msg;

    public User() {
    }

    public User(String phone, String name, String birthdate, String address, String error_msg) {
        Phone = phone;
        Name = name;
        Birthdate = birthdate;
        Address = address;
        this.error_msg = error_msg;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
