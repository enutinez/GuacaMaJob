package com.enuar.prueba;

public class DataUser {

    private static Data User = new Data();
    public static void Save (Data c){ User = c;}
    public static Data Get(){return User;}

}
