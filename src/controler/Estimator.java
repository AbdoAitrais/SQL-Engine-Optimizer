package controler;

import model.bo.Jointure;
import model.bo.LogicalTree;
import model.bo.Node;
import model.bo.Selection;
import model.dictionnary.Dictionnary;
import model.dictionnary.Entity;
import model.utilities.Algorithms;

import java.util.Hashtable;

public class Estimator {

    /******************************** Fonctions de couts ********************************/
    public double coutAvecMaterialisation(Node leaf){
        if (leaf == null)
            return 0.0;
        return leaf.cost() + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
    }





    public double coutAvecPipelinage(Node node) {
        if (node == null)
            return 0.0;
        double leftMax = coutAvecPipelinage(node.getLeft());
        double rightMax = coutAvecPipelinage(node.getRight());
        return Math.max(node.cost(), Math.max(leftMax, rightMax));
    }
    public Hashtable<Node,Double> calculateCostMaterialization(LogicalTree logicalTree){
        Hashtable<Node,Double> treesCosts = new Hashtable<>();
        for (Node node:logicalTree.getPhysicalTrees()){
            treesCosts.put(node,coutAvecMaterialisation(node));
        }
        return treesCosts;
    }
    public Hashtable<Node,Double> calculateCostPipelinage(LogicalTree logicalTree){
        Hashtable<Node,Double> treesCosts = new Hashtable<>();
        for (Node node:logicalTree.getPhysicalTrees()){
            treesCosts.put(node,coutAvecPipelinage(node));
        }
        return treesCosts;
    }
    /******************************** Fonction d'utilite ********************************/

    private boolean isSelect(Node nd){
        return nd.toString().charAt(0) == 'σ';
    }
    private boolean isJoin(Node nd){
        if (nd != null)
            return nd.toString().charAt(0) == '⋈';
        return false;
    }
    private boolean isCartesianProduct(Node nd){
        return nd.toString().charAt(0) == 'X';
    }

    /******************************** Fonction de calculs ********************************/
    static public double selectionBalayage(double nbrLignes, Entity entity){
        return ((calculateBT(nbrLignes,entity))* Dictionnary.TempsTrans);
    }
    static public double selectionCleUnique(Selection selection){
        Entity entity = Dictionnary.findEntityByTableName(selection.getTable().getName());
        assert entity != null;
        if (entity.isIndexSecondaire())
            return (calculateHauteur(selection.getLeft().NbrLignes(),entity)+1)*Dictionnary.tempsESBloc();
        return calculateHauteur(selection.getLeft().NbrLignes(),entity)*Dictionnary.tempsESBloc();
    }
    static public double selectionEgaliteHashage(Selection selection){
        Entity entity = Dictionnary.findEntityByTableName(selection.getTable().getName());
        assert entity != null;
        return (selection.getLeft().NbrLignes()/calculateTHt(selection.getLeft().NbrLignes(),entity) * entity.getFBT())*Dictionnary.tempsESBloc();
    }
    static public double triFusion(double nbrLignes, Entity entity){
        return 2*((calculateBT(nbrLignes,entity) / Dictionnary.M)*Dictionnary.TempsPasDebut + calculateBT(nbrLignes,entity)*Dictionnary.TempsTrans )
                + calculateBT(nbrLignes,entity)*(2*(Math.log10(calculateBT(nbrLignes,entity) / Dictionnary.M) / (Dictionnary.M - 1)) - 1) * Dictionnary.tempsESBloc();
    }
    static public double boucleImbriqueBlocs(Jointure jointure){
        Entity entity1 = Dictionnary.findEntityByTableName(jointure.getTable1().getName());
        Entity entity2 = Dictionnary.findEntityByTableName(jointure.getTable2().getName());
        assert entity1 != null;
        assert entity2 != null;
        return calculateBT(jointure.getLeft().NbrLignes(),entity1)*(Dictionnary.tempsESBloc() + (calculateBT(jointure.getRight().NbrLignes(),entity2)*Dictionnary.TempsTrans) + Dictionnary.TempsPasDebut );
    }
    static public double boucleImbriqueIndex(Jointure jointure){
        Entity entity = Dictionnary.findEntityByTableName(jointure.getTable1().getName());
        assert entity != null;
        return (calculateBT(jointure.getLeft().NbrLignes(),entity) * Dictionnary.tempsESBloc());
    }
    static public double jointureTriFusion(Jointure jointure){
        Entity entity1 = Dictionnary.findEntityByTableName(jointure.getTable1().getName());
        Entity entity2 = Dictionnary.findEntityByTableName(jointure.getTable2().getName());
        assert entity1 != null;
        assert entity2 != null;
        return (triFusion(jointure.getLeft().NbrLignes(),entity1) + triFusion(jointure.getRight().NbrLignes(),entity2)
                + 2*(calculateBT(jointure.getLeft().NbrLignes(),entity1)
                        + calculateBT(jointure.getRight().NbrLignes(), entity2))*Dictionnary.tempsESBloc());
    }
    static public double jointureHashage(Jointure jointure){
        Entity entity1 = Dictionnary.findEntityByTableName(jointure.getTable1().getName());
        Entity entity2 = Dictionnary.findEntityByTableName(jointure.getTable2().getName());
        assert entity1 != null;
        assert entity2 != null;
        return (selectionBalayage(jointure.getLeft().NbrLignes(),entity1) +
                selectionBalayage(jointure.getRight().NbrLignes(), entity2) +
                2*((calculateBT(jointure.getLeft().NbrLignes(),entity1)+
                        calculateBT(jointure.getRight().NbrLignes(), entity2))*Dictionnary.tempsESBloc()));
    }
    static public double preJointure(Jointure jointure){
        Entity entity1 = Dictionnary.findEntityByTableName(jointure.getTable1().getName());
        Entity entity2 = Dictionnary.findEntityByTableName(jointure.getTable2().getName());
        assert entity1 != null;
        assert entity2 != null;
        return selectionBalayage(jointure.getLeft().NbrLignes(),entity1) +
                selectionBalayage(jointure.getRight().NbrLignes(), entity2);
    }
    /******************* Fonction de clalcule ********************/
    static double calculateHauteur(double nbrLignes,Entity entity){
        return Math.log(nbrLignes)/Math.log(entity.getAvgOrder());
    }
    static double calculateBT(double nbrLignes,Entity entity){
        return nbrLignes/entity.getFBT();
    }
    static public double calculateTHt(double nbrLignes,Entity entity){
        return nbrLignes/entity.calculateFBt();
    }
}
