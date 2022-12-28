package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Error.Error;
import Error.ErrorRecord;
import Save.Word;
import Save.lexerWord;


/*
Stmt → LVal '=' Exp ';'
| [Exp] ';'
| Block
| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
| 'while' '(' Cond ')' Stmt
| 'break' ';' | 'continue' ';'
| 'return' [Exp] ';'
| LVal '=' 'getint''('')'';'
| 'printf''('FormatString{','Exp}')'';'
 */
public class Stmt extends SymbAnalyse{
    Word word;
    Error error;
    public Stmt(TreeNode parent){
        super("<Stmt>",parent);
        System.out.println("start <stmt>");
        boolean run = false;

        System.out.println("In Stmt : sym is " + sym.content);
        //1 - if
        if(sym.content.equals("if")){
            run = true;
            parserList.add(sym);
            TreeNode IfNode = new TreeNode(sym);
            IfNode.addNode(this.node);
            nextSym();

            //1.1
            if(sym.content.equals("(")){
                parserList.add(sym);
                TreeNode LParentNode = new TreeNode(sym);
                LParentNode.addNode(this.node);
                nextSym();

                //1.2
               Cond cond = new Cond(this.node);

               //1.3
               if(sym.content.equals(")")){
                   parserList.add(sym);
                   sym.typeCode = "CondEnd";
                   TreeNode RParentNode = new TreeNode(sym);
                   RParentNode.addNode(this.node);
                   nextSym();

                   //1.4
                   Stmt stmt = new Stmt(this.node);

                   //1.5
                   if(sym.content.equals("else")){
                       parserList.add(sym);
                       TreeNode elseNode = new TreeNode(sym);
                       elseNode.addNode(this.node);
                       nextSym();

                       //1.6
                       Stmt stmt2 = new Stmt(this.node);

                       //insertList();
                       System.out.println("Printout <stmt>");
                       parserList.add(this.node.node);
                   }
                   else{
                       System.out.println("Printout <stmt>");
                       parserList.add(this.node.node);
                   }
               }else{
                   word = save.getSym(listIndex-2);
                   System.out.println("Check word is what ,should sth before ):" + word.content);
                   error = new Error(word.line,'j');
                   PcodeGenerator.errorRecord.addError(error);
                   word = new lexerWord("CondEnd",")", word.line);
                   TreeNode RParentNode = new TreeNode(word);
                   RParentNode.addNode(this.node);
                   parserList.add(word);

                   System.out.println("Check sym is what:" + sym.content);
                   //1.4
                   Stmt stmt = new Stmt(this.node);

                   //1.5
                   if(sym.content.equals("else")){
                       parserList.add(sym);
                       TreeNode elseNode = new TreeNode(sym);
                       elseNode.addNode(this.node);
                       nextSym();

                       //1.6
                       Stmt stmt2 = new Stmt(this.node);

                       //insertList();
                       System.out.println("Printout <stmt>");
                       parserList.add(this.node.node);
                   }
                   else{
                       System.out.println("Printout <stmt>");
                       parserList.add(this.node.node);
                   }
               }
           }
        }

        //2 - while
        else if(sym.content.equals("while")){
            run = true;
            parserList.add(sym);
            TreeNode whileNode = new TreeNode(sym);
            whileNode.addNode(this.node);
            nextSym();
            //2.1
            if(sym.content.equals("(")){
                parserList.add(sym);
                TreeNode LParentNode = new TreeNode(sym);
                LParentNode.addNode(this.node);
                nextSym();
                //2.2
                Cond cond = new Cond(this.node);
                //2.3
                if(sym.content.equals(")")){
                    parserList.add(sym);
                    sym.typeCode = "CondEnd";
                    TreeNode RParentNode = new TreeNode(sym);
                    RParentNode.addNode(this.node);
                    nextSym();
                    //2.4
                    Stmt stmt = new Stmt(this.node);
                    System.out.println("Printout <stmt>");
                    parserList.add(this.node.node);
                }else{
                    word = save.getSym(listIndex-2);
                    System.out.println("Check word is what ,should sth before ):" + word.content);
                    error = new Error(word.line,'j');
                    PcodeGenerator.errorRecord.addError(error);
                    word = new lexerWord("CondEnd",")", word.line);
                    TreeNode RParentNode = new TreeNode(word);
                    RParentNode.addNode(this.node);
                    parserList.add(word);

                    System.out.println("Check sym is what:" + sym.content);
                    Stmt stmt = new Stmt(this.node);
                    System.out.println("Printout <stmt>");
                    parserList.add(this.node.node);
                }
            }
            //parserList.add(this.node.node);
        }

        //3 - break
        else if(sym.content.equals("break")){
            run = true;
            parserList.add(sym);
            TreeNode breakNode = new TreeNode(sym);
            breakNode.addNode(this.node);
            nextSym();
            //3.1
            if(sym.content.equals(";")){
                parserList.add(sym);
                TreeNode semicnNode = new TreeNode(sym);
                semicnNode.addNode(this.node);
                nextSym();

                //insertList();
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }else{
                Word word = save.getSym(listIndex-2);
                System.out.println("Check word is what:" + word.content);
                Error error = new Error(word.line,'i');
                PcodeGenerator.errorRecord.addError(error);
                word = new lexerWord("SEMICN",",", word.line);
                TreeNode semicnNode = new TreeNode(word);
                semicnNode.addNode(this.node);

                System.out.println("Check sym is what:" + sym.content);
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }
        }

        //4 - continue;
        else if(sym.content.equals("continue")){
            run = true;
            parserList.add(sym);
            TreeNode continueNode = new TreeNode(sym);
            continueNode.addNode(this.node);
            nextSym();
            //4.1
            if(sym.content.equals(";")){
                parserList.add(sym);
                TreeNode semicnNode = new TreeNode(sym);
                semicnNode.addNode(this.node);
                nextSym();

                //insertList();
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);

            }else{
                Word word = save.getSym(listIndex-2);
                System.out.println("Check word is what:" + word.content);
                Error error = new Error(word.line,'i');
                PcodeGenerator.errorRecord.addError(error);
                word = new lexerWord("SEMICN",",", word.line);
                TreeNode semicnNode = new TreeNode(word);
                semicnNode.addNode(this.node);

                System.out.println("Check sym is what:" + sym.content);
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }
        }

        //5 - return
        else if(sym.content.equals("return")){
            run = true;
            parserList.add(sym);
            TreeNode returnNode = new TreeNode(sym);
            returnNode.addNode(this.node);
            nextSym();
            //5.1
            if(AddExp.scanAhead() != ParserType.ERROR){
                Exp exp = new Exp(this.node);
            }
            //5.2
            if(sym.content.equals(";")){
                parserList.add(sym);
                TreeNode semicnNode = new TreeNode(sym);
                semicnNode.addNode(this.node);
                nextSym();

                //insertList();
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }else{
                Word word = save.getSym(listIndex-2);
                System.out.println("Check word is what:" + word.content);
                Error error = new Error(word.line,'i');
                PcodeGenerator.errorRecord.addError(error);
                word = new lexerWord("SEMICN",",", word.line);
                TreeNode semicnNode = new TreeNode(word);
                semicnNode.addNode(this.node);

                System.out.println("Check sym is what:" + sym.content);
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }
        }

        //6 - printf
        else if(sym.typeCode.equals("PRINTFTK")){
            run = true;

            parserList.add(sym);
            TreeNode printfNode = new TreeNode(sym);
            printfNode.addNode(this.node);
            nextSym();
            //6.1
            if (sym.content.equals("(")){
                parserList.add(sym);
                TreeNode LParentNode = new TreeNode(sym);
                LParentNode.addNode(this.node);
                nextSym();
                //6.2
                if(sym.typeCode.equals("STRCON")){
                    parserList.add(sym);
                    TreeNode stringNode = new TreeNode(sym);
                    stringNode.addNode(this.node);
                    nextSym();
                    //6.3
                    while(sym.content.equals(",")){
                        parserList.add(sym);
                        TreeNode commaNode = new TreeNode(sym);
                        commaNode.addNode(this.node);
                        nextSym();
                        //6.4
                        Exp exp = new Exp(this.node); //compulsory
                    }
                    //6.5
                    if (sym.content.equals(")")){
                        parserList.add(sym);
                        sym.typeCode = "PRINTEND";
                        TreeNode RParentNode = new TreeNode(sym);
                        RParentNode.addNode(this.node);
                        nextSym();
                        //6.6
                        if(sym.content.equals(";")){
                            parserList.add(sym);
                            TreeNode semicnNode = new TreeNode(sym);
                            semicnNode.addNode(this.node);
                            nextSym();

                            //insertList();
                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);

                        }else{
                            Word word = save.getSym(listIndex-2);
                            System.out.println("Check word is what:" + word.content);
                            Error error = new Error(word.line,'i');
                            PcodeGenerator.errorRecord.addError(error);
                            word = new lexerWord("SEMICN",",", word.line);
                            TreeNode semicnNode = new TreeNode(word);
                            semicnNode.addNode(this.node);

                            System.out.println("Check sym is what:" + sym.content);
                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);
                        }

                    }else{
                        word = save.getSym(listIndex-2);
                        System.out.println("Check word is what ,should sth before ):" + word.content);
                        error = new Error(word.line,'j');
                        PcodeGenerator.errorRecord.addError(error);
                        word = new lexerWord("PRINTEND",")", word.line);
                        TreeNode RParentNode = new TreeNode(word);
                        RParentNode.addNode(this.node);
                        parserList.add(word);

                        System.out.println("Check sym is what:" + sym.content);
                        if(sym.content.equals(";")){
                            parserList.add(sym);
                            TreeNode semicnNode = new TreeNode(sym);
                            semicnNode.addNode(this.node);
                            nextSym();

                            //insertList();
                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);

                        }else{
                            word = save.getSym(listIndex-2);
                            System.out.println("Check word is what:" + word.content);
                            error = new Error(word.line,'i');
                            PcodeGenerator.errorRecord.addError(error);
                            word = new lexerWord("SEMICN",",", word.line);
                            TreeNode semicnNode = new TreeNode(word);
                            semicnNode.addNode(this.node);

                            System.out.println("Check sym is what:" + sym.content);
                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);
                        }
                    }
                }

            }
            System.out.println("quit printf" + " sym:" + sym.content);
        }

        // 7 - Block : need scan - start with {
        else if(sym.content.equals("{")){
            System.out.println("BLOCK IN STMT");
            run = true;
            Block block = new Block(this.node);

            //this.insertList();
            System.out.println("Printout <stmt>");
            parserList.add(this.node.node);
        }

        //8 - LVal
        else if(AddExp.scanAhead() == ParserType.LVal){
            boolean isAssign = this.scanAssign();
            run = true;
            System.out.println("Exp in stmt");

            if(isAssign){
                LVal lVal = new LVal(this.node);
                if(sym.content.equals("=")){
                    parserList.add(sym);
                    TreeNode assignNode = new TreeNode(sym);
                    assignNode.addNode(this.node);
                    nextSym();

                    if(sym.typeCode.equals("GETINTTK")){
                        parserList.add(sym);
                        TreeNode getintNode = new TreeNode(sym);
                        getintNode.addNode(this.node);
                        nextSym();

                        if(sym.content.equals("(")){
                            parserList.add(sym);
                            TreeNode LParentNode = new TreeNode(sym);
                            LParentNode.addNode(this.node);
                            nextSym();

                            if(sym.content.equals(")")){
                                parserList.add(sym);
                                TreeNode RParentNode = new TreeNode(sym);
                                RParentNode.addNode(this.node);
                                nextSym();

                                if(sym.content.equals(";")){
                                    parserList.add(sym);
                                    TreeNode semicnNode = new TreeNode(sym);
                                    semicnNode.addNode(this.node);
                                    nextSym();

                                    // this.insertList();
                                    System.out.println("Printout <stmt>");
                                    parserList.add(this.node.node);
                                }else{
                                    Word word = save.getSym(listIndex-2);
                                    System.out.println("Check word is what:" + word.content);
                                    Error error = new Error(word.line,'i');
                                    PcodeGenerator.errorRecord.addError(error);
                                    word = new lexerWord("SEMICN",",", word.line);
                                    TreeNode semicnNode = new TreeNode(word);
                                    semicnNode.addNode(this.node);

                                    System.out.println("Check sym is what:" + sym.content);
                                    System.out.println("Printout <stmt>");
                                    parserList.add(this.node.node);
                                }
                            }
                        }
                    }
                    else{
                        Exp exp = new Exp(this.node);

                        if(sym.content.equals(";")){
                            parserList.add(sym);
                            TreeNode semicnNode = new TreeNode(sym);
                            semicnNode.addNode(this.node);
                            nextSym();

                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);
                        }else{
                            Word word = save.getSym(listIndex-2);
                            System.out.println("Check word is what:" + word.content);
                            Error error = new Error(word.line,'i');
                            PcodeGenerator.errorRecord.addError(error);
                            word = new lexerWord("SEMICN",",", word.line);
                            TreeNode semicnNode = new TreeNode(word);
                            semicnNode.addNode(this.node);

                            System.out.println("Check sym is what:" + sym.content);
                            System.out.println("Printout <stmt>");
                            parserList.add(this.node.node);
                        }
                    }
                }
            }
            else{
                Exp exp = new Exp(this.node);

                if(sym.content.equals(";")){
                    parserList.add(sym);
                    TreeNode semicnNode = new TreeNode(sym);
                    semicnNode.addNode(this.node);
                    nextSym();

                    System.out.println("Printout <stmt>");
                    parserList.add(this.node.node);
                }else{
                    Word word = save.getSym(listIndex-2);
                    System.out.println("Check word is what:" + word.content);
                    Error error = new Error(word.line,'i');
                    PcodeGenerator.errorRecord.addError(error);
                    word = new lexerWord("SEMICN",",", word.line);
                    TreeNode semicnNode = new TreeNode(word);
                    semicnNode.addNode(this.node);

                    System.out.println("Check sym is what:" + sym.content);
                    System.out.println("Printout <stmt>");
                    parserList.add(this.node.node);
                }
            }
        }
        //9 [Exp] ;

        else if(AddExp.scanAhead() != ParserType.ERROR){
            System.out.println("exp in stmt");
            run = true;
            Exp exp = new Exp(this.node);

            if(sym.content.equals(";")){
                parserList.add(sym);
                TreeNode semicnNode = new TreeNode(sym);
                semicnNode.addNode(this.node);
                nextSym();

                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }else{
                Word word = save.getSym(listIndex-2);
                System.out.println("Check word is what:" + word.content);
                Error error = new Error(word.line,'i');
                PcodeGenerator.errorRecord.addError(error);
                word = new lexerWord("SEMICN",",", word.line);
                TreeNode semicnNode = new TreeNode(word);
                semicnNode.addNode(this.node);

                System.out.println("Check sym is what:" + sym.content);
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }
        }

        else if(sym.content.equals(";")){
            parserList.add(sym);
            TreeNode semicnNode = new TreeNode(sym);
            semicnNode.addNode(this.node);
            nextSym();

            System.out.println("Printout <stmt>");
            parserList.add(this.node.node);
        }

        if(!run){
            System.out.println("Not Match" + " sym: " +sym.content );
            System.out.println("Not Match line: " + (listIndex));
        }

    }

    public boolean scanAssign(){
        int index = listIndex;
        scanSym(index);
        while(!scanSym.content.equals(";")){
            if(scanSym.content.equals("=")){
                return true;
            }
            index += 1;
            scanSym(index);
        }
        return false;
    }
}

/*
Stmt → LVal '=' Exp ';'
| [Exp] ';'
| Block
| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
| 'while' '(' Cond ')' Stmt
| 'break' ';'
| 'continue' ';'
| 'return' [Exp] ';'
| LVal '=' 'getint''('')'';'
| 'printf''('FormatString { ',' Exp } ')' ';'
 */