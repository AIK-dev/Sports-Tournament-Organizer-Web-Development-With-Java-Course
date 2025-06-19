package com.tournament_organizer.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team implements Serializable {

    private long id;

    private String name;
    private String ageGroup;
    private String type;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<Player>();

    public Team() {

    }

    public Team(String name, String ageGroup, String type) {
        this.players = new ArrayList<Player>();
        this.name = name;
        this.ageGroup = ageGroup;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "ageGroup", nullable = false)
    public String getAgeGroup() {
        return ageGroup;
    }

    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    @Column(name = "players", nullable = false, length = 1024)
    public List<Player> getPlayers() {
        return players;
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


    public void setName(String name) {
        this.name = name;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }



}
