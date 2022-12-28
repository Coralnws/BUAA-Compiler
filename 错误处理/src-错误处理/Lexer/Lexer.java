package Lexer;

import java.io.*;

import Save.*;
import Vocab.*;
import static java.lang.Character.*;

public class Lexer {  //词法分析
    private char sym;  //当前字符
    private StringBuilder currentWord = new StringBuilder("");  //拼的字符串
    private int line;   //行号
    static String currentLine;
    static int readIndex=0;
    private boolean singleLineComment;
    private boolean multiLineComment;
    private BufferedReader reader;
    private BufferedReader readLine;
    private static Word saveWord;
    //private List<Save.Word> wordList = new ArrayList<Save.Word>();
    public static SaveContent save = SaveContent.getInstance();
    static boolean readStrAdy = false;

    public Lexer() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("testfile.txt"));

        line = 1;
    }

    public void nextSym() throws IOException {
        sym = (char) reader.read();
        readIndex++;
        if(sym == '\n'){   //换行
            line += 1;
            readStrAdy = false;
        }
        /*
        if(sym == (char)-1){  //文件结束
            reader.close();
        }
        */

    }

    public void save(String typeCode,String content){
        if(!singleLineComment && !multiLineComment){
            saveWord = new lexerWord(typeCode,content,line);
            //System.out.println(content);
            save.addLexerWord(saveWord);
        }
    }

    public void start() throws IOException {   //正式开始分析
        nextSym();
        while(sym != (char)-1){
            //System.out.println("char:" + sym);
            currentWord.delete( 0, currentWord.length() );  //每次重置字符串
            //System.out.println("CurrentWord:" + currentWord);
            while(sym == ' '){        //检查是不是空格
                nextSym();
                //System.out.println("space");
            }
            // 1 - 数字串（数字开头必定是数字串）
            if(isDigit(sym)) {
                while (isDigit(sym)) {
                    currentWord.append(sym);
                    nextSym();
                }
                //System.out.println(currentWord);
                save("INTCON",currentWord.toString());
                //saveWord = new Save.Word("INTCON",currentWord.toString(),line);
                //wordList.add(saveWord);
            }
            // 2 - 关键字 + 标识符 （字母或_开头，由字母/数字/_组成 = 标识符 ； 字母开头且只有字母 = 关键字）
            // 先不区分，拼好后检查vocab,有的话就是关键字，没有就是标识符
            else if((sym>='a' && sym<='z') || (sym>='A' && sym<='Z') || sym == '_'){
                //currentWord.append(sym);
                while((sym>='a' && sym<='z') || (sym>='A' && sym<='Z') || sym == '_' || isDigit(sym)){
                    currentWord.append(sym);
                    nextSym();
                }
                //System.out.println(currentWord);
                //检查是不是关键字

                if(Vocab.lexicalVocab.containsKey(currentWord.toString())){
                    String keyWord = Vocab.lexicalCode(currentWord.toString());
                    //System.out.println(keyWord);
                    save(keyWord,currentWord.toString());
                }
                else{
                    save("IDENFR",currentWord.toString());
                }
                //System.out.println(currentWord);
            }
            // 3- formatString （“” 开头和结尾）
            else if(sym == '"'){
                if(readStrAdy){
                    lexerWord word = save.CombineWord("STRCON");
                    System.out.println("Check word from CombineWord: " + word.content);
                    word.content += sym;
                    System.out.println("Check after combine \" " + word.content);
                    nextSym();
                }else{
                    currentWord.append(sym);
                    nextSym();
                    if(singleLineComment || multiLineComment)
                        continue;
                    while(sym != '"'){
                        currentWord.append(sym);
                        nextSym();
                    }
                    currentWord.append(sym);
                    nextSym();
                    System.out.println("Check end of strcon : " + sym);
                    save("STRCON",currentWord.toString());
                    readStrAdy = true;
                }
            }
            /* 4 - 注释 : / 开头 ， // 或 /* 或 除的/ （三种可能）
             * 开头 ， 注释结束 或 乘的 * （两种可能）
             */
            else if(sym == '/'){
                currentWord.append(sym);
                nextSym();
                if(sym == '/'){
                    if(!multiLineComment)
                        singleLineComment = true;
                }
                else if(sym == '*'){
                    if(!singleLineComment)
                        multiLineComment = true;
                }
                else{  //除
                    save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
                    continue;
                }
                nextSym();
            }
            else if(sym == '*'){
                currentWord.append(sym);
                nextSym();
                if(sym == '/'){
                    multiLineComment = false;
                }
                else{  //除
                    save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
                    continue;
                }
                nextSym();
            }
            // 5 - < 或 > 开头 ，有可能<= , >=
            else if(sym == '<' || sym == '>'){
                currentWord.append(sym);
                nextSym();
                if(sym == '='){
                    currentWord.append(sym);
                    nextSym();
                }
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
            }
            // 6 - &开头 - && / error
            else if(sym == '&'){
                currentWord.append(sym);
                nextSym();
                if(sym == '&'){
                    currentWord.append(sym);
                    nextSym();
                }
                else{
                    //error
                }
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
            }
            // 7 - |开头 - || / error
            else if(sym == '|'){
                currentWord.append(sym);
                nextSym();
                if(sym == '|'){
                    currentWord.append(sym);
                    nextSym();
                }
                else{
                    //error
                }
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
            }
            // 8 - !开头 : != / !
            else if(sym == '!'){
                currentWord.append(sym);
                nextSym();
                if(sym == '='){
                    currentWord.append(sym);
                    nextSym();
                }
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
            }
            // 9  - =开头 : == / =
            else if(sym == '='){
                currentWord.append(sym);
                nextSym();
                if(sym == '='){
                    currentWord.append(sym);
                    nextSym();
                }
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
            }
            // 10 - 单字符
            else if(sym == '+' || sym == '-' || sym == '%' || sym == '[' || sym == ']' || sym == '{' || sym == '}' || sym == '(' || sym == ')' || sym == ';' || sym == ','){
                currentWord.append(sym);
                save(Vocab.lexicalCode(currentWord.toString()),currentWord.toString());
                nextSym();
                readStrAdy = false;
            }
            else if(sym == '\n'){
                nextSym();
                singleLineComment = false;
            }
            else{
                //System.out.println("here:" + sym);
                if(sym == '\\'){
                    currentWord.append(sym);
                    save("\\",currentWord.toString());
                    //nextSym();
                }
                nextSym();
            }

            System.out.println("line: " + line + ",word: " + currentWord);
        }
        reader.close();
    }
}


/*
class Reader{
    public static BufferedReader reader;
    private static char currentChar;

    public Reader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("testfile.txt"));
    }

    public char nextSym() throws IOException {
        currentChar = (char) reader.read();
        return currentChar;
    }

    public static void closeFile() throws IOException {
        reader.close();
    }
}

 */
