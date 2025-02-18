package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.DeliveryAddressRequestDto;
import com.hansarangdelivery.dto.DeliveryAddressResponseDto;
import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final LocationService locationService;
    private final DeliveryAddressRepository deliveryAddressRepository;


    public void createDeliveryAddress(User user, DeliveryAddressRequestDto requestDto) {
        if (!locationService.existsById(requestDto.getLocationId())) {
            throw new IllegalArgumentException("해당 위치 정보를 찾을 수 없습니다.");
        }

        if (deliveryAddressRepository.countByUser(user) >= 3) {
            throw new IllegalArgumentException("배송지는 최대 3개까지 등록할 수 있습니다."); // 한 유저당 배송지 등록은 최대 3개까지로 제한
        }

        deliveryAddressRepository.resetDefault(user); // 기존에 존재하던 기본 배송지를 (is_default = false 로 수정)

        DeliveryAddress deliveryAddress = new DeliveryAddress(user, requestDto.getLocationId(), requestDto.getRequestMessage());
        deliveryAddressRepository.save(deliveryAddress);
    }

    public DeliveryAddressResponseDto getDeliveryAddress(Long userId) {
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findByUserIdAndDefaultIsTrue(userId)
            .orElseThrow(() -> new IllegalArgumentException("기본 배송지가 설정되어 있지 않습니다."));

        return new DeliveryAddressResponseDto(deliveryAddress);
    }

    public List<DeliveryAddressResponseDto> getAllDeliveryAddresses(Long userId) {
        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findAllByUserId(userId);

        if (deliveryAddressList.isEmpty()) {
            throw new IllegalArgumentException("등록된 배송지가 없습니다.");
        }

        return deliveryAddressList.stream().map(DeliveryAddressResponseDto::new).toList();
    }
}
