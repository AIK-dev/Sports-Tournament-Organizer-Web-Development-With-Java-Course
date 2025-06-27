package com.tournament_organizer.controller;

import com.tournament_organizer.dto.venue.VenueInDTO;
import com.tournament_organizer.dto.venue.VenueOutDTO;
import com.tournament_organizer.enums.Sport;
import com.tournament_organizer.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/venues")
public class VenueController {
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }
    @PostMapping
    public ResponseEntity<VenueOutDTO> create(@Valid @RequestBody VenueInDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(dto));
    }
    @GetMapping
    public List<VenueOutDTO> getAll() {
        return venueService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<VenueOutDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.findById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<VenueOutDTO> update(@PathVariable Long id, @Valid @RequestBody VenueInDTO dto) {
        return ResponseEntity.ok(venueService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        venueService.deleteById(id);
    }
    @PostMapping("/{id}/sports/{sport}")
    @ResponseStatus(NO_CONTENT)
    public void addSport(@PathVariable Long id, @PathVariable Sport sport) {
        venueService.addSport(id, sport);
    }
    @DeleteMapping("/{id}/sports/{sport}") @ResponseStatus(NO_CONTENT)
    public void removeSport(@PathVariable Long id, @PathVariable Sport sport) {
        venueService.removeSport(id, sport);
    }
}
