package Pcode;

import Pcode.Symbol.Func;
import Pcode.Symbol.Symbol;
import Save.SaveContent;
import Save.Word;
import Save.lexerWord;
import Save.parserWord;
import Error.ErrorRecord;
import Error.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class PcodeGenerator {
    public static ArrayList<Word> treeList = SaveContent.getTreeList();
    public static Word currentWord;
    static Word wordAhead;
    static Word scanWord;
    static Word scanWordAhead;
    static int index = 2;
    StringBuilder pcode = new StringBuilder("");
    static Writer writer;
    static boolean ignoreParser=false;
    static int ifNum = 0;
    //static int whileNum = 0;

    //static HashMap<Integer, HashMap<String,Symbol>> symbTable = new HashMap<Integer, HashMap<String,Symbol>>();  //记录所有层的符号表
    static Stack<HashMap<String,Symbol>> symbTable = new Stack<HashMap<String,Symbol>>();
    static HashMap<String,Symbol> currentSymbTable = new HashMap<String,Symbol>();
    static HashMap<String, Func> funcList = new HashMap<String,Func>(); //记录函数
    static HashMap<String,Symbol> publicSymbTable = new HashMap<String,Symbol>();
    static Func currentFunc;
    static boolean isWhile=false;
    static int whileNum=0;
    static String currentExp;
    static boolean hasRet = false;
    static int blockNum = 0;


    public static ErrorRecord errorRecord = new ErrorRecord();

    static ArrayList<String> ignoreList = new ArrayList<String>(){{
        add("<AddExp>");
        add("<MulExp>");
        add("<UnaryExp>");
        add("<PrimaryExp>");
        add("<Number>");
        add("<ConstExp>");
        //add("<ConstDef>");
        //add("<VarDef>");
    }};

    static {
        try {
            writer = new Writer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //for exp
    static int var_index=1;
    static String varT;

    //for cond
    static int cond_index=1;
    static String condT;

    public void generate() throws IOException {
        System.out.println("Start Pcode Generator");
        nextWord();
        HashMap<String,Symbol> floorTable = new HashMap<String,Symbol>();
        //symbTable.push(floorTable);
        currentSymbTable = floorTable;
        publicSymbTable = floorTable;
        while(currentWord != null){
            //这边读的是CompUnit的部分，之后那些就好像语法分析那样，从内部来调用
            System.out.println("In PcodeGenerator , Currentword:" + currentWord.typeCode);
            if(currentWord.typeCode.equals("<FuncDef>")){
                nextWord();
                nextWord();
                FuncDef funcDef = new FuncDef();
            }else if(currentWord.typeCode.equals("<Decl>")){
                Decl decl = new Decl();
            }else if(currentWord.typeCode.equals("<MainFuncDef>")){
                nextWord();
                System.out.println("From Pcode Generator go to MainFuncDef");
                FuncDef funcDef = new FuncDef();
            }
        }
        errorRecord.print();
    }


    public static void nextWord(){
        //System.out.println("treelist.size() : "+treeList.size());
        if(index < treeList.size()) {
            currentWord = SaveContent.getWord(index++);
            if(currentWord.typeCode.equals("<LVal>")){
                Word word = SaveContent.getWord(index);
                System.out.println("is LVal, word: " + word.content);
                if(checkVarExist(word.content) == null){
                    System.out.println("LVal , gotError c");
                    Error error = new Error(word.line,'c');
                    errorRecord.addError(error);
                }
            }
            //System.out.println(currentWord.typeCode);
            if(ignoreParser){
                //System.out.println("ignoring parser");
                while(currentWord instanceof parserWord){
                    currentWord = treeList.get(index++);
                }
            }
            if(currentWord instanceof lexerWord){
                System.out.println("CurrentLexerWord: " + currentWord.content);
            }else if(currentWord instanceof parserWord){
                while(ignoreList.contains(currentWord.typeCode)){
                    currentWord = SaveContent.getWord(index++);
                }
                System.out.println("CurrentParserWord: " + currentWord.typeCode);
            }

            if(index < treeList.size()){
                wordAhead = treeList.get(index);
            }
            else{
                wordAhead = null;
            }
        }else{
            currentWord = null;
        }
    }

    public static void prevWord(){
        if(index > 0) {
            index -= 2;
            currentWord = treeList.get(index);

            if(currentWord instanceof lexerWord){
                System.out.println(currentWord.content);
            }else if(currentWord instanceof parserWord){
                System.out.println(currentWord.typeCode);
            }
            index++;
        }else{
            currentWord = null;
        }
    }

    public static void scanWord(int scanIndex){
        if(scanIndex < treeList.size()){
            scanWord = treeList.get(scanIndex);
            if(currentWord.typeCode.equals("<LVal>")){
                Word word = SaveContent.getWord(index);
                System.out.println("is LVal, word: " + word.content);
                if(checkVarExist(word.content) == null){
                    System.out.println("LVal , gotError c");
                    Error error = new Error(word.line,'c');
                    errorRecord.addError(error);
                }
            }
            System.out.println("scanWord: " + scanWord.typeCode);
            if(scanIndex < treeList.size() - 1 ){
                scanWordAhead = treeList.get(scanIndex+1);
            }else{
                scanWordAhead =null;
            }
        }else{
            scanWord =null;
        }
    }

    public static void nextTVar(){
        varT = var_index + "t";
        var_index++;
    }

    public static void nextIf(){
        ifNum++;
    }

    public static void nextWhile(){
        whileNum++;
    }
    public static int ifNum(){
        return ifNum;
    }

    public static String getIfNum(){
        return "#if"+ifNum;
    }
    public static String getElseNum(){
        return "#else"+ifNum;
    }

    public static String getWhileNum(){
        return "#while"+whileNum;
    }



    public static void nextCondT(){
        condT = "#cond"+cond_index;
        cond_index++;
    }
    public static void reCondNum(){
        cond_index = 1;
    }

    public void appendPcode(String str){
        this.pcode.append(str);
    }

    public static boolean checkCurrentSymbExist(String name) {  //只需要查当前层
        if(currentSymbTable.containsKey(name)) {
            return true;
        }
        return false;
    }

    public static Symbol checkVarExist(String name){
        Symbol symb = null;
        if(currentSymbTable.containsKey(name)){
            symb = currentSymbTable.get(name);
            return symb;
        }

        if(symb == null && blockNum > 0){

            int index = symbTable.size()-2;
            int diff = 0;
            int n = index-diff;
            while(n >= 0 ){
                System.out.println("getVar,index:" + n);
                symb = symbTable.get(n).get(name);
                diff++;
                n = index-diff;
            }
            if(symb != null){
                return symb;
            }
            System.out.println("CHECK IN BLOCK, NOT IN BLOCK");
        }

        if(symb == null && publicSymbTable.containsKey(name)){
            symb = publicSymbTable.get(name);
            return symb;
        }

        return symb;
    }

    public static Func getFunc(String name){
        return funcList.get(name);
    }
}

