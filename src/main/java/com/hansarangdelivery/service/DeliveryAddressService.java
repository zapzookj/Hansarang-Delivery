package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.DeliveryAddressRequestDto;
import com.hansarangdelivery.dto.DeliveryAddressResponseDto;
import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.DeliveryAddressRepository;
import com.hansarangdelivery.repository.DeliveryAddressRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final LocationService locationService;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final DeliveryAddressRepositoryQueryImpl deliveryAddressRepositoryQuery;

    @Transactional
    @CacheEvict(value = "deliveryAddress", key = "user.id")
    public DeliveryAddressResponseDto createDeliveryAddress(User user, DeliveryAddressRequestDto requestDto) {
        if (!locationService.existsById(requestDto.getLocationId())) {
            throw new ResourceNotFoundException("해당 위치 정보를 찾을 수 없습니다.");
        }

        int count = deliveryAddressRepositoryQuery.countByUserId(user.getId());
        boolean isDefault;

        if (count >= 3) {
            throw new IllegalArgumentException("배송지는 최대 3개까지 등록할 수 있습니다."); // 한 유저당 배송지 등록은 최대 3개까지로 제한
        }

        isDefault = count == 0 || requestDto.getIsDefault(); // 처음 등록하는 배송지라면 기본 배송지로 설정

        if (isDefault || count >= 1) {
            deliveryAddressRepositoryQuery.resetDefault(user.getId()); // 기존에 존재하던 기본 배송지를 is_default = false 로 수정
        }

        DeliveryAddress deliveryAddress = new DeliveryAddress(user, requestDto.getLocationId(), requestDto.getRequestMessage(), isDefault);
        deliveryAddressRepository.save(deliveryAddress);
        return new DeliveryAddressResponseDto(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public DeliveryAddressResponseDto readDeliveryAddress(Long userId) {
        DeliveryAddress deliveryAddress = deliveryAddressRepositoryQuery.findDefaultByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("기본 배송지가 설정되어 있지 않습니다."));

        return new DeliveryAddressResponseDto(deliveryAddress);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "deliveryAddress", key = "userId")
    public List<DeliveryAddressResponseDto> searchDeliveryAddresses(Long userId) {
        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepositoryQuery.findAllByUserId(userId);

        if (deliveryAddressList.isEmpty()) {
            throw new ResourceNotFoundException("등록된 배송지가 없습니다.");
        }

        return deliveryAddressList.stream().map(DeliveryAddressResponseDto::new).toList();
    }

    @Transactional
    @CacheEvict(value = "deliveryAddress", key = "userId")
    public DeliveryAddressResponseDto updateDeliveryAddress(Long userId, UUID addressId, DeliveryAddressRequestDto requestDto) {
        DeliveryAddress deliveryAddress = deliveryAddressRepositoryQuery.findByIdAndUserId(addressId, userId).orElseThrow(
            () -> new ResourceNotFoundException("해당 Id를 가진 배송지 정보를 찾을 수 없습니다. 또는 권한이 없습니다.")
        );

        if (requestDto.getIsDefault()) {
            deliveryAddressRepositoryQuery.resetDefault(userId); // 기존에 존재하던 기본 배송지를 is_default = false 로 수정
        }
        deliveryAddress.update(requestDto.getLocationId(), requestDto.getRequestMessage(), requestDto.getIsDefault());
        return new DeliveryAddressResponseDto(deliveryAddress);
    }

    @Transactional
    @CacheEvict(value = "deliveryAddress", key = "userId")
    public void deleteDeliveryAddress(Long userId, UUID addressId) {
        if (deliveryAddressRepositoryQuery.existsByIdAndUserId(addressId, userId)) {
            deliveryAddressRepository.deleteById(addressId);
        } else {
            throw new ResourceNotFoundException("해당 Id를 가진 배송지 정보를 찾을 수 없습니다. 또는 권한이 없습니다.");
        }
    }
}
