package com.nighthawk.spring_portfolio.mvc.qrCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private List<LinkFreq> linkFreqs = new ArrayList<>();

    private String name;

    public QrCode(String Name){
        this.name = Name;
    }

    public void addLink(LinkFreq linkFreq ){
        linkFreqs.add(linkFreq);
        return;
    }
}
