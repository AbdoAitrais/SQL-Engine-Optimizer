package controler;

import model.bo.Node;
import model.dictionnary.Dictionnary;
import model.dictionnary.Entity;

import java.math.BigDecimal;
import java.util.Hashtable;

public class Estimator {


    Hashtable<Node, Double> coutsPipelinage;
    Hashtable<Node, Double> coutsMaterialisation;

    public double coutAvecPipelinage(Node leaf){
        //TODO::ecrice la fonction
        return 0.0;
    }
    public double coutAvecMaterialisation(Node leaf){
        //TODO::ecrice la fonction
        return 0.0;
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
