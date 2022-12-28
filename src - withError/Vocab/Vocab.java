package Vocab;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Vocab {
    public static final Map<String,String> lexicalVocab = new HashMap<String,String>(){{
        put("Ident","IDENFR");
        put("IntConst","INTCON");
        put("FormatString","STRCON");
        put("main","MAINTK");
        put("const","CONSTTK");
        put("int","INTTK");
        put("break","BREAKTK");
        put("continue","CONTINUETK");
        put("if","IFTK");
        put("else","ELSETK");
        put("!","NOT");
        put("&&","AND");
        put("||","OR");
        put("while","WHILETK");
        put("getint","GETINTTK");
        put("printf","PRINTFTK");
        put("return","RETURNTK");
        put("+","PLUS");
        put("-","MINU");
        put("void","VOIDTK");
        put("*","MULT");
        put("/","DIV");
        put("%","MOD");
        put("<","LSS");
        put(">","GRE");
        put("<=","LEQ");
        put(">=","GEQ");
        put("==","EQL");
        put("!=","NEQ");
        put("=","ASSIGN");
        put(";","SEMICN");
        put(",","COMMA");
        put("(","LPARENT");
        put(")","RPARENT");
        put("[","LBRACK");
        put("]","RBRACK");
        put("{","LBRACE");
        put("}","RBRACE");
    }};

    public static String lexicalCode(String key){
        return lexicalVocab.get(key);
    }
}
