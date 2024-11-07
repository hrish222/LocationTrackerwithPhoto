package com.example.LocationOpenStreetApi.controller;


import com.drew.imaging.ImageProcessingException;
import com.example.LocationOpenStreetApi.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PhotoLocationController {

    private final LocationService locationService;

    public PhotoLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/photo/location")
    public ResponseEntity<String> extractLocationFromPhoto(@RequestParam("file") MultipartFile file) {
        try {
            String location = locationService.extractLocationFromPhoto(file);
            if (location.equals("Could not fetch the GPS details from the photo.")) {
                return ResponseEntity.badRequest().body(location);
            }

            String[] latLong = location.split(",");
            double latitude = Double.parseDouble(latLong[0].replace("Latitude:", "").trim());
            String longitude = latLong[1].replace("Longitude:", "").trim();

            String addressWithCoordinates = locationService.getlocationAddressWithCoordinates(latitude, Double.parseDouble(longitude));
            return ResponseEntity.ok(addressWithCoordinates);
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract location from photo.");
        }
    }

}



