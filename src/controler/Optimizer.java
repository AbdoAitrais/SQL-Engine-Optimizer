package controler;

import model.bo.LogicalTree;
import model.bo.Node;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class Optimizer {
    Estimator estimator;
    Transformer transformer;
    double minMatValue;
    double minPipeValue;
    Node minLogical;
    Node minPhysical;

    public double getMinMatValue() {
        return minMatValue;
    }

    public double getMinPipeValue() {
        return minPipeValue;
    }

    public Node getMinLogical() {
        return minLogical;
    }

    public Node getMinPhysical() {
        return minPhysical;
    }

    public Optimizer(Estimator estimator, Transformer transformer){
        this.estimator = estimator;
        this.transformer = transformer;
    }
    public void calculateOptimalTree(){
        minMatValue = Double.MAX_VALUE;
        for (LogicalTree logicalTree: transformer.logicalTrees){
            Hashtable<Node,Double> pt = estimator.calculateCostMaterialization(logicalTree);

            double min = Collections.min(pt.values());
            System.out.println(min);
            if (min < minMatValue){
                minMatValue = min;
                minLogical = logicalTree.getLogicalTree();
                for (Map.Entry<Node, Double> entry : pt.entrySet()) {
                    if (entry.getValue().equals(min)) {
                        minPhysical = entry.getKey();
                        break; // stop searching once we find the first key associated with the value
                    }
                }
            }
        }

        minPipeValue = Double.MAX_VALUE;
        for (LogicalTree logicalTree: transformer.logicalTrees){
            Hashtable<Node,Double> pt = estimator.calculateCostPipelinage(logicalTree);

            double min = Collections.min(pt.values());
            System.out.println(min);
            if (min < minPipeValue){
                minPipeValue = min;
                minLogical = logicalTree.getLogicalTree();
                for (Map.Entry<Node, Double> entry : pt.entrySet()) {
                    if (entry.getValue().equals(min)) {
                        minPhysical = entry.getKey();
                        break; // stop searching once we find the first key associated with the value
                    }
                }
            }
        }
    }
}
