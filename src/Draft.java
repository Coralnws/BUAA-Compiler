import Pcode.Symbol.Symbol;

import javax.net.ssl.SSLContext;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Draft {
    BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"));

    public Draft() throws IOException {
        String str = "Hello!";
        str = str.substring(0,0);
        ////System.out.println(str);

    }

    public static void main(String[] args) throws IOException {
        //System.out.println(5&9);

        /*
        List<String> str = new ArrayList<>();
        str = strToStrList("+-+44*-3");
        //System.out.println(str);
        //System.exit(0);

        String str2 = "-43";
        String ch1,ch2;
        String newStr = "";

        int i = 0;
        boolean meetNum = false;
        boolean meetPlusMin = false;
        boolean meetMulDiv = false;
        boolean addBrac = false;
        int Lbrac = 0;
        int Rbrac = 0;

        ch1 = str.get(i);
        ch2 = str.get(i+1);
        while (i < str.size()) {
            //System.out.println("new round: ch:" + str.get(i));
            //System.out.println(ch1);
            ch1 = str.get(i);
            //System.out.println(ch1);
            if(isPlusMin(ch1)) {  //+-+3 , ch1+ ch2-
                //starting
                if (!meetNum && !meetPlusMin) {
                    //System.out.println("!meetNum && !meetPlusMin");
                    ch2 = str.get(i + 1);
                    if (isPlusMin(ch2)) {  //+- or -+  definitely only who plus min
                        if (ch1 != ch2) {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"-");
                            //System.out.println(str);
                            //str = '-' + str.substring(i + 2);
                            //newStr += '-';
                        } else {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"+");
                            //System.out.println(str);
                            //newStr += '+';
                        }
                        meetPlusMin = true;
                    } else if (isNumeric(String.valueOf(ch2))) { //-1 or +1
                        str.add(i+2,")");
                        str.add(i,"0");
                        str.add(i,"(");

                        //str = "(0" + ch1 + ch2 + ")" + str.substring(i + 2);
                        //newStr += "(0"+ch1+ch2+")";   //add zero and bracket
                        //System.out.println(str);
                        i += 5;
                        meetPlusMin = false;
                        meetNum = true;
                    }
                } else if (!meetNum && meetPlusMin) {  //+-+ kind
                    //System.out.println("!meetNum && meetPlusMin");
                    ch2 = str.get(i + 1);
                    //System.out.println("ch1:" + ch1 + " ch2:" + ch2);
                    if (isPlusMin(ch2)) {  //+- or -+  definitely only who plus min
                        if (ch1 != ch2) {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"-");
                            //System.out.println(str);
                            //newStr += '-';
                        } else {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"+");
                            //System.out.println(str);
                            //str = '+' + str.substring(i + 2);
                            //newStr += '+';
                        }
                        meetPlusMin = true;
                    } else if (isNumeric(String.valueOf(ch2))) { //-+-1 or +-+1
                        //System.out.println("ch1:" + ch1 + " ch2:" + ch2);
                        //System.out.println("isNumeric");
                        str.add(i+2,")");
                        str.add(i,"0");
                        str.add(i,"(");
                        //System.out.println(str);
                        //str = "(0" + ch1 + ch2 + ")" + str.substring(i + 2);
                        i += 5;
                        meetPlusMin = false;
                        meetNum = true;
                    }
                    else{
                        i++;
                    }
                }
                //middle
                else if (meetNum && !meetMulDiv && !meetPlusMin) {  //
                    //System.out.println("ch1:" + ch1);
                    //System.out.println("meetNum && !meetPlusMin");
                    ch2 = str.get(i + 1);
                    if (isNumeric(String.valueOf(ch2))) { //-+-1 or +-+1
                        //System.out.println("isNumeric");
                        i++;
                    }
                    else{
                        i++;
                        meetPlusMin = true;
                    }
                } else if (meetNum && meetPlusMin) {
                    //System.out.println("meetNum && meetPlusMin");
                    ch2 = str.get(i + 1);
                    //System.out.println("ch1:" + ch1 + " ch2:" + ch2);
                    if (isPlusMin(ch2)) {  //+- or -+  definitely only who plus min
                        if (ch1 != ch2) {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"-");
                            //newStr += '-';
                        } else {
                            str.remove(i);
                            str.remove(i);
                            str.add(i,"+");
                            //newStr += '+';
                        }
                        //meetPlusMin = true;
                    } else if (isNumeric(String.valueOf(ch2))) { //-+-1 or +-+1
                        //System.out.println("isNumeric");
                        str.add(i+2,")");
                        str.add(i,"0");
                        str.add(i,"(");
                        //System.out.println(str);
                        //str = "(0" + ch1 + ch2 + ")" + str.substring(i + 2);
                        i += 5;
                        if(i<str.size()){
                            //System.out.println("after jump 5 step ,ch :" + str.get(i));
                        }
                        meetPlusMin = false;
                        meetMulDiv = false;
                        //meetNum = true;
                    }
                    else{
                        i++;
                    }
                }
            } else if(isMulDiv(ch1)){
                ch2 = str.get(i+1);
                if(isPlusMin(ch2)){
                    //System.out.println("isMuldiv + isPlusMin");
                    str.add(i+1,"0");
                    str.add(i+1,"(");
                    addBrac = true;
                    Lbrac++;
                    i+=2;
                    meetNum = false;
                    meetPlusMin= false;
                    meetMulDiv = false;
                }
                else{
                    meetMulDiv = true;
                    i++;
                }
            } else if(isNumeric(String.valueOf(ch1))) {
                meetNum = true;
                i++;
            }else if(ch1.equals("(")){
                if(addBrac){
                    Lbrac++;
                }
                ch2 = str.get(i+1);
                if(isPlusMin(ch2)){
                    str.add(i+1,"0");
                }
                //System.out.println(str);
                i++;
                meetPlusMin = false;
                meetMulDiv = false;
                meetNum = false;
            }else if(ch1.equals(")")){
                if(addBrac){
                    Rbrac++;
                    if((Lbrac-Rbrac) == 1){
                        str.add(i+1,")");
                        addBrac = false;
                        Lbrac = 0;
                        Rbrac = 0;
                    }
                }
                i++;
            }
            else{
                i++;
            }
            if(i == str.size()){
                if(addBrac && ((Lbrac-Rbrac) == 1)){
                    str.add(i,")");
                }
            }
            //System.out.println(Lbrac+ " " + Rbrac);
        }
        //System.out.println(str.toString());
        String exp = String.join(",", str);
        exp = exp.replaceAll(",","");
        //System.out.println(exp);

        /*
        String[] a = {"var","v","a","=","1t"};

        String[] b = new String[a.length-4];

        for(int i=0,j=4;j<a.length;i++,j++) {
            b[i] = a[j];
        }
        //System.out.println(Arrays.toString(b));


         */



        /*
        Scanner in = new Scanner(//System.in);
        int value = 123;
String str = String.valueOf(value);
        //System.out.println(str);
        ////System.out.println(Integer.parseInt("123") +1 );
        content obj = new content();
        obj.value.add(1);
        obj.value.add(2);
        obj.value.add(3);
        obj.value.add(4);

        //a[3]
        updateArr(obj,0,1,10);
        obj.print();
        */

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
        ////System.out.println(String.valueOf(obj.value.get(index)));
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
        ////System.out.println(value.toString());
    }
}

