
package com.example.LocationOpenStreetApi.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class LocationService {

    public String extractLocationFromPhoto(MultipartFile file) throws IOException, ImageProcessingException {
        File photoFile = convertMultipartFileToFile(file);
        Metadata metadata = ImageMetadataReader.readMetadata(photoFile);
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if (gpsDirectory == null) {
            return "Could not fetch the GPS details from the photo.";
        }

        double latitude = gpsDirectory.getGeoLocation().getLatitude();
        double longitude = gpsDirectory.getGeoLocation().getLongitude();
        return "Latitude: " + latitude + ", Longitude: " + longitude;
    }


    //    public String getlocationAddress(String latitude, String longitude) {
//        String apiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + latitude + "&lon=" + longitude;
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonNode root = objectMapper.readTree(response.getBody());
////            JsonNode streetName = root.get("display_name");
//            String fullAddress = root.path("display_name").toPrettyString();
//            return fullAddress;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return "Street name not found";
//    }
    public String getlocationAddressWithCoordinates(double latitude, double longitude) {
        String apiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + latitude + "&lon=" + longitude;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String fullAddress = root.path("display_name").asText();
            return "Latitude: " + latitude + "\nLongitude: " + longitude + "\nAddress: " + fullAddress;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "Street name not found";
    }





    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(convertedFile)) {
            os.write(file.getBytes());
        }
        return convertedFile;
    }
}


