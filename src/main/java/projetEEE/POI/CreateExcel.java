package projetEEE.POI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

public class CreateExcel {

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public CreateExcel(ResultSet resultSet,String destpath) throws SQLException {

        //resultSet = mysqliteDb.getResult();

        //String link_path = "JPEG_20220322_193052_4086193540502885980.jpg";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("projet eee");
        MysqliteDb mysqliteDb = new MysqliteDb();


        //TODO HYPERLINK
        // HSSFHyperlink hlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
        // hlink.setAddress("C:/Users/lacom/Desktop/zz.jpg");
        // hlink.setAddress(link_path);
        //hlink.setLabel("label");
        //XSSFHyperlink xlink = new XSSFHyperlink(hlink);



        //recupere les données

        List<String> nomColonne = new ArrayList<>();


        try {
            nomColonne = getNomColonne(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int rownum = 0;
        Cell cell;
        Row row;
        //TODO TEST HYPERLINK
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
//        cell = row.createCell(0);
//        cell.setCellValue("Ouvrirphoto");
//        cell.setHyperlink(hlink);

        //nom de chaque colonnes
        int i = 0;
        for(String nom: nomColonne){
            System.out.println(nom);
            //TODO remettre normal
            cell = row.createCell(i,CellType.STRING);
            cell.setCellValue(nom);
            cell.setCellStyle(style);
            i++;
        }

        // Data

        while(resultSet.next()){
            rownum++;

            //créer une nouvelle ligne
            row = sheet.createRow(rownum);

            //cellule suivante en ligne
            for(int k=0;k<nomColonne.size();k++){

                cell = row.createCell(k, CellType.STRING);

                //pour l'attribut chemin on ajoute l'hyperlien
                if(k == 3){
                    HSSFHyperlink hlink1 = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);

                    String path = resultSet.getString(k+1);
                    System.out.println(path);
                    cell.setCellValue(path);
                    hlink1.setAddress(path);
//                    hlink.setLabel("label");
//                    cell.setCellValue("Ouvrirphoto");
                    cell.setHyperlink(hlink1);
                }else{
                    cell.setCellValue(resultSet.getString(k+1));
                }

            }


        }

        //chemin du fichier

        File file = new File(destpath);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("******fichier xls crée!*****: " + file.getAbsolutePath());
    }


    /**
     * recupere le nom de chaque occurence
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getNomColonne(ResultSet resultSet) throws SQLException {

        ResultSetMetaData metadata = null;
        int columnCount = 0;
        ArrayList<String> colonne = new ArrayList<String>();

        metadata = resultSet.getMetaData();
        columnCount = metadata.getColumnCount();


        //la premiere colonne commence a 1
        for (int i = 1; i < columnCount+1 ; i++) {
            String columnName = metadata.getColumnName(i);
            colonne.add(columnName);
        }
        return  colonne;
    }


    /**
     * recupere le type de chaque occurence
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public ArrayList<Integer> getTypeColonne(ResultSet resultSet) throws SQLException {

        ResultSetMetaData metadata = null;
        int columnCount = 0;
        ArrayList<Integer> colonne = new ArrayList<Integer>();

        metadata = resultSet.getMetaData();
        columnCount = metadata.getColumnCount();
        for (int i = 1; i < columnCount +1; i++) {
            int type = metadata.getColumnType(i);
            colonne.add(type);
        }
        return  colonne;
    }
}