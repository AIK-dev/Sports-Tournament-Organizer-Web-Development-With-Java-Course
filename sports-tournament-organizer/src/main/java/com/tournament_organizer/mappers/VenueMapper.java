package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.venue.VenueInDTO;
import com.tournament_organizer.dto.venue.VenueOutDTO;
import com.tournament_organizer.entity.Venue;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VenueMapper {
    public Venue toEntity(VenueInDTO dto) {
        Venue v = new Venue();
        v.setName(dto.getName().trim());
        v.setCity(dto.getCity().trim());
        v.setCapacity(dto.getCapacity());
        v.setSupportedSports(new HashSet<>(dto.getSupportedSports()));
        return v;
    }

    public void updateEntity(Venue entity, VenueInDTO patch) {
        entity.setName(patch.getName().trim());
        entity.setCity(patch.getCity().trim());
        entity.setCapacity(patch.getCapacity());
        entity.setSupportedSports(new HashSet<>(patch.getSupportedSports()));
    }

    public VenueOutDTO toDto(Venue entity) {
        return new VenueOutDTO(
                entity.getId(),
                entity.getName(),
                entity.getCity(),
                entity.getCapacity(),
                entity.getSupportedSports()
        );
    }
}
