package Pcode.Symbol;

import java.util.ArrayList;

public class Func extends Symbol {
    public int addrS,addrE,floor;  //index in wordlist
    //TYPE : 0-void , 1-int
    public ArrayList<Para> paraList = new ArrayList<Para>();
    public int paraNum = 0;
    public int pt=-1;

    public Func(String name,int type){
        super(name);
        this.type = type;
    }

    public void addPara(Para para){
        paraList.add(para);
        pt = paraNum;
        paraNum++;
        //System.out.println("Here to check para in func:" + paraList.get(0));
    }

    public Para getPara(){
        if(pt>=0){
            //System.out.println(paraList.get(pt));
            return paraList.get(pt--);
        }
        else{
            return null;
        }
    }

    public void recoverPt(){
        pt = paraNum-1;
    }

    public Para getSpefPara(int index){
        if(index < paraList.size()){
            return paraList.get(index);
        }else{
            return null;
        }

    }
}
