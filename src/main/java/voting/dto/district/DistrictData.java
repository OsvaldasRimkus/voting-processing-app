package voting.dto.district;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import voting.dto.county.CountyData;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

/**
 * Created by domas on 1/12/17.
 */

public class DistrictData {

    private Long id;

    @NotNull(message = "Apygardos pavadinimas būtinas")
    @Length(min = 3, max = 40, message = "Apygardos pavadinimas nuo 3 iki 40 simbolių")
    @Pattern(regexp = "^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ\\s\\-][^qQwWxX0-9]*)$", message = "Netinkamas apygardos pavadinimas")
    private String name;

    @Valid
    @JsonProperty("counties")
    private List<CountyData> countiesData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CountyData> getCountiesData() {
        return countiesData;
    }

    public void setCountiesData(List<CountyData> countiesData) {
        this.countiesData = countiesData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictData that = (DistrictData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(countiesData, that.countiesData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countiesData);
    }

    @Override
    public String toString() {
        return "DistrictData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countiesData=" + countiesData +
                '}';
    }
}
