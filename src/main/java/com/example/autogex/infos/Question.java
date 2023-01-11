package com.example.autogex.infos;

import java.util.ArrayList;

public class Question {
    public int maxSize,minSize,fixedSize;
    public ArrayList<String> prefix = new ArrayList<String>(),suffix=new ArrayList<String>();
    public ArrayList<Repetition> repetitions=new ArrayList<Repetition>();
    public Question(){
    }

    public Question copy(){
        Question c = new Question();
        c.maxSize =maxSize;
        c.minSize = minSize;
        c.fixedSize = fixedSize;
        c.prefix=prefix;
        c.suffix=suffix;
        c.repetitions=repetitions;
        return c;

    }

}
