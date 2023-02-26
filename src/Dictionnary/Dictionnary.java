package Dictionnary;


import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import java.util.ArrayList;
        import java.util.List;

        import BusinessObject.Table;

public class Dictionnary {

    static int TailleBloc = 2;
    final static int TailleDescripteurBloc = 3;
    Table table;
    int Nt;
    int TailleLigneT;
    int FBT;
    List<ColumnMeta> metaColumn = new ArrayList<>();


    public  void ReadDataFromFileDictionary()
    {

        try {
            String ligne;
            // Création du flux de lecture
            BufferedReader br = new BufferedReader(new FileReader("meta.txt"));


            while((ligne = br.readLine()) != null){
                String[] infos = ligne.split("    ");
                table = new Table(infos[0]);
                Nt = Integer.parseInt(infos[1]);
                TailleBloc = Integer.parseInt(infos[2]);
                FBT = Integer.parseInt(infos[3]);
                String[] colonne = infos[4].split(",");
                metaColumn.add(new ColumnMeta(  colonne[0],
                        Integer.parseInt(colonne[1]),
                        Integer.parseInt(colonne[2]),
                        Integer.parseInt(colonne[3])
                ));

            }
        } catch(IOException e) {}

    }



//Source: https://stackoverflow.com/questions/2049380










}