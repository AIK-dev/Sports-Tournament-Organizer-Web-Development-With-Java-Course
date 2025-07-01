package com.tournament_organizer.repository;

import com.tournament_organizer.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByNameIgnoreCaseAndCityIgnoreCase(String name, String city);
    boolean existsByNameIgnoreCaseAndCityIgnoreCaseAndIdNot(String name,String city, Long id);
}
