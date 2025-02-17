package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, UUID> {

}
