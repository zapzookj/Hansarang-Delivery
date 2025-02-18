//package com.hansarangdelivery.service;
//
//import com.hansarangdelivery.dto.ResultResponseDto;
//import com.hansarangdelivery.dto.ReviewRequestDto;
//import com.hansarangdelivery.dto.ReviewResponseDto;
//import com.hansarangdelivery.entity.Order;
//import com.hansarangdelivery.entity.Restaurant;
//import com.hansarangdelivery.entity.Review;
//import com.hansarangdelivery.repository.OrderRepository;
//import com.hansarangdelivery.repository.RestaurantRepository;
//import com.hansarangdelivery.repository.ReviewRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class ReviewService {
//    private final ReviewRepository reviewRepository;
//    private final RestaurantRepository restaurantRepository;
//    private final OrderRepository orderRepository;
//
//    public ResultResponseDto<Void> addReview(ReviewRequestDto requestDto) {
//
//        // orderId는 Unique 제약 조건으로 한 주문은 한 개의 리뷰만 작성 가능
//        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
//            throw new IllegalArgumentException("이미 리뷰가 작성된 주문입니다.");
//        }
//
//        Review review = new Review(
//            requestDto.getOrderId(), requestDto.getContent(), requestDto.getRating()
//        );
//
//        reviewRepository.save(review);
//
//        return new ResultResponseDto<>("리뷰 작성 완료", 200);
//    }
//
//    public ResultResponseDto<List<ReviewResponseDto>> readReview(UUID restaurantId) {
//        // List<Order> orderList = orderRepository.findByRestaurantId(restaurantId);
//
//        List<Review> reviewList = reviewRepository.findAll();
//
//        List<ReviewResponseDto> responseList = new ArrayList<>();
//
//        // 레스토랑 관련 정보 어떻게 처리할 것인지
//        // 1. orderId를 이용해 orderService 와 restaurantService 호출하여 정보 전달
//        // 2. restaurantId 컬럼을 이용하여 restaurantService 만 호출
//        // 3. Restaurant 정보를 캐싱하여 리뷰 조회시 전달
//        // 3-1. orderId -> restaurantId 매핑하여 Redis 에 저장한다고 함. (경험 없음)
//        // 1~2번은 MSA 로 확장할 때 주로 사용되는 방법이라고 함.
//
//        return new ResultResponseDto<>("리뷰 조회 성공", 200, responseList);
//    }
//
//    public ResultResponseDto<List<ReviewResponseDto>> readMyReview(UUID userId) {
//        List<Review> reviewList = reviewRepository.findAllByCreatedBy(userId)
//            .orElseThrow(() -> new IllegalArgumentException("작성된 리뷰가 없습니다."));
//
//        List<ReviewResponseDto> responseList = new ArrayList<>();
//
//        for (Review review : reviewList) {
//            responseList.add(
//                new ReviewResponseDto(
//                    review.getId(), review.getCreatedAt(), review.getCreatedBy(), review.getOrderId(), review.getContent(), review.getRating()
//                )
//            );
//        }
//
//        return new ResultResponseDto<>("나의 리뷰 조회 성공", 200, responseList);
//    }
//
//    public ResultResponseDto<Void> updateReview(UUID reviewId, ReviewRequestDto requestDto) {
//        Review review = reviewRepository.findById(reviewId).orElseThrow(
//            () -> new IllegalArgumentException("찾는 리뷰가 없습니다.")
//        );
//
//        review.update(requestDto.getContent(), requestDto.getRating());
//
//        return new ResultResponseDto<>("리뷰 수정 완료", 201);
//    }
//
//    public ResultResponseDto<Void> deleteReview(UUID reviewId) {
//
//        if (!reviewRepository.existsById(reviewId)) {
//            throw new IllegalArgumentException("이미 삭제되거나 리뷰 정보를 찾을 수 없습니다.");
//        }
//
//        reviewRepository.deleteById(reviewId);
//
//        return new ResultResponseDto<>("리뷰 삭제 완료", 200);
//    }
//}
