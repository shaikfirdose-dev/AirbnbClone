package com.firdose.project.airBnbApp.repository;

import com.firdose.project.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

}
