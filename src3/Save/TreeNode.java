package Save;

import java.util.ArrayList;

public class TreeNode {  //语法树
    public Word node;  //节点本身
    public ArrayList<TreeNode> child; //指向子节点
    public TreeNode parent; //指向父节点

    public TreeNode(Word node){
        this.node = node;
        this.child = new ArrayList<TreeNode>();
    }

    public void addChild(TreeNode child){
        this.child.add(child);
        //addParent(this);
    }
    public void addParent(TreeNode parent){
        this.parent = parent;
    }

    public void addNode(TreeNode parent){
        if(parent != null){
            parent.addChild(this);
            this.addParent(parent);
        }
    }
}
