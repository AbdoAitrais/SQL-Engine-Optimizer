package bo;

public class Catalog {
    public static Table[] tables = {
            new Table("employees","emp",
                new Column[]{
                        new Column("employee_id","emp_id"),
                        new Column("employee_name", "emp_name"),
                        new Column("salary", "sal"),
                        new Column("department_id","dep_id"),
                        new Column("country", "cntry")
                }),
            new Table("departments","dep",
                new Column[]{
                        new Column("department_id","dep_id"),
                        new Column("department_name","dep_name")
                }),
            
            new Table("Countries","cntry",
                new Column[]{
                        new Column("country_name","cntry_name")
                })
    };
    public static Table getTableByColumnName(String colName){
        for (Table table:tables) {
            if (table.findColumn(colName))
                return table;
        }
        return null;
    }
}
