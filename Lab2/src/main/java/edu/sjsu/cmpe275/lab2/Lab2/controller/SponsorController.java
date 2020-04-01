package edu.sjsu.cmpe275.lab2.Lab2.controller;

import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
import edu.sjsu.cmpe275.lab2.Lab2.service.PlayerService;
import edu.sjsu.cmpe275.lab2.Lab2.service.SponsorService;
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
public class SponsorController  {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private SponsorService sponsorService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/test")
    public String get() {
        return "Working!";
    }

    @GetMapping("/sponsors")
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @GetMapping("/sponsor/{name}")
    public ResponseEntity<?> getSponsorByName(@PathVariable(value = "name") String name,  @RequestParam(value = "format", required=false) Optional<String> format){
        System.out.println("Inside get sponsor by name"+name);
        return sponsorService.getSponsor(name,format);
    }

    @RequestMapping(value="/sponsor" , method= RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSponsor(
            @RequestParam(value ="name",required = false) String name,
            @RequestParam(value ="description",required =false) Optional<String> description,
            @RequestParam(value ="street",required =false) Optional<String> street,
            @RequestParam(value ="city",required =false) Optional<String> city,
            @RequestParam(value ="state",required =false) Optional<String> state,
            @RequestParam(value ="zip",required =false) Optional<String> zip,
            @RequestParam(value = "format", required=false) Optional<String> format,
            HttpServletRequest request
    ) {
        Map<String, String[]> params = request.getParameterMap();
        Set<String> validParams = new HashSet<>();
        validParams.add("name");
        validParams.add("description");
        validParams.add("city");
        validParams.add("street");
        validParams.add("state");
        validParams.add("zip");
        validParams.add("format");

        if (params.size() >7 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Extra Parameters are present"); // or do redirect
        }else {
            for(Map.Entry<String, String[]> entry:  params.entrySet()){
                if(!validParams.contains(entry.getKey())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | "+entry.getKey()+" is not valid param");
                }
            }
        }
        System.out.println("inside create sponsor controller");
        return sponsorService.createSponsor(name,description,street,city,state,zip,format);
    }

    @RequestMapping(value="/sponsor/{name}" ,  method= RequestMethod.PUT, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSponsor(
            @PathVariable("name") String name,
            @RequestParam(value ="description",required =false) Optional<String> description,
            @RequestParam(value ="street",required =false) Optional<String> street,
            @RequestParam(value ="city",required =false) Optional<String> city,
            @RequestParam(value ="state",required =false) Optional<String> state,
            @RequestParam(value ="zip",required =false) Optional<String> zip,
            @RequestParam(value = "format", required=false) Optional<String> format,
            HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Set<String> validParams = new HashSet<>();

        validParams.add("description");
        validParams.add("city");
        validParams.add("street");
        validParams.add("state");
        validParams.add("zip");
        validParams.add("format");

        if (params.size() >6 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Extra Parameters are present"); // or do redirect
        }else {
            for(Map.Entry<String, String[]> entry:  params.entrySet()){
                if(!validParams.contains(entry.getKey())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter | "+entry.getKey()+" is not valid param");
                }
            }
        }
        return sponsorService.updateSponsor(name,description,street,city,state,zip,format);
    }

    @DeleteMapping(value = "/sponsor/{name}")
    public ResponseEntity<?> deleteSponsorByName(@PathVariable("name") String name,  @RequestParam(value = "format", required=false) Optional<String> responseType) {
        System.out.println("Fetching & Deleting Sponsor with name " + name);
        return sponsorService.deleteSponsor(name, responseType);
    }
}


