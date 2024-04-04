

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FileExplorer implements Runnable {

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JTree tree;
    private String selectedPath = "";

    @Override
    public void run() {

        File fileRoot = new File(System.getProperty("user.dir") + "/backup");
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);

        CreateChildNodes ccn =
                new CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();

        //EVENT
//        tree.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent me) {
//                TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
//                System.out.println(tp.getLastPathComponent());
//
//            }
//        });
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                String value ="";
                TreePath treepath = e.getPath();
                //System.out.println("Java: " + treepath.getLastPathComponent());
                Object elements[] = treepath.getPath();
                for (int i = 0, n = elements.length; i < n; i++) {
                    // JOptionPane.showMessageDialog(null,"->"+elements[i]);
                    //lblNewLabel.setText(">"+ elements[i]);
                    selectedPath = value += elements[i] + "/";
                    System.out.println(selectedPath);
                }
            }
        });

        treeModel.reload();
    }

    public void init(){
        SwingUtilities.invokeLater(new FileExplorer());
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        public CreateChildNodes(File fileRoot,
                                DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot,
                                    DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
                DefaultMutableTreeNode childNode =
                        new DefaultMutableTreeNode(new FileNode(file));
                node.add(childNode);
                if (file.isDirectory()) {
                    createChildren(file, childNode);
                }
            }
        }
    }

    public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }

    public JTree getTree() {
        return tree;
    }
    public String getSelectedPath(){
        return selectedPath;
    }
    public void reloadTree(){
        this.treeModel.reload();
    }
}