package edu.sjsu.cmpe275.lab2.Lab2.controller;

import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
import edu.sjsu.cmpe275.lab2.Lab2.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Transactional
@RestController
@RequestMapping("/api/v1")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    PlayerService playerService;

    @GetMapping("/")
    public String get() {
        return "Working!";
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

//    @GetMapping("/players/{id}")
//    public ResponseEntity<Player> getPlayersById(@PathVariable(value = "id") Long userId)
//            throws InvalidConfigurationPropertyValueException {
//        Player player =
//                playerRepository
//                        .findById(userId)
//                        .orElseThrow(() -> new InvalidConfigurationPropertyValueException("", null, "User not found on :: " + userId));
//        return ResponseEntity.ok().body(player);
//    }


//    @PostMapping("/createPlayers")
//    public ResponseEntity<?> createPlayer(@Valid @RequestBody Player player){
//        String firstname = player.getFirstName().trim();
//        String lastname = player.getLastName().trim();
//        String email = player.getEmail().trim();
//        String description = player.getDescription().trim();
////        String address = player.getAddress().trim();
////        String [] add = address1.split(",");
//        String street = add[0];
//        String city = add[1];
//        String state = add[2];
//        String zip = add[3];
//        String sponsor = player.getSponsor().trim();
////        String sponsor = "";
//
////        Address address = new Address();
////        address.setStreet(street);
////        address.setCity(city);
////        address.setState(state);
////        address.setZip(zip);
////
////        Address address2 = addressRepository.save(address);
////        long add_id = address2.getId();
//
////        player.setAddress(String.valueOf(add_id));
//
//        if(firstname == null || lastname == null || email == null || firstname.equals("") || lastname.equals("") || email.equals("") ){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
//        }
//        System.out.println("Inside create player method " + player);
//        playerRepository.save(player);
//        return ResponseEntity.status(HttpStatus.CREATED).body(player);
//    }

    // to create a player
    @RequestMapping(value="/player", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPlayer(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("description") Optional<String> description,
            @RequestParam("street") Optional<String> street,
            @RequestParam("city") Optional<String> city,
            @RequestParam("state") Optional<String> state,
            @RequestParam("zip") Optional<String> zip,
            @RequestParam("sponsor") Optional<String> sponsor,
            @RequestParam(value = "format", required=false) Optional<String> format,
            HttpServletRequest request
    ) {
        Map<String, String[]> params = request.getParameterMap();
        Set<String> validParams = new HashSet<>();
        validParams.add("firstname");
        validParams.add("lastname");
        validParams.add("email");
        validParams.add("description");
        validParams.add("city");
        validParams.add("street");
        validParams.add("state");
        validParams.add("zip");
        validParams.add("sponsor");
        validParams.add("format");
        if (params.size() >10 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Extra Parameters are present"); // or do redirect
        }else {
            for(Map.Entry<String, String[]> entry:  params.entrySet()){
                if(!validParams.contains(entry.getKey())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | "+entry.getKey()+" is not valid param");
                }
            }
        }
        System.out.println("inside create sponsor controller");
        return playerService.createPlayer(firstname,lastname,email,description,street,city,state,zip,sponsor,format);


//        return playerService.createPlayer(firstname,lastname,email,description,street,city,state,zip,sponsor);
    }

    // to get player
    @RequestMapping(value="/player/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getPlayer(@PathVariable Long id,
                                       @RequestParam(value = "format", required=false) Optional<String> format){


        return playerService.getPlayer(id,format);
    }

    // to delete player
    @RequestMapping(value="/player/{id}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, })
    public ResponseEntity<?> deletePlayer(@PathVariable Long id, @RequestParam(value = "format", required=false) Optional<String> responseType){
        System.out.println("Inside delete player controller");

        return playerService.deletePlayer(id,responseType);
    }
    //
    // to update player
    @RequestMapping(value="/player/{id}", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePlayer(@PathVariable Long id,
            @RequestParam(value = "firstname",required = true) String firstname,
            @RequestParam(value = "lastname" , required = true) String lastname,
            @RequestParam(value = "email") String email,
            @RequestParam(value ="description", required = false) Optional<String> description,
            @RequestParam(value ="street", required = false) Optional<String> street,
            @RequestParam(value ="city", required = false) Optional<String> city,
            @RequestParam(value ="state", required = false) Optional<String>state,
            @RequestParam(value ="zip", required = false) Optional<String> zip,
            @RequestParam(value ="sponsor", required = false) Optional<String> sponsor,
            @RequestParam(value = "format", required=false) Optional<String> format, HttpServletRequest request
    ) {
        if(id !=(Long)id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request | Id should be of type Long"); // or do redirect
        }
        Map<String, String[]> params = request.getParameterMap();
        Set<String> validParams = new HashSet<>();
        validParams.add("firstname");
        validParams.add("lastname");
        validParams.add("email");
        validParams.add("description");
        validParams.add("city");
        validParams.add("street");
        validParams.add("state");
        validParams.add("zip");
        validParams.add("sponsor");
        validParams.add("format");
//        System.out.printf();
        if (params.size() >10 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Extra Parameters are present"); // or do redirect
        }else {
            for(Map.Entry<String, String[]> entry:  params.entrySet()){
                if(!validParams.contains(entry.getKey())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | "+entry.getKey()+" is not valid param");
                }
            }
        }
        System.out.println("inside update player controller");
        Optional<Sponsor> sp;
        Player p;

        if (sponsor.isPresent()){
            sp =  sponsorRepository.findById(sponsor.get());
            System.out.println("Inside sp");
            if (!sp.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Sponsor does not exist");
            }
            return playerService.updatePlayer(id, firstname,lastname,email,description,street,city,state,zip,sponsor,format);
        }
//        Sponsor empty_sp = null;
        return playerService.updatePlayer(id, firstname,lastname,email,description,street,city,state,zip,sponsor,format);
    }

//    //add opponent
//
//    @RequestMapping(value="/opponents/{id1}/{id2}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> addOpponent(
//            @PathVariable String id1,
//            @PathVariable String id2
//    ) {
//
//        System.out.println("inside create add opponent controller");
//
//        Player p = playerRepository.findByGenId(Long.parseLong(id1));
//        Player p1 = playerRepository.findByGenId(Long.parseLong(id2));
//
//        p.getOpponents().add(p1);
//        p1.getOpponents().add(p);
//
//        System.out.println(p.getOpponents());
//        System.out.println(p1.getOpponents());
//
//        return ResponseEntity.status(HttpStatus.OK).body(p);
////        p.setOpponents(pl);
//
//    }

//    @RequestMapping(value="/opponents/{id1}/{id2}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> addOpponent(
//            @PathVariable String id1,
//            @PathVariable String id2
//    ) {
//
//        System.out.println("inside create add opponent controller");
//
//        Player p = playerRepository.findByGenId(Long.parseLong(id1));
//        Player p1 = playerRepository.findByGenId(Long.parseLong(id2));
//
//        p.getOpponent().get().add(p1);
//        p.getOpponent_of().get().add(p1);
//
//        System.out.println(p.getOpponent());
//        System.out.println(p1.getOpponent_of());
//
//        return ResponseEntity.status(HttpStatus.OK).body(p);
////        p.setOpponents(pl);
//
//    }


    //     to delete opponents
//    @RequestMapping(value="/player/{id1}/{id2}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> deleteOpponent(@PathVariable String id1,@PathVariable String id2){
//        System.out.println("Inside delete player controller");
//
//        return playerService.deleteOpponent(id1,id2);
//    }


}


//package edu.sjsu.cmpe275.lab2.Lab2.controller;
//
//import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
//import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
//import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
//import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
//import edu.sjsu.cmpe275.lab2.Lab2.service.PlayerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@Transactional
//@RestController
//@RequestMapping("/api/v1")
//public class PlayerController {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Autowired
//    private SponsorRepository sponsorRepository;
//
//    @Autowired
//    PlayerService playerService;
//
//    @GetMapping("/")
//    public String get() {
//        return "Working!";
//    }
//
//    @GetMapping("/players")
//    public List<Player> getAllPlayers() {
//        return playerRepository.findAll();
//    }
//
//    @GetMapping("/players/{id}")
//    public ResponseEntity<Player> getPlayersById(@PathVariable(value = "id") Long userId)
//            throws InvalidConfigurationPropertyValueException {
//        Player player =
//                playerRepository
//                        .findById(userId)
//                        .orElseThrow(() -> new InvalidConfigurationPropertyValueException("", null, "User not found on :: " + userId));
//        return ResponseEntity.ok().body(player);
//    }
//
//
////    @PostMapping("/createPlayers")
////    public ResponseEntity<?> createPlayer(@Valid @RequestBody Player player){
////        String firstname = player.getFirstName().trim();
////        String lastname = player.getLastName().trim();
////        String email = player.getEmail().trim();
////        String description = player.getDescription().trim();
//////        String address = player.getAddress().trim();
//////        String [] add = address1.split(",");
////        String street = add[0];
////        String city = add[1];
////        String state = add[2];
////        String zip = add[3];
////        String sponsor = player.getSponsor().trim();
//////        String sponsor = "";
////
//////        Address address = new Address();
//////        address.setStreet(street);
//////        address.setCity(city);
//////        address.setState(state);
//////        address.setZip(zip);
//////
//////        Address address2 = addressRepository.save(address);
//////        long add_id = address2.getId();
////
//////        player.setAddress(String.valueOf(add_id));
////
////        if(firstname == null || lastname == null || email == null || firstname.equals("") || lastname.equals("") || email.equals("") ){
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
////        }
////        System.out.println("Inside create player method " + player);
////        playerRepository.save(player);
////        return ResponseEntity.status(HttpStatus.CREATED).body(player);
////    }
//
//    // to create a player
//    @RequestMapping(value="/player", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createPlayer(
//            @RequestParam(value="firstname", required = true) String firstname,
//            @RequestParam(value = "lastname", required = true) String lastname,
//            @RequestParam(value = "email", required = true) String email,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "street", required = false) String street,
//            @RequestParam(value ="city", required = false) String city,
//            @RequestParam(value ="state", required = false) String state,
//            @RequestParam(value ="zip", required = false) String zip,
//            @RequestParam(value ="sponsor", required = false) String sponsor,
//            @RequestParam(value ="opponent", required = false) String opponent
//    ) {
//
//        System.out.println("inside create player controller");
//        Optional<Sponsor> sp;
//        Player p;
//
//        if(firstname.equals("") || lastname.equals("") || email.equals("") ){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
//        }
//
//        if (!sponsor.equals("")|| (sponsor != null)){
//            sp =  sponsorRepository.findById(sponsor);
//            if (sp == null){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Sponsor does not exist");
//            }
//        }
//
//        p = playerRepository.findByEmail(email);
//        if (p != null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Email already exists");
//        }
//
//        return playerService.createPlayer(firstname,lastname,email,description,street,city,state,zip,sponsor,opponent);
//    }
//
//    // to get player
//    @RequestMapping(value="/player/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> getPlayer(@PathVariable String id,
//                                          @RequestParam(value = "format", required=false) String xml){
//        System.out.println("Inside get player controller");
//        String responseType="json";
//
//        if(xml != null && xml.equalsIgnoreCase("xml")){ // ?xml=true
//            responseType="xml";
//        }
//
//        return playerService.getPlayer(id, responseType);
//    }
//
//    // to update player
//    @RequestMapping(value="/player/{id}", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> updatePlayer(
//            @RequestParam(value="firstname", required = true) String firstname,
//            @RequestParam(value = "lastname", required = true) String lastname,
//            @RequestParam(value = "email", required = true) String email,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "street", required = false) String street,
//            @RequestParam(value ="city", required = false) String city,
//            @RequestParam(value ="state", required = false) String state,
//            @RequestParam(value ="zip", required = false) String zip,
//            @RequestParam(value ="sponsor", required = false) String sponsor,
//            @RequestParam(value ="opponent", required = false) String opponent
//    ) {
////        if(opponent == null){
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | cannot provide opponent parameter");
////        }
//
//        System.out.println("inside update player controller");
//        Optional<Sponsor> sp;
//        Player p;
//
//        if(email.equals("") ){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | email must be present");
//        }
//
//        if (!sponsor.equals("")){
//            sp =  sponsorRepository.findById(sponsor);
//            if (sp == null){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Sponsor does not exist");
//            }
//        }
//
//        return playerService.updatePlayer(firstname,lastname,email,description,street,city,state,zip,sponsor,opponent);
//    }
//
//
//    @DeleteMapping(value = "/player/{id}")
//    public ResponseEntity<Player> deletPlayerById(@PathVariable("id") Long Id) {
//        System.out.println("Fetching & Deleting Player with name " + Id);
//
//        Player player = playerRepository.findByGenId(Id);
//        if (player == null) {
//            System.out.println("Unable to delete Player with name " + Id + " not found");
//            return ResponseEntity.ok().body(player);
//        }
////        Set<Player> list = playerRepository.findBysponsor_name(name);
////        for(Player p: list ){
////            Address addrs = p.getAddress();
////            playerService.updatePlayer(p.getFirstName(),p.getLastName(),p.getEmail(),p.getDescription(),addrs.getStreet(),addrs.getCity(),addrs.getState(),addrs.getZip(),null,null);
////        }
//        playerRepository.deleteById(Id);
//        return ResponseEntity.ok().body(player);
//    }
//
//
//}
