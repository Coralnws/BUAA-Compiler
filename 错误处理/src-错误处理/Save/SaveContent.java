package Save;

import Lexer.Lexer;
import Pcode.PcodeGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SaveContent { //记录wordlist
    private static ArrayList<Word> wordList = new ArrayList<Word>();
    private static SaveContent saveList = new SaveContent();
    public static ArrayList<Word> treeList = new ArrayList<Word>();

    private SaveContent() {}

    public static SaveContent getInstance(){
        return saveList;
    }

    public void addLexerWord(Word lexerWord){
        wordList.add(lexerWord);
    }

    public void addParserWord(int index, Word parserWord){
        wordList.add(index,parserWord);
    }



    public Word getSym(int index){
        if(index < wordList.size()){  //如果wordList里面还有内容
            Word word = new Word();
            word = wordList.get(index);
            return word;  //返回typeCode or content在这边调
        }
        else{
            return null;
        }
    }

    public static void print() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        String content = "";
        for(int i=0;i<wordList.size();i++){
            Word word = wordList.get(i);
            if(word instanceof lexerWord){
                if(word.content.equals("main")){
                    word.typeCode = "MAINTK";
                }
                content = word.typeCode + " " + word.content + '\n';
            }
            if(word instanceof parserWord){
                content = word.typeCode + '\n';
            }
            writer.write(content);
            writer.flush();
            //System.out.println(word.typeCode + " " + word.content);
        }
    }

    public static ArrayList<Word> getWordList(){
        return wordList;
    }

    public static void addTreeList(Word word){
        treeList.add(word);
    }

    public void printTree() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("tree.txt"));
        String content ="";
        for(int i=0;i<treeList.size();i++){
            Word word = treeList.get(i);
            if(word instanceof lexerWord){
                content = word.content + '\n';
            }
            if(word instanceof parserWord){
                content = word.typeCode + '\n';
            }
            //PcodeGenerator.treeList.add(word);
            writer.write(content);
            writer.flush();
            //System.out.println(word.typeCode + " " + word.content);
        }
    }

    public static Word getWord(int index){
        if(index < treeList.size()){
            Word word = new Word();
            word = treeList.get(index);
            return word;
        }
        else{
            return null;
        }
    }


    public static ArrayList<Word> getTreeList() {
        return treeList;
    }

    public static lexerWord CombineWord(String typecode){
        System.out.println("Start combine word");
        StringBuilder str = new StringBuilder("");
        lexerWord word;
        for(int i=wordList.size()-1;i>=0;i--){
            word = (lexerWord) wordList.get(i);
            if(word.typeCode == typecode){
                word.content += str;
                System.out.println("In CombineWord, check content:" + word.content);
                return word;
            }else{
                str.insert(0,word.content);
                wordList.remove(i);
            }
        }
        return null;  //不存在
    }
}