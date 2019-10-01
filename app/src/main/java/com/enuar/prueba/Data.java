package com.enuar.prueba;

public class Data  {
    private String Name, UrlPhoto, ID, Email;

    public Data() {
    }

    public Data(String name, String urlPhoto, String ID, String email) {
        Name = name;
        UrlPhoto = urlPhoto;
        this.ID = ID;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrlPhoto() {
        return UrlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        UrlPhoto = urlPhoto;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public void SaveUser (){
        DataUser.Save(this);
    }

}
