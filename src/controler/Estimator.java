package controler;

import model.dictionnary.Dictionnary;
import model.dictionnary.Entity;

public class Estimator {
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
//    public double triFusion(Entity entity){
//        return 2*((entity.calculateBT()/Dictionnary.))
//    }
}
