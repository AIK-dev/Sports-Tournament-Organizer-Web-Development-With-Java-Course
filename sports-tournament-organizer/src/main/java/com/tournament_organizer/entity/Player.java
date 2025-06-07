package com.tournament_organizer.entity;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "players")
public class Player implements Serializable {

    private long id;
    private String name;
    private Integer age;
    private String gender;
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Player() {

    }

    public Player(String name, Integer age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }
    
    @Column(name = "age", nullable = false)
    public Integer getAge() {
        return age;
    }
    
    @Column(name = "gender", nullable = false)
    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


    @Override
    public String toString() {
        return "Player [" +
                "id=" + id +
                ", Team Name ='" + name + '\'' +
                ", Age ='" + age + '\'' +
                ", Gender =" + gender + '\''  +
                ']';
    }


}
