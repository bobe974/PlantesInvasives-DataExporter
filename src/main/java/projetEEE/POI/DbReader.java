package projetEEE.POI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbReader {

    ResultSet rs;

    public DbReader(){
        Connection connection = null;

        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/etienne/Desktop/PlanteInvasives.sqlite");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("SELECT * FROM Fiche \n" +
                    "              INNER JOIN Photographie \n" +
                    "             ON Fiche.id_fiche = Photographie.id_photo\n" +
                    "             INNER JOIN Plante \n" +
                    "             ON Fiche.id_fiche = Plante.id_plante\n" +
                    "             INNER JOIN Lieu \n" +
                    "            ON Fiche.id_fiche = Lieu.id_lieu ");

            //lecture de la base
//            while(rs.next())
//            {
//                // read the result set
//                System.out.println("id = " + rs.getInt(1));
//                System.out.println("nometablissement  = " + rs.getString(2));
//                System.out.println("path = " + rs.getString(4));
//                System.out.println("datephoto = " + rs.getString(5));
//                System.out.println("nomPlante:  = " + rs.getString(7));
//                System.out.println("etatplante:  = " + rs.getString(8));
//                System.out.println("stade plante = " + rs.getString(9));
//                System.out.println("description plante:  = " + rs.getString(10));
//                System.out.println("type lieu = " + rs.getString(12));
//                System.out.println("surface lieu = " + rs.getString(13));
//                System.out.println("nbindividu  = " + rs.getString(14));
//                System.out.println("latittude = " + rs.getString(15));
//                System.out.println("longitude = " + rs.getString(16));
//                System.out.println("remarque = " + rs.getString(17));
//
//            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
//        finally
//        {
//            try
//            {
//                if(connection != null)
//                    connection.close();
//            }
//            catch(SQLException e)
//            {
//                // connection close failed.
//                System.err.println(e.getMessage());
//            }
//        }
    }

    public ResultSet getResult(){
        return this.rs;
    }
}
