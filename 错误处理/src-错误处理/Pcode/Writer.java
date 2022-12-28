package Pcode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer{
    static BufferedWriter writer;
    static BufferedWriter writelist;
    static ArrayList<String> pcodeList = new ArrayList<String>();

    public Writer() throws IOException {
        writer = new BufferedWriter(new FileWriter("pcodeList.txt"));
        writelist = new BufferedWriter(new FileWriter("pcodeList.txt"));
        //pcode.append(floor);
    }

    public static void write(String pcode) throws IOException {
        System.out.println("Write : " + pcode);
        if(pcode.length() > 0){
            pcodeList.add(pcode);
            pcode += '\n';
            writer.write(pcode);
            writer.flush();
        }
    }

    public static void showList() throws IOException {
        for(int i=0;i<pcodeList.size();i++){
            if(pcodeList.get(i) != null){
                writelist.write(pcodeList.get(i));
                writelist.flush();
            }
        }
    }

    public static String readPcode(int addr){
        if(addr <= pcodeList.size()){
            return pcodeList.get(addr - 1);
        }
        else{
            return null;
        }
    }
    public static int pcodeLength(){
        return pcodeList.size();
    }

    public static ArrayList<String> getPcodeList(){
        return pcodeList;
    }
}