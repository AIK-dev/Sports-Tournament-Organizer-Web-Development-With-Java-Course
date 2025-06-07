package com.tournament_organizer.entity;

import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String sport;
    private int participationLimit;
    private Date startDate;
    private Date endDate;
    @OneToMany
    private ArrayList<Venue> venues;
    private String rules;
}
