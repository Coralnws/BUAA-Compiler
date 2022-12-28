package Pcode;

import ParserAnalyse.SymbAnalyse;
import Pcode.Symbol.Symbol;
import Save.SaveContent;
import Save.Word;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Generator {
    public ArrayList<Word> wordList = SaveContent.getWordList();
    public ArrayList<Word> parserList = SymbAnalyse.getList();
    public static int index = 0;
    public static int startIndex = 0;
    public static int endIndex = 0;
    static Word sym;
    StringBuilder pcode = new StringBuilder("");
    static Writer writer;
    static Map<String,Symbol> SymbTable = new HashMap<String,Symbol>(); //可以直接通过名字来定位symbol
    //static ArrayList<Symbol> symbols = new ArrayList<Symbol>();
    boolean inFunc = false;
    Symbol currentFunc;
    static int LbracketNum = 0;
    static int RbracketNum = 0;
    public static boolean fromExp = false;

    static {
        try {
            writer = new Writer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int floor;  //floor在解释执行的时候才记录？
    Stack<Integer> stack = new Stack<Integer>();
    static int var_index = 1 ;

    public Generator() throws IOException {
        if(index == 0) {
            getSym();
        }
        //System.out.println("start:"+ sym.content);
        //parserList = SymbAnalyse.getList();
    }

    public void getSym(){
        if(index < wordList.size()){
            sym = wordList.get(index);
            if(sym.content.equals("(")){
                LbracketNum++;
            }
            if(sym.content.equals(")")){
                RbracketNum++;
            }
            index++;
            System.out.println("getSym() : " + sym.content);
        }
        else {
            System.out.println("sym is null");
            sym = null;
        }
    }

    public Word scanSym(int scanIndex){
        Word word = new Word();
        if(scanIndex < wordList.size()){
            word = wordList.get(scanIndex);
            System.out.println("scanSym() : "+ word.content);
            return word;
        }
        else{
            System.out.println("sym is null");
            return null;
        }
    }

    public int getFloor(){
        floor++;
        return floor-1;
    }

    public Symbol getSymbol(String key){
        return SymbTable.get(key);
    }

    public static void putTable(String name,Symbol obj){
        SymbTable.put(name,obj);
    }

    public static class SymbTable {

    }
    public static String getTVar(){
        String str = var_index + "t";
        var_index++;
        return str;
    }
    public static void renewV_Index(){
        var_index = 1;
    }

    public boolean isFunc(){
        //System.out.println("in IsFunc");
        int scanIndex = index-1;
        Word word = scanSym(scanIndex++);
        System.out.println("isFunc() : "+word.content );
        if(word.typeCode.equals("IDENFR")){
            word = scanSym(scanIndex++);
            if(word.content.equals("(")){
                return true;
            }else{
                System.out.println("return false");
                return false;
            }
        }
        System.out.println("return false");
        return false;
    }

    public boolean isArr(){
        int scanIndex = index-1;
        Word word = scanSym(scanIndex++);
        if(word.typeCode.equals("IDENFR")){
            word = scanSym(scanIndex++);
            if(word.content.equals("[")){
                System.out.println("isArr: return true");
                return true;
            }else{
                System.out.println("isArr: return false");
                return false;
            }
        }
        return false;
    }

    public boolean isNum(){
        return (sym.typeCode.equals("INTCON"));
    }

    public boolean isVar(){
        int scanIndex = index-1;
        Word word = scanSym(scanIndex++);
        if(word.typeCode.equals("IDENFR")){
            if(!isFunc() && !isArr()){
                return true;
            } else{
                return false;
            }
        }
        return false;
    }

    public boolean hasOperator(){
        int scanIndex = index-1;
        Word word = scanSym(scanIndex++);

        while(!word.content.equals(";") && !sym.typeCode.equals("PARAEND") && !sym.typeCode.equals("PRINTEND") && !word.content.equals(",")){
            if(word.content.equals("+") || word.content.equals("-") || word.content.equals("/") || word.content.equals("%") || word.content.equals("*")){
                return true;
            }
            word = scanSym(scanIndex++);
        }
        return false;
    }

    public boolean paraHasOperator(){
        int scanIndex = index-1;
        Word word = scanSym(scanIndex);
        System.out.println("paraHasOperator : " + word.typeCode);
        while(!word.typeCode.equals("PARAEND") && !word.typeCode.equals("PARACOMMA") && !word.typeCode.equals("PRINTEND")){
            System.out.println("paraHasOperator : " + word.typeCode);
            if(word.content.equals("+") || word.content.equals("-") || word.content.equals("/") || word.content.equals("%") || word.content.equals("*")){
                return true;
            }
            scanIndex++;
            word = scanSym(scanIndex);
        }
        return false;
    }

    public boolean isOperator(){
        int scanIndex = index-1;
        Word word = scanSym(scanIndex++);

        if(word.content.equals("+") || word.content.equals("-") || word.content.equals("/") || word.content.equals("%") || word.content.equals("*")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isLBracket(){
        if(sym.content.equals("(")){
            return true;
        }
        return false;
    }

    public boolean isParaComma(int startIndex){
        return false;
    }

    public boolean isParaEnd(int scanIndex){
        //System.out.println("IN PARAEND");
        //int scanIndex = index-1;
        if(LbracketNum == RbracketNum){
            return true;
        }
        else{
            return false;
        }
        /*
        Word word = scanSym(scanIndex++);
        if(word.content.equals(")")){
            word = scanSym(scanIndex++);
            if(!word.content.equals(")")){
                System.out.println("Is paraend");
                return true;
            }
            else{
                System.out.println("not paraend");
                return false;
            }
        }
        return false;

         */
    }

    public void append(String content){
        if(!content.equals("{") && !content.equals("}") && !content.equals(";")){
            pcode.append(content);
        }
    }
    public void clear(){
        pcode.delete( 0, pcode.length() );
    }
    public void startFunc(){
        LbracketNum = 0;
        RbracketNum = 0;
    }

}


