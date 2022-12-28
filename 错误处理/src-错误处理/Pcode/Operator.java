package Pcode;

import java.util.HashMap;
import java.util.Map;

public class Operator {
    public static final Map<String,Integer> priority = new HashMap<String,Integer>(){{
        put("+",1);
        put("-",1);
        put("*",2);
        put("/",2);
        put("%",2);
    }};

    public static int getPrior(String key){
        return priority.get(key);
    }
}
