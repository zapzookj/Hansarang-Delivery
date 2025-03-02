package com.hansarangdelivery.location.repository;

import com.hansarangdelivery.location.model.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
