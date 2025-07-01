package com.tournament_organizer.service;

import com.tournament_organizer.dto.venue.VenueInDTO;
import com.tournament_organizer.dto.venue.VenueOutDTO;
import com.tournament_organizer.entity.Venue;
import com.tournament_organizer.enums.Sport;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.VenueMapper;
import com.tournament_organizer.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueService(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }
    @Transactional
    public VenueOutDTO create(VenueInDTO venueInDTO){
        if(venueRepository.existsByNameIgnoreCaseAndCityIgnoreCase(venueInDTO.getName(), venueInDTO.getCity())){
            throw new IllegalArgumentException(String.format("Venue with name %s in %s already exists",
                    venueInDTO.getName(), venueInDTO.getCity()));
        }
        return venueMapper.toDto(venueRepository.save(venueMapper.toEntity(venueInDTO)));
    }
    @Transactional
    public VenueOutDTO update(Long id, VenueInDTO vanueInDTO) {
        if (venueRepository.existsByNameIgnoreCaseAndCityIgnoreCaseAndIdNot(vanueInDTO.getName(), vanueInDTO.getCity(), id)) {
            throw new IllegalStateException(String.format("Venue with name %s in %s already exists",
                    vanueInDTO.getName(), vanueInDTO.getCity()));
        }
        Venue entity = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue " + id + " not found"));
        venueMapper.updateEntity(entity, vanueInDTO);
        return venueMapper.toDto(entity);
    }
    @Transactional public void addSport(Long id, Sport sport) {
        Venue v = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue " + id + " not found"));
        if (!v.getSupportedSports().add(sport))
            throw new IllegalStateException("Sport already present");
    }
    @Transactional
    public void removeSport(Long id, Sport sport) {
        Venue v = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue " + id + " not found"));
        if (!v.getSupportedSports().remove(sport))
            throw new IllegalStateException("Sport not present");
    }
    public List<VenueOutDTO> findAll() {
        return venueRepository.findAll().stream().map(venueMapper::toDto).toList();
    }
    public VenueOutDTO findById(Long id) {
        return venueMapper.toDto(venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue " + id + " not found")));
    }
    public void deleteById(Long id) {
        venueRepository.deleteById(id);
    }
}