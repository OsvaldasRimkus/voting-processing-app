package voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import voting.dto.county.CountyDTO;
import voting.dto.county.CountyData;
import voting.exception.MultiErrorException;
import voting.service.DistrictService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domas on 1/10/17.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/county")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CountyController {

    private DistrictService districtService;

    @Autowired
    public CountyController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping()
    public List<CountyDTO> getCounties() {
        return districtService.getCounties().stream().map(CountyDTO::new).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public CountyDTO getCounty(@PathVariable Long id) {
        return new CountyDTO(districtService.getCounty(id));
    }

    @DeleteMapping("{id}")
    public void deleteCounty(@PathVariable Long id) {
        districtService.deleteCounty(id);
    }

    @PostMapping("{id}")
    public CountyDTO updateCounty(@PathVariable("id") Long id, @Valid @RequestBody CountyData countyData, BindingResult result) {
        if (result.hasErrors()) {
            throw new MultiErrorException("Klaida atnaujinant apylinkę " + countyData.getName(), result.getAllErrors());
        }
        return new CountyDTO(districtService.updateCounty(id, countyData));
    }

}