package edu.sjsu.cmpe275.lab2.Lab2.service;

import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
import edu.sjsu.cmpe275.lab2.Lab2.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpponentService {

    @Autowired
    private PlayerRepository playerRepository;

    public ResponseEntity<?> addOpponent(Long firstplayer, Long secondplayer) {

        System.out.println("Inside addOpponent()  Service ");
        Player p1 = playerRepository.findByGenId(firstplayer);
        Player p2 = playerRepository.findByGenId(secondplayer);

        Optional<List<Player>> opponents = p1.getOpponent();
        if(!opponents.get().contains(p2))
        {
            opponents.get().add(p2);
        }
        p1.setOpponent(opponents.get());

        Optional<List<Player>> opponentsreverse = p2.getOpponent();
        if(!opponentsreverse.get().contains(p1))
        {
            opponentsreverse.get().add(p1);
        }
        p2.setOpponent(opponentsreverse.get());

        playerRepository.saveAndFlush(p1);
        playerRepository.saveAndFlush(p2);


        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    public ResponseEntity<?> removeOpponent(Long firstplayer, Long secondplayer) {

        System.out.println("Inside removeOpponent()  Service ");
        Player p1 = playerRepository.findByGenId(firstplayer);
        Player p2 = playerRepository.findByGenId(secondplayer);

        Optional<List<Player>> opponents = p1.getOpponent();
        if(opponents.get().contains(p2))
        {
            opponents.get().remove(p2);
            p1.setOpponent(opponents.get());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The two players are not opponents!");
        }

        Optional<List<Player>> opponentsreverse = p2.getOpponent();
        if(opponentsreverse.get().contains(p1))
        {
            opponentsreverse.get().remove(p1);
            p2.setOpponent(opponentsreverse.get());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The two players are not opponents!");
        }

        playerRepository.saveAndFlush(p1);
        playerRepository.saveAndFlush(p2);


        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}