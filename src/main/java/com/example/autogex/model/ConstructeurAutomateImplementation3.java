package com.example.autogex.model;

import java.util.*;

public class ConstructeurAutomateImplementation3 implements ConstructeurAutomate{
    List<String> regexAlphabet;

    public ConstructeurAutomateImplementation3(List<String> Alphabet){
        this.regexAlphabet = Alphabet;
    }

    @Override
    public Automate constructAutomate(List<String> alphabet, String regex) {
        this.regexAlphabet = alphabet;
        String[] Alphabet = new String[alphabet.size()];
        int i=0;
        for(String s : alphabet){
            Alphabet[i] = s;
            i++;
        }
        List<String> regexDecoupee = decouperLaRegexEnSymbolesEtOperateurs(regex,alphabet);
       AutomataGraph graph = new AutomataGraph();
       graph.root = buildAutomataGraphNode(regexDecoupee,Alphabet,1,1,false);
       Automate automate = new AutomateImplementationv1(graph);
        return automate;
    }

    private  AutomataGraphNode buildAutomataGraphNode(List<String> regex,String[] Alphabet,int condMin,int condMax,boolean fromStack){
        //former les groupes a partir des plus hors parentheses et mettre les groupes dans un Queue

        AutomataGraphNode node = new AutomataGraphNode();
        node.minCount = condMin;
        node.maxCount = condMax;

        Queue<List<String>> initialQueue = new LinkedList<>();
        List<String> temp = new ArrayList<>();
        boolean consumeGroup = false;
        int openParanthesisCount = 0;
        for(String s : regex){

            if(consumeGroup){
                if(stringIsParanthesisRight(s)){
                    openParanthesisCount--;
                    temp.add(s);
                }else if(stringIsParanthesisLeft(s)){
                    openParanthesisCount++;
                    temp.add(s);
                }else if(openParanthesisCount > 0){
                    temp.add(s);
                }else{
                    consumeGroup = false;
                    temp.add(s);
                }

            }else{
                if(stringIsParanthesisLeft(s)){
                    consumeGroup = true;
                    openParanthesisCount++;
                    temp.add(s);
                }else if(stringIsUnionOperator(s)){
                    initialQueue.add(List.copyOf(temp));
                    temp.clear();
                }
                else{
                    temp.add(s);
                }
            }

        }

        if(!temp.isEmpty()){
            initialQueue.add(List.copyOf(temp));
            temp.clear();
        }

        if(initialQueue.size()==1){
            //constructing the children nodes
            Queue<SymbolConditionCouple> initialQueue2 = new LinkedList<>();
            List<String> temp2 = new ArrayList<>();
            String conditon2 = "";
            boolean consumeGroup2 = false;
            boolean readCondition2 = false;
            int openParanthesisCount2 = 0;
            for(String s : initialQueue.peek()){
                if(readCondition2){
                   if(stringIsConditionCloseCharacter(s)){
                        SymbolConditionCouple scc = new SymbolConditionCouple();
                        scc.condition = conditon2;
                        scc.symbol = List.copyOf(temp2);
                        initialQueue2.add(scc);
                        temp2.clear();
                        conditon2 = "";
                        readCondition2 = false;
                   }else{
                       conditon2+=s;
                   }
                }
                else if(consumeGroup2){
                    if(stringIsParanthesisRight(s)){
                        openParanthesisCount2--;
                        if(openParanthesisCount2 > 0){
                            temp2.add(s);
                        }else{
                            consumeGroup2 = false;
                        }
                    }else if(stringIsParanthesisLeft(s)){
                        temp2.add(s);
                        openParanthesisCount2++;
                    }else if(openParanthesisCount2 > 0){
                        temp2.add(s);
                    }else{
                        consumeGroup2 = false;
                        if(stringIsParanthesisLeft(s)){
                            consumeGroup2 = true;
                            openParanthesisCount2++;

                        }else if(stringIsConditionOpenCharacter(s)){
                            readCondition2 = true;
                        }
                        else{
                            SymbolConditionCouple scc = new SymbolConditionCouple();
                            scc.condition = "1,1";
                            scc.symbol = List.copyOf(temp2);
                            initialQueue2.add(scc);
                            temp2.clear();
                            temp2.add(s);
                        }
                    }

                }else{
                    if(stringIsParanthesisLeft(s)){
                        consumeGroup2 = true;
                        if(!temp2.isEmpty()){
                            SymbolConditionCouple scc = new SymbolConditionCouple();
                            scc.condition = "1,1";
                            scc.symbol = List.copyOf(temp2);
                            initialQueue2.add(scc);
                            temp2.clear();
                            temp2.add(s);
                        }
                        openParanthesisCount2++;
                    }else if(stringIsConditionOpenCharacter(s)){
                        readCondition2 = true;
                    }
                    else{
                        if(temp2.isEmpty()){
                            temp2.add(s);
                        }else{
                            SymbolConditionCouple scc = new SymbolConditionCouple();
                            scc.condition = "1,1";
                            scc.symbol = List.copyOf(temp2);
                            initialQueue2.add(scc);
                            temp2.clear();
                            temp2.add(s);
                        }

                    }
                }
            }
            if(!temp2.isEmpty()){
                SymbolConditionCouple scc = new SymbolConditionCouple();
                scc.condition = "1,1";
                scc.symbol = List.copyOf(temp2);
                initialQueue2.add(scc);
                temp2.clear();
            }
            if(initialQueue2.size() > 1) {
                while (!initialQueue2.isEmpty()) {
                    SymbolConditionCouple couple = initialQueue2.poll();
                    node.children.add(buildAutomataGraphNode(couple.symbol, Alphabet, couple.getConditionMin(), couple.getConditionMax(),false));
                }
            }else{
                SymbolConditionCouple lone = initialQueue2.poll();
                if(lone.symbol.size() == 1){
                    node.concatenateLeafValue =lone.symbol.get(0);
                    if(fromStack){
                        node.minCount = lone.getConditionMin();
                        node.maxCount = lone.getConditionMax();
                    }
                }else{
                    node = buildAutomataGraphNode(lone.symbol,Alphabet, lone.getConditionMin(), lone.getConditionMax(),false);
                    if(condMin > 1 || condMax > 1) {
                        node.minCount = condMin;
                        node.maxCount = condMax;
                    }
                }
            }
        }else {
            while (!initialQueue.isEmpty()) {
                node.stackRepository.add(buildAutomataGraphNode(initialQueue.poll(), Alphabet,1,1,true));
            }
        }

        return node;
    }

    @Override
    public List<String> verifierAlphabet(List<String> alphabet) {
        return null;
    }

    @Override
    public String verifierRegex(String regex) {
        return verificationRegex(regex, this.regexAlphabet);
    }
    class SymbolConditionCouple{
        List<String> symbol;
        String condition;

        int getConditionMin(){
            return Integer.valueOf(condition.split(",")[0]);
        }
        int getConditionMax(){

            String val = condition.split(",")[1];
            if(val.equals("+") || val.equals("*")){
                return Integer.MAX_VALUE;
            }else{
                return Integer.valueOf(val);
            }
        }
    }

    public class AutomataGraph{
        AutomataGraphNode root;

    }
    public class AutomataGraphNode{
        List<AutomataGraphNode> children;
        List<AutomataGraphNode> stackRepository;
        Stack<AutomataGraphNode> executionStack;
        int minCount;
        int maxCount;
        int currentCount;
        String concatenateLeafValue = "";
        List<String> selectionLeafValue;

        public AutomataGraphNode(){
            this.children = new ArrayList<>();
            this.stackRepository = new ArrayList<>();
            this.executionStack = new Stack<>();
            this.selectionLeafValue = new ArrayList<>();
        }

        boolean hasAlternatives(){
            return this.stackRepository.size() > 0;
        }
        boolean hasChildren(){
            return this.children.size() > 0;
        }
    }
    private List<String> decouperLaRegexEnSymbolesEtOperateurs(String regex,List<String>alph){
        String[] alphabet = new String[alph.size()];
        for(int i=0;i<alph.size();i++){
            alphabet[i]=alph.get(i);
        }
        List<String> res = new ArrayList<>();
        Stack<Character> caracterStack = new Stack<>();
        char[] regexChars = regex.toCharArray();
        if(regexChars.length >= 1){
            String currentSymbol = "";
            boolean readCondition = false;
            //caracterStack.push(regexChars[0]);
            for(int i=0;i<regexChars.length;i++){
                if(readCondition){
                    if(stringIsConditionCloseCharacter(String.valueOf(regexChars[i]))){
                        readCondition = false;
                    }
                    res.add(String.valueOf(regexChars[i]));
                    continue;
                }
                currentSymbol = "";
                if(caracterIsAnOperator(regexChars[i])){
                    String supposedSymbol = "";
                    while(!caracterStack.empty()){
                        supposedSymbol+=caracterStack.peek();
                        caracterStack.pop();
                    }
                    if(stringIsConditionOpenCharacter(String.valueOf(regexChars[i]))){
                        readCondition = true;
                    }
                    if(stringIsASymbolOf(alphabet,supposedSymbol)){
                        res.add(supposedSymbol);
                        res.add(String.valueOf(regexChars[i]));
                    }else if(supposedSymbol.length()==0){res.add(String.valueOf(regexChars[i]));}
                    else {
                            //we found a symbol not found in the alphabet
                            System.out.println(supposedSymbol+" is not a symbol of the described alphabet");
                        }
                }else {
                    currentSymbol+=regexChars[i];
                    if(isNot_A_PrefixOf_A_Symbol(alphabet,String.valueOf(currentSymbol))){
                        if(stringIsASymbolOf(alphabet,currentSymbol)){
                            res.add(currentSymbol);
                        }
                    }else{
                        caracterStack.push(regexChars[i]);
                    }


                }
            }
        }else{
            System.out.println("The regex is an empty string or just a single caracter");
        }
        return res;
    }
    private boolean caracterIsAnOperator(char c){
        char[] operators = {'(',')','+','*','.','{','}'};
        for (char s:operators) {
            if(s == c){
                return true;
            }
        }
        return false;
    }
    private boolean stringIsASymbolOf(String[] alphabet,String s){
        for (String symbol:alphabet) {
            if(symbol.equals(s)){
                return true;
            }
        }
        return false;
    }
    private boolean isNot_A_PrefixOf_A_Symbol(String[] alphabet,String s){
        for (String symbol:alphabet) {
            if(symbol.startsWith(s) && (!symbol.equals(s))){
                return false;
            }
        }
        return true;
    }
    private boolean stringIsUnionOperator(String s){
        return s.charAt(0) == '+';
    }
    private boolean stringIsParanthesisLeft(String s){
        return s.charAt(0) == '(';
    }
    private boolean stringIsParanthesisRight(String s){
        return s.charAt(0) == ')';
    }
    private boolean stringIsConditionOpenCharacter(String s){
        return s.charAt(0) == '{';
    }
    private boolean stringIsConditionCloseCharacter(String s){
        return s.charAt(0) == '}';
    }
    private String getLargerCondition(String c1,String c2){
        c1 = c1.replace("{","");
        c1 = c1.replace("}","");
        c2 = c2.replace("{","");
        c2 = c2.replace("}","");
        String[] Condition1 = c1.split(",");
        String[] Condition2 = c2.split(",");
        String maxs = null;
        int min = Integer.valueOf(Condition1[0])<Integer.valueOf(Condition2[0]) ? Integer.valueOf(Condition1[0]):Integer.valueOf(Condition2[0]);
        if(Condition1[1].equals("*") || Condition2[1].equals("*")){
            maxs = "*";
        }else if(Condition1[1].equals("+") || Condition2[1].equals("+")){
            maxs = "+";
        }else{
            int max = Integer.valueOf(Condition1[1])>Integer.valueOf(Condition2[1]) ? Integer.valueOf(Condition1[1]):Integer.valueOf(Condition2[1]);
            return "{"+min+","+max+"}";
        }
        if(maxs != null){
            return "{"+min+","+maxs+"}";
        }
        return null;

    }
    private String removeDoublesFromRegex(String regex, String[] Alphabet){
        //replace all ^* , ^+ and \+
        regex = correctFormat(regex,Arrays.asList(Alphabet));
        char[] regexChars = regex.toCharArray();
        boolean lireCondition = false;

        String condtemp = "";
        List<Character> symbolTemp = new ArrayList<>();
        String stringSymbolTemp = "";
        Stack<String> cleanedRegex = new Stack<>();
        for(int i=0;i<regexChars.length;i++){
            if(lireCondition){
                if(stringIsConditionCloseCharacter(String.valueOf(regexChars[i]))) {
                    if(cleanedRegex.size()>0){
                        if(stringIsConditionOpenCharacter(String.valueOf(cleanedRegex.peek().charAt(0)))){
                            String peekCond = cleanedRegex.peek();
                            condtemp+=String.valueOf(regexChars[i]);
                            String largerCondition = getLargerCondition(peekCond,condtemp);
                            cleanedRegex.pop();
                            cleanedRegex.push(largerCondition);
                        }else{
                            if(stringSymbolTemp.length()>0) {
                                cleanedRegex.push(stringSymbolTemp);
                                stringSymbolTemp = "";
                            }
                            condtemp+=String.valueOf(regexChars[i]);
                            cleanedRegex.push(condtemp);
                            condtemp="";
                        }
                    }else{
                        if(stringSymbolTemp.length()>0) {
                            cleanedRegex.push(stringSymbolTemp);
                            stringSymbolTemp = "";
                        }
                        condtemp+=String.valueOf(regexChars[i]);
                        cleanedRegex.push(condtemp);
                        condtemp="";
                    }
                    if(stringSymbolTemp.length()>0){
                        cleanedRegex.push(stringSymbolTemp);
                        stringSymbolTemp = "";
                    }
                    lireCondition = false;
                }else{
                    condtemp+=String.valueOf(regexChars[i]);
                }
            }else{
                if(regexChars[i]=='{'){
                    lireCondition = true;
                    condtemp+=String.valueOf(regexChars[i]);
                }else if(regexChars[i]=='+'){
                    if(stringSymbolTemp.length() > 0){
                        cleanedRegex.push(stringSymbolTemp);
                        stringSymbolTemp = "";
                    }
                    if(!cleanedRegex.peek().equals("+")){
                        cleanedRegex.push(String.valueOf(regexChars[i]));
                    }

                }else{
                    //reading a symbol
                    symbolTemp.add(regexChars[i]);
                    stringSymbolTemp+=String.valueOf(regexChars[i]);
                    //check if formed symbol so far is part of the alphabet

                    cleanedRegex.push(stringSymbolTemp);
                    stringSymbolTemp="";


                }
            }
        }
        List<String> res = new ArrayList<>();
        while(!cleanedRegex.isEmpty()){
            res.add(cleanedRegex.peek());
            cleanedRegex.pop();
        }
        Collections.reverse(res);
        String result = "";
        for(String s : res){
            result+= s;
        }
        return result;
    }
    private String cleaning_regex(String reg_ex){
        reg_ex = reg_ex.trim().replaceAll("[. ]","");
        for (int i = 0; i <reg_ex.length()-1; i++) {
            int s=i+2;

            if(reg_ex.substring(i,s).equals("()"))
                reg_ex = reg_ex.replace(""+reg_ex.substring(i,s), "");
        }
        return reg_ex;
    }

    private String brackets_validation(String reg_ex){
        int num_cbracket=0;
        int num_obracket=0;
        int n = 0;
        if(reg_ex.charAt(0)==')')
            return "Invalid";
        if(reg_ex.charAt(0)=='(')
            n = num_obracket++;
        for (int i = 1; i < reg_ex.length(); i++) {
            if(reg_ex.charAt(i)==')'){
                num_obracket=0;
                num_cbracket++;
                for (int j = 0; j < i; j++) {
                    if(reg_ex.charAt(j)=='('){
                        num_obracket++;
                    }
                }
                if((num_obracket + n) < num_cbracket){
                    return null;
                }
            }
        }
        if(num_cbracket==0 && (num_obracket+n)!=0){
            return null;
        }

        if ((num_obracket + n) != num_cbracket)
            return null;
        return "ok";
    }

    private boolean verifyIfInAlphabet(String regex, List<String> alphabet) {
        boolean flag = false;
        List<String> temp = decouperLaRegexEnSymbolesEtOperateurs(regex, alphabet);
        String [] splittedRegex = new String[temp.size()];
        List<String>  conditions = new ArrayList<>();
        conditions.add("*"); conditions.add("+"); conditions.add("("); conditions.add(")"); conditions.add("{");conditions.add("}");conditions.add(",");conditions.add("0");conditions.add("1");
        for(int i=0;i<temp.size();i++){
            splittedRegex[i]=temp.get(i);
        }
        for (int i = 0; i <= regex.length() - 1; i++) {
            if (alphabet.contains(splittedRegex[i]) || conditions.contains(splittedRegex[i])) {
                flag = true;
            } else {
                flag = false;
                i++;
                System.out.println("the error is the " + splittedRegex[i - 1] + " at point " + i + " so please modify");
                break;
            }
        }
        return flag;
    }
    private boolean verifyIfOperatorsAreWellWritten(String regex, List <String> alphabet) {
        String[] nonDebuts = {"+", ".", "^*","^+", ")", "^", "*"};
        String[] nonFins = {"+", "("};
        String[] invalides = {"+.", "(+", "(^+","(^*", "+)", ".+"};
        boolean flag = true;

        for (int i = 0; i < invalides.length; i++) {
            if (regex.contains(invalides[i])) {
                System.out.println("It contains and invalid character combination");
                flag = false;
            }
        }
        List<String> temp = decouperLaRegexEnSymbolesEtOperateurs(regex, alphabet);
        String[] splittedRegex = new String[temp.size()];
        for(int i=0;i<temp.size();i++){
            splittedRegex[i]=temp.get(i);
        }

        if (Arrays.asList(nonDebuts).contains(splittedRegex[0])) {
            System.out.println(" The expression cannot start with " + splittedRegex[0] + " at first position ");
            flag = false;
        } else if ((regex.substring(0,1).contains("^*") || regex.substring(0,1).contains("^+"))) {
            System.out.println(" The expression cannot start with " + regex.substring(0,1) + " at first position");
            flag = false;
        }

        if (Arrays.asList(nonFins).contains(splittedRegex[splittedRegex.length - 1])) {
            System.out.println("The expression cannot end with " + splittedRegex[splittedRegex.length - 1] + " last at position ");
            flag = false;
        }

        return flag;
    }


    private String simplifyRegex(String regex, List <String> alphabet){
        String simplifiedRegex = "", word = "";
        List<String> temp = decouperLaRegexEnSymbolesEtOperateurs(regex, alphabet);
        String[] splittedRegex = new String[temp.size()];
        for(int i=0;i<splittedRegex.length;i++){
            splittedRegex[i]=temp.get(i);
        }
        for (int i = 0; i < splittedRegex.length; i++) {
            if ( i+4 > splittedRegex.length) {
                System.out.println(i+4);
                break;
            }else {
                if ((splittedRegex[i].equals(splittedRegex[i + 3])) &&
                        (splittedRegex[i + 1].equals("^") && splittedRegex[i + 2].equals("*"))) {
                    splittedRegex[i+2] = "+";
                    splittedRegex[i+3] = "";
                }
            }
        }

        for (int i = 0; i < splittedRegex.length ; i++) {
            if(splittedRegex[i].equals("")){
                continue;
            }else {
                word += splittedRegex[i];
            }
        }

        return word;
    }
    private String correctFormat(String regex, List<String> alphabet){
        String word = "";
        regex = regex.replace("^*","{0,*}");
        regex = regex.replace("^+","{1,*}");
        return regex;
    }

    private String verificationRegex(String regex,List <String> alphabet) {
        Boolean flag = false;
        String[] alph = new String[alphabet.size()];
        for(int i=0;i<alph.length;i++){
            alph[i] = alphabet.get(i);
        }
        String cleanedRegex = cleaning_regex(regex);
        cleanedRegex = removeDoublesFromRegex(cleanedRegex,alph);

       if (verifyIfInAlphabet(cleanedRegex,alphabet)){
            if (verifyIfOperatorsAreWellWritten(cleanedRegex, alphabet)){
              if(brackets_validation(cleanedRegex) != null){
                  flag = true;
                }
            }
            }else{
                flag = false;
             }
        if (flag==true){
            return cleanedRegex;
        }else{
            return null;
        }
        //return cleanedRegex;
    }

}
