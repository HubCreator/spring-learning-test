package nextstep.helloworld.auth;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import nextstep.helloworld.auth.dto.MemberResponse;
import nextstep.helloworld.auth.dto.TokenRequest;
import nextstep.helloworld.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    private static final String USERNAME_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "1234";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void sessionLogin() {
        MemberResponse member = RestAssured
                .given().log().all()
                .auth().form(EMAIL, PASSWORD, new FormAuthConfig("/login/session", USERNAME_FIELD, PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void tokenLogin() {
        String accessToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract().as(TokenResponse.class).getAccessToken();

        MemberResponse member = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/you")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void basicLogin() {
        // preemtive : 사전 인증 보기를 반환합니다. 이는 서버가 인증에 대한 도전을 했는지 여부와 관계없이 인증 세부 정보가 요청 헤더에 전송되는 것을 의미합니다.
        MemberResponse member = RestAssured
                .given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/my")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }
}