package voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import voting.dto.countyrep.CountyRepresentativeDTO;
import voting.dto.countyrep.CountyRepresentativeData;
import voting.exception.MultiErrorException;
import voting.service.CountyRepService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by domas on 1/10/17.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api/county-rep")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CountyRepController {

    private CountyRepService countyRepService;

    @Autowired
    public CountyRepController(CountyRepService countyRepService) {
        this.countyRepService = countyRepService;
    }

    @GetMapping
    public List<CountyRepresentativeDTO> getCountyReps() {
        return countyRepService.getCountyReps()
                               .stream()
                               .map(CountyRepresentativeDTO::new)
                               .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public CountyRepresentativeDTO getCountyRep(@PathVariable Long id) {
        return new CountyRepresentativeDTO(countyRepService.getCountyRep(id));
    }

    @PostMapping
    public CountyRepresentativeDTO addNewCountyRep(
            @Valid @RequestBody CountyRepresentativeData countyRepData,
            BindingResult result) throws MessagingException {
        if (result.hasErrors()) {
            throw new MultiErrorException(
                    String.format("Klaida registruojant apygardos atstovą %s %s",
                            countyRepData.getFirstName(), countyRepData.getLastName()),
                    result.getAllErrors());
        }
        return new CountyRepresentativeDTO(countyRepService.addNewCountyRep(countyRepData));
    }

    @DeleteMapping("{id}")
    public void deleteCountyRep(@PathVariable Long id) {
        countyRepService.deleteCountyRep(id);
    }
}
