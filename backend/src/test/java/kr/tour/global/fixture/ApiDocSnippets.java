package kr.tour.global.fixture;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ApiDocSnippets {

  public static FieldDescriptor[] getPageResponseFields() {
    return new FieldDescriptor[]{
            // Page 정보 (Spring Data Page 기본 필드들)
            fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
            fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 설정 여부"),
            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬됨"),
            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않음"),
            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지당 개수"),
            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 미적용 여부"),

            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 데이터 개수"),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 개수"),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
    };
  }
}
