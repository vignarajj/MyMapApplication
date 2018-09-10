package com.vig.myapp.models;

public class UserData {
    private String picture;

    private String _id;

    private String email;

    private Location location;

    private String name;

    public UserData(String _id,String name,String email,String picture, Location location) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [picture = " + picture + ", _id = " + _id + ", email = " + email + ", location = " + location + ", name = " + name + "]";
    }
}
