package com.hansarangdelivery.location.service;

import com.hansarangdelivery.location.model.Location;
import com.hansarangdelivery.location.model.LocationDocument;
import com.hansarangdelivery.location.repository.LocationEsRepository;
import com.hansarangdelivery.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationMigrationService {

    private final LocationRepository locationRepository;
    private final LocationEsRepository locationEsRepository;
    private final ModelMapper modelMapper;

    public void migrate() {
        List<Location> locationList = locationRepository.findAll();

        List<LocationDocument> docs = locationList.stream()
            .map(location -> modelMapper.map(location, LocationDocument.class))
            .toList();

        locationEsRepository.saveAll(docs);
    }

}
