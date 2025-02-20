package com.hansarangdelivery.config;

import com.hansarangdelivery.entity.PageType;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PageableConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 10,
            Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("updatedAt")
            ))
        ); // 기본값 설정
        resolver.setMaxPageSize(50); // 최대 페이지 크기 제한
        resolvers.add(resolver);
    }

    public static void validatePageSize(Pageable pageable) {
        if(pageable.getPageSize() != PageType.TEN.getSize()
            && pageable.getPageSize() != PageType.THIRTY.getSize()
            && pageable.getPageSize() != PageType.FIFTY.getSize()
        ) {
            throw new IllegalArgumentException("페이지 사이즈는 10 / 30 / 50 중에서 입력 가능합니다.");
        }
    }
}

