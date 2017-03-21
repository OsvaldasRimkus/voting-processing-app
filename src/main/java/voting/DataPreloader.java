package voting;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import voting.model.*;
import voting.repository.*;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by domas on 1/11/17.
 */

//@Component
public class DataPreloader implements CommandLineRunner {

    private static Random rand = new Random(0);
    private static Faker faker = new Faker();
    private final CountyRepRepository countyRepRepository;
    private final AdminRepository adminRepRepository;
    private final DistrictRepository districtRepository;
    private final PartyRepository partyRepository;
    private final CandidateRepository candidateRepository;
    private final CountyRepository countyRepository;
    private final EntityManager em;

    @Autowired
    public DataPreloader(CountyRepRepository countyRepRepository,
                         DistrictRepository districtRepository,
                         PartyRepository partyRepository,
                         CandidateRepository candidateRepository,
                         AdminRepository adminRepository, CountyRepository countyRepository, EntityManager em) {
        this.countyRepRepository = countyRepRepository;
        this.districtRepository = districtRepository;
        this.partyRepository = partyRepository;
        this.candidateRepository = candidateRepository;
        this.adminRepRepository = adminRepository;
        this.countyRepository = countyRepository;
        this.em = em;
    }

    @Override
    public void run(String... strings) throws Exception {
        //loadStressData();
        /*System.out.println("=======================");
        System.out.println("STRESS LOADED");
        System.out.println("=======================");*/

        District district1 = new District("Vilnius");
        District district2 = new District("Kaunas");
        District district3 = new District("Klaipėda");
        District district4 = new District("Alytus");
        District district5 = new District("Šiauliai");
        District district6 = new District("Panevėžys");
        District district7 = new District("Tauragės");
        District district8 = new District("Plungės");
        District district9 = new District("Kretingos-Palangos");
        District district10 = new District("Kuršo");

        County county1 = new County("Senamiestis", 3000L, faker.address().streetAddress());
        County county2 = new County("Naujamiestis", 3000L, faker.address().streetAddress());
        County county3 = new County("Pašilaičiai", 3000L, faker.address().streetAddress());
        County county4 = new County("Senamiestis", 3000L, faker.address().streetAddress());
        County county5 = new County("Šilainiai", 3000L, faker.address().streetAddress());
        County county6 = new County("Barstyčių", 10000L, faker.address().streetAddress());
        County county7 = new County("Gėsalų", 3000L, faker.address().streetAddress());
        County county8 = new County("Kūlupėnų", 3000L, faker.address().streetAddress());
        County county9 = new County("Laisvės", 3000L, faker.address().streetAddress());
        County county10 = new County("Lazdininkų", 3000L, faker.address().streetAddress());
        County county11 = new County("Leliūnų", 3000L, faker.address().streetAddress());
        County county12 = new County("Luknės", 10000L, faker.address().streetAddress());
        County county13 = new County("Mosėdžio", 3000L, faker.address().streetAddress());
        County county14 = new County("Nasrėnų", 3000L, faker.address().streetAddress());
        County county15 = new County("Pašilės", 3000L, faker.address().streetAddress());
        County county16 = new County("Rubulių", 3000L, faker.address().streetAddress());
        County county17 = new County("Šačių", 3000L, faker.address().streetAddress());
        County county18 = new County("Skuodo", 10000L, faker.address().streetAddress());
        County county19 = new County("Šliktinės", 3000L, faker.address().streetAddress());
        County county20 = new County("Trumplaukės", 3000L, faker.address().streetAddress());
        County county21 = new County("Vilniaus", 3000L, faker.address().streetAddress());
        County county22 = new County("Ylakių", 3000L, faker.address().streetAddress());
        County county23 = new County("Žvainių", 3000L, faker.address().streetAddress());
        County county24 = new County("Gabijos", 10000L, faker.address().streetAddress());
        County county25 = new County("Kęstučio", 3000L, faker.address().streetAddress());
        County county26 = new County("Leckavos", 3000L, faker.address().streetAddress());
        County county27 = new County("Pavasario", 3000L, faker.address().streetAddress());
        County county28 = new County("Reivyčių", 3000L, faker.address().streetAddress());
        County county29 = new County("Senamieščio", 3000L, faker.address().streetAddress());
        County county30 = new County("Sodų", 10000L, faker.address().streetAddress());

        List<County> countyList1 = new ArrayList<County>(Arrays.asList(county1, county2, county3));
        List<County> countyList2 = new ArrayList<County>(Arrays.asList(county4, county5));
        List<County> countyList3 = new ArrayList<County>(Arrays.asList(county6, county27, county28, county29, county30));
        List<County> countyList4 = new ArrayList<County>(Arrays.asList(county7, county8));
        List<County> countyList5 = new ArrayList<County>(Arrays.asList(county9, county10));
        List<County> countyList6 = new ArrayList<County>(Arrays.asList(county11, county24, county25, county26));
        List<County> countyList7 = new ArrayList<County>(Arrays.asList(county12, county13, county14, county19));
        List<County> countyList8 = new ArrayList<County>(Arrays.asList(county15, county16));
        List<County> countyList9 = new ArrayList<County>(Arrays.asList(county17, county18));
        List<County> countyList10 = new ArrayList<County>(Arrays.asList(county21, county22, county20, county23));

        countyList1.forEach(district1::addCounty);
        countyList2.forEach(district2::addCounty);
        countyList3.forEach(district3::addCounty);
        countyList4.forEach(district4::addCounty);
        countyList5.forEach(district5::addCounty);
        countyList6.forEach(district6::addCounty);
        countyList7.forEach(district7::addCounty);
        countyList8.forEach(district8::addCounty);
        countyList9.forEach(district9::addCounty);
        countyList10.forEach(district10::addCounty);

        List<District> districts = new ArrayList<District>(Arrays.asList(
                new District[]{
                        district1,
                        district2,
                        district3,
                        district4,
                        district5,
                        district6,
                        district7,
                        district8,
                        district9,
                        district10
                }
        ));

        districtRepository.save(districts);

        List<County> counties = new ArrayList<>(countyList1);
        counties.addAll(countyList2);
        counties.addAll(countyList3);
        counties.addAll(countyList4);
        counties.addAll(countyList5);
        counties.addAll(countyList6);
        counties.addAll(countyList7);
        counties.addAll(countyList8);
        counties.addAll(countyList9);
        counties.addAll(countyList10);

        List<CountyRep> reps = new ArrayList<>();
        IntStream.range(0, 29).forEach(i -> {
            String _1name1 = faker.name().firstName();
            String _2name1 = faker.name().firstName();
            reps.add(new CountyRep(
                    _1name1,
                    _2name1,
                    faker.internet().emailAddress(_1name1.toLowerCase().concat(".").concat(_2name1.toLowerCase())),
                    "pass",
                    counties.get(i),
                    new String[]{"ROLE_REPRESENTATIVE"}
            ));
        });

        reps.add(new CountyRep(
                        "Faker",
                        "Faker",
                        faker.internet().emailAddress("faker.faker"),
                        "faker",
                        counties.get(counties.size()-1),
                        new String[]{"ROLE_REPRESENTATIVE"}
                )
        );

        countyRepRepository.save(reps);
        Admin admin = new Admin("Admin", "Admin", "admin@admin.lt", new String[]{"ROLE_ADMIN"});
        adminRepRepository.save(admin);

        List<Party> parties = createParties();
        List<Candidate> candidates = new ArrayList<>();

        for (int i = 0; i < parties.size(); i++) {
            for (int j = 0; j < 20; j++) {
                Candidate c = new Candidate(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        generateRandomPersonId(),
                        faker.shakespeare().hamletQuote()
                );
                candidates.add(c);
                parties.get(i).addCandidate(c);
            }
        }

        for (int i = 0; i < districts.size(); i++) {
            for (int j = 0; j < 10; j++) {
                Candidate c = new Candidate(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        generateRandomPersonId(),
                        faker.shakespeare().hamletQuote()
                );
                Candidate cc = new Candidate(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        generateRandomPersonId(),
                        faker.shakespeare().hamletQuote()
                );
                districts.get(i).addCandidate(cc);
                districts.get(i).addCandidate(c);
                parties.get(i).addCandidate(c);
                candidates.add(cc);
                candidates.add(c);
            }
        }

        partyRepository.save(parties);
        candidateRepository.save(candidates);
    }

    public static String generateRandomPersonId() {
        StringBuilder id = new StringBuilder();
        id.append(rand.nextInt(2) + 3);
        int year = rand.nextInt(100);
        id.append((year >= 10) ? year : "0"+year);
        int month = rand.nextInt(12) + 1;
        id.append((month >= 10) ? month : "0"+month);
        int bound = (month == 2) ? 28 : 31;
        int day = rand.nextInt(bound) + 1;
        id.append((day >= 10) ? day : "0"+day);
        rand.ints(4, 0, 10).forEach(digit -> id.append(digit));
        return id.toString();
    }

    private void loadStressData() {
        //GENERATES DISTRICTS, COUNTIES AND THEIR VOTER_COUNT
        String districtsFile = "src/main/resources/districts_and_counties.csv";
        //String representativesFile = "src/main/resources/county_representatives.csv";

        BufferedReader fileReader = null;
        //BufferedReader fileReader2 = null;

        final String DELIMITER = ";";
        List<CountyRep> allCountyRepresentatives = new ArrayList<CountyRep>();

        try {
            String line = "";
            fileReader = new BufferedReader(new FileReader(districtsFile));
            //fileReader2 = new BufferedReader(new FileReader(representativesFile));
            long linesCount = fileReader.lines().count();
            fileReader.close();
            fileReader = new BufferedReader(new FileReader(districtsFile));
            String currentDistrictName = "";
            String currentCountyName = "";
            String currentCountyAddress = "";
            String districtName = "";
            String countyName = "";
            String countyAddress = "";

            /*String representativeName = "";
            String representativeSurname = "";
            String representativeEmail = "";
            String representativePassword = "";*/

            long currentVotersCount = 0;
            long votersCount = 0;
            District currentDistrictObject = null;
            int lineNumber = 0;
            //int countyCount = 0;
            while ((line = fileReader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) { continue; }
                int tokenNumber = 0;
                String[] tokens = line.split(DELIMITER);

                for (String token : tokens) {
                    switch (tokenNumber) {
                        case 1:
                            districtName = token;
                            break;
                        case 3:
                            countyName = token;
                            break;
                        case 4:
                            countyAddress = token;
                            break;
                        case 5:
                            votersCount = Integer.parseInt(token);
                            break;
                    }
                    tokenNumber++;
                }
                if (currentDistrictName.equals("") && currentCountyName.equals("") && currentCountyAddress.equals("")) {
                    currentDistrictName = districtName;
                    currentCountyName = countyName;
                    currentCountyAddress = countyAddress;
                    currentDistrictObject = new District(districtName);
                }
                if (!countyName.equals(currentCountyName)) {
                    currentVotersCount = currentVotersCount + votersCount;
                    currentCountyAddress = currentCountyAddress.substring(9);
                    County currentCountyObject = new County(currentCountyName, currentVotersCount, currentCountyAddress);
                    currentDistrictObject.addCounty(currentCountyObject);

                    // GENERATES COUNTY REPRESENTATIVE FOR CURRENT COUNTY OBJECT
                    /*if ((line = fileReader2.readLine()) != null) {
                        int tokenNumber2 = 0;
                        String[] tokens2 = line.split(DELIMITER);
                        for (String token : tokens2) {
                            switch (tokenNumber2) {
                                case 0:
                                    representativeName = token;
                                    break;
                                case 1:
                                    representativeSurname = token;
                                    break;
                                case 2:
                                    representativeEmail = token;
                                    break;
                                case 3:
                                    representativePassword = token;
                                    break;
                            }
                            tokenNumber2++;
                        }
                    }*/

                    String fName = faker.name().firstName();
                    String lName = faker.name().lastName();
                    String email = faker.internet().emailAddress(fName + "." + lName);
                    String password = "password";

                    CountyRep currentCountyRepresentative = new CountyRep(
                            fName,
                            lName,
                            email,
                            password,
                            currentCountyObject,
                            "ROLE_REPRESENTATIVE"
                    );

                    allCountyRepresentatives.add(currentCountyRepresentative);
                    currentVotersCount = 0;
                    //countyCount++;
                } else {
                    currentVotersCount = currentVotersCount + votersCount;
                }

                if (!districtName.equals(currentDistrictName) || linesCount == lineNumber + 1) {
                    districtRepository.save(currentDistrictObject);
                    currentDistrictObject = new District(districtName);
                    //int countFromRepository = StreamSupport.stream(districtRepository.findAll().spliterator(), false).mapToInt(d -> d.getCounties().size()).sum();
                }

                if (!currentDistrictName.equals(districtName)) {
                    currentDistrictName = districtName;
                }

                if (!currentCountyName.equals(countyName)) {
                    currentCountyName = countyName;
                }

                if (!currentCountyAddress.equals(countyAddress)) {
                    currentCountyAddress = countyAddress;
                }
            }
            countyRepRepository.save(allCountyRepresentatives);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*try {
                fileReader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        System.out.println("=======================");
        System.out.println("territorials created");

        List<Party> parties = createParties();
        System.out.println("=====================");
        System.out.println("parties created");

        List<Candidate> candidates = new ArrayList<>();

        parties.forEach(p -> createCandidates(p, candidates));
        System.out.println("=====================");
        System.out.println("candidates created");

        List<District> districts = new ArrayList<>();
        districtRepository.findAll().forEach(districts::add);
        Random randomizer = new Random();
        int delegates = randomizer.nextInt(parties.size()) + 1;
        districts.forEach(d -> {
           List<Party> randParties = getRandomParties(parties, delegates);

           randParties.forEach(p -> {
               List<Candidate> partyCandidates = p.getCandidates();
               Candidate candidate = candidateRepository.save(
                       new Candidate(
                           faker.name().firstName(),
                           faker.name().lastName(),
                           generateRandomPersonId(),
                           faker.shakespeare().kingRichardIIIQuote(),
                           (long) partyCandidates.size() + 1
                       )
               );
               p.addCandidate(candidate);
               d.addCandidate(candidate);

           });
           IntStream.range(0, delegates).forEach(i -> {
               Candidate candidate = candidateRepository.save(new Candidate(
                       faker.name().firstName(),
                       faker.name().lastName(),
                       generateRandomPersonId(),
                       faker.shakespeare().kingRichardIIIQuote()
               ));
               d.addCandidate(candidate);
           });
        });
        districts.forEach(d -> em.persist(em.merge(d)));
        parties.forEach(p -> em.persist(em.merge(p)));
        System.out.println("=====================");
        System.out.println("SM candidates assigned");

        /*List<County> counties = new ArrayList<>();
        countyRepository.findAll().forEach(counties::add);
        counties.forEach(c -> {
            c.setSmResult(new CountySMResult());
        });

        countyRepository.save(counties);*/
    }

    private List<Party> getRandomParties(List<Party> list, int num) {
        Set<Party> set = new HashSet<>();
        Random randomizer = new Random();
        IntStream.range(0, num).forEach(i -> set.add((Party)list.get(randomizer.nextInt(num))));
        return new ArrayList<>(set);
    }

    private List<Party> createParties() {
        Party party1 = new Party("Soc-demai");
        Party party2 = new Party("TT");
        Party party3 = new Party("Liberalai");
        Party party4 = new Party("Už Lietuvą");
        Party party5 = new Party("Žalioji");
        Party party6 = new Party("Reformų");
        Party party7 = new Party("Žemaičių");
        Party party8 = new Party("Žmonių");
        Party party9 = new Party("LS");
        Party party10 = new Party("Frontas");

        List<Party> parties = new ArrayList<>(
                Arrays.asList(new Party[]{party1, party2, party3, party4, party5, party6, party7, party8, party9, party10})
        );
        partyRepository.save(parties);
        return parties;
    }

    private void createCandidates(Party party, List<Candidate> candidates) {
        IntStream.range(0, 70).forEach(i -> {
            Candidate candidate = new Candidate(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    generateRandomPersonId(),
                    faker.shakespeare().kingRichardIIIQuote(),
                    (long) i+1
            );
            party.addCandidate(candidate);
            candidates.add(candidate);
        });
        partyRepository.save(party);
        candidateRepository.save(candidates);
    }
}
