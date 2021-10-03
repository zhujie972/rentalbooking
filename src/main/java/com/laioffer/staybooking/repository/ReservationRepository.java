package com.laioffer.staybooking.repository;

import com.laioffer.staybooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.laioffer.staybooking.model.Stay;
import com.laioffer.staybooking.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(User guest);

    List<Reservation> findByStay(Stay stay);

    //save delete jpa实现了
//10 02 lecture 33 给 delete stay用的
    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);
}