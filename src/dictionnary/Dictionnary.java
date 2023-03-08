package dictionnary;

import bo.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionnary {
    ArrayList<Entity> entities;
    static int TailleBloc = 2;
    final static int TailleDescripteurBloc = 3;
    final static int tailleCleIndex = 1;
    final static int taillePointeurBloc = 2;
    final double TempsTrans = 0.1;

    public  void ReadDataFromFileDictionary() {

        try {
            String ligne;
            // Création du flux de lecture
            BufferedReader br = new BufferedReader(new FileReader("meta.txt"));


            while((ligne = br.readLine()) != null){
                String[] infos = ligne.split("\\s+");
                Table table = new Table(infos[0]);
                int Nt = Integer.parseInt(infos[1]);
                int tailleLigneT = Integer.parseInt(infos[2]);
                int FBT = Integer.parseInt(infos[3]);
                String[] colonne = infos[4].split(",");


                List<MetaColumn> metaColumn = new ArrayList<MetaColumn>();
                metaColumn.add(new MetaColumn(  colonne[0],
                        Integer.parseInt(colonne[1]),
                        Integer.parseInt(colonne[2]),
                        Integer.parseInt(colonne[3])
                ));

                entities.add(new Entity(table,Nt,tailleLigneT,FBT,metaColumn));
            }
        } catch(IOException e) {}
    }
    public static double calculateTailleEntree(){
        return (double) (Dictionnary.tailleCleIndex + Dictionnary.TailleDescripteurBloc);
    }
    public double calculateOrder(){
        return (double) (Dictionnary.TailleBloc - Dictionnary.TailleDescripteurBloc/calculateTailleEntree());
    }
}