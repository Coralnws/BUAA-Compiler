package Pcode;

import java.io.IOException;
import Error.Error;
import Error.ErrorRecord;
/**  printf("a=%d\n",r);
 * <BlockItem>
 * <Stmt>
 * printf
 * (
 * "a=%d\n"
 * ,
 * <Exp>
 * <AddExp>
 * <MulExp>
 * <UnaryExp>
 * <PrimaryExp>
 * <LVal>
 * r
 * )
 * ;
 */
public class Print extends PcodeGenerator{
    int strnum = 1;
    int insnum = 1;
    int anwnum = 1;
    int lnnum = 1;

    public Print() throws IOException {
        //currentWord 应该是 printf
        System.out.println("Check current is printf : "+ currentWord.typeCode);
        nextWord();  //(
        nextWord();  //strcon
        System.out.println("Check current is strcon : "+ currentWord.content);
        String str = currentWord.content;
        char ch;

        for(int i = 1; i<str.length()-1 ;i++){  //skip过 “ ”
            ch = str.charAt(i);
            if(ch == '%'){
                if(str.charAt(++i) == 'd'){ //数字
                    if(pcode.length() > 0 ){
                        pcode.insert(0,getStrNum() + " =");
                        writer.write(String.valueOf(pcode));
                        pcode.delete( 0, pcode.length() );
                    }
                    pcode.append(getInsNum());
                    writer.write(String.valueOf(pcode));
                    pcode.delete( 0,pcode.length() );
                }else{
                    Error error = new Error(currentWord.line,'a');
                    errorRecord.addError(error);
                }
            }
            else if(ch == '\\'){
                if(str.charAt(++i) == 'n'){
                    if(pcode.length() > 0 ){
                        pcode.insert(0,getStrNum() + " =");
                        writer.write(String.valueOf(pcode));
                        pcode.delete( 0, pcode.length() );
                    }
                    pcode.append(getLnNum());
                    writer.write(String.valueOf(pcode));
                    pcode.delete( 0,pcode.length() );
                }else{
                    Error error = new Error(currentWord.line,'a');
                    errorRecord.addError(error);
                }
            }else if(!(ch == 32 || ch == 33 || (ch>=40 && ch<=126))){
                System.out.println("STRCON error");
                Error error = new Error(currentWord.line,'a');
                errorRecord.addError(error);
            }
            else{
                pcode.append(ch);
            }
        }
        if(pcode.length() > 0 ){
            pcode.insert(0,getStrNum() + " =");
            writer.write(String.valueOf(pcode));
            pcode.delete( 0, pcode.length() );
        }
        //处理完了strcon
        ignoreParser = true;
        nextWord(); //, or )

        if(currentWord.content.equals(",")){
            nextWord(); // Exp内容
            while(!currentWord.typeCode.equals("PRINTEND")){
                if(!currentWord.content.equals(",") && !currentWord.typeCode.equals("PRINTEND")){  //分割每一个para
                    if(PrintTestExp()){
                        System.out.println("Print 有Exp");
                        Exp exp = new Exp();
                        pcode.append(" " + varT);
                        if(pcode.length() > 0 ){
                            pcode.insert(0,getAnwNum() + " =");
                            writer.write(String.valueOf(pcode));
                            pcode.delete( 0, pcode.length() );
                        }
                    }else{
                        System.out.println("Print 没有Exp");
                        pcode.append(currentWord.content);
                        nextWord();
                    }
                }
                else{
                    if(pcode.length() > 0){
                        pcode.insert(0,getAnwNum() + " = ");
                        writer.write(String.valueOf(pcode));
                        pcode.delete( 0, pcode.length() );
                    }
                    ignoreParser = true;
                    nextWord();
                }
            }
            if(pcode.length() > 0){
                System.out.println("last");
                pcode.insert(0,getAnwNum() + " = ");
                writer.write(String.valueOf(pcode));
                pcode.delete( 0, pcode.length() );
            }
        }

        if(insnum != anwnum){
            Error error = new Error(currentWord.line,'l');
            errorRecord.addError(error);
        }

        writer.write("PRINTF");
        ignoreParser = false;
        System.out.println("Check current is ) : " + currentWord.content);
        nextWord();
        System.out.println("Check current is ; : " + currentWord.content);
        nextWord();



    }

    String getStrNum(){
        String str = "str-"+strnum;
        strnum++;
        return str;
    }

    String getInsNum(){
        String str = "ins-"+insnum;
        insnum++;
        return str;
    }
    String getAnwNum(){
        String str = "anw-"+anwnum;
        anwnum++;
        return str;
    }

    String getLnNum(){
        String str = "ln-"+lnnum;
        lnnum++;
        return str;
    }

    public static boolean PrintTestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("PRINTEND") && !scanWord.typeCode.equals("COMMA")) {
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
            scanIndex ++;
            scanWord(scanIndex);
        }
        return false;
    }

}
