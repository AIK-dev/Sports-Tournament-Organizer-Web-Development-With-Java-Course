package com.tournament_organizer.controller;

import com.tournament_organizer.dto.participation.ParticipationInDTO;
import com.tournament_organizer.dto.participation.ParticipationOutDTO;
import com.tournament_organizer.service.ParticipationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public List<ParticipationOutDTO> getAllParticipations() {
        return participationService.findAll();
    }
    @PreAuthorize("""
        hasRole('ADMIN') ||
        (hasRole('ORGANIZER') &&
            ((#participation.teamId   != null && @authz.isTeamOwner(#participation.teamId)) ||
             (#participation.playerId != null && @authz.isPlayerOwner(#participation.playerId))))
    """)
    @PostMapping
    public ResponseEntity<ParticipationOutDTO> createParticipation(@Valid @RequestBody ParticipationInDTO participation) {
        ParticipationOutDTO createdParticipation= participationService.createParticipation(participation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipationOutDTO> getParticipationById(@PathVariable Long id)  {
        ParticipationOutDTO found = participationService.findById(id);
        return ResponseEntity.ok(found);
    }
    @PreAuthorize("""
        hasRole('ADMIN') ||
        (hasRole('ORGANIZER') &&
            ((#participationDetails.teamId   != null && @authz.isTeamOwner(#participationDetails.teamId)) ||
             (#participationDetails.playerId != null && @authz.isPlayerOwner(#participationDetails.playerId))))
    """)
    @PatchMapping("/{id}")
    public ResponseEntity <ParticipationOutDTO> updateParticipation(@PathVariable Long id,
                                                                    @Valid @RequestBody ParticipationInDTO participationDetails)  {
        ParticipationOutDTO resultParticipation = participationService.updateParticipation(id, participationDetails);
        return ResponseEntity.ok(resultParticipation);
    }
    @PreAuthorize("""
    hasRole('ADMIN') ||
    (hasRole('ORGANIZER') && @authz.isParticipationOwner(#id))
""")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participationService.deleteParticipation(id);
    }
}
