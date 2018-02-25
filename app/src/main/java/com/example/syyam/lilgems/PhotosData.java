package com.example.syyam.lilgems;

/**
 * Created by Syyam on 27-Sep-17.
 */

public class PhotosData {

    private String Email,Image,Name,Phone,Goals, Date,Hour,Minute,Notes,othersName,comments;

    public PhotosData()
    {

    }
    public PhotosData( String othersName,String comments,String Date, String Hour, String Minute, String Notes, String Goals, String Image, String Name, String Phone) {

        this.comments=comments;
        this.othersName=othersName;
        this.Date=Date;
        this.Hour=Hour;
        this.Minute=Minute;
        this.Notes=Notes;
        this.Goals= Goals;
        this.Image = Image;
        this.Name = Name;
        this.Phone = Phone;
    }

    public String getOthersName() {
        return othersName;
    }

    public void setOthersName(String othersName) {
        this.othersName = othersName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGoals() {
        return Goals;
    }

    public void setGoals(String goals) {
        Goals = goals;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }


}
