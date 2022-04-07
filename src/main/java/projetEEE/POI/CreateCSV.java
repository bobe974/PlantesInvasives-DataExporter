package projetEEE.POI;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class CreateCSV {

    public CreateCSV(ResultSet resultSet) throws SQLException, IOException {
        // File path and name.
        File filePath = new File("/Users/etienne/Desktop");
        String fileName = filePath.toString() + "/personexport.csv";
        ArrayList<String> nomColonne;
        System.out.println(fileName);

        // Check to see if the file path exists.
        if (filePath.isDirectory()) {
            try {
                ResultSet results  = resultSet;
                nomColonne =getNomColonne(resultSet);

                // Open CSV file.
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));

                // Add table headers to CSV file.
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(results.getMetaData()).withQuoteMode(QuoteMode.ALL));

                // Add data rows to CSV file.
                int i = 1;
                while (results.next()) {

                    System.out.println("***********CSVVV");

                    csvPrinter.printRecord(
                            results.getString(1),
                            results.getString(2),
                            results.getString(3),
                            results.getString(4),
                            results.getString(5),
                            results.getString(6),
                            results.getString(7),
                            results.getString(8),
                            results.getString(9),
                            results.getString(10),
                            results.getString(11),
                            results.getString(12),
                            results.getString(13),
                            results.getString(14));

                    i++;

                }

                // Close CSV file.
                csvPrinter.flush();
                csvPrinter.close();

                // Message stating export successful.
                System.out.println("Data export successful.");

            } catch (SQLException e) {

                // Message stating export unsuccessful.
                System.out.println("Data export unsuccessful.");
                System.exit(0);

            } catch (IOException e) {

                // Message stating export unsuccessful.
                System.out.println("Data export unsuccessful.");
                System.exit(0);

            }

        } else {

            // Display a message stating file path does not exist and exit.
            System.out.println("File path does not exist.");
            System.exit(0);

        }
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


}

