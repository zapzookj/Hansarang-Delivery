package com.hansarangdelivery.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Entity
@Table(name = "p_location")
public class Location {

    @Id
    private Long id;

    @Column(nullable = false, length = 20)
    private String lawCode; // 법정동 코드

    @Column(nullable = false, length = 50)
    private String city; // 시도명

    @Column(nullable = false, length = 50)
    private String district; // 시군구명

    @Column(nullable = false, length = 50)
    private String subdistrict; // 법정읍면동명

    @Column(length = 10)
    private String mainLotNumber; // 지번본번(번지)

    @Column(length = 10)
    private String subLotNumber; // 지번부번(호)

    @Column(nullable = false)
    private String roadNameCode; // 도로명 코드

    @Column(length = 10)
    private String buildingMainNumber; // 건물 본번

    @Column(length = 10)
    private String buildingSubNumber; // 건물 부번
}
