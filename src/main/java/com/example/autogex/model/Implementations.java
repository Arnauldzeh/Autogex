package com.example.autogex.model;

import java.util.List;

public class Implementations {
    public static ConstructeurAutomate getConstructeurAutomate(List<String> alphabet){
       return  new ConstructeurAutomateImplementation3(alphabet);
    }
}
