package Parsing;

// import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Optimizer optimizer = new Optimizer();
        // String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2, table3 t3, table4 t4 WHERE t1.column1 = t2.column3 AND t1.column4 = t3.column3 AND t1.column = 55";
        String query = "SELECT * FROM Al as  A,Bl as B , Cib C WHERE A.id = B.id and B.age = 5 and B.id = C.id";

        optimizer.queryComponentExtraction(query);
        for (Table t:optimizer.query.tables) {
            System.out.println(t.getName());
        }
        for (Column c:optimizer.query.columns) {
            System.out.println(c.getName());
        }
        System.out.println(optimizer.query.whereClause);

        System.out.println("");
        System.out.println("");


        optimizer.query.root = optimizer.query.createTree();
        System.out.println(optimizer.query);
        

        

    }
}


// public static void affi_2D(Node arbre, int espace) {
//     int i;
//     if (arbre == null)
//       return;
//     espace += 50;
//     affi_2D(arbre.fd, espace);
//     System.out.println();
//     for (i = 10; i < espace; i++)
//       System.out.print(" ");
//     System.out.println(arbre.c);
//     affi_2D(arbre.fg, espace);
//   }

//   public static void affichageGraphique(Node arbre) {
//     affi_2D(arbre, 0);
//   }
