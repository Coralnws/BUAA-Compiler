package ParserAnalyse;

import Save.*;

import java.io.IOException;

//CompUnit → {Decl} {FuncDef} MainFuncDef
public class CompUnit extends SymbAnalyse{
    public CompUnit(){
        super("<CompUnit>",firstNode);
        //System.out.println("Start <CompUnit>");

        nextSym(); //开始读
        while(scanAhead() == ParserType.Decl){
            Decl decl = new Decl(this.node);
        }
        while(scanAhead() == ParserType.FuncDef){
            FuncDef funcDef = new FuncDef(this.node);
        }
        if(scanAhead() == ParserType.MainFuncDef){
            MainFuncDef mainFuncDef = new MainFuncDef(this.node);
        }

        //放进输出的wordlist里面
        /*
        save.addParserWord(listIndex,this.node.node);
        listIndex++;
         */
        //System.out.println("Printout <CompUnit>");
        parserList.add(this.node.node);
    }

    public static ParserType scanAhead(){
        //System.out.println("Start CompUnit.scanAhead()");
        scanSym(listIndex-1);
        if(scanSym.content.equals("const")){
            //System.out.println("scanAhead return Decl");
            return ParserType.Decl;
        }
        else if(scanSym.content.equals("void")){
            //System.out.println("scanAhead return FuncDef");
            return ParserType.FuncDef;
        }
        else if(scanSym.content.equals("int")){
            scanSym(listIndex);
            if(scanSym.content.equals("main")){
                scanSym(listIndex+1);
                if(scanSym.content.equals("(")){
                    //System.out.println("scanAhead return MainFuncDef");
                    return ParserType.MainFuncDef;
                }
                else if(scanSym.content.equals("=")){
                    //System.out.println("scanAhead return Decl");
                    return ParserType.Decl;
                }
            }
            else{
                scanSym(listIndex+1);
                if(scanSym.content.equals("(")){  //函数
                    //System.out.println("scanAhead return FuncDef");
                    return ParserType.FuncDef;
                }
                else{
                    //System.out.println("scanAhead return Decl");
                    return ParserType.Decl;
                }
            }
        }
        return ParserType.ERROR;
    }
}
