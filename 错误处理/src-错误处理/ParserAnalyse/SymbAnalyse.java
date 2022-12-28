package ParserAnalyse;

import Pcode.Generator;
import Save.*;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;


public class SymbAnalyse {
    public static Word sym;
    public static Word scanSym;
    public static SaveContent save = SaveContent.getInstance();
    public static int listIndex = 0;   //add(listIndex, content)
    public TreeNode node;
    public static TreeNode firstNode = new TreeNode(new Word());
    public ArrayList<SymbAnalyse> childList;
    public SymbAnalyse parent;
    public boolean error = true;
    public static ArrayList<Word> parserList = new ArrayList<Word>();


    public SymbAnalyse(String typeCode,TreeNode parent){
        this.node = new TreeNode(new parserWord(typeCode));
        this.node.addNode(parent);
    }

    public static void nextSym(){
        //取出词法分析的wordList
        sym = save.getSym(listIndex);
        //parserList.add(sym);
        listIndex++;
        //System.out.println("listIndex:" + listIndex);
        if(sym != null){
            if(sym.content.equals("main")){
                if(!save.getSym(listIndex).content.equals("(")){
                    sym.typeCode = "IDENFR";
                }
            }
            System.out.println("sym:" + sym.content);
        }
    }

    public static void scanSym(int index){
        scanSym = save.getSym(index);
        //System.out.println("scanSym:" + sym.content);
    }

    public void insertList(){
        save.addParserWord(listIndex,this.node.node);
        listIndex++;
    }

    public static ArrayList<Word> getList(){
        return parserList;
    }

    public static void printTree() throws IOException {
        System.out.println("printTree");
        BufferedWriter writer = new BufferedWriter(new FileWriter("tree.txt"));
        TreeNode scanNode = firstNode;
        System.out.println(firstNode);
        scanTree(scanNode,writer);
    }

    public static void scanTree(TreeNode scanNode,BufferedWriter writer) throws IOException {
        String content = "";
        save.addTreeList(scanNode.node);
        for(int i=0;i<scanNode.child.size();i++){
            scanTree(scanNode.child.get(i),writer);
        }
    }
}
