
import Transfert.PhoneToPc;
import jmtp.PortableDevice;
import projetEEE.POI.CreateExcel;
import projetEEE.POI.MysqliteDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Fenetre de prévisualisatiton des données
 * @author etienne baillif
 * @version 1.0
 */
public class Fenetre extends JFrame {

    JTable jTable;
    JScrollPane scrollTable;
    MysqliteDb mysqliteDb;
    String req = "SELECT * FROM Fiche \n" +
            "              INNER JOIN Photographie \n" +
            "             ON Fiche.id_fiche = Photographie.id_photo\n" +
            "             INNER JOIN Plante \n" +
            "             ON Fiche.id_fiche = Plante.id_plante\n" +
            "             INNER JOIN Lieu \n" +
            "            ON Fiche.id_fiche = Lieu.id_lieu ";

    //TODO modif column
    private String[] columns = new String[] {
            "Id_fiche","Etablissement","id_photo","path","date_photo","id_plante","Nom","etat",
            "stade","Description","id_lieu","type","surface","nb_individu","latitude","longitude","remarques"
    };

    //données du jtable
    private Object[][] data = new Object[2000][17];

    public  Fenetre(MysqliteDb mysqliteDb){
        //frame de contextuelle
        super("Prévisualisation des données");
        Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int height = tailleEcran.height;
        int width = tailleEcran.width;
        //taille est un demi la longueur et l'hauteur
        setSize(width/2, height/2);
        setSize(760,400);
        setLayout(new FlowLayout());
        setLocationRelativeTo ( null );
        this.mysqliteDb = mysqliteDb;
    }

    public  void init(String dbPath){

        ResultSet resultset = mysqliteDb.getResultset(dbPath,req);
        data = feedJtable(resultset);
        mysqliteDb.closeConnEx();

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JButton btnAggreger = new JButton("Agréger les données");
        JButton btncancel = new JButton("Annuler");
        jTable = new JTable(data,columns);
        jTable.setShowGrid(true);
        jTable.setShowHorizontalLines(true);

        scrollTable = new JScrollPane(jTable);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable.setPreferredSize(new Dimension(730,300));
        panel.add(scrollTable);
        panel2.add(btncancel);
        panel2.add(btnAggreger);
        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, panel, panel2);

        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.HORIZONTAL);
        add(sep);


        /************EVENT*********/

        btnAggreger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // connection usb

                //se connecte a une base et alimente la base principal
                try {
                    mysqliteDb.feedDb(dbPath);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                //affiche le contenu dans la jtable principale
                MainFrame.updateJtable();
                dispose();
            }
        });

        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public Object[][] feedJtable(ResultSet resultSet){
        //affiche le contenu dans la jtable

        try {
            //resultSet = mysqliteDb.getAllMainDB();
            int i = 0;
            while (resultSet.next()){
                for (int j = 0; j < mysqliteDb.getNomColonne(resultSet).size(); j++) {
                    //ligne / colonnes
                    data[i][j] = resultSet.getString(j+1);
                }
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}