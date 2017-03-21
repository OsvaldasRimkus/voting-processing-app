package voting.service;

import com.opencsv.exceptions.CsvException;
import voting.dto.candidate.CandidateData;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by domas on 1/21/17.
 */
public interface ParsingService {

    public List<CandidateData> parseSingleMandateCandidateList(File file) throws CsvException, IOException;

    public List<CandidateData> parseMultiMandateCandidateList(File file) throws CsvException, IOException;
}
