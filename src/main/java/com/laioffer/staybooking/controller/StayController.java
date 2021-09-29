package com.laioffer.staybooking.controller;

import org.springframework.web.bind.annotation.RestController;
import com.laioffer.staybooking.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import com.laioffer.staybooking.model.Stay;
import org.springframework.web.bind.annotation.*;

import com.laioffer.staybooking.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class StayController {
    private StayService stayService;

    @Autowired
    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @GetMapping(value = "/stays")
    public List<Stay> listStays(@RequestParam(name = "host") String hostName) {
        return stayService.findByHost(hostName);
    }

    @GetMapping(value = "/stays/{stayId}")
    public Stay getStay(@PathVariable Long stayId) {
        return stayService.findByID(stayId);
    }

//    @PostMapping("/stays")
//    public void addStay(@RequestBody Stay stay) {
//        stayService.add(stay);
//    }
    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("host") String host,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images) {

        Stay stay = new Stay.Builder().setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .setHost(new User.Builder().setUsername(host).build())
                .build();
        stayService.add(stay, images);
    }


    @DeleteMapping("/stays/{stayId}")
    public void deleteStay(@PathVariable Long stayId) {
        stayService.delete(stayId);
    }

}