package com.tournament_organizer.controller;

import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.dto.participation.ParticipationDisplayDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.service.ParticipationService;
import com.tournament_organizer.web.HeaderUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/participations")
public class ParticipationController {

    private final ParticipationService participationService;
    @Autowired
    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping
    public List<ParticipationDisplayDTO> getAllParticipations() {
        return participationService.findAll();
    }

    @PostMapping
    public ResponseEntity<ParticipationDisplayDTO > createParticipation(@Valid @RequestBody ParticipationCreationDTO participation) {
        ParticipationDisplayDTO createdParticipation= participationService.createParticipation(participation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipationDisplayDTO> getParticipationById(@PathVariable Long id)  {
        ParticipationDisplayDTO found = participationService.findById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(found);
    }

    @PatchMapping("/{id}")
    public ResponseEntity <ParticipationDisplayDTO> updateParticipation(@PathVariable Long id,
                                                                @Valid @RequestBody ParticipationCreationDTO participationDetails)  {
        ParticipationDisplayDTO resultParticipation = participationService.updateParticipation(id, participationDetails);
        return ResponseEntity.ok(resultParticipation);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participationService.deleteParticipation(id);
    }
}
