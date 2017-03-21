package voting.dto.countyrep;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Created by domas on 1/12/17.
 */

public class CountyRepresentativeData {

    private Long id;

    @NotNull(message = "Vardas būtinas")
    @Length(min = 3, max = 40, message = "Vardo ilgis nuo 3 iki 40 simbolių")
    @Pattern(regexp = "^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ\\s\\-][^qQwWxX0-9]*)$", message = "Netinkamas vardo formatas")
    private String firstName;

    @NotNull(message = "Pavardė būtina")
    @Length(min = 3, max = 40, message = "Pavardės ilgis nuo 3 iki 40 simbolių")
    @Pattern(regexp = "^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ\\s\\-][^qQwWxX0-9]*)$", message = "Netinkamas pavardės formatas")
    private String lastName;

    @NotNull(message = "El. paštas būtinas")
    @Pattern(regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", message = "Netinkamas el. paštas")
    private String email;

    @NotNull(message = "Apylinkės identifikatorius būtinas")
    private Long countyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountyRepresentativeData that = (CountyRepresentativeData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(countyId, that.countyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, countyId);
    }

    @Override
    public String toString() {
        return "CountyRepresentativeData{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", countyId=" + countyId + '}';
    }
}
