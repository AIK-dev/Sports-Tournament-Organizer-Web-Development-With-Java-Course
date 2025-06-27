package com.tournament_organizer.dto.player;

import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerOutDTO {
    private long id;
    private String firstName;
    private String secondName;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup level;

    private String associatedTeam;

}
