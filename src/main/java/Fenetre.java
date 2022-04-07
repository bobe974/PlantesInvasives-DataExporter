import projetEEE.POI.MysqliteDb;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Fenetre extends JFrame {

    private JPanel panneau;
    private JTextArea jTextArea, jTextrdf, jtextsparql, jresultsparql;
    private JTable jTable;
    private JScrollPane scrollTable;
    private  JButton btnTransfert, btnAdd, btnExport;

    /**************DATA************/
    private MysqliteDb mysqliteDb;
    //En-têtes pour JTable
    private String[] columns = new String[] {
            "Id_fiche","Etablissement","Nom_plante","État","Stade","Description","Photo","Date_photo",
            "Type","Surface","Nb_individu","Latitude","Longitude","Remarques"
    };

    //données du jtable
    private Object[][] data = new Object[2000][14];

    /**************DATA************/

    public static void main(String[] args) {
        Fenetre window = new Fenetre();
        window.setSize(1500, 1000);
        window.setVisible(true);

    }

    public Fenetre() {
        super("Projet EEE");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //centrer la jframe
        //récuperer la taille de l'écran
        Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        int height = tailleEcran.height;
        int width = tailleEcran.width;
        //taille est un demi la longueur et l'hauteur
        setSize(width/2, height/2);
        setLocationRelativeTo(null);
        // créer un modèle vide
        initialize();

    }

    private void initialize() {

        //initialisation bdd
        mysqliteDb = new MysqliteDb();

        //panneau principal
        panneau = new JPanel();
        panneau.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        //jtable
        jTable = new JTable(data, columns);
        scrollTable = new JScrollPane(jTable);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable.setPreferredSize(new Dimension(1000, 400));
        panneau.add(scrollTable);


        //bouttons
        btnTransfert = new JButton("Récupérer les données");
        panneau.add(btnTransfert);
        btnAdd = new JButton("Ajouter des données");
        panneau.add(btnAdd);
        btnExport = new JButton("Exporter...");
        panneau.add(btnExport);


        //ajout fans le jpanel
        add(panneau);


        //event
        btnTransfert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // connection usb

                //se connecte a une base et alimente la base principal
                mysqliteDb.feedDb("PlanteInvasives.sqlite");

                //affiche le contenu dans la jtable
                ResultSet resultSet;
                try {
                     resultSet = mysqliteDb.getAllMainDB();
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
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //PhoneToPc phoneToPc = new PhoneToPc();
                //phoneToPc.TransfertPhoto();
                //phoneToPc.TransfertDb();
            }
        });
    }


}