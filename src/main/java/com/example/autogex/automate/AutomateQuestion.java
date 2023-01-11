package com.example.autogex.automate;

import com.example.autogex.infos.AppConst;
import com.example.autogex.infos.Question;
import com.example.autogex.infos.QuestionInfo;
import com.example.autogex.infos.Repetition;

import java.util.ArrayList;
import java.util.List;

public class AutomateQuestion {
    QuestionInfo questionInfo = AppConst.questionInfo;
    List<Partie> parties=new ArrayList<>();
    String regex;

    public AutomateQuestion(){
        for (Question question : questionInfo.questions){
            List<Repeat> repeats=new ArrayList<>();
            for(Repetition repetition : question.repetitions){
                repeats.add(new Repeat(repetition.getWord(), repetition.getNumber()));
            }
            parties.add(new Partie(questionInfo.alphabet,question.prefix,question.suffix,repeats));
        }

        regex = GenerateRegexByPartie.generateRegexByQuestions(parties);

    }

    public boolean verifieWord(String word) throws Exception{
        AutomateRegex automateRegex = new AutomateRegex(regex);
        return automateRegex.verifieWord(word);
    }

    public String getRegex(){
        return regex;
    }
}
