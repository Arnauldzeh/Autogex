package com.example.autogex.infos;


import java.util.ArrayList;

public class TransitionTable {
    public  int numberEtat=0;
    public  ArrayList<Integer> etatsInitiaux = new ArrayList<Integer>();
    public  ArrayList<Integer> etatsFinaux = new ArrayList<Integer>();

    public ArrayList<Transition> transitions = new ArrayList<Transition>();
    public TransitionTable(){

    }

}
