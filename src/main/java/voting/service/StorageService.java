package voting.service;

import org.springframework.web.multipart.MultipartFile;
import voting.exception.StorageException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by domas on 1/16/17.
 */
public interface StorageService {

    Path store(String fileName, MultipartFile file) throws StorageException;

    Path storeTemporary(MultipartFile file) throws StorageException;

    Path load(String filename);

    void delete(Path filePath) throws IOException;

    void deleteAll();

}
