package edu.sjsu.cmpe275.lab2.Lab2.repository;

import edu.sjsu.cmpe275.lab2.Lab2.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    public Player findByEmail(String email);
    public Player findByGenId(Long genId);
    public Set<Player> findBysponsor_name(String sponsor_name);

}
