package com.example.autogex.model;

import java.util.List;

public interface Automate {
    public List<ErrorMessage> verifierChaine(String chaine);
    public class ErrorMessage{
        int position;
        public String message;
        public ErrorMessage(int p,String m){
            this.position = p;
            this.message = m;
        }
    }
}
