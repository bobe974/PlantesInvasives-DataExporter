package projetEEE.POI;

import java.io.File;
import java.sql.*;


public class MysqliteDb {

    public static Connection conMainDb;
    private static boolean hasData = false;
    private ResultSet mainrs;
    private ResultSet rs,rs2;
    private String reqCreatedb = "CREATE TABLE IF NOT EXISTS \"Fiche\" (\n" +
            "\t\"id_fiche\"\tINTEGER NOT NULL,\n" +
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
            "\tPRIMARY KEY(\"id_fiche\" AUTOINCREMENT)\n" +
            ");";

    public MysqliteDb(){

        try {
            initialise();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //connectToDb();
    }


    //créer la base de données locale
    public void initialise() throws SQLException, ClassNotFoundException {

        //vérifie si la base existe
        File file = new File("Maindb.db");
        if(file.exists())
        {
            //si la base existe on se connecte
            System.out.print("*******connection a la base existante*******");
            //créer un la base a cet emplacement
            conMainDb = DriverManager.getConnection("jdbc:sqlite:Maindb.db");

        }
        else{

            //créer la base
            Class.forName("org.sqlite.JDBC");
            conMainDb = DriverManager.getConnection("jdbc:sqlite:Maindb.db");
            Statement statement = conMainDb.createStatement();
            statement.executeUpdate(reqCreatedb);
            System.out.println("*********réation d'une base*********");

        }


    }

    public ResultSet getResult(){
        return this.rs2;
    }

    public ResultSet getAllMainDB() throws SQLException {
        Statement statement = conMainDb.createStatement();
        ResultSet res;
        res = statement.executeQuery("SELECT * FROM Fiche");
        return res;
    }

    //connection a une base
    public void feedDb(String dbname){
        Connection connection = null;

        try
        {
            //connection a une base existante
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbname);
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
            while(rs.next())
            {
                insert(rs.getString(2),rs.getString(7), rs.getString(8),
                        rs.getString(9),rs.getString(10),rs.getString(4),
                        rs.getString(5),rs.getString(12),rs.getString(13),
                        rs.getString(14),rs.getString(15),rs.getString(16),
                        rs.getString(17));

                System.out.println("**************insertion succes***************");
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

            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    //ajoute un champs dans la base principale
    public void insert(String etablissement, String nomPlante, String etat, String stade, String description, String path, String date, String type, String surface, String nb_indiv, String latitude ,String longitude, String remarques) throws SQLException {
        String sql = "INSERT INTO  Fiche (nom_etablissement,Nom_plante,etat,stade,description,chemin_fichier,DatePhoto,type,surface,nb_individu,latitude,longitude,remarques) \n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conMainDb.prepareStatement(sql);
        statement.setString(1, etablissement);
        statement.setString(2, nomPlante);
        statement.setString(3, etat);
        statement.setString(4, stade);
        statement.setString(5, description);
        //recupere une partie du path
        statement.setString(6, path.substring(76));
        statement.setString(7, date);
        statement.setString(8, type);
        statement.setString(9, surface);
        statement.setString(10, nb_indiv);
        statement.setString(11, latitude);
        statement.setString(12, longitude);
        statement.setString(13, remarques);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("une fiche a été ajouté avec succes!");
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MysqliteDb sqliteTest = new MysqliteDb();
        sqliteTest.insert("pjac","tulipe","sale","grand","riendire","path","122929","typedemerde","carré","3","23","24","riendie");
    }


}
