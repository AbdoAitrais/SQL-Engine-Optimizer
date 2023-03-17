package controler;

import model.bo.Jointure;
import model.bo.Node;
import model.bo.Selection;
import model.dictionnary.Dictionnary;
import model.dictionnary.Entity;
import model.utilities.Algorithms;

import java.math.BigDecimal;
import java.util.Hashtable;

public class Estimator {
//    Hashtable<Node, Double> coutsPipelinage;
//    Hashtable<Node, Double> coutsMaterialisation;

    public double coutAvecMaterialisation(Node leaf){
        if (leaf == null)
            return 0.0;
        if (isJoin(leaf)){
            Jointure join = (Jointure) leaf;
            Entity entity1 = Dictionnary.findEntityByTableName(join.getTable1().getName());
            Entity entity2 = Dictionnary.findEntityByTableName(join.getTable2().getName());
            assert entity1 != null;
            assert entity2 != null;
            switch (join.getAlgorithm()){
                case Algorithms.BIB -> {
                    return boucleImbriqueBlocs(entity1,entity2) + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
                }
                case Algorithms.BII -> {
                    return boucleImbriqueIndex(entity1) + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
                }
                case Algorithms.JH -> {
                    return jointureHashage(entity1,entity2) + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
                }
                case Algorithms.JTF -> {
                    return jointureTriFusion(entity1,entity2) + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
                }
                case Algorithms.PJ -> {
                    return preJointure(entity1,entity2) + coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
                }
            }
        }else if (isSelect(leaf)){
            Selection selection = (Selection) leaf;
            Entity entity = Dictionnary.findEntityByTableName(selection.getTable().getName());
            assert entity != null;
            switch (selection.getAlgorithm()){
                case Algorithms.SE -> {
                    return selectionEgaliteHashage(entity) + coutAvecMaterialisation(leaf.getLeft());
                }
                case Algorithms.SB -> {
                    return selectionBalayage(entity) + coutAvecMaterialisation(leaf.getLeft());
                }
                case Algorithms.SNUK -> {
                    return selectionCleUnique(entity) + coutAvecMaterialisation(leaf.getLeft());
                }
            }
        }
        return coutAvecMaterialisation(leaf.getLeft()) + coutAvecMaterialisation(leaf.getRight());
    }
    public double coutAvecPipelinage(Node leaf){
        //TODO::ecrice la fonction
        return 0.0;
    }

    /******************************** Fonction d'utilite ********************************/

    boolean isJH(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.JH);
    }
    boolean isPJ(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.PJ);
    }
    boolean isJTF(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.JTF);
    }
    boolean isBIB(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.BIB);
    }
    boolean isBII(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.BII);
    }
    boolean isSE(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.SE);
    }
    boolean isSB(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.SB);
    }
    boolean isSNUK(String algo){
        if (algo == null)
            return false;
        return algo.equals(Algorithms.SNUK);
    }
    private boolean isProject(Node nd){
        return nd.toString().charAt(0) == 'π';
    }
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
    public double selectionBalayage(Entity entity){
        return entity.calculateBT()* Dictionnary.TempsTrans;
    }
    public double selectionCleUnique(Entity entity){
        if (entity.isIndexSecondaire())
            return (entity.calculateHauteur()+1)*Dictionnary.tempsESBloc();
        return entity.calculateHauteur()*Dictionnary.tempsESBloc();
    }
    public double selectionEgaliteHashage(Entity entity){
        return (entity.getNt()/entity.calculateTHt() * entity.getFBT())*Dictionnary.tempsESBloc();
    }
    public double triFusion(Entity entity){
        return 2*((entity.calculateBT() / Dictionnary.M)*Dictionnary.TempsPasDebut + entity.calculateBT()*Dictionnary.TempsTrans )
                + entity.calculateBT()*(2*(Math.log10(entity.calculateBT() / Dictionnary.M) / (Dictionnary.M - 1)) - 1) * Dictionnary.tempsESBloc();
    }
    public double boucleImbriqueBlocs(Entity entity1, Entity entity2){
        return entity1.calculateBT()*(Dictionnary.tempsESBloc() + (entity2.calculateBT()*Dictionnary.TempsTrans) + Dictionnary.TempsPasDebut );
    }
    public double boucleImbriqueIndex(Entity entity){
        return (entity.calculateBT() * Dictionnary.tempsESBloc());
    }
    public double jointureTriFusion(Entity entity1, Entity entity2){
        return (triFusion(entity1) + triFusion(entity2) + 2*(entity1.calculateBT()+entity2.calculateBT())*Dictionnary.tempsESBloc());
    }
    public double jointureHashage(Entity entity1, Entity entity2){
        return (selectionBalayage(entity1) + selectionBalayage(entity2) + 2*(entity1.calculateBT()+entity2.calculateBT())*Dictionnary.tempsESBloc());
    }
    public double preJointure(Entity entity1, Entity entity2){
        return selectionBalayage(entity1) + selectionBalayage(entity2);
    }


}
