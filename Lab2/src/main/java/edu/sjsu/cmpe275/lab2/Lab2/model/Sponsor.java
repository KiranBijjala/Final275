package edu.sjsu.cmpe275.lab2.Lab2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table (name = "Sponsor")
@JsonIgnoreProperties({"opponent"})
@EntityListeners(AuditingEntityListener.class)
public class Sponsor {

    @Id
    @Column(name = "sponsor_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

//    @OneToMany(mappedBy = "bookCategory", cascade = CascadeType.ALL)
//    private Set<Book> books;

    @Embedded
    private Address address;

//    @Column(name = "beneficiaries", nullable = false)
//    private String beneficiaries;

    @JsonIgnoreProperties({"opponent","sponsor"})
     @OneToMany(fetch = FetchType.EAGER, mappedBy = "sponsor")
     private List<Player> beneficiaries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public Optional<List<Player>> getBeneficiaries() {
        return Optional.ofNullable(beneficiaries);
    }

    public void setBeneficiaries(List<Player> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

}
