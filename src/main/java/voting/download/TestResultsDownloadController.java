package voting.download;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import voting.model.County;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andrius on 2/26/17.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/download")
public class TestResultsDownloadController {

    @GetMapping(path = "testers")
    public void downloadTestersCSV(HttpServletResponse response) throws IOException {
        String csvFileName = "test-results.csv";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        String[] header = { "Name", "VoterCount", "Address" };

        response.setContentType("text/csv;charset=utf-8");
        response.setHeader(headerKey, headerValue);

        County county1 = new County("Senamiestis", 3000L, "Adresas 1");
        County county2 = new County("Naujamiestis", 3000L, "Adresas 2");
        County county3 = new County("Pašilaičiai", 3000L, "Adresas 3");
        County county4 = new County("Senamiestis", 3000L, "Adresas 4");
        County county5 = new County("Šilainiai", 3000L, "Adresas 5");
        List<County> list = Arrays.asList(county1, county2, county3, county4, county5);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(header);
        list.forEach(c -> {
            try {
                csvWriter.write(c, header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvWriter.close();
    }
}
