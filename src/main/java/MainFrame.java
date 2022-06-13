//TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE
import Transfert.MTPUtil;
import Transfert.PhoneToPc;
import jmtp.PortableDevice;

import projetEEE.POI.CreateCSV;
import projetEEE.POI.CreateExcel;
import projetEEE.POI.MysqliteDb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class MainFrame extends JFrame {

    public static JTable jTable;
    private JList jList;
    //chemin des fichiers de l'app
    final String PATH_APP = System.getProperty("user.dir") ;
    //créer le modèle qui  va contenir les appareils connectés
    DefaultListModel<String> model = new DefaultListModel<>();
    private JScrollPane scrollTable;
    private  JButton  btnExport, btnTransfert, btnRefresh, btnDelete;
    private String deviceName ;
    JTree projectExplorerTree;
    DefaultMutableTreeNode treeBackup;
    String selectedPath = "";
    DefaultTreeModel defaultTreeModel;


    /**************DATA************/
    private static MysqliteDb mysqliteDb;
    //En-têtes pour JTable
    private  static String[] columns = new String[] {
            "Num_Fiche", "Id_fiche","Etablissement","Nom_plante","État","Stade","Description","Photo","Date_photo",
            "Type","Surface","Nb_individu","Latitude","Longitude","Remarques"
    };

    //données du jtable
    public static Object[][] data = new Object[2000][15];

    /**************DATA************/

    public MainFrame() {
        super("Projet EEE");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // créer un modèle vide
        //initialize();

    }


    private void initialize() {
        System.out.println(PATH_APP);
        //initialisation bdd
        mysqliteDb = new MysqliteDb();

        //créer le fichier delete
        //vérifie si la base existe
        File file = new File(PATH_APP+"/1.225_172303_8349967972327207504");
        if(!(file.exists()))
        {
            System.out.print("*******création fichier delete*******");
            try {
                file.createNewFile();
            }catch (Exception e){
                System.out.println(e);
            }

        }

        //créer le dossier backup
        File backup = new File(PATH_APP+"/backup");
        if(!(backup.exists()))
        {
            System.out.print("*******création dossier backup*******");
            try {
                backup.mkdir();
            }catch (Exception e){
                System.out.println(e);
            }

        }

        Fenetre fenetre = new Fenetre(mysqliteDb);
        fenetre.setVisible(false);

        //panneau principal
        JPanel panneau= (JPanel) getContentPane();

        // --- EXPLORATEUR DE PROJET
        JPanel fileTreePanel = new JPanel();

        JLabel jLabeltree = new JLabel("Fichiers de restauration");
        jLabeltree.setBounds(25,1,150,40);
        fileTreePanel.add(jLabeltree);

        //Jtree
        fileTreePanel.setLayout(null);
        fileTreePanel.setPreferredSize(new Dimension(170,100));

        treeBackup = new DefaultMutableTreeNode("Backup");
        defaultTreeModel = new DefaultTreeModel(treeBackup);
        //recupere la liste des noms de fichiers dans le dossier backup
        ArrayList<String> filesname = getFilename(PATH_APP+"/backup");
        //affichage dans le jtree
        feedJtree(treeBackup,filesname);
        projectExplorerTree = new JTree(defaultTreeModel);
        fileTreePanel.add(projectExplorerTree);
        projectExplorerTree.setBounds(10,40,150,200);

        JButton btnRefreshTree = new JButton("Actualiser");
        btnRefreshTree.setBounds(40,250,100,20);
        fileTreePanel.add(btnRefreshTree);
        JButton btnSelectNode = new JButton("Aperçu de la base");
        btnSelectNode.setBounds(10,300,170,40);
        fileTreePanel.add(btnSelectNode);
        JButton btnDeletetree = new JButton("Supprimer backup");
        btnDeletetree.setBounds(10,350,170,40);
        fileTreePanel.add(btnDeletetree);

        JScrollPane projectScrollPane = new JScrollPane(fileTreePanel);
        projectScrollPane.setPreferredSize( new Dimension( 200, 0 ) );

        // --- PARTIE PRINCIPALE JTABLE
        JPanel mainPanel = new JPanel();
        System.out.println(data[2][1]);
        DefaultTableModel dmodel = new DefaultTableModel(data, columns);
        jTable = new JTable(dmodel);
        jTable.setShowGrid(true);
        jTable.setShowHorizontalLines(true);
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
        //TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE
        feedJlist();
        JPanel devicesPanel = new JPanel();
        devicesPanel.setLayout(null);
        devicesPanel.setPreferredSize(new Dimension(170,100));

        JLabel jLabel = new JLabel("Appareils connectés");
        jLabel.setBounds(25,1,150,40);
        devicesPanel.add(jLabel);
        //espace
        devicesPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        jList = new JList(model);
        jList.setBounds(10,40,150,200);
        devicesPanel.add(jList);
        btnRefresh = new JButton("Actualiser");
        btnRefresh.setBounds(40,250,100,20);
        devicesPanel.add(btnRefresh);
        btnTransfert = new JButton("Récupérer les données");
        btnTransfert.setBounds(10,300,170,40);
        devicesPanel.add(btnTransfert);
        btnDelete = new JButton("Réinitialiser la base");
        btnDelete.setBounds(10,350,170,40);
        devicesPanel.add(btnDelete);


        JScrollPane rightpanel = new JScrollPane( devicesPanel);

        // --- AJOUT
        JSplitPane documentSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, scrollTable, rightpanel );
        documentSplitPane.setResizeWeight( 0.8 );

        //pane du dessous aligné verticalement
        JSplitPane rightSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, documentSplitPane, bottompanel );
        rightSplitPane.setResizeWeight( 0.9 );


        JSplitPane mainSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, projectScrollPane, rightSplitPane );
        mainSplitPane.setResizeWeight( 0.16 );

        getContentPane().add(mainSplitPane);

        /*************************EVENT**************************/


        btnTransfert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loop();
                int index = jList.getSelectedIndex();
                String s = (String) jList.getSelectedValue();
                deviceName = s;

                if (s == null){
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un appareil"
                            , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                }
                //transfert
                //TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE
                     PhoneToPc phoneToPc = new PhoneToPc();
                     phoneToPc.TransfertPhoto(s,PATH_APP);
                     phoneToPc.TransfertDb(s,PATH_APP);

                //vérifie si le fichier existe
                File fichier = new File(PATH_APP+"/PlanteInvasives.sqlite");

                if(fichier.exists()){
                    JOptionPane.showMessageDialog(null, "Succes"
                            , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                }

                ArrayList<String> lesPhotos = getPhotosNames();
                createBackup(deviceName, lesPhotos);

                //lance la fenetre apercu
                fenetre.setVisible(true);
                fenetre.init("PlanteInvasives.sqlite");
                updateJtree();
            }
        });

        projectExplorerTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                String value ="";
                TreePath treepath = e.getPath();
                Object elements[] = treepath.getPath();
                for (int i = 0, n = elements.length; i < n; i++) {
                    selectedPath = value += elements[i] + "/";
                    System.out.println(selectedPath);
                }
            }
        });

        btnSelectNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nodePath = selectedPath;
                //copie des photos dans le répertoire principale (pour l'export)
                try {
                    copyAllByExtension(nodePath,PATH_APP,".jpg");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //apercu de la base chargé
                fenetre.setVisible(true);
                fenetre.init(nodePath + "PlanteInvasives.sqlite");
            }
        });

        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //vérifie si les cases sont selectionnées
                if(!(checkcsv.isSelected() || checkxls.isSelected())){
                    JOptionPane.showMessageDialog(null,
                            "Veuillez choisir le format d'exportation"
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
                                //copie des images au meme endroit que le fichier xls

                                System.out.println(fichier.getParent());
                                copyAllByExtension(PATH_APP,fichier.getParent(),".jpg");
                            }
                            if(checkcsv.isSelected()){
                                System.out.println("absolute choose name : " + fichier.getAbsolutePath());
                                CreateCSV createCSV = new CreateCSV(resultSet1,fichier.getAbsolutePath());
                            }

                            //vérifie si le fichier existe
                            fichier = new File(jFileChooser.getSelectedFile().getAbsolutePath()+".xls");
                            if(fichier.exists()){
                                JOptionPane.showMessageDialog(null, "Export effectué"
                                        , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                            }

                        }

                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mysqliteDb.deleteAll();
                    data = new Object[2000][15];
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Données supprimées"
                        , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                setTableModel();
                //supprimer les fichier jpg
                try {
                    deleteAllByExtension(PATH_APP,".jpg");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //supprimer la base externe
                try {

                    deleteByName(PATH_APP,"PlanteInvasives.sqlite");
                    deleteByName(PATH_APP,"PlanteInvasives.sqlite-shm");
                    deleteByName(PATH_APP,"PlanteInvasives.sqlite-wal");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnDeletetree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //supprime le contenu du répertoire backup
                deleteAllInDir(PATH_APP+"/backup");
                //verifie si le dossier est bien vide
                File file = new File(PATH_APP+"/backup");
                if(file.isDirectory()) {
                    if (file.list().length <= 0) {
                        JOptionPane.showMessageDialog(null, "Fichiers de restauration supprimées"
                                , "Projet EEE", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                updateJtree();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                //TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE
                feedJlist();
                jList.repaint();
            }
        });

        btnRefreshTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateJtree();
            }
        });
    }

    // --- Point d'entrée du programme ---
    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel( new NimbusLookAndFeel());
        MainFrame window = new MainFrame();
        window.setSize(1200,800);
        window.initialize();
        window.setVisible(true);
        MysqliteDb mysqliteDb = new MysqliteDb();
        ResultSet resultSet = mysqliteDb.getAllMainDB();
        window.feedJtable(resultSet);
        window.setTableModel();
    }

    /**
     * alimente les données d'une jtbable depuis la base principale
     * @return
     */
    public static Object[][] feedJtable(ResultSet resultSet){
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

    public void setTableModel(){
        jTable.setModel(new DefaultTableModel(data,columns));
    }

    public static void updateJtable(){
        //affiche le contenu dans la jtable principale
        try {
            ResultSet res = mysqliteDb.getAllMainDB();
            data = feedJtable(res);
            jTable.setModel(new DefaultTableModel(data,columns));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateJtree(){
        ArrayList<String> names = getFilename(PATH_APP+"/backup");
        //affichage dans le jtree
        treeBackup.removeAllChildren();
        feedJtree(treeBackup,names);
        defaultTreeModel.reload();
    }

    /**
     * copie/colle tout les fichiers avec la meme extension
     * @param pathtarget
     * @param pathdest
     * @param extension
     * @throws IOException
     */
    public void copyAllByExtension(String pathtarget, String pathdest, String extension) throws IOException {
        File dir  = new File(pathtarget);
        File[] liste = dir.listFiles();
        for(File item : liste){
            if(item.isFile())
            {

                if (item.getName().toString().endsWith(extension)) {
                    File file = new File(pathdest + "/" + item.getName().toString());
                    System.out.println(file.getAbsolutePath());
                    System.out.format("Nom du fichier: %s%n", item.getName());
                    //copié coller a la destination
                    Files.copy(item.toPath(),file.toPath());
                }
            }
            else if(item.isDirectory())
            {
                if (item.getName().toString().endsWith(".jpg")) {
                    System.out.format("Nom du fichier: %s%n", item.getName());
                }

            }
        }
    }

    public void copyByName(String fileName, String pathtarget, String pathdest) throws IOException {
        File dir  = new File(pathtarget);
        File[] liste = dir.listFiles();
        for(File item : liste){
            if(item.isFile())
            {
                if (item.getName().toString().equals(fileName)) {
                    File file = new File(pathdest + "/" + item.getName().toString());
                    System.out.println(file.getAbsolutePath());
                    System.out.format("Nom du fichier: %s%n", item.getName());
                    //copié coller a la destination
                    Files.copy(item.toPath(),file.toPath());
                }
            }
            else if(item.isDirectory())
            {
                if (item.getName().toString().endsWith(".jpg")) {
                    System.out.format("Nom du fichier: %s%n", item.getName());
                }
            }
        }
    }

    public void deleteAllByExtension(String pathtarget,String extension) throws IOException {
        File dir  = new File(pathtarget);
        File[] liste = dir.listFiles();
        for(File item : liste){
            if(item.isFile())
            {
                if (item.getName().toString().endsWith(extension)) {
                    item.delete();
                    System.out.format( item.getName() +"supprimé");
                }
            }
            else if(item.isDirectory())
            {
                System.out.println("supprime le répertoire:"+ item.getName());
                item.delete();
            }
        }
    }

    public void deleteByName(String pathtarget,String filename) throws IOException {
        File dir  = new File(pathtarget);
        File[] liste = dir.listFiles();

        for(File item : liste){
            if(item.isFile())
            {
                if (item.getName().toString().equals(filename)) {
                    item.delete();
                    System.out.format( item.getName() +"fichier "+filename+"supprimé");
                }

            }
        }
    }

    public void deleteAllInDir(String pathtarget){
        File dir  = new File(pathtarget);
        File[] liste = dir.listFiles();
        for(File item : liste){
             if(item.isDirectory())
            {
                deleteDir(item);
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    //TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE TODO REMOVE
    public void feedJlist(){
        //recuperer tous les appareils
        MTPUtil mtpUtil = new MTPUtil();
        for (PortableDevice portableDevice : mtpUtil.getDevices()){
            portableDevice.open();
            model.addElement(portableDevice.getModel());
            portableDevice.close();
        }
    }


    public void createBackup(String folderName, ArrayList lesPhotographies){ //, ArrayList lesPhotos
        String path = PATH_APP+"/backup/"+folderName;
        //recupere la date actuelle
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String fileName = path+"-"+ dtf.format(LocalDateTime.now());
        System.out.println("DATTTTTTTE" + fileName);

        File file = new File(fileName);
        if(!(file.exists()))
        {
            //si la base existe on se connecte
            System.out.print("*******création du backup*******");
            try {
                file.mkdir();
            }catch (Exception e){
                System.out.println(e);
            }
            //copie de la base de données
            try {
                copyByName("PlanteInvasives.sqlite",PATH_APP,fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //copie des photos
            ArrayList<String> lesPhotos = lesPhotographies;
            for(String unePhoto : lesPhotos){
                //copie de chaque photos trouvé depuis le répertoire de l'application
                try {
                    copyByName(unePhoto,PATH_APP,fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ArrayList getPhotosNames(){
        ArrayList<String> lesPhotos = new ArrayList<>();
        String req = "SELECT chemin_fichier FROM Photographie";
        ResultSet resultSet = mysqliteDb.getResultset("PlanteInvasives.sqlite",req);
        //parcours du résultset
        try {
            while (resultSet.next()){
                //ajout de chaque Nom dans la liste
                String PhotoName = resultSet.getString(1).substring(76);
                lesPhotos.add(PhotoName);
                System.out.println(PhotoName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lesPhotos;
    }

    public ArrayList getFilename(String DirPath){
        String[] pathnames;
        ArrayList<String> names = new ArrayList<>();
        File dir = new File(DirPath);
        pathnames = dir.list();

        for (String s : pathnames) {
            System.out.println("fichier dans le dossier: "+ s);
            names.add(s);
        }
        return names;
    }

    public void feedJtree(DefaultMutableTreeNode defaultMutableTreeNode, ArrayList<String> nodes){
        for(String s : nodes){
            defaultMutableTreeNode.add(new DefaultMutableTreeNode(s));
        }
    }

}