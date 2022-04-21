
//import Transfert.PhoneToPc;
//import jmtp.PortableDevice;
import projetEEE.POI.CreateExcel;
import projetEEE.POI.MysqliteDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Fenetre extends JFrame {

    JTable jTable;
    JScrollPane scrollTable;
    private  JProgressBar progressBar;

    ResultSet resultSet;
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
            "Id_fiche","Etablissement","Nom_plante","État","Stade","Description","Photo","Date_photo",
            "Type","Surface","Nb_individu","Latitude","Longitude","Remarques","d","dd","ddd"
    };

    //données du jtable
    private Object[][] data = new Object[2000][17];

    public  Fenetre(MysqliteDb mysqliteDb){
        //frame de contextuelle
        super("Apercu des données de l'appareil");
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
        //init();
    }

    public  void init(){

        ResultSet resultset = mysqliteDb.getResultset("PlanteInvasives.sqlite",req);
        data = feedJtable(resultset);

        //        progressBar = new JProgressBar(0,1000);
//        progressBar.setBounds(35,40,165,30);
//        progressBar.setValue(0);
//        progressBar.setStringPainted(true);
//        frameProgress.add(progressBar);

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JButton btnAggreger = new JButton("Agréger les données");
        JButton btncancel = new JButton("Annuler");
        jTable = new JTable(data,columns);
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
                mysqliteDb.feedDb("PlanteInvasives.sqlite");

                //affiche le contenu dans la jtable principale
                MainFrame.updateJtable();
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

    // fonction pour augmenter le progressBar
    public void loop()
    {
        int i=0;
        while(i <= 1000)
        {
            // remplit la barre
            progressBar.setValue(i);
            i = i + 10;
            try
            {
                // retarder le thread
                Thread.sleep(120);
            }
            catch(Exception e){}
        }
    }
}