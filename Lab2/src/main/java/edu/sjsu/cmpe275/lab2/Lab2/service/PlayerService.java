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

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;


    public ResponseEntity<?> returnType(Optional<String> format, Player player ,String type ){
        String frmt;
        if(format.isPresent()) {
             frmt = format.get().trim();
        }else{
            frmt="json";
        }
        switch (type.toUpperCase()) {

            case "FOUND":
                if (format.isPresent() && frmt.equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_XML).body(player);
                else
                    return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON).body(player);
            case "CREATED":
                if (format.isPresent() && frmt.equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_XML).body(player);
                else
                    return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(player);
            case "OK":
                if (format.isPresent() && frmt.equalsIgnoreCase("xml"))
                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(player);
                else
                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(player);
            default:
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(player);
        }

    }

    public ResponseEntity<?> createPlayer(String firstname, String lastname,
                                          String email, Optional<String> description, Optional<String> street, Optional<String> city,
                                          Optional<String> state, Optional<String> zip, Optional<String> sponsor, Optional<String>format) {
        System.out.println("inside create player service");

        Optional<Sponsor> sp;
        Player validatePlayerExists;
        Player newPlayer = new Player();

        if(firstname.equals("") || lastname.equals("") || email.equals("") ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
        }

        if (sponsor.isPresent()){
            sp = sponsorRepository.findById(sponsor.get().trim());
            if (!sp.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Sponsor does not exist");
            }
        }

        validatePlayerExists = playerRepository.findByEmail(email.trim());
        if (validatePlayerExists!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Bad Parameter | Email already exists");
        }

        if (city.isPresent() || state.isPresent() || street.isPresent() || zip.isPresent()) {

            Address address = new Address();
            if (city.isPresent()) {
                address.setCity(city.get().trim());

            }
            if (state.isPresent()) {
                address.setState(state.get().trim());

            }
            if (street.isPresent()) {
                address.setStreet(street.get().trim());

            }
            if (zip.isPresent()) {
                address.setZip(zip.get().trim());
            }

            if(address==null){
                System.out.println(address.toString());
            }
            newPlayer.setAddress(address);
        }


        newPlayer.setFirstName(firstname.trim());
        newPlayer.setLastName(lastname.trim());
        newPlayer.setEmail(email.trim());
        if(description.isPresent()) {
            newPlayer.setDescription(description.get().trim());
        }
        if(sponsor.isPresent()) {
            newPlayer.setSponsor(sponsorRepository.findByName(sponsor.get().trim()));
        }

        playerRepository.saveAndFlush(newPlayer);
//        return ResponseEntity.status(HttpStatus.CREATED).body(player);
        return returnType(format, newPlayer,"OK");
    }

    public ResponseEntity<?> getPlayer(Long id, Optional<String> format){
        System.out.println("getPlayer() service");
        Player player = playerRepository.findByGenId(id);
        if(player == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player with " + id + "not found");
        }

        return returnType(format, player,"OK");
    }

    public ResponseEntity<?> updatePlayer(Long id, String  firstname, String lastname,
                                          String email, Optional<String> description, Optional<String> street, Optional<String> city,
                                          Optional<String> state, Optional<String> zip, Optional<String> sponsor, Optional<String>format) {
        //topics.add(topic);
        System.out.println("inside updatePlayer() service");

        System.out.println("");

        Player player = playerRepository.findByGenId(id);

        if(player==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad Parameter | Player not found");
        }

        Player playerWithNewEmailExists = playerRepository.findByEmail(email.trim());
        if(playerWithNewEmailExists!=null && !playerWithNewEmailExists.getEmail().equalsIgnoreCase(player.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | Email already exists");
        }
        Optional<Sponsor> sponsor1 ;
        player.setEmail(email.trim());
        player.setFirstName(firstname.trim());
        player.setLastName(lastname.trim());
        if (city.isPresent() || state.isPresent() || street.isPresent() || zip.isPresent()) {
            Optional<Address> address = Optional.of(new Address());

            if (city.isPresent()) {
                address.get().setCity(city.get().trim());
            }else{
                address.get().setCity(null);
            }
            if (state.isPresent()) {
                address.get().setState(state.get().trim());
            }else{
                address.get().setState(null);
            }
            if (street.isPresent()) {
                address.get().setStreet(street.get().trim());
            }else{
                address.get().setStreet(null);
            }
            if (zip.isPresent()) {
                address.get().setZip(zip.get().trim());
            }else{
                address.get().setZip(null);
            }


            player.setAddress(address.get());
        }else{
            player.setAddress(null);
        }



        if (description.isPresent()) {
            player.setDescription(description.get().trim());
        }else{
            player.setDescription(null);
        }

        if(sponsor.isPresent()){
            sponsor1 = sponsorRepository.findById(sponsor.get().trim());
            if (sponsor1.isPresent()) {
                player.setSponsor(sponsor1.get());
            }
        }else{
            player.setSponsor(null);
        }

        playerRepository.saveAndFlush(player);
        return returnType(format, player,"OK");
    }

    public ResponseEntity<?> deletePlayer(Long id ,Optional<String> format) {
        System.out.println("inside delete player service");
        Optional<Player> player = playerRepository.findById(id);
        if(!player.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error : Player Not Found");
        }

        playerRepository.delete(player.get());

        return returnType(format, player.get(), "OK");

//        Optional<Player> player = playerRepository.findById(id);
//        if (!player.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error : Player Not Found");
//        }
//        playerRepository.deleteById(id);
//        System.out.println(player.get());
//        return returnType(format, player.get(),"OK");
    }



    //delete opponents
//    public ResponseEntity<?> deleteOpponent(String id1, String id2) {
//        System.out.println("inside delete player service");
//        Player player1 = playerRepository.findByGenId(Long.parseLong(id1));
//        Player player2 = playerRepository.findByGenId(Long.parseLong(id2));
//
//        Optional<List<Player>> l1 = player1.getOpponent();
//        Optional<List<Player>> l2 = player1.getOpponent_of();
//
//        if (l1.get().contains(player2)){
//            l1.get().remove(player2);
//            l2.get().remove(player2);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body("Removed");
//    }
}



//package edu.sjsu.cmpe275.lab2.Lab2.service;
//
//import edu.sjsu.cmpe275.lab2.Lab2.model.Address;
//import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
//import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
//import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
//import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class PlayerService {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Autowired
//    private SponsorRepository sponsorRepository;
//
//    public ResponseEntity<?> createPlayer(String firstname, String lastname,
//                                          String email, String description, String street, String city,
//                                          String state, String zip, String sponsor, String opponent) {
////        System.out.println("inside create player service" +name);
//        Optional<Sponsor> sponsor1 = sponsorRepository.findById(sponsor);
//        List<Player> opponentSet = new ArrayList<>();
//        Player opponent1 = playerRepository.findByEmail(opponent);
//        opponentSet.add(opponent1);
//        Address address = new Address();
//        address.setStreet(street.trim());
//        address.setCity(city.trim());
//        address.setState(state.trim());
//        address.setZip(zip.trim());
//
//        Player player = new Player();
//        player.setFirstName(firstname.trim());
//        player.setLastName(lastname.trim());
//        player.setEmail(email.trim());
//        player.setAddress(address);
//        player.setDescription(description.trim());
//        player.setSponsor(sponsor1.get());
//        player.setOpponent(opponentSet);
//
////        player.setSponsor_name(sponsor.trim());
//
//        playerRepository.saveAndFlush(player);
//        return ResponseEntity.status(HttpStatus.CREATED).body(player);
//    }
//
//    public ResponseEntity<?> getPlayer(String id, String responseType){
////        System.out.println("getPlayer() service");
//        Player player = playerRepository.findByGenId(Long.parseLong(id));
//        if(player != null){
//            if(responseType.equals("xml")){
//                return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_XML).body(player);
//            }
//            else
//            {
//                return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON).body(player);
//            }
//        } else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player with " + id + "not found");
//        }
//    }
//
//    public ResponseEntity<?> updatePlayer(String firstname, String lastname,
//                                          String email, String description, String street, String city,
//                                          String state, String zip, String sponsor, String opponent) {
//        //topics.add(topic);
//        System.out.println("inside updatePlayer() service");
//        Player player = playerRepository.findByEmail(email);
//        Optional<Sponsor> sponsor1 = sponsorRepository.findById(sponsor);
//        JSONObject json = new JSONObject();
//        List<Player> opponentSet;
//        if(player == null){
////            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
//            opponentSet= new ArrayList<>();
//        }else{
//            opponentSet = (List<Player>) player.getOpponent();
//        }
//        System.out.println("Updating User");
//
//            Address address = new Address();
//            address.setStreet(street.trim());
//            address.setCity(city.trim());
//            address.setState(state.trim());
//            address.setZip(zip.trim());
//
//            player.setFirstName(firstname.trim());
//            player.setLastName(lastname.trim());
//            player.setEmail(email.trim());
//            player.setAddress(address);
//            player.setDescription(description.trim());
//            player.setSponsor(sponsor1.get());
//
//            Player opponent1 = playerRepository.findByEmail(opponent);
//            opponentSet.add(opponent1);
//            player.setOpponent(opponentSet);
//            playerRepository.save(player);
//            return ResponseEntity.status(HttpStatus.OK).body(player);
//    }
//
//    public String playerToJSONString(Player player){
//        JSONObject result = new JSONObject();
//        System.out.println("inside playerToJSONString()#####");
//
//
//        System.out.println("inside playerToJSONString() try");
//
//
//        Map fields = new LinkedHashMap();
//        Map add = new LinkedHashMap();
//
//        fields.put("id", ""+player.getGenId());
//        fields.put("firstname", player.getFirstName());
//        fields.put("lastname", player.getLastName());
//        fields.put("email", ""+player.getEmail());
//        fields.put("description", player.getDescription());
//
//        fields.put("address", add);
//
//        add.put("street",player.getAddress().getStreet());
//        add.put("city",player.getAddress().getCity());
//        add.put("state",player.getAddress().getState());
//        add.put("zip",player.getAddress().getZip());
//
//
//        JSONObject field = new JSONObject(fields);
//
//        System.out.println(field.toString() );
//        System.out.println(result );
//
//
//        return field.toString();
//    }
//
//    public JSONObject sponsorToJSONString(Sponsor sponsor){
//        JSONObject result = new JSONObject();
//        JSONObject reservationsJSON = new JSONObject();
//        JSONObject arr[] = null;
//        System.out.println("inside sponsorToJSONString()#####");
//
//
//        System.out.println("inside sponsorToJSONString() try");
//
//
//        Map fields = new LinkedHashMap();
//        Map add = new LinkedHashMap();
//
//        System.out.println(sponsor.getName());
//
//
//        fields.put("name", sponsor.getName());
//        if(sponsor.getDescription().isPresent()) {
//            fields.put("description", sponsor.getDescription());
//        }
//
//        if(sponsor.getAddress().isPresent()) {
//            add.put("street", sponsor.getAddress().get().getStreet());
//            add.put("city", sponsor.getAddress().get().getCity());
//            add.put("state", sponsor.getAddress().get().getState());
//            add.put("zip", sponsor.getAddress().get().getZip());
//        }
//        fields.put("address", add);
//        JSONObject field = new JSONObject(fields);
//
//        System.out.println(field.toString());
//        System.out.println(result );
//
//        return field;
//    }
//}
