
import projetEEE.POI.CreateCSV;
import projetEEE.POI.CreateExcel;
import projetEEE.POI.MysqliteDb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/*
 * Dans cet exemple, on imagine refaire une interface graphique
 * qui ressemble (sur quelques points, bien entendu) à l'IDE Eclipse.
 */
public class MainFrame extends JFrame {


    private JPanel panneau;
    private JTextArea jTextArea, jTextrdf, jtextsparql, jresultsparql;
    private JTable jTable;
    private JList jList;
    //créer le modèle qui  va contenir les appareils connectés
    DefaultListModel<String> model = new DefaultListModel<>();
    private JScrollPane scrollTable;
    private  JButton btnAggreger, btnExport, btnSelectioner;

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

    public MainFrame() {
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
        JPanel panneau= (JPanel) getContentPane();

        // --- EXPLORATEUR DE PROJET
        JTree projectExplorerTree = new JTree();
        JScrollPane projectScrollPane = new JScrollPane( projectExplorerTree );
        projectScrollPane.setPreferredSize( new Dimension( 200, 0 ) );

        // --- PARTIE PRINCIPALE JTABLE
        JPanel mainPanel = new JPanel();
        jTable = new JTable(data, columns);
        scrollTable = new JScrollPane(jTable);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable.setPreferredSize(new Dimension(730,300));

        mainPanel.add(scrollTable);

        // --- PARTIE EXPORT CSV XLS
        //bouttons
        JPanel jbottomPanel = new JPanel();
        jbottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10,0));
        // créer une case à cocher
        JCheckBox checkxls = new JCheckBox("format .xls");

        JCheckBox checkcsv = new JCheckBox("format .csv");

        jbottomPanel.add(checkxls);
        jbottomPanel.add(checkcsv);
        btnExport = new JButton("Exporter...");

        jbottomPanel.add(btnExport);

        JScrollPane bottompanel = new JScrollPane( jbottomPanel );

        // --- PARTIE APPAREIL CONNECTE
        //Jlist
        //recuperer tous les appareils
//        MTPUtil mtpUtil = new MTPUtil();
//        for (PortableDevice portableDevice : mtpUtil.getDevices()){
//            portableDevice.open();
//            model.addElement(portableDevice.getModel());
//            portableDevice.close();
//        }
        JPanel devicesPanel = new JPanel();
        devicesPanel.setLayout(null);
        devicesPanel.setPreferredSize(new Dimension(170,100));

        JLabel jLabel = new JLabel("Appareils connectés");
        jLabel.setBounds(25,1,150,40);
        devicesPanel.add(jLabel);

        model.addElement("galaxy  s9");
        model.addElement("iphone 2");
        jList = new JList(model);
        jList.setBounds(10,40,150,200);
        devicesPanel.add(jList);

        btnSelectioner = new JButton("Récupérer les données");
        btnSelectioner.setBounds(10,300,170,40);
        devicesPanel.add(btnSelectioner);

        btnAggreger = new JButton("Agréger les données");
        btnAggreger.setBounds(10,350,170,40);
        devicesPanel.add(btnAggreger);

        JScrollPane rightpanel = new JScrollPane( devicesPanel);

        // --- AJOUT
        JSplitPane documentSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, scrollTable, rightpanel );
        documentSplitPane.setResizeWeight( 0.8 );

        //pane du dessous aligné verticalement
        JSplitPane rightSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, documentSplitPane, bottompanel );
        rightSplitPane.setResizeWeight( 0.8 );

        JSplitPane mainSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, projectScrollPane, rightSplitPane );
        mainSplitPane.setResizeWeight( 0.16 );

        getContentPane().add(mainSplitPane);

        // Pour changer la taille d'un séperateur de panneaux
        // mainSplitPane.setDividerSize( 100 );        // Bof

        // Pour changer un composant dans le JSplitPane après sa construction
        // setTopComponent, setBottomComponent, setLeftComponent, setRightComponent

        /*************************EVENT**************************/

        btnSelectioner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = jList.getSelectedIndex();
                String s = (String) jList.getSelectedValue();
                System.out.println("Value Selected: " + s);
                //transfert
//                     PhoneToPc phoneToPc = new PhoneToPc();
//                     //c:/Users/lacom/Downloads/projetEEE
//                //TODO user qui choisi un emplacement system au premier lancement
//                     phoneToPc.TransfertPhoto(s,System.getProperty("user.dir"));
//                     phoneToPc.TransfertDb(s,System.getProperty("user.dir"));

                //vérifie si le fichier existe
                File fichier = new File(System.getProperty("user.dir")+"/PlanteInvasives.sqlite");
                //TODO copier les photos dans le meme dossier
                if(fichier.exists()){
                    JOptionPane.showMessageDialog(null, "Succes"
                            , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                }


            }
        });
        btnAggreger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // connection usb

                //se connecte a une base et alimente la base principal
                mysqliteDb.feedDb("PlanteInvasives.sqlite");

                //affiche le contenu dans la jtable
                data = feedJtable();
                JOptionPane.showMessageDialog(null, "Données sauvegardées"
                        , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
            }
        });


        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //vérifie si les cases sont selectionné
                if(!(checkcsv.isSelected() || checkxls.isSelected())){
                    JOptionPane.showMessageDialog(null, "Veuillez choisir le format d'exportation"
                            , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                }else{
                    ResultSet resultSet = null;
                    try {
                        resultSet = mysqliteDb.getAllMainDB();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    ResultSet resultSet1 = null;
                    try {
                        resultSet1 = mysqliteDb.getAllMainDB();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        //selection de l'emplacement du fichier
                        JFileChooser jFileChooser = new JFileChooser();
                        int reponse = jFileChooser.showSaveDialog(null); //fichier a ouvrir

                        File fichier = null;
                        if(reponse == JFileChooser.APPROVE_OPTION){
                            fichier = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                            System.out.println(fichier);
                            if(checkxls.isSelected()){
                                //export xls
                                CreateExcel createExcel = new CreateExcel(resultSet,fichier.getAbsolutePath());
                            }
                            if(checkcsv.isSelected()){
                                //TODO EXPORT CSV
                                //CreateCSV createCSV = new CreateCSV(resultSet);
                            }

                            //vérifie si le fichier existe
                            fichier = new File(jFileChooser.getSelectedFile().getAbsolutePath()+".xls");
                            //TODO copier les photos dans le meme dossier
                            if(fichier.exists()){
                                JOptionPane.showMessageDialog(null, "Export effectué"
                                        , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                            }

                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
//                try {
//                    CreateCSV createCSV = new CreateCSV(resultSet1);
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
                }


            }
        });
    }

    // --- Point d'entrée du programme ---
    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel( new NimbusLookAndFeel());
        MainFrame window = new MainFrame();
        window.setSize(1200,800);
        window.setVisible(true);
        window.feedJtable();
    }


    /**
     * alimente les données d'une jtbable depuis la base principale
     * @return
     */
    public Object[][] feedJtable(){
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
        return data;
    }

}