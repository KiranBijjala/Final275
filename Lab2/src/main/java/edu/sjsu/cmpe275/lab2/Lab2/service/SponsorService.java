package edu.sjsu.cmpe275.lab2.Lab2.service;

import edu.sjsu.cmpe275.lab2.Lab2.model.Address;
import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class SponsorService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private PlayerService playerService;


    public ResponseEntity<?> returnType(Optional<String> format, Sponsor sponsor ,String type ){

        switch (type.toUpperCase()) {

            case "FOUND":
                if (format.isPresent() && format.get().equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_XML).body(sponsor);
                else
                    return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON).body(sponsor);
            case "CREATED":
                if (format.isPresent() && format.get().equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_XML).body(sponsor);
                else
                    return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(sponsor);
            case "OK":
                if (format.isPresent() && format.get().equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(sponsor);
                else
                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(sponsor);
            default:
                return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(sponsor);
        }

    }


    public ResponseEntity<?> getSponsor(String name, Optional<String> responseType) {
        Optional<Sponsor> sponsor = sponsorRepository.findById(name);
        if (!sponsor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error : Sponsor Not Found");
        }
        return returnType(responseType, sponsor.get(),"OK");
    }

    public ResponseEntity<?> createSponsor(String name, Optional<String> description, Optional<String> street, Optional<String> city,
                                           Optional<String> state, Optional<String> zip , Optional<String>responseType) {
        System.out.println("Inside create Sponsor service" + name);

        if( name==null || name.isEmpty() ||  name.length()<2 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
        }

        Optional<Sponsor> sp;
        sp =  sponsorRepository.findById(name);
        if (sp.isPresent() ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Bad Parameter | Sponsor already exists");
        }

        Player p;
        Sponsor sponsor = new Sponsor();

        if (city.isPresent() || state.isPresent() || street.isPresent() || zip.isPresent()) {

            Address address = new Address();
            if (city.isPresent()) {
                address.setCity(city.get());
            }
            if (state.isPresent()) {
                address.setState(state.get());
            }
            if (street.isPresent()) {
                address.setStreet(street.get());
            }
            if (zip.isPresent()) {
                address.setZip(zip.get());
            }

            sponsor.setAddress(address);
        }
        System.out.println("reached sponsor.get().getName()");

        if (description.isPresent()) {
            sponsor.setDescription(description.get().trim());
        }
        sponsor.setName(name.trim());

        sponsorRepository.saveAndFlush(sponsor);

        return returnType(responseType, sponsor,"OK");
    }

    public ResponseEntity<?> updateSponsor(String name, Optional<String> description, Optional<String> street, Optional<String> city,
                                           Optional<String> state, Optional<String> zip,Optional<String> responseType) {
        System.out.println("Inside create Sponsor service" + name);

        if( name == null || name.isEmpty() ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter : Invalid name parameter");
        }else if(name.length()<2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter : Name length is less than 2 characters");
        }

        Optional<Sponsor> sponsor = sponsorRepository.findById(name);
        if (!sponsor.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad Parameter | Sponsor doesn't exist");
        }

        if (city.isPresent() || state.isPresent() || street.isPresent() || zip.isPresent()) {
            Optional<Address> address = Optional.of(new Address());

            if (city.isPresent()) {
                address.get().setCity(city.get().trim());
            }
            if (state.isPresent()) {
                address.get().setState(state.get().trim());
            }
            if (street.isPresent()) {
                address.get().setStreet(street.get().trim());
            }
            if (zip.isPresent()) {
                address.get().setZip(zip.get().trim());
            }


        sponsor.get().setAddress(address.get());
    }
        if (description.isPresent()) {
            sponsor.get().setDescription(description.get().trim());
        }
//        sponsor.get().setName(name.trim());

        sponsorRepository.saveAndFlush(sponsor.get());

        return returnType(responseType, sponsor.get(),"OK");
    }

    public ResponseEntity<?> deleteSponsor(String name, Optional<String> format) {
        Optional<Sponsor> sponsor = sponsorRepository.findById(name);
        if (!sponsor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error : Sponsor Not Found");
        }
        Set<Player> list = playerRepository.findBysponsor_name(name);
        for(Player p: list ){
            Optional<Address> addrs = p.getAddress();

            String firstname = p.getFirstName();
            String lastname = p.getLastName();
            Optional<String> street = Optional.ofNullable(addrs.get().getStreet());
            Optional<String> city = Optional.ofNullable(addrs.get().getCity());
            Optional<String> state = Optional.ofNullable(addrs.get().getState());
            Optional<String> zip = Optional.ofNullable(addrs.get().getZip());
            Long id = p.getGenId();
            playerService.updatePlayer(id, firstname,lastname,p.getEmail(),p.getDescription(),street,city,state,zip,null,format);
        }
        sponsorRepository.deleteById(name);
        return returnType(format, sponsor.get(),"OK");
    }

}
