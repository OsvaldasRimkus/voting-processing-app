package voting.model;

import voting.utils.DateUtils;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by domas on 1/10/17.
 */
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String personId;
    private String firstName;
    private String lastName;

    @ManyToOne
    private District district;

    @ManyToOne
    private Party party;

    private Long positionInPartyList;
    private String description;
    private Calendar birthDate;

    public Candidate() { }

    public Candidate(String firstName, String lastName, String personId, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.description = description;
        this.birthDate = DateUtils.stringToCalendar(personId);
    }

    public Candidate(String firstName, String lastName, String personId, String description, Long positionInPartyList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.description = description;
        this.birthDate = DateUtils.stringToCalendar(personId);
        this.positionInPartyList = positionInPartyList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Long getPositionInPartyList() {
        return positionInPartyList;
    }

    public void setPositionInPartyList(Long positionInPartyList) {
        this.positionInPartyList = positionInPartyList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(id, candidate.id) &&
                Objects.equals(personId, candidate.personId) &&
                Objects.equals(firstName, candidate.firstName) &&
                Objects.equals(lastName, candidate.lastName) &&
                Objects.equals(positionInPartyList, candidate.positionInPartyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personId, firstName, lastName, positionInPartyList);
    }

    @Override
    public String toString() {
        return String.format("%s %s (a.k. %s)", firstName, lastName, personId, DateUtils.stringifyCalendar(birthDate, "-"));
    }
}
