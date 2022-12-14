package soloproject.api.restdocs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import soloproject.api.v1.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerDocumentationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    private static List<Member> members;
    private static List<MemberResponseDto> members1;
    private static List<MemberResponseDto> members2;
    private static List<MemberResponseDto> members3;
    private static List<MemberResponseDto> members4;

    @BeforeAll
    public static void beforeAll() {
        CompanyLocation companyLocation1 = new CompanyLocation(1L,"??????");
        CompanyLocation companyLocation2 = new CompanyLocation(2L,"??????");

        CompanyType companyType1 = new CompanyType(1L, "?????????");
        CompanyType companyType2 = new CompanyType(2L, "?????????");
        CompanyType companyType3 = new CompanyType(3L, "?????????");

        MemberResponseDto member1 = new MemberResponseDto(1L,"?????????",  "M", "????????????????????????", companyLocation1, companyType3);
        MemberResponseDto member2 = new MemberResponseDto(2L,"?????????",  "M", "???????????????", companyLocation1, companyType1);
        MemberResponseDto member3 = new MemberResponseDto(3L,"?????????",  "M", "???????????????", companyLocation1, companyType1);
        MemberResponseDto member4 = new MemberResponseDto(4L,"?????????",  "W", "????????????", companyLocation2, companyType1);
        MemberResponseDto member5 = new MemberResponseDto(5L,"?????????",  "W", "??????????????????", companyLocation2, companyType3);
        MemberResponseDto member6 = new MemberResponseDto(6L,"?????????",  "M", "????????????", companyLocation2, companyType2);

        members = List.of(new Member(1L,"?????????", "12345", "M", "????????????????????????", companyLocation1, companyType3));
        members1 = List.of(member1, member2, member3, member4, member5, member6);
        members2 = List.of(member2, member3, member4);
        members3 = List.of(member4, member5, member6);
        members4 = List.of(member2, member3);
    }

    @Test
    public void getMembersTest() throws Exception {
        // TODO ????????? MemberController??? getMembers() ????????? ????????? API ?????? ????????? ???????????? ????????? ???????????? ?????? ?????????.
        // given
        given(memberService.getMemebers()).willReturn(members);
        given(mapper.membersToMemberResponses(Mockito.any())).willReturn(members1);

        // when
        ResultActions resultActions = mockMvc.perform(get("/v1/members"));

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6))
                .andDo(document("get-members",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].sex").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("???????????????"),
                                        subsectionWithPath("data[].companyLocation").type(JsonFieldType.OBJECT).description("??????"),
                                        subsectionWithPath("data[].companyType").type(JsonFieldType.OBJECT).description("??????")
                                )
                        )
                ))
                .andReturn();

    }

    @Test
    public void getMemebersByTypeIdTest() throws Exception {
        // TODO ????????? MemberController??? getMembers() ????????? ????????? API ?????? ????????? ???????????? ????????? ???????????? ?????? ?????????.
        // given
        given(memberService.getMemebersByTypeId(1L)).willReturn(members);
        given(mapper.membersToMemberResponses(Mockito.any())).willReturn(members2);
        // when
        ResultActions resultActions = mockMvc.perform(get("/v1/members")
                .param("typeId","1"));

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andDo(document("get-membersByTypeId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("typeId").description("??????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].sex").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("???????????????"),
                                        subsectionWithPath("data[].companyLocation").type(JsonFieldType.OBJECT).description("??????"),
                                        subsectionWithPath("data[].companyType").type(JsonFieldType.OBJECT).description("??????")
                                )
                        )
                ))
                .andReturn();

    }


    @Test
    public void getMemebersByLocationIdTest() throws Exception {
        // TODO ????????? MemberController??? getMembers() ????????? ????????? API ?????? ????????? ???????????? ????????? ???????????? ?????? ?????????.
        // given
        given(memberService.getMemebersByLocationId(2L)).willReturn(members);
        given(mapper.membersToMemberResponses(Mockito.any())).willReturn(members3);
        // when
        ResultActions resultActions = mockMvc.perform(get("/v1/members")
                .param("locationId","2"));

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andDo(document("get-membersByLocationId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("locationId").description("??????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].sex").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("???????????????"),
                                        subsectionWithPath("data[].companyLocation").type(JsonFieldType.OBJECT).description("??????"),
                                        subsectionWithPath("data[].companyType").type(JsonFieldType.OBJECT).description("??????")
                                )
                        )
                ))
                .andReturn();

    }

    @Test
    public void getMemebersByTypeIdAndLocationIdTest() throws Exception {
        // TODO ????????? MemberController??? getMembers() ????????? ????????? API ?????? ????????? ???????????? ????????? ???????????? ?????? ?????????.
        // given
        given(memberService.getMembersByTypeIdAndLocationId(1L,1L)).willReturn(members);
        given(mapper.membersToMemberResponses(Mockito.any())).willReturn(members4);

        // when
        ResultActions resultActions = mockMvc.perform(get("/v1/members")
                .param("typeId","1").param("locationId","1"));

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(document("get-membersByTypeIdAndLocationId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("typeId").description("??????"),
                                parameterWithName("locationId").description("??????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].sex").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("???????????????"),
                                        subsectionWithPath("data[].companyLocation").type(JsonFieldType.OBJECT).description("??????"),
                                        subsectionWithPath("data[].companyType").type(JsonFieldType.OBJECT).description("??????")
                                )
                        )
                ))
                .andReturn();

    }


}
