package Pcode.Symbol;

public class Variable extends Symbol {
    int floor;
    //public int value;
    int type; //0-const ,1-normal

    public Variable(String name,int type) {
        super(name);
        this.type = type;
    }

    public void addValue(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
/*
class normalVar extends Variable{
    //int value;
    public normalVar(String name) {
        super(name);
    }
    public static normalVar getInstance(String name){
        normalVar newObj = new normalVar(name);
        return newObj;
    }
}

class constVar extends Variable{
    //int value;
    public constVar(String name, int value) {
        super(name);
        this.value = value;
    }

    public static constVar getInstance(String name,int value){
        constVar newObj = new constVar(name,value);
        return newObj;
    }
}

class paraVar extends Variable{
    //int value;

    public paraVar(String name) {
        super(name);
    }

    public static paraVar getInstance(String name){
        paraVar newObj = new paraVar(name);
        return newObj;
    }
}
 */