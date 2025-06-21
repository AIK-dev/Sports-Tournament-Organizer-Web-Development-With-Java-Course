package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String city;
    private int capacity;

    @ManyToMany(mappedBy = "venues")
    private List<Tournament> tournaments = new ArrayList<Tournament>();
}
