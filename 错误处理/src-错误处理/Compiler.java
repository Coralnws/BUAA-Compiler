import java.io.*;

import Pcode.PcodeGenerator;
import Save.*;
import Parser.*;
import Lexer.*;
import Pcode.Writer;


public class Compiler {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        lexer.start();
        //SaveContent.print();
        Parser parser = new Parser();
        parser.start();
        //SaveContent.print();
        System.out.println("End");
        PcodeGenerator pcodeGenerator = new PcodeGenerator();
        pcodeGenerator.generate();

    }
}


