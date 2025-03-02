package com.hansarangdelivery.location.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, insertable = false)
    private UUID id;

    @Column(updatable = false, nullable = false, length = 20)
    private String lawCode; // 법정동 코드

    @Column(updatable = false, nullable = false, length = 50)
    private String city; // 시도명

    @Column(updatable = false, nullable = false, length = 50)
    private String district; // 시군구명

    @Column(updatable = false, nullable = false, length = 50)
    private String subDistrict; // 법정읍면동명

    @Column(updatable = false, insertable = false, length = 10)
    private String mainLotNumber; // 지번본번(번지)

    @Column(updatable = false, insertable = false, length = 10)
    private String subLotNumber; // 지번부번(호)

    @Column(updatable = false,  nullable = false)
    private String roadNameCode; // 도로명 코드

    @Column(updatable = false, insertable = false, length = 10)
    private String buildingMainNumber; // 건물 본번

    @Column(updatable = false, insertable = false, length = 10)
    private String buildingSubNumber; // 건물 부번

    public Location(String lawCode, String city, String district, String subDistrict, String roadNameCode){
        this.lawCode = lawCode;
        this.city = city;
        this.district = district;
        this.subDistrict = subDistrict;
        this.roadNameCode = roadNameCode;
    }
}
