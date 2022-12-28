package Error;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ErrorRecord {
    static ArrayList<Error> errorList = new ArrayList<Error>();

    public static void addError(Error error){
        int line = error.line;
        Error error1;
        for(int i=0;i<errorList.size();i++){
            error1 = errorList.get(i);
            if(error1.line == line){
                return;
            }
        }
        errorList.add(error);
    }

    public static void print() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("error.txt"));
        sortErrorList();
        for(int i=0;i<errorList.size();i++){
            Error error = errorList.get(i);
            writer.write(error.line + " " + error.errorType + '\n');
            writer.flush();
        }
    }

    public static Comparator<Error> errorLine = new Comparator<Error>() {

        public int compare(Error s1, Error s2) {

            int rollno1 = s1.line;
            int rollno2 = s2.line;

            /*For ascending order*/
            return rollno1-rollno2;

            /*For descending order*/
            //rollno2-rollno1;
        }};

    public static void sortErrorList(){
        Collections.sort(errorList, errorLine);
    }
    public static void deleteSame(){

    }
}
