package Save;

import java.util.ArrayList;

public class parserWord extends Word {
    //如果要记录每个语法成分里面有哪些token的话，就设一个Arraylist<Save.Word> contentList
    public ArrayList<Word> childList;
    public parserWord(String typeCode){
        this.typeCode = typeCode;
    }

    public void addChild(Word child){
        this.childList.add(child);
    }
}