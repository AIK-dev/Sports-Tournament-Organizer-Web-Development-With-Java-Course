package com.tournament_organizer.enums;

public enum Gender {
    MALE, FEMALE;

    public TeamType toTeamType() {
        switch (this) {
            case MALE:
                return TeamType.MALE;
            case FEMALE:
                return TeamType.FEMALE;
            default:
                throw new IllegalArgumentException("Unexpected Gender: " + this);
        }
    }

}
