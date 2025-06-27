package com.tournament_organizer.entity;

import com.tournament_organizer.enums.Sport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @ElementCollection(targetClass = Sport.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "venue_sports",
            joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "sport", nullable = false)
    private Set<Sport> supportedSports = new HashSet<>();

    @ManyToMany(mappedBy = "venues")
    private List<Tournament> tournaments = new ArrayList<>();
}
