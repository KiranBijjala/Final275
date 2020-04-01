package edu.sjsu.cmpe275.lab2.Lab2.repository;

import edu.sjsu.cmpe275.lab2.Lab2.model.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, String> {

    public Sponsor findByName(String s);
//    public Sponsor deleteByName(String s);


}
