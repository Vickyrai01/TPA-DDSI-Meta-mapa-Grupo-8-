package csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneradorCSV {
    public static void writeCsv(String path, List<String[]> rows, String[] headers) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withHeader(headers)
                .withQuoteMode(null);
        try (FileWriter writer = new FileWriter(path);
             CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (String[] row : rows) {
                printer.printRecord((Object[]) row);
            }

            System.out.println("Archivo CSV generado en: " + path);
        }
    }
}
