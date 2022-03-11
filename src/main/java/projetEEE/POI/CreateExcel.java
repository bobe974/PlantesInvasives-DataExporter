package projetEEE.POI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class CreateExcel {

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static void main(String[] args) throws IOException, SQLException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("projet eee");
        CreateExcel createExcel = new CreateExcel();
        DbReader dbReader = new DbReader();

        //recupere les données

        List<String> nomColonne = new ArrayList<>();
        ResultSet resultSet = dbReader.getResult();

        try {
             nomColonne = createExcel.getNomColonne(dbReader.getResult());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        //nom de chaque colonnes
        int i = 0;
        for(String nom: nomColonne){
            System.out.println(nom);
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
                cell.setCellValue(resultSet.getString(k+1));
            }


        }

        //chemin du fichier
        File file = new File("/Users/etienne/Desktop/employee.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("fichier xls créer: " + file.getAbsolutePath());

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