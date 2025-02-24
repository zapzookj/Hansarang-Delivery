package com.hansarangdelivery.location.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.model.LocationDocument;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public PageResponseDto<LocationDocument> searchLocationAsEs(LocationRequestDto requestDto) {
        // 1) BoolQuery 를 빌드하기 위한 Builder
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        // 2) city (시도명, required) : Edge N-gram(2.0) + fuzzy(0.5)
        boolBuilder.must(buildNgramFuzzyMultiMatch(
            requestDto.getCity(),
            "city.ngram",
            "city",
            2.0f,
            0.5f
        ));

        // 3) district (시군구명, required) : Edge N-gram(2.0) + fuzzy(0.5)
        boolBuilder.must(buildNgramFuzzyMultiMatch(
            requestDto.getDistrict(),
            "district.ngram",
            "district",
            2.0f,
            0.5f
        ));

        // 4) subDistrict (법정읍면동명, optional) : Edge N-gram(2.0) + fuzzy(0.5)
        if (StringUtils.hasText(requestDto.getSubDistrict())) {
            boolBuilder.must(buildNgramFuzzyMultiMatch(
                requestDto.getSubDistrict(),
                "subDistrict.ngram",
                "subDistrict",
                2.0f,
                0.5f
            ));
        }

        // 5) mainLotNumber ~ buildingSubNumber (지번 본번 ~ 건물 부번, optional) : 정확 매핑
        if (StringUtils.hasText(requestDto.getMainLotNumber())) {
            boolBuilder.must(buildExactTermQuery("mainLotNumber", requestDto.getMainLotNumber()));
        }
        if (StringUtils.hasText(requestDto.getSubLotNumber())) {
            boolBuilder.must(buildExactTermQuery("subLotNumber", requestDto.getSubLotNumber()));
        }
        if (StringUtils.hasText(requestDto.getBuildingMainNumber())) {
            boolBuilder.must(buildExactTermQuery("buildingMainNumber", requestDto.getBuildingMainNumber()));
        }
        if (StringUtils.hasText(requestDto.getBuildingSubNumber())) {
            boolBuilder.must(buildExactTermQuery("buildingSubNumber", requestDto.getBuildingSubNumber()));
        }

        // 6) 최종 Query 생성
        Query finalQuery = new Query.Builder()
            .bool(boolBuilder.build())
            .build();

        // 7) from/size 계산
        int from = Math.max(0, requestDto.getPage()) * requestDto.getSize();
        int size = requestDto.getSize();

        // 8) SearchRequest 생성
        SearchRequest searchRequest = new SearchRequest.Builder()
            .index("location")   // 수동 생성해둔 인덱스명
            .query(finalQuery)
            .from(from)
            .size(size)
            .build();

        // 9) 검색 실행
        SearchResponse<LocationDocument> response;
        try {
            response = elasticsearchClient.search(searchRequest, LocationDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("ElasticSearch 검색 실패", e);
        }

        // 10) 검색 결과 반환
        long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;
        List<LocationDocument> contents = response.hits().hits().stream()
            .map(Hit::source)
            .toList();

        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        PageImpl<LocationDocument> mappedPage = new PageImpl<>(contents, pageable, totalHits);
        return new PageResponseDto<>(mappedPage);
    }

    private Query buildNgramFuzzyMultiMatch(
        String input, String nGramField, String originField, float nGramBoost, float fuzzyBoost) {

        MultiMatchQuery.Builder multiMatchBuilder = new MultiMatchQuery.Builder()
            .query(input)
            .fields(List.of(
                nGramField + "^" + nGramBoost,
                originField + "^" + fuzzyBoost)
            )
            .fuzziness("AUTO")
            .prefixLength(1)
            .maxExpansions(50)
            .type(TextQueryType.BestFields);

        return new Query.Builder()
            .multiMatch(multiMatchBuilder.build())
            .build();
    }

    private Query buildExactTermQuery(String fieldName, String value) {
        TermQuery termQuery = new TermQuery.Builder()
            .field(fieldName)
            .value(value)
            .build();

        return new Query.Builder()
            .term(termQuery)
            .build();
    }
}
