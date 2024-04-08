package projetEEE.POI;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe de manipulation de base de données
 * @author etienne baillif
 * @version 1.0
 */
public class MysqliteDb {

    public static Connection conMainDb, connEx;
    private static boolean hasData = false;
    private ResultSet mainrs;
    private ResultSet rs, rs2;
    private String reqCreatedb = "CREATE TABLE IF NOT EXISTS \"Fiche\" (\n" +
            "\t\"num_fiche\"\tINTEGER NOT NULL,\n" +
            "\t\"id_fiche\"\tINTEGER NOT NULL UNIQUE,\n" +
            "\t\"nom_etablissement\"\tTEXT,\n" +
            "\t\"Nom_plante\"\tTEXT,\n" +
            "\t\"etat\"\tTEXT,\n" +
            "\t\"stade\"\tTEXT,\n" +
            "\t\"description\"\tTEXT,\n" +
            "\t\"chemin_fichier\"\tTEXT,\n" +
            "\t\"DatePhoto\"\tTEXT,\n" +
            "\t\"type\"\tTEXT,\n" +
            "\t\"surface\"\tTEXT,\n" +
            "\t\"nb_individu\"\tTEXT,\n" +
            "\t\"latitude\"\tREAL NOT NULL,\n" +
            "\t\"longitude\"\tREAL NOT NULL,\n" +
            "\t\"remarques\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"num_fiche\"AUTOINCREMENT)\n" +
            ");";

    public MysqliteDb() {

        try {
            initialise();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //créer la base de données locale
    private void initialise() throws SQLException, ClassNotFoundException {

        //vérifie si la base existe
        File file = new File("Maindb.db");
        if (file.exists()) {
            //si la base existe on se connecte
            System.out.print("*******connection a la base existante*******");
            //créer un la base a cet emplacement
            conMainDb = DriverManager.getConnection("jdbc:sqlite:Maindb.db");
            System.out.println();
        } else {
            //créer la base
            Class.forName("org.sqlite.JDBC");
            conMainDb = DriverManager.getConnection("jdbc:sqlite:Maindb.db");
            Statement statement = conMainDb.createStatement();
            statement.executeUpdate(reqCreatedb);
            System.out.println("*********création d'une base*********");
        }
    }
    /******************METHODE CRUD MAIN ***********************/
    public ResultSet getAllMainDB() throws SQLException {
        Statement statement = conMainDb.createStatement();
        ResultSet res;
        res = statement.executeQuery("SELECT * FROM Fiche");
        return res;
    }

    public void deleteDB() throws SQLException {
        Statement statement = conMainDb.createStatement();
        statement.executeUpdate("DELETE FROM Fiche");
    }

    /**
     * connection a une base de données, récupere toutes les occurences et insertion la base pricipale
     * @param dbname
     * @throws SQLException
     */
    public void feedDb(String dbname) throws SQLException {
        Connection connection = null;

        try {
            //connection a une base existante
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();
            statement.setQueryTimeout(30);
            rs = statement.executeQuery("SELECT * FROM Fiche \n" +
                    "              INNER JOIN Photographie \n" +
                    "             ON Fiche.id_fiche = Photographie.id_photo\n" +
                    "             INNER JOIN Plante \n" +
                    "             ON Fiche.id_fiche = Plante.id_plante\n" +
                    "             INNER JOIN Lieu \n" +
                    "            ON Fiche.id_fiche = Lieu.id_lieu ");

            rs2 = statement2.executeQuery("SELECT * FROM Fiche \n" +
                    "              INNER JOIN Photographie \n" +
                    "             ON Fiche.id_fiche = Photographie.id_photo\n" +
                    "             INNER JOIN Plante \n" +
                    "             ON Fiche.id_fiche = Plante.id_plante\n" +
                    "             INNER JOIN Lieu \n" +
                    "            ON Fiche.id_fiche = Lieu.id_lieu ");

            //parcours de la base et ajout des champs dans la base principale

            while (rs.next()) {
                insert(rs.getString(4), rs.getString(2), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(4),
                        rs.getString(5), rs.getString(12), rs.getString(13),
                        rs.getString(14), rs.getString(15), rs.getString(16),
                        rs.getString(17));

                System.out.println("**************insertion succes***************");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        connection.close();
    }

    /**
     * recupere le resulset de la base de données ciblée
     * @param dbname
     * @param req
     * @return
     */
    public ResultSet getResultset(String dbname, String req) {
        Connection connection = null;

        try {
            //connection a une base existante
            connEx = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            Statement statement = connEx.createStatement();
            statement.setQueryTimeout(30);
            rs = statement.executeQuery(req);
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;

    }

    /**
     * ajoute un champs dans la base principale
     * @param id_fiche
     * @param etablissement
     * @param nomPlante
     * @param etat
     * @param stade
     * @param description
     * @param path
     * @param date
     * @param type
     * @param surface
     * @param nb_indiv
     * @param latitude
     * @param longitude
     * @param remarques
     * @throws SQLException
     */
    public void insert(String id_fiche, String etablissement, String nomPlante, String etat, String stade, String description, String path, String date, String type, String surface, String nb_indiv, String latitude, String longitude, String remarques) throws SQLException {
        String sql = "INSERT INTO  Fiche (id_fiche,nom_etablissement,Nom_plante,etat,stade,description,chemin_fichier,DatePhoto,type,surface,nb_individu,latitude,longitude,remarques) \n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conMainDb.prepareStatement(sql);
        statement.setString(1, id_fiche.substring(81, id_fiche.length() - 4));
        statement.setString(2, etablissement);
        statement.setString(3, nomPlante);
        statement.setString(4, etat);
        statement.setString(5, stade);
        statement.setString(6, description);
        //recupere une partie du path
        statement.setString(7, path.substring(76));
        statement.setString(8, date);
        statement.setString(9, type);
        statement.setString(10, surface);
        statement.setString(11, nb_indiv);
        statement.setString(12, latitude);
        statement.setString(13, longitude);
        statement.setString(14, remarques);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("une fiche a été ajouté avec succes!");
        }
        statement.close();
    }

    /**
     * Supprime toutes les occurences de la base de données
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM Fiche";
        String reset = "DELETE FROM sqlite_sequence WHERE name = 'Fiche'";
        PreparedStatement preparedStatement = conMainDb.prepareStatement(sql);
        preparedStatement.execute();
        //reset auto increment
        PreparedStatement preparedStatement1 = conMainDb.prepareStatement(reset);
        preparedStatement1.execute();
        preparedStatement.close();
        preparedStatement1.close();
    }

    /**
     * recupere le nom de chaque occurence de la base
     * @return
     * @throws SQLException
     */
    public ArrayList < String > getNomColonne(ResultSet resultSet) throws SQLException {

        ResultSetMetaData metadata = null;
        int columnCount = 0;
        ArrayList < String > colonne = new ArrayList < String > ();

        metadata = resultSet.getMetaData();
        columnCount = metadata.getColumnCount();

        //la premiere colonne commence a 1
        for (int i = 1; i < columnCount + 1; i++) {
            String columnName = metadata.getColumnName(i);
            colonne.add(columnName);
        }
        return colonne;
    }
    public void closeConnEx() {
        try {
            connEx.close();
        } catch (Exception e) {
            System.out.println("erreur fermeture base:" + e);
        }
    }

}