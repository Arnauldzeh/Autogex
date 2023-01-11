package com.example.autogex.infos;

public class Repetition {
    private String word;
    private int number;

    public Repetition(String str,int num){
        this.word=str;
        this.number=num;
    }

    public void setNumber(int num){
        number=num;
    }
    public void setWord(String str){
        word=str;
    }
    public int getNumber() {
        return number;
    }
    public String getWord() {
        return word;
    }
}
