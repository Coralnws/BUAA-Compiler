package Pcode.Symbol;

import java.util.ArrayList;
import java.util.Stack;

public class Symbol {
    public String name;
    public int value;
    public ArrayList<Integer> valueList = new ArrayList<Integer>();
    public ArrayList<Integer> dimension = new ArrayList<Integer>();
    public int dimensionNum;
    public boolean skipLevel = false;
    public int level;
    public Stack<Integer> levelStack = new Stack<Integer>();
    public int type;

    public Symbol(String name){
        this.name = name;
        //Generator.putTable(name,this);
    }
}
