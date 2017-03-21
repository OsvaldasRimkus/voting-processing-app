package voting.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import voting.dto.candidate.CandidateData;
import voting.exception.MultiErrorException;
import voting.utils.DateUtils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by domas on 1/21/17.
 */
@Service
public class ParsingServiceImpl implements ParsingService {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator javaxValidator = factory.getValidator();
    private final SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);

    public ParsingServiceImpl() {
    }


    @Override
    public List<CandidateData> parseSingleMandateCandidateList(File file) throws IOException, CsvException {
        CandidateParsingStrategy strategy = new SingleMandateCandidateParsingStrategy();
        return parseCandidateList(strategy, file);
    }

    @Override
    public List<CandidateData> parseMultiMandateCandidateList(File file) throws IOException, CsvException {
        CandidateParsingStrategy strategy = new MultiMandateCandidateParsingStrategy();
        return parseCandidateList(strategy, file);
    }

    private List<CandidateData> parseCandidateList(CandidateParsingStrategy strategy, File file) throws CsvException, IOException {

        String[] expectedHeader = strategy.getHeader();
        int expectedColumnCount = expectedHeader.length;

        List<CandidateData> candidateDataList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] header = reader.readNext();

            if (header == null || !Arrays.equals(header, expectedHeader)) {
                throw new CsvException("Bloga failo vidinė antraštė. Turi būti - " + String.join(",", expectedHeader));
            }

            String[] line;
            int lineNumber = 2; // line 1 was header

            while ((line = reader.readNext()) != null) {
                if (line.length != expectedColumnCount) {
                    throw new CsvException("Duomenų klaida eilutėje - " + lineNumber);
                }
                try {
                    CandidateData candidateData = strategy.parseCandidateData(line);
                    strategy.validate(candidateData, lineNumber);
                    candidateDataList.add(candidateData);
                } catch (NumberFormatException ex) {
                    throw new CsvException("Duomenų klaida eilutėje - " + lineNumber + " - netinkamas skaičiaus formatas");
                }
                lineNumber++;
            }
        } catch (IOException e) {
            throw (new IOException("Klaida skaitant CSV failą"));
        }
        return candidateDataList;
    }


    private abstract class CandidateParsingStrategy {

        private String[] header;

        public CandidateParsingStrategy(String[] header) {
            this.header = header;
        }

        public String[] getHeader() {
            return header;
        }

        abstract CandidateData parseCandidateData(String[] line);

        abstract void validate(CandidateData candidateData, int lineNumber) throws CsvException;
    }

    private class SingleMandateCandidateParsingStrategy extends CandidateParsingStrategy {

        private SingleMandateCandidateParsingStrategy() {
            super("Vardas,Pavardė,Asmens_kodas,Partija,Aprasymas".split(","));
        }

        @Override
        public CandidateData parseCandidateData(String[] line) {
            CandidateData candidateData = new CandidateData();
            candidateData.setFirstName(line[0]);
            candidateData.setLastName(line[1]);
            candidateData.setPersonId(line[2]);
            candidateData.setPartyName(line[3]);
            candidateData.setDescription(line[4]);
            candidateData.setBirthDate(DateUtils.stringToCalendar(line[2]));
            return candidateData;
        }

        @Override
        void validate(CandidateData candidateData, int lineNumber) throws CsvException {

            Errors bindingResult = new BeanPropertyBindingResult(candidateData, "candidateData");
            validator.validate(candidateData, bindingResult);

            if (bindingResult.hasErrors()) {
                throw new MultiErrorException("Duomenų klaidą eilutėje - " + lineNumber, bindingResult.getAllErrors());
            }
        }
    }


    private class MultiMandateCandidateParsingStrategy extends CandidateParsingStrategy {

        private MultiMandateCandidateParsingStrategy() {
            super("Numeris,Vardas,Pavardė,Asmens_kodas,Aprasymas".split(","));
        }

        @Override
        public CandidateData parseCandidateData(String[] line) {
            CandidateData candidateData = new CandidateData();
            candidateData.setPositionInPartyList(Long.parseLong(line[0]));
            candidateData.setFirstName(line[1]);
            candidateData.setLastName(line[2]);
            candidateData.setPersonId(line[3]);
            candidateData.setDescription(line[4]);
            candidateData.setBirthDate(DateUtils.stringToCalendar(line[3]));
            return candidateData;
        }

        @Override
        void validate(CandidateData candidateData, int lineNumber) throws CsvException {
            Errors bindingResult = new BeanPropertyBindingResult(candidateData, "candidateData");
            validator.validate(candidateData, bindingResult);

            int expectedPositionInPartyList = lineNumber - 1;
            if (candidateData.getPositionInPartyList() != expectedPositionInPartyList) {
                bindingResult.rejectValue("positionInPartyList", HttpStatus.BAD_REQUEST.toString(), "Nepaeiliui einančios pozicijos sąraše");
            }

            if (bindingResult.hasErrors()) {
                throw new MultiErrorException("Duomenų klaida eilutėje - " + lineNumber, bindingResult.getAllErrors());
            }
        }
    }
}
