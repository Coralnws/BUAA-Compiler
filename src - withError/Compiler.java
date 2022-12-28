import java.io.*;

import Executor.Executor;
import Pcode.PcodeGenerator;
import Save.*;
import Parser.*;
import Lexer.*;
import Pcode.Writer;


public class Compiler {
    public static boolean testError=false;
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        //lexer.testError=true;
        lexer.start();
        //SaveContent.print();
        Parser parser = new Parser();
        parser.start();
        //SaveContent.print();
        ////System.out.println("End");
        PcodeGenerator pcodeGenerator = new PcodeGenerator();
        pcodeGenerator.generate();
        //Writer.showList();

        Executor executor = new Executor();
        executor.execute();
    }
}


