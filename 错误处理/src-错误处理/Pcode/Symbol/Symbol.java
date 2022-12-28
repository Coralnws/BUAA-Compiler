package Pcode.Symbol;

import Pcode.Generator;

import java.util.ArrayList;

public class Symbol {
    public String name;
    public int value;
    public ArrayList<Integer> valueList = new ArrayList<Integer>();
    public ArrayList<Integer> dimension = new ArrayList<Integer>();
    public boolean skipLevel = false;
    public int level;
    public int dimensionNum=0;
    public int type;

    public Symbol(String name){
        this.name = name;
        //Generator.putTable(name,this);
    }
}
