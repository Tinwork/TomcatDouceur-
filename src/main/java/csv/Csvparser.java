package csv;

import helper.Loghandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by lookitsmarc on 23/05/2017.
 */
public class Csvparser {

    private String csv;
    private final String[] HEADER = {"url", "password", "captcha", "start_date", "end_date", "multiple_password"};
    private FileReader reader;

    /**
     *
     * @param csvfile
     */
    public Csvparser(String csvfile) {
        this.csv = csvfile;
        init();
    }

    /**
     * Init
     */
    public void init(){
        try {
            this.reader = new FileReader(this.csv);
        } catch (FileNotFoundException e) {
            Loghandler.log(e.toString(), "fatal");
        }
    }

    /**
     * Parse
     */
    public void parse(){
        CSVFormat fileFormat = CSVFormat.DEFAULT.withHeader(HEADER);

        try {
            CSVParser csvparse = new CSVParser(this.reader, fileFormat);

            for (CSVRecord record: csvparse) {
                Loghandler.log(record.toString(), "info");
            }
        } catch (IOException e) {
            Loghandler.log(e.toString(), "warn");
        }
    }
}
