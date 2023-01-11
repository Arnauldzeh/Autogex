package com.example.autogex.infos;

import java.util.ArrayList;

public class Transition {
    public int debut;
    public int fin;
    public ArrayList<String> symboles = new ArrayList<String>();
    public Transition(int debut,int fin, ArrayList<String> symboles){
        this.debut= debut;
        this.fin = fin;
        this.symboles = symboles;
    }
}
