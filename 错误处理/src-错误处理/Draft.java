import Pcode.Symbol.Symbol;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Draft {
    BufferedReader reader;
    String currentLine;
    int lineIndex = 0;

    public Draft() throws IOException {
        reader = new BufferedReader(new FileReader("draftfile.txt"));
        currentLine = reader.readLine();
        nextSym();
    }

    public void nextSym() throws IOException {
        while(currentLine!= null){
            if(lineIndex  == currentLine.length()){
                //System.out.println("hit");
                currentLine = reader.readLine();
                lineIndex = 0;
            }else{
                System.out.println(currentLine.charAt(lineIndex++));
            }
            //System.out.println("currentLine:" + currentLine);
        }
    }

    public static void main(String[] args) throws IOException {
        Draft draft = new Draft();
    }
    private static List<String> strToStrList(String str) {
        List<String> strList = new ArrayList<>();
        int begin=0,end=0,post=0,i=0;
        for (; i < str.length(); i++) {
            if (str.charAt(i) == '+' || str.charAt(i) == '-' || str.charAt(i) == '*' || str.charAt(i) == '%'
                    || str.charAt(i) == '/' || str.charAt(i) == '(' || str.charAt(i) == ')') {
                end = i;
                if (begin < end) {
                    strList.add(str.substring(begin, end));
                    strList.add(str.substring(i,i+1));
                }
                //连续符号时
                else {
                    strList.add(str.substring(i,i+1));
                }
                begin = i + 1;
            }
        }
        //如果没有if包裹，当最后一个字符为操作符时，List末尾会添加一个空字符串
        if (!"".equals(str.substring(begin, str.length()))){
            strList.add(str.substring(begin, str.length()));
        }
        return strList;
    }
    public static boolean isPlusMin(String ch){
        if(ch.equals("+") || ch.equals("-")){
            return true;
        }
        return false;
    }

    public static boolean isMulDiv(String ch){
        if(ch.equals("*")|| ch.equals("/") || ch.equals("%") ){
            return true;
        }
        return false;
    }
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void updateArr(content obj, int dimension1, int dimension2, int newValue) {
        int index = obj.dimension.get(1) * dimension1 + dimension2;
        //System.out.println(String.valueOf(obj.value.get(index)));
        obj.value.set(index,newValue);
    }
}

class content{
    String str;
    ArrayList<Integer> value = new ArrayList<Integer>();
    ArrayList<Integer> dimension = new ArrayList<Integer>();

    public content(){
        dimension.add(0);
        dimension.add(4);
    }

    public void print(){
        //System.out.println(value.toString());
    }
}

