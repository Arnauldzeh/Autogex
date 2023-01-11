package com.example.autogex.automate;

import com.example.autogex.infos.Transition;
import com.example.autogex.infos.TransitionTable;

import java.util.List;
import java.util.stream.Collectors;

public class AutomateTransition {
    TransitionTable transitionTable;
    public AutomateTransition(TransitionTable transition){
        this.transitionTable = transition;
    }

    public boolean verifieWord(String str)throws Exception{
        str = str.trim();
        for( Integer i : transitionTable.etatsInitiaux){
            Integer currentEtats = i;
            while (!str.equals("")){
                Tuple tuple = next(str,currentEtats);
                str=tuple.second;
                currentEtats = tuple.first;
            }
            if(transitionTable.etatsFinaux.contains(currentEtats)){
                return true;
            }
        }
        return false;
    }

    private Tuple next (String word, Integer current) throws Exception {
        List<Transition> transitions = transitionTable.transitions.stream().filter(t->t.debut == current).collect(Collectors.toList());
        for (Transition transition : transitions){
            for (String symbole : transition.symboles){
                if(word.startsWith(symbole)){
                    return new Tuple(transition.fin,word.substring(symbole.length()));
                }
            }
        }
        throw new Exception("Aucune transition possible a l'etat "+ current+" pour le mots "+word);
    }
}
