import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;


public class Fenetre extends JFrame {

    private JPanel panneau;
    private JTextArea jTextArea, jTextrdf, jtextsparql, jresultsparql;
    private JScrollPane sp, sp2;
    private JTable jTable;
    private  JButton btnTransfert;
    //En-têtes pour JTable
    private String[] columns = new String[] {
            "Nom",
            "Prénom",
            "URI"
    };
    private Object[][] data = new Object[20][20];

    public static void main(String[] args) {
        Fenetre window = new Fenetre();
        window.setSize(800, 500);
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

        //panneau principal
        panneau = new JPanel();
        panneau.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        //bouttons
        btnTransfert = new JButton("Récupérer les données");

        //ajout fans le jpanel
        panneau.add(btnTransfert);
        add(panneau);


        //event
        btnTransfert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


}