package com.tournament_organizer.controller;

import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.service.ParticipationService;
import com.tournament_organizer.web.HeaderUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/participations")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    public final String BASE_API_PATH = "/api/v1/participations/";
    public final String ENTITY_NAME = "Participation";

    @GetMapping
    public List<Participation> getAllParticipations() {
        return participationService.findAll();
    }

    @PostMapping
    public ResponseEntity<Participation> createParticipation(@Valid @RequestBody ParticipationCreationDTO participation) throws URISyntaxException, ResourceNotFoundException {
        // Note: This would be good to be replaced by a potential ParticipationDTO
        Participation createdParticipation= participationService.createParticipation(participation);
        return ResponseEntity.created(new URI(BASE_API_PATH + createdParticipation.getParticipantId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, createdParticipation.getParticipantId().toString()))
                .body(createdParticipation);
    }

    @GetMapping("/{id}")
    public ResponseEntity< Participation > getParticipationById(@PathVariable Long participationId) throws URISyntaxException, ResourceNotFoundException {
        Participation participation = participationService.findById(participationId);
        if (participation != null) {
            return ResponseEntity.ok().body(participation);
        } else {
            throw new ResourceNotFoundException("Participation not found for the following id: " + participationId);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity < Participation > updateParticipation(@PathVariable(value = "id") Long participationId,
                                                                @Valid @RequestBody ParticipationCreationDTO participationDetails) throws ResourceNotFoundException {
        Participation resultParticipation = participationService.updateParticipation(participationId, participationDetails);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resultParticipation.getParticipantId().toString()))
                .body(resultParticipation);
    }
}
