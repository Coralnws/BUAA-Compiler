package Pcode.Symbol;

import java.util.ArrayList;

public class Arr extends Symbol{
    //public ArrayList<Integer> dimension = new ArrayList<Integer>();
    //public ArrayList<Integer> valueList = new ArrayList<Integer>();
    //int type; //0-const , 1-normal
    //int dimensionNum;

    public Arr(String name,int type) {
        super(name);
        this.type = type;
    }

    public void addValue(int value){
        valueList.add(value);
    }
    public void addDimension(int value){
        dimension.add(value);
    }
    public void setSpace(){
        int value = dimension.get(0) * dimension.get(1) + dimension.get(1);
        for(int i =0;i<=value;i++){
            valueList.add(0);
        }
    }
    public int getValue(){
        return valueList.get(1);
    }
}

/*
class constArr extends Arr{

    public constArr(String name,int dimension1,int dimension2,ArrayList<Integer> value) {
        super(name, dimension1,dimension2,value);
    }

    public static constArr getInstance(String name,int dimension1,int dimension2,ArrayList<Integer> value){
        constArr newObj = new constArr(name, dimension1,dimension2,value);
        return newObj;
    }
}

class varArr extends Arr{

    public varArr(String name,int dimension1,int dimension2,ArrayList<Integer> value) {
        super(name, dimension1,dimension2,value);
    }

    public static varArr getInstance(String name,int dimension1,int dimension2,ArrayList<Integer> value){
        varArr newObj = new varArr(name, dimension1,dimension2,value);
        return newObj;
    }
}

class paraArr extends Arr{

    public paraArr(String name,int dimension1,int dimension2,ArrayList<Integer> value) {
        super(name, dimension1,dimension2,value);
    }

    public static paraArr getInstance(String name,int dimension1,int dimension2,ArrayList<Integer> value){
        paraArr newObj = new paraArr(name, dimension1,dimension2,value);
        return newObj;
    }
}
 */