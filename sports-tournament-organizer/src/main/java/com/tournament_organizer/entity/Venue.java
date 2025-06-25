package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
//TODO OPTIONAL Add logic from the enum sport like in this venue you can play this and that sport nothing else this is for
// creating a tournament and if the venue is only for football but want to make basketball tournament it will give error
// I think for a list of the Sport and when you create a tournament you will get the sports from this venue and if it
// match with the sport of the tournament it will be 200+ or error
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String city;
    private int capacity;

    @ManyToMany(mappedBy = "venues")
    private List<Tournament> tournaments = new ArrayList<>();
}
