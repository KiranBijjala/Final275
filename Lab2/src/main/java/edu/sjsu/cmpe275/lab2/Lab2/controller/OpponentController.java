package edu.sjsu.cmpe275.lab2.Lab2.controller;

import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
import edu.sjsu.cmpe275.lab2.Lab2.repository.SponsorRepository;
import edu.sjsu.cmpe275.lab2.Lab2.service.OpponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
//@RequestMapping("/api/v1")
public class OpponentController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    OpponentService opponentService;


    // create opponent
    @RequestMapping(value="/opponents/{firstplayer}/{secondplayer}", method=RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addOpponent(
            @PathVariable Long firstplayer,
            @PathVariable Long secondplayer
    ) {

        System.out.println("inside adding opponent method handler");
        Player p1;
        Player p2;

        if(firstplayer==null || secondplayer == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
        }

        p1 = playerRepository.findByGenId(firstplayer);
        p2 = playerRepository.findByGenId(secondplayer);

        if(p1==null || p2==null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" User does not exist");
        }

        if (p1 == p2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request | Same users");
        }

        return opponentService.addOpponent(firstplayer,secondplayer);
    }

    @RequestMapping(value="/opponents/{firstplayer}/{secondplayer}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> removeOpponent(
            @PathVariable Long firstplayer,
            @PathVariable Long secondplayer
    ) {

        System.out.println("inside removing opponent method handler");
        Player p1;
        Player p2;

        if(firstplayer==null || secondplayer == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Parameter");
        }

        p1 = playerRepository.findByGenId(firstplayer);
        p2 = playerRepository.findByGenId(secondplayer);

        if(p1==null || p2==null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" User does not exist");
        }

        if (p1 == p2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request | Same users");
        }

        return opponentService.removeOpponent(firstplayer,secondplayer);
    }











}