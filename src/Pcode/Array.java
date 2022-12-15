package Pcode;
import java.io.IOException;


//只有作为ConstExp或者Exp的Array才会来到这里，最外层不用找值而是指数组本身的array会在Param和VarDef的时候就append进Pcode
public class Array extends PcodeGenerator{
    public Array() throws IOException {
        //System.out.println("To Array Class.");
        //会用到这个Array的都是找值的，所以维度一定都有
        //currentWord应该是arrName IDENFR
        ignoreParser = true;
        if(currentWord.typeCode.equals("IDENFR")){
            pcode.append(currentWord.content);
            nextWord(); // [
            while(currentWord.content.equals("[")){
                pcode.append(" " + currentWord.content);  // append [

                nextWord();
                //inside DIM
                //System.out.println("检查取值Array DIM 里：" + currentWord.content);
                if(ArrayTestExp()){
                    //System.out.println("取值Array DIM 里是Exp");
                    ConstExp constExp = new ConstExp();
                    pcode.append(" " + varT);
                    pcode.append(" "+ currentWord.content);
                    ignoreParser = true;
                    nextWord();
                    //System.out.println("debug: " + currentWord.content);
                    //这边要确保出来的时候是 [ or leave array ,总之是 ] 的下一个 - 所以需要nextWord()
                }
                else{
                    //System.out.println("取值Array DIM 里没有Exp");
                    pcode.append(" " + currentWord.content);  //append var or value
                    nextWord(); // ]
                    pcode.append(" " + currentWord.content);
                    nextWord(); // [ or leave array
                }
            }
        }
        nextTVar();
        if(pcode.charAt(0) != ' '){
            pcode.insert(0," ");
        }
        writer.write(varT + " =" + pcode);
        ignoreParser = false;
    }

    public static boolean ArrayTestExp(){
        boolean hasFunc = false,hasExp = false,hasArray = false;
        int scanIndex = index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("RBRACK")) {
            //hasFunc
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LPARENT")){
                return true;
            }
            //hasExp
            if(scanWord.typeCode.equals("PLUS") || scanWord.typeCode.equals("MINU") || scanWord.typeCode.equals("MULT") || scanWord.typeCode.equals("DIV") || scanWord.typeCode.equals("MOD") || scanWord.typeCode.equals("NOT")){
                return true;
            }
            //hasArray
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LBRACK")){
                return true;
            }
            scanIndex++;
            scanWord(scanIndex);
        }
        //System.out.println("检查Exp返回false");
        return false;
    }
}
