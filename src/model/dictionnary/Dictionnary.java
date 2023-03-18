package model.dictionnary;

import model.bo.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionnary {
    public static ArrayList<Entity> entities;
    public static double TR = 0.8;
    public static int M = 50;
    public static int TailleBloc = 2;

    public final static int TailleDescripteurBloc = 3;
    public final static int tailleCleIndex = 1;
    public final static int taillePointeurBloc = 2;
    public final static double TempsTrans = 0.1;
    public final static double TempsPasDebut = 1;

    public Dictionnary(){
        entities = new ArrayList<>();
        ReadDataFromFileDictionary();
    }
    public  void ReadDataFromFileDictionary() {

        try {
            String ligne;
            // Création du flux de lecture
            BufferedReader br = new BufferedReader(new FileReader("src/model/dictionnary/statistics.txt"));


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
        } catch(IOException e) {
            System.err.println(e.getClass() + " : " + e.getMessage());
        }
    }
    public static double calculateTailleEntree(){
        return (double) (Dictionnary.tailleCleIndex + Dictionnary.TailleDescripteurBloc);
    }
    public double calculateOrder(){
        return (double) (Dictionnary.TailleBloc - Dictionnary.TailleDescripteurBloc/calculateTailleEntree());
    }
    public static double tempsESBloc(){
        return TempsTrans+TempsPasDebut;
    }
    public static Entity findEntityByTableName(String tableName){
        for (Entity entity:entities) {
            if (entity.table.getName().equals(tableName))
                return entity;
        }
        return null;
    }
    public static Table getTableByColumnName(String colName){
        for (Entity entity:entities) {
            if (entity.table.findColumn(colName))
                return entity.table;
        }
        return null;
    }
}
