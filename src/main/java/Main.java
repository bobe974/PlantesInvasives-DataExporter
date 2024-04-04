import projetEEE.POI.CreateCSV;
import projetEEE.POI.CreateExcel;
import projetEEE.POI.MysqliteDb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        //instancie la bdd principale
        MysqliteDb mysqliteDb = new MysqliteDb();
        //se connecte a une base et alimente la base principal
        mysqliteDb.feedDb("PlanteInvasives.sqlite");
        //resultset contenant toutes les données
        ResultSet resultSet = mysqliteDb.getAllMainDB();
        ResultSet resultSet1 = mysqliteDb.getAllMainDB();
        //créer le fichier excel
        //c:/Users/lacom/Downloads/projetEEE/employee.xls
        CreateExcel createExcel = new CreateExcel(resultSet,"./employe.xls");
       // CreateCSV createCSV = new CreateCSV(resultSet1);
    }
}
