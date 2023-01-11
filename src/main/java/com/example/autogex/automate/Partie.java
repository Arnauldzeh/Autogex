/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.autogex.automate;

import java.util.List;

/**
 *
 * @author MARTIAL
 */
public class Partie {
    public Partie(){
        
    }
    public Partie(List<String> sigma, List<String> prefixe, List<String> suffixe, List<Repeat> RegexParts){
       this.Sigma = sigma;
       this.Prefixe = prefixe;
       this.Surfixe = suffixe;
       this.RegexParts = RegexParts;
   }
    List<String>  Sigma;
    List<String> Prefixe;
    List<String> Surfixe;
    List<Repeat> RegexParts;
}
