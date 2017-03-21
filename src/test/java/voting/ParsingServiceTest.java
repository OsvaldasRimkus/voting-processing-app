package voting;

import com.opencsv.exceptions.CsvException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import voting.dto.candidate.CandidateData;
import voting.exception.MultiErrorException;
import voting.service.ParsingService;
import voting.service.ParsingServiceImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by domas on 2/2/17.
 */
@RunWith(SpringRunner.class)
public class ParsingServiceTest {


    private String singleMandateHeader = "Vardas,Pavardė,Asmens_kodas,Partija,Aprasymas";
    private String multiMandateHeader = "Numeris,Vardas,Pavardė,Asmens_kodas,Aprasymas";

    ParsingService sut = new ParsingServiceImpl();


    List<String> lines = new ArrayList<String>();

    private String[] singleMandateSampleData = {
            "Vardas1,Pavardė,33300033301,Partija 1,labai ilgas aprasymas",
            "Vardas2,Pavardė,33300033302,Partija 2,labai ilgas aprasymas",
            "Vardas3,Pavarde3,33300033303,Partija 3,labai ilgas aprasymas"
    };

    private String[] multiMandateSampleData = {
            "1,Pirmas,Pavardė,33300033301,labai ilgas aprasymas",
            "2,Antras,Pavardė,33300033302,labai ilgas aprasymas",
            "3,Trecias,Pavarde3,33300033303,labai ilgas aprasymas"
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setup() {
        lines.clear();
    }


    @Test
    public void shouldThrowCsvExceptionWhenFileHasNoHeader() throws Exception {
        //Setup
        thrown.expect(CsvException.class);
        thrown.expectMessage("Bloga failo vidinė antraštė");

        lines.addAll(Arrays.asList(singleMandateSampleData));
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);
    }

    @Test
    public void shouldThrowCsvExceptionWhenFileHasIncorrectHeader() throws Exception {
        //Setup
        thrown.expect(CsvException.class);
        thrown.expectMessage("Bloga failo vidinė antraštė");

        String invalidHeader = "Vardas,Pavardė,Asmens_kodas,Partija,XXX";
        lines.add(invalidHeader);
        lines.addAll(Arrays.asList(singleMandateSampleData));
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);
    }

    @Test
    public void shouldThrowCsvExceptionWhenFileHasIncorrectData() throws Exception {
        //Setup
        thrown.expect(CsvException.class);
        thrown.expectMessage("Duomenų klaida eilutėje - 3");

        String goodLine = "Vardas,Pavardė,33300033301,Partija,labai ilgas aprasymas";
        String badLine = "33300033302,Partija";
        lines.add(singleMandateHeader);
        lines.add(goodLine);
        lines.add(badLine);
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);
    }

    @Test
    public void shouldThrowCsvExceptionWhenFileHasWrongFormatNumber() throws Exception {
        //Setup
        thrown.expect(CsvException.class);
        thrown.expectMessage("Duomenų klaida eilutėje - 3");

        String goodLine = "1,Pirmas,Pavardė,33300033301,labai ilgas aprasymas";
        String badLine = "x,Antras,Pavardė,33300033302,labai ilgas aprasymas";
        lines.add(multiMandateHeader);
        lines.add(goodLine);
        lines.add(badLine);
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseMultiMandateCandidateList(file);
    }

    @Test
    public void parsingWorksCorrectlyWithDoubleQuotedValues() throws Exception {
        //Setup
        String line1 = "\"Pirmas\",\"Pavardė\",33300033301,Partija 1,labai ilgas aprasymas";
        lines.add(singleMandateHeader);
        lines.add(line1);
        File file = createNewFile("file", lines);

        CandidateData candidateData1 = createCandidateData("Pirmas", "Pavardė", "33300033301", "Partija 1", "labai ilgas aprasymas");

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);

        //Verify
        assertThat(candidateData1, is(candidateList.get(0)));
    }


    @Test
    public void parsingShouldTreatSingleQuotesAsNormalSymbols() throws Exception {
        //Setup
        String line1 = "Pirmas,Pavardė,33300033301,Partija,Monty Python's Flying Circus";
        lines.add(singleMandateHeader);
        lines.add(line1);
        File file = createNewFile("file", lines);

        CandidateData candidateData1 = createCandidateData("Pirmas", "Pavardė", "33300033301", "Partija", "Monty Python's Flying Circus");

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);

        //Verify
        assertThat(candidateData1, is(candidateList.get(0)));
    }

//    @Ignore
    @Test
    public void parsingWorksCorrectlyWithSeparatorSymbolInQuotedValue() throws Exception {
        //Setup
        String line1 = "Pirmas,Pavardė,33300033301,Partija,\"labai, ilgas, aprasymas\"";
        lines.add(singleMandateHeader);
        lines.add(line1);
        File file = createNewFile("file", lines);

        CandidateData candidateData1 = createCandidateData("Pirmas", "Pavardė", "33300033301", "Partija", "labai, ilgas, aprasymas");

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);

        //Verify
        assertThat(candidateData1, is(candidateList.get(0)));
    }

    @Test
    public void parsingSingleMandateListWorksCorrectly() throws Exception {
        //Setup
        String line1 = "Pirmas,Pavardė,33300033301,Partija 1,labai ilgas aprasymas";
        String line2 = "Antras,Pavardė,33300033302,Partija 2,labai ilgas aprasymas";
        lines.add(singleMandateHeader);
        lines.add(line1);
        lines.add(line2);
        File file = createNewFile("file", lines);

        CandidateData candidateData1 = createCandidateData("Pirmas", "Pavardė", "33300033301", "Partija 1", "labai ilgas aprasymas");
        CandidateData candidateData2 = createCandidateData("Antras", "Pavardė", "33300033302", "Partija 2", "labai ilgas aprasymas");

        //Exercise
        List<CandidateData> candidateList = sut.parseSingleMandateCandidateList(file);

        //Verify
        assertThat(candidateList.size(), is(2));
        assertThat(candidateData1, is(candidateList.get(0)));
        assertThat(candidateData2, is(candidateList.get(1)));
    }




    @Test
    public void parsingMultiMandateListWorksCorrectly() throws Exception {
        //Setup
        String line1 = "1,Pirmas,Pavarde,33300033301,labai ilgas aprasymas";
        String line2 = "2,Antras,Pavarde,33300033302,labai ilgas aprasymas";
        lines.add(multiMandateHeader);
        lines.add(line1);
        lines.add(line2);
        File file = createNewFile("file", lines);

        CandidateData candidateData1 = createCandidateData(1L, "Pirmas", "Pavarde", "33300033301", "labai ilgas aprasymas");
        CandidateData candidateData2 = createCandidateData(2L, "Antras", "Pavarde", "33300033302", "labai ilgas aprasymas");

        //Exercise
        List<CandidateData> candidateList = sut.parseMultiMandateCandidateList(file);

        //Verify
        assertThat(candidateList.size(), is(2));
        assertThat(candidateData1, is(candidateList.get(0)));
        assertThat(candidateData2, is(candidateList.get(1)));
    }


    @Test
    public void incontinuousPartyPositionsInMultiMandateListShouldThrowCsvException() throws Exception {
        //Setup
        thrown.expect(MultiErrorException.class);
        thrown.expectMessage("Duomenų klaida eilutėje - 2");
//        thrown.expectMessage("incontinuous position in party list");

        String line1 = "2,Pirmas,Pavardė,33300033301,labai ilgas aprasymas";
        String line2 = "1,Antras,Pavardė,33300033302,labai ilgas aprasymas";
        lines.add(multiMandateHeader);
        lines.add(line1);
        lines.add(line2);
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseMultiMandateCandidateList(file);
    }

    @Test
    public void duplicatingPartyPositionsInMultiMandateListShouldThrowCsvException() throws Exception {
        //Setup
        thrown.expect(MultiErrorException.class);
        thrown.expectMessage("Duomenų klaida eilutėje - 3");
//        thrown.expectMessage("incontinuous position in party list");

        String line1 = "1,Pirmas,Pavardė,33300033301,labai ilgas aprasymas";
        String line2 = "1,Antras,Pavardė,33300033302,labai ilgas aprasymas";
        lines.add(multiMandateHeader);
        lines.add(line1);
        lines.add(line2);
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseMultiMandateCandidateList(file);
    }

    @Test
    public void shouldThrowCsvExceptionWhenCandidateDataContraintsAreViolated() throws Exception {
        //Setup
        thrown.expect(MultiErrorException.class);
        thrown.expectMessage("Duomenų klaida eilutėje - 2");

        String line1 = "1,,Pavardė,333,labai ilgas aprasymas"; // null lastName, invalid pid
        lines.add(multiMandateHeader);
        lines.add(line1);
        File file = createNewFile("file", lines);

        //Exercise
        List<CandidateData> candidateList = sut.parseMultiMandateCandidateList(file);
    }


    private File createNewFile(String fileName, List<String> lines) throws IOException {
        File file = Files.createTempFile(fileName, ".tmp").toFile();
        FileWriter writer = new FileWriter(file);
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.close();
        return file;
    }

    private CandidateData createCandidateData(Long position, String firstName, String lastName, String personId, String description) {
        CandidateData cd = new CandidateData();
        cd.setPositionInPartyList(position);
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setPersonId(personId);
        cd.setDescription(description);
        return cd;
    }

    private CandidateData createCandidateData(String firstName, String lastName, String personId, String partyName, String description) {
        CandidateData cd = new CandidateData();
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setPersonId(personId);
        cd.setPartyName(partyName);
        cd.setDescription(description);
        return cd;
    }

}
