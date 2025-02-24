package com.hansarangdelivery.location.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "location")
public class LocationDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private String lawCode;

    @Field(type = FieldType.Text, analyzer = "nori_custom_analyzer", searchAnalyzer = "nori_custom_analyzer")
    private String city;

    @Field(type = FieldType.Text, analyzer = "nori_custom_analyzer", searchAnalyzer = "nori_custom_analyzer")
    private String district;

    @Field(type = FieldType.Text, analyzer = "nori_custom_analyzer", searchAnalyzer = "nori_custom_analyzer")
    private String subDistrict;

    @Field(type = FieldType.Keyword)
    private String mainLotNumber;

    @Field(type = FieldType.Keyword)
    private String subLotNumber;

    @Field(type = FieldType.Keyword)
    private String roadNameCode;

    @Field(type = FieldType.Keyword)
    private String buildingMainNumber;

    @Field(type = FieldType.Keyword)
    private String buildingSubNumber;
}
