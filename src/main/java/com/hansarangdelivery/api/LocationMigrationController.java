package com.hansarangdelivery.api;

import com.hansarangdelivery.location.service.LocationMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location-migration")
@RequiredArgsConstructor
public class LocationMigrationController {

    private final LocationMigrationService locationMigrationService;

    @PostMapping
    public ResponseEntity<String> migrate() {
        locationMigrationService.migrate();
        return ResponseEntity.ok("RDB -> Elastic Search 데이터 동기화 완료.");
    }
}
