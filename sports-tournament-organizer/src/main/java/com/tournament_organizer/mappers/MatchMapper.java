package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.match.MatchInDTO;
import com.tournament_organizer.dto.match.MatchOutDTO;
import com.tournament_organizer.entity.Match;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.entity.Venue;
import com.tournament_organizer.enums.Result;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.ParticipationRepository;
import com.tournament_organizer.repository.TournamentRepository;
import com.tournament_organizer.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final TournamentRepository tournamentRepo;
    private final ParticipationRepository participationRepo;
    private final VenueRepository venueRepo;
    public Match toEntity(MatchInDTO dto) {
        if (dto.getParticipant1Id().equals(dto.getParticipant2Id())) {
            throw new IllegalStateException("A match must have two *different* participants.");
        }
        Tournament tournament = tournamentRepo.findById(dto.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament " + dto.getTournamentId() + " not found"));
        Participation firstParticipant = participationRepo.findById(dto.getParticipant1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Participant " + dto.getParticipant1Id() + " not found"));
        Participation secondParticipant = participationRepo.findById(dto.getParticipant2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Participant " + dto.getParticipant2Id() + " not found"));
        if (!tournament.equals(firstParticipant.getTournament()) || !tournament.equals(secondParticipant.getTournament())) {
            throw new IllegalStateException("Both participants must belong to tournament " + tournament.getId());
        }
        Match match = new Match();
        match.setTournamentId(tournament);
        match.setParticipant1Id(firstParticipant);
        match.setParticipant2Id(secondParticipant);
        match.setScheduledStart(Timestamp.valueOf(dto.getScheduledStart()));
        match.setResult(dto.getResult() == null ? Result.NOT_PLAYED : dto.getResult());

        if (dto.getVenueId() != null) {
            Venue venue = venueRepo.findById(dto.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue " + dto.getVenueId() + " not found"));
            if(tournament.getVenues()
                    .stream()
                    .noneMatch(v -> v.getId() == venue.getId())){
                throw new IllegalStateException("Venue " + venue.getId() + " is not registered for tournament "
                        + tournament.getId());
            }
            match.setVenue(venue);
        }
        return match;
    }
    public void updateEntity(Match entity, MatchInDTO patch) {
        entity.setScheduledStart(Timestamp.valueOf(patch.getScheduledStart()));
        if (patch.getResult() != null) {
            entity.setResult(patch.getResult());
        }
        if (patch.getVenueId() != null) {
            Venue venue = venueRepo.findById(patch.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue " + patch.getVenueId() + " not found"));
            if(entity.getTournamentId().getVenues()
                    .stream()
                    .noneMatch(v -> v.getId() == venue.getId())){
                throw new IllegalStateException("Venue " + venue.getId() + " is not registered for tournament "
                        + entity.getTournamentId().getId());
            }
            entity.setVenue(venue);
        }
    }
    public MatchOutDTO toDto(Match match) {
        MatchOutDTO dto = new MatchOutDTO();
        dto.setId(match.getId());
        dto.setTournamentId(match.getTournamentId().getId());
        dto.setParticipant1Id(match.getParticipant1Id().getParticipantId());
        dto.setParticipant2Id(match.getParticipant2Id().getParticipantId());
        dto.setVenueId(match.getVenue() == null ? null : match.getVenue().getId());
        dto.setScheduledStart(((Timestamp) match.getScheduledStart()).toLocalDateTime());
        dto.setResult(match.getResult());
        return dto;
    }
}
