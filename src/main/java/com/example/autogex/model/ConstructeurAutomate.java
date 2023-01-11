package com.example.autogex.model;

import java.util.List;

public interface ConstructeurAutomate {
    public Automate constructAutomate(List<String> alphabet, String regex);

    public List<String> verifierAlphabet(List<String> alphabet);

    public String verifierRegex(String regex);

}
