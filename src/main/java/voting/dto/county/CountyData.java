package voting.dto.county;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Created by domas on 1/12/17.
 */

public class CountyData {

    @NotNull(message = "Pavadinimas būtinas")
    @Length(min=3, max=40, message = "Apylinkės pavadinimas nuo 3 iki 40 simbolių")
    @Pattern(regexp = "^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ0-9\\s\\-][^qQwWxX]*)$", message = "Netinkamas apylinkės pavadinimas")
    private String name;

    @Min(value = 100, message = "Mažiausiai balsuotojų - 100")
    @Max(value = 100000, message = "Daugiausiai balsuotojų - 100_000")
    private Long voterCount;

    @NotNull(message = "Adresas būtinas")
    @Length(min = 6, message = "Adreso ilgis nuo 6 iki 70 simbolių")
    private String address;

    private Long districtId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVoterCount() {
        return voterCount;
    }

    public void setVoterCount(Long voterCount) {
        this.voterCount = voterCount;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountyData that = (CountyData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(voterCount, that.voterCount) &&
                Objects.equals(districtId, that.districtId) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, voterCount, districtId, address);
    }

    @Override
    public String toString() {
        return "CountyData{" +
                "name='" + name + '\'' +
                ", voterCount=" + voterCount +
                ", districtId=" + districtId +
                ", address=" + address +
                '}';
    }
}
