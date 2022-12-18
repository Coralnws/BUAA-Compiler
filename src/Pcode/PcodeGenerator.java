package Pcode;

import Save.SaveContent;
import Save.Word;
import Save.lexerWord;
import Save.parserWord;

import java.io.IOException;
import java.util.ArrayList;

public class PcodeGenerator {
    public static ArrayList<Word> treeList = SaveContent.getTreeList();
    public static Word currentWord;
    static Word wordAhead;
    static Word lastOp = null;
    static Word scanWord;
    static Word scanWordAhead;
    static int index = 2;
    StringBuilder pcode = new StringBuilder("");
    static Writer writer;
    static boolean ignoreParser=false;
    static int ifNum = 0;
    static int whileNum = 0;

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
        nextWord();
        while(currentWord != null){
            //这边读的是CompUnit的部分，之后那些就好像语法分析那样，从内部来调用
            //System.out.println("In PcodeGenerator , Currentword:" + currentWord.typeCode);
            if(currentWord.typeCode.equals("<FuncDef>")){
                nextWord();
                nextWord();
                FuncDef funcDef = new FuncDef();
            }else if(currentWord.typeCode.equals("<Decl>")){
                Decl decl = new Decl();
            }else if(currentWord.typeCode.equals("<MainFuncDef>")){
                nextWord();
                FuncDef funcDef = new FuncDef();
            }
        }
    }


    public static void nextWord(){
        if(index < treeList.size()) {

            currentWord = SaveContent.getWord(index++);

            if(ignoreParser){
                while(currentWord instanceof parserWord){
                    currentWord = treeList.get(index++);
                }
            }
            if(currentWord instanceof lexerWord){
                ////System.out.println("CurrentLexerWord: " + currentWord.content);
            }else if(currentWord instanceof parserWord){
                while(ignoreList.contains(currentWord.typeCode)){
                    currentWord = SaveContent.getWord(index++);
                }

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

            }else if(currentWord instanceof parserWord){
            }
            index++;
        }else{
            currentWord = null;
        }
    }

    public static void scanWord(int scanIndex){
        if(scanIndex < treeList.size()){
            scanWord = treeList.get(scanIndex);
            ////System.out.println("scanWord: " + scanWord.typeCode);
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
}