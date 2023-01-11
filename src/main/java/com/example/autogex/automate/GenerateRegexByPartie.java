package com.example.autogex.automate;

import java.util.List;

public class GenerateRegexByPartie {


    public static String  generateRegexByQuestions(List<Partie> RegexPartie ){
        StringBuilder reg = new StringBuilder();

        for (Partie partie:RegexPartie){
            reg.append(generateRegexPartie(partie));
        }

        return reg.toString();
    }


    public static String generateRegexPartie(Partie objet){
        String regex = "";
        String[] regexTab = {"", "", ""};
        regexTab[0] = (objet.Prefixe.size()!=0) ? "(" : "";
        for (int i=0; i<objet.Prefixe.size(); i++){
            if(i==objet.Prefixe.size()-1){
                regexTab[0] += objet.Prefixe.get(i) + ")" ;
            }else{
                regexTab[0] += objet.Prefixe.get(i) + "+" ;
            }
        }

        for (int i=0; i < objet.RegexParts.size(); i++){
            for(int y=0; y < objet.RegexParts.get(i).occurence; y++){
                regexTab[1] += objet.RegexParts.get(i).word;
            }

        }

        regexTab[2] = (objet.Surfixe.size()!=0) ? "(" : "";
        for (int i=0; i<objet.Surfixe.size(); i++){
            if(i==objet.Surfixe.size()-1){
                regexTab[2] += objet.Surfixe.get(i) + ")" ;
            }else{
                regexTab[2] += objet.Surfixe.get(i) + "+" ;
            }
        }

        String anyWord = "(";

        for (int i=0; i<objet.Sigma.size(); i++){
            if(i==objet.Sigma.size()-1){
                anyWord += objet.Sigma.get(i) + ")^*";
            }else{
                anyWord += objet.Sigma.get(i) + "+";
            }
        }

        regexTab[1] = (objet.RegexParts.size()!=0) ? anyWord + regexTab[1] + anyWord : anyWord;

        regex = regexTab[0] + regexTab[1] + regexTab[2];


        return regex;
    }


}
