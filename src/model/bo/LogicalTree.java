package model.bo;

import java.util.ArrayList;
import java.util.Hashtable;

public class LogicalTree {
    Node logicalTree;
    ArrayList<Node> physicalTrees;

    public Node getLogicalTree() {
        return logicalTree;
    }

    public void setPhysicalTrees(ArrayList<Node> physicalTrees) {
        this.physicalTrees = physicalTrees;
    }

    public ArrayList<Node> getPhysicalTrees() {
        return physicalTrees;
    }

    public LogicalTree() {
    }

    public LogicalTree(Node logicalTree) {
        this.logicalTree = logicalTree;
    }
}
