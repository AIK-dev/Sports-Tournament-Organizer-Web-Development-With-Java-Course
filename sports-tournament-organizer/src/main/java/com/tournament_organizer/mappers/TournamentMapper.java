package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.participation.ParticipationOutDTO;
import com.tournament_organizer.dto.tournament.TournamentInDTO;
import com.tournament_organizer.dto.tournament.TournamentOutDTO;
import com.tournament_organizer.dto.venue.VenueOutDTO;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.entity.Venue;
import com.tournament_organizer.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TournamentMapper {
    private final VenueRepository venueRepo;
    public Tournament toEntity(TournamentInDTO dto) {
        Tournament t = new Tournament();
        t.setName(dto.getName());
        t.setSport(dto.getSport());
        t.setParticipationLimit(dto.getParticipationLimit());
        t.setStartDate(Timestamp.valueOf(dto.getStartDate()));
        t.setEndDate(Timestamp.valueOf(dto.getEndDate()));
        t.setDrawType(dto.getDrawType());
        t.setRules(dto.getRules());
        if (dto.getVenueIds() != null && !dto.getVenueIds().isEmpty()) {
            List<Venue> venues = venueRepo.findAllById(dto.getVenueIds());
            t.setVenues(venues);
        }
        return t;
    }
    public void updateEntity(Tournament entity, TournamentInDTO dto) {
        entity.setName(dto.getName());
        entity.setSport(dto.getSport());
        entity.setParticipationLimit(dto.getParticipationLimit());
        entity.setStartDate(Timestamp.valueOf(dto.getStartDate()));
        entity.setEndDate(Timestamp.valueOf(dto.getEndDate()));
        entity.setDrawType(dto.getDrawType());
        entity.setRules(dto.getRules());
        if (dto.getVenueIds() != null) {
            entity.setVenues(venueRepo.findAllById(dto.getVenueIds()));
        }
    }

    public TournamentOutDTO toDto(Tournament t) {
        List<VenueOutDTO> venueDtos = t.getVenues()
                .stream()
                .map(v -> new VenueOutDTO(
                        v.getId(), v.getName(), v.getCity(), v.getCapacity()))
                .toList();
        // TODO: Implement this with information supplied from TeamOutDTOs and PlayerOutDTOs
        List<ParticipationOutDTO> participationDTOs = t.getParticipations()
                .stream()
                .map(p -> new ParticipationOutDTO(
                        p.getParticipantId(),
                        p.getPlayer() == null ? null : p.getPlayer().getId(),
                        p.getTeam() == null ? null : p.getTeam().getId(),
                        p.getTournament().getId()
                )).toList();
        return new TournamentOutDTO(
                t.getId(),
                t.getName(),
                t.getSport(),
                t.getParticipationLimit(),
                t.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                t.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                t.getDrawType(),
                venueDtos,
                participationDTOs,
                t.getRules()
        );
    }
}
