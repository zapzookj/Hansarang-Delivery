package com.hansarangdelivery.location.repository;

import com.hansarangdelivery.location.model.LocationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface LocationEsRepository extends ElasticsearchRepository<LocationDocument, UUID> {
}
