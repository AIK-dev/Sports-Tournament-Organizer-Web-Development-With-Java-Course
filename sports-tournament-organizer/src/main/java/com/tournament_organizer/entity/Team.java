package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "teams")
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "ageGroup", nullable = false)
    private String ageGroup;
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    public Team() {

    }

    public Team(String name, String ageGroup, String type) {
        this.players = new ArrayList<Player>();
        this.name = name;
        this.ageGroup = ageGroup;
        this.type = type;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setTeam(this);
    }
    public void removePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
    }
    @Override
    public String toString() {
        return "Team [" +
                "id=" + id +
                ", Team Name ='" + name + '\'' +
                ", Age Group ='" + ageGroup + '\'' +
                ", Team Type =" + type +
                ", Team Roster = ['" + "TBD" + '\'' + ']' +
                ']';
    }
}