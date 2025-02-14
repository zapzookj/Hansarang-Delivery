package com.hansarangdelivery.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "p_location")
public class Location extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id; // 고유 식별자

    @Column(name = "location_code")
    private Long location_code; // 위치 코드
    // (법정동 코드 ex)111010100 서울특별시(시도+시군구 11110) 종로구(110) 청운효자동(101) 리 없어서 생략(00) )

    @Column(name = "location" ,length = 255, nullable = false)
    private String location; // 상세주소
}
