package Parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ParserAnalyse.CompUnit;
import ParserAnalyse.SymbAnalyse;
import Save.*;

public class Parser {
    private Word sym;
    public static SaveContent save = SaveContent.getInstance();
    private static int listIndex = 0;   //add(listIndex, content)
    public static boolean testError = false;


    public void nextSym() throws IOException {
        //取出词法分析的wordList
        sym = save.getSym(listIndex);
    }

    public void start() throws IOException {
        System.out.println("Start Parser");
        CompUnit compUnit = new CompUnit();
        //printParser();
        SymbAnalyse.printTree();
        SymbAnalyse.save.printTree();
    }

    public static void printParser() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        String content = "";
        for (int i = 0; i < SymbAnalyse.parserList.size(); i++) {
            Word word = SymbAnalyse.parserList.get(i);
            if (word instanceof lexerWord) {
                /*
                if (word.content.equals("main")) {
                    word.typeCode = "MAINTK";
                }
                */
                content = word.typeCode + " " + word.content + '\n';
            }
            if (word instanceof parserWord) {
                content = word.typeCode + '\n';
            }
            writer.write(content);
            writer.flush();
        }
    }
}
