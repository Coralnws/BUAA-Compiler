package Pcode.Symbol;

import java.util.ArrayList;

public class paraArr extends Para{
    //public ArrayList<Integer> dimension = new ArrayList<Integer>();
    public Symbol content;

    public paraArr(String name) {
        super(name);
    }

    public void addDimension(int value){
        dimension.add(value);
    }

    public void setContent(Symbol symb){
        this.content = symb;
    }
}