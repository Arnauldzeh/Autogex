package com.example.autogex.model;

import java.util.ArrayList;
import java.util.List;

public class AutomateImplementationv1 implements Automate{

    ConstructeurAutomateImplementation3.AutomataGraph graph;
    private static int currentCharacterCheck = 0;
    private static int trialCharacterCheck = 0;

    private static List<ErrorMessage> log;
    public AutomateImplementationv1(ConstructeurAutomateImplementation3.AutomataGraph g){
        this.graph = g;
    }
    @Override
    public List<ErrorMessage> verifierChaine(String chaine) {
        trialCharacterCheck = 0;
        log = new ArrayList<>();
       boolean res = reachFinalStateIn(this.graph.root,chaine,trialCharacterCheck);
       boolean finalRes = (trialCharacterCheck == chaine.length()) && res ? true : false;
       log.add(0,new ErrorMessage(Integer.MAX_VALUE,"UnnacceptedWord due to the following reasons"));
       if(finalRes){
           log.add(new ErrorMessage(Integer.MAX_VALUE,"the word "+chaine+" is recognized by the automata"));
           log.add(new ErrorMessage(Integer.MAX_VALUE,"1"));
       }else{log.add(new ErrorMessage(Integer.MAX_VALUE,"the word "+chaine+" IS NOT  recognized by the automata"));
           log.add(new ErrorMessage(Integer.MAX_VALUE,"-1"));
       }

       return log;
    }
    private boolean reachFinalStateIn(ConstructeurAutomateImplementation3.AutomataGraphNode node,String chaine,int initialTrialCount){
        if(node.hasAlternatives()){
            boolean hasOneSuccess = false;
            for(ConstructeurAutomateImplementation3.AutomataGraphNode n : node.stackRepository){
                node.executionStack.push(n);
            }
            while(node.currentCount < node.minCount) {
                while (!node.executionStack.isEmpty()) {
                    if (reachFinalStateIn(node.executionStack.peek(), chaine, trialCharacterCheck)) {
                        node.currentCount++;
                        hasOneSuccess = true;
                        break;
                    } else {
                        node.executionStack.pop();
                        trialCharacterCheck = initialTrialCount;
                    }
                }
                if(!hasOneSuccess){
                    node.currentCount = 0;
                    AddToLogIfNecessary(new ErrorMessage(trialCharacterCheck,"could not reach final state after position: "+(trialCharacterCheck-1)));
                    return false;
                }
            }
            if(hasOneSuccess || node.minCount == 0){

                while (node.currentCount < node.maxCount && (trialCharacterCheck < chaine.length())){
                    node.executionStack.clear();
                    for(ConstructeurAutomateImplementation3.AutomataGraphNode n : node.stackRepository){
                        node.executionStack.push(n);
                    }
                    hasOneSuccess = false;
                    while (!node.executionStack.isEmpty()) {
                        if (reachFinalStateIn(node.executionStack.peek(), chaine, trialCharacterCheck)) {
                            node.currentCount++;
                            hasOneSuccess = true;
                            break;
                        } else {
                            node.executionStack.pop();
                            trialCharacterCheck = initialTrialCount;
                        }
                    }
                    if(!hasOneSuccess){
                        node.currentCount = 0;
                        return true;
                    }else{
                        initialTrialCount = trialCharacterCheck;
                    }

                }
                node.currentCount = 0;
                return true;

            }
            node.currentCount =0;
            return false;
        }else if(node.hasChildren()){
            for(int i=0;i<node.minCount;i++) {
                for (ConstructeurAutomateImplementation3.AutomataGraphNode n : node.children) {
                    if (!reachFinalStateIn(n, chaine,trialCharacterCheck)) {
                        // could not reach final state in one of the children nodes, so unnaccepted word
                        trialCharacterCheck = initialTrialCount;// rewind to last accepted position
                        node.currentCount =0;
                        return false;
                    }
                }
                node.currentCount++;
            }
            //handle max count
            int temp = trialCharacterCheck;
            while(node.currentCount < node.maxCount){
                for (ConstructeurAutomateImplementation3.AutomataGraphNode n : node.children) {
                    if (!reachFinalStateIn(n, chaine,trialCharacterCheck)) {
                        // could not reach final state in one of the children nodes, so unnaccepted word
                        //log.add("could not reach max at "+trialCharacterCheck);
                        //trialCharacterCheck = initialTrialCount;// rewind to last accepted position
                        node.currentCount =0;
                        return true;
                    }else{
                        node.currentCount++;
                    }
                }
            }
            node.currentCount =0;
            return true;
        }else{
            //handle min count first
            for(int i=0;i<node.minCount;i++){
                if(trialCharacterCheck>=chaine.length())break;
                if(node.concatenateLeafValue.equals(String.valueOf((chaine.charAt(trialCharacterCheck)) ))){
                    trialCharacterCheck++;
                    node.currentCount++;
                }else{
                    // min number of final states not reached due to unnaccepted symbol
                    AddToLogIfNecessary(new ErrorMessage(trialCharacterCheck,"Unaccepted symbol : "+String.valueOf((chaine.charAt(trialCharacterCheck)))+" At : "+trialCharacterCheck+" Expected : "+node.concatenateLeafValue));
                    //log.add("Unaccepted symbol : "+String.valueOf((chaine.charAt(trialCharacterCheck)))+" At : "+trialCharacterCheck+" Expected : "+node.concatenateLeafValue);
                    node.currentCount =0;
                    return false;
                }
            }
            if(node.currentCount == node.minCount){
                // handle max number of final states
                while(node.currentCount < node.maxCount && trialCharacterCheck <chaine.length()){
                    if(node.concatenateLeafValue.equals(String.valueOf((chaine.charAt(trialCharacterCheck)) ))){
                        trialCharacterCheck++;
                        node.currentCount++;
                    }else{
                        // max number of final states not reached due to unnaccepted symbol
                        //log.add("Unaccepted symbol : "+String.valueOf((chaine.charAt(trialCharacterCheck)))+" At position: "+trialCharacterCheck+" Expected : "+node.concatenateLeafValue);
                        node.currentCount =0;
                        return true;
                    }
                }
                node.currentCount =0;
                return true;
            }else{
                // min number of final states not reached due to insufficient symbols
                AddToLogIfNecessary(new ErrorMessage(trialCharacterCheck,"Insufficient symbols could not reach minimum state at "+trialCharacterCheck));
                //log.add("Insufficient symbols could not reach minimum state at "+trialCharacterCheck);
                node.currentCount =0;
                return false;
            }


        }
    }
    private void AddToLogIfNecessary(ErrorMessage message){
        boolean atLeastOneRemoved = false;
        for(int i=0;i<log.size();i++){
            if(log.get(i).position < message.position){
                log.remove(i);
                atLeastOneRemoved = true;
            }
        }
        if(atLeastOneRemoved || log.size()==0)
            log.add(message);
    }

}
