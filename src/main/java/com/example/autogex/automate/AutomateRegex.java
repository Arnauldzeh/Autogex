package com.example.autogex.automate;

import com.example.autogex.infos.AppConst;
import com.example.autogex.model.Automate;
import com.example.autogex.model.AutomateImplementationv1;
import com.example.autogex.model.ConstructeurAutomate;
import com.example.autogex.model.Implementations;
import com.example.autogex.verfieAlpha.AmbigueAlphabet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutomateRegex {

    String regex= AppConst.regex;
    public AutomateRegex(){

    }

    public AutomateRegex(String regex){
        this.regex = regex;
    }
    public boolean verifieWord(String word) throws Exception{
       // List<String> Alphabet = new ArrayList<>(new AmbigueAlphabet().verifierAlphabet(List.of(regex.toCharArray()));
        List<String> Alphabet = new ArrayList<>();
        String regexClear = regex.replace("^","");
        regexClear = regexClear.replace("+","");
        regexClear = regexClear.replace("*","");
        regexClear = regexClear.replace("/","");
        regexClear = regexClear.replace("\\","");
        regexClear = regexClear.replace("{","");
        regexClear = regexClear.replace("}","");
        regexClear = regexClear.replace("(","");
        regexClear = regexClear.replace(")","");
        regexClear = regexClear.replace(".","");
        char[] alphabetBis = regexClear.toCharArray();
        Set<Character> set = new HashSet<>();
        for(char c : alphabetBis){
            set.add(c);
        }
        for(Character c : set){
            Alphabet.add(String.valueOf(c));
        }
        word = word.replace(" ","");
        ConstructeurAutomate constructeurAutomate = Implementations.getConstructeurAutomate(Alphabet);
        String cleanRegex = constructeurAutomate.verifierRegex(regex);
        Automate automate = constructeurAutomate.constructAutomate(Alphabet,cleanRegex);
        List<Automate.ErrorMessage> res = automate.verifierChaine(word);
        if(Integer.parseInt((res.get(res.size()-1)).message)>0){
            return true;
        }else{
            String finalRes = "";
            for(Automate.ErrorMessage a : res){
                finalRes+=a.message;
                finalRes+="\n";
            }
            throw new Exception(finalRes);
        }

        
    }
}
