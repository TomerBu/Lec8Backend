package edu.tomerbu.lec4tdd;

import edu.tomerbu.lec4tdd.errors.APIErrorMessageDTO;
import edu.tomerbu.lec4tdd.shared.GenericResponse;
import edu.tomerbu.lec4tdd.user.User;


import edu.tomerbu.lec4tdd.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") //dev, prod, test //prefer application.properties-test
//integration tests: test the app as a whole;
// web server will be started for the tests
//inject app related components
public class UserControllerTests {
    public static final String API_1_USERS = "/api/1/users";
    //Controllers are tested TestRestTemplate
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    private void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    //check that @AutoWired is working for expected fields.
    public void contextLoads() {
        assertThat(testRestTemplate).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();

        postSignUp(user, Object.class);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_receiveOK() {
        User user = createValidUser();
        ResponseEntity<Object> response =
                postSignUp(user, Object.class);
        HttpStatus status = response.getStatusCode();
        assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_whenUserIsValid_receiveSuccessMessage() {
        var user = createValidUser();

        var response =
                postSignUp(user, GenericResponse.class);

        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isNotNull();
        assertThat(body.getMessage()).isEqualTo("User Saved");
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        var user = createValidUser();

        //add 1 user to the DB
        postSignUp(user, GenericResponse.class);

        var users = userRepository.findAll();
        //there are users after we added 1
        assertThat(users).isNotNull();
        //there is exactly 1 user.
        assertThat(users.size()).isEqualTo(1);
        //get the userFromDB:
        User userFromDb = users.get(0);
        //check that the password is not saved as is
        assertThat(user.getPassword()).isNotEqualTo(userFromDb.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullUserName_receiveBadRequest(){
        var user = createValidUser();
        user.setUserName(null);

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullDisplayName_receiveBadRequest(){
        var user = createValidUser();
        user.setDisplayName(null);

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullPassword_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword(null);

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasUserNameShorterThanRequired_receiveBadRequest(){
        var user = createValidUser();
        user.setUserName("a");

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasDisplayNameShorterThanRequired_receiveBadRequest(){
        var user = createValidUser();
        user.setDisplayName("a");

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordShorterThanRequired_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword("P4ss!"); //5 chars -> less than 6 (includes upper/digit/lower/punctuation)

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasUserNameLongerThanRequired_receiveBadRequest(){
        var user = createValidUser();
        user.setUserName("a".repeat(256));//256 times the letter 'a'

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasDisplayNameLongerThanRequired_receiveBadRequest(){
        var user = createValidUser();
        //String collect = IntStream.rangeClosed(1, 256).mapToObj(String::valueOf).collect(Collectors.joining());
        user.setDisplayName("a".repeat(256));//256 times the letter 'a'

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordLongerThanRequired_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword("a".repeat(253).concat("P4!"));//256 times the letter 'a'

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordAllLowerCase_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword("alllowercase");

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordAllUpperCase_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword("ALLUPPERCASE");

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordAllDigits_receiveBadRequest(){
        var user = createValidUser();
        user.setPassword("123456789");

        var response = postSignUp(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserIsInvalid_receiveApiError(){
        User user = new User();
        ResponseEntity<APIErrorMessageDTO> response = postSignUp(user, APIErrorMessageDTO.class);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getFailedURL()).isEqualTo("/api/1/users");
    }

    @Test
    public void postUser_whenUserIsInvalid_receiveApiErrorWithValidationErrors(){
        User user = new User();
        ResponseEntity<APIErrorMessageDTO> response = postSignUp(user, APIErrorMessageDTO.class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
    }


    //helper:
    public <T> ResponseEntity<T> postSignUp(
            Object request,
            Class<T> responseType
    ) {
        return testRestTemplate.postForEntity(API_1_USERS,
                request,
                responseType);
    }

    //helpers: //ctrl+alt+m
    private User createValidUser() {
        User user = new User();
        user.setUserName("Moe");
        user.setDisplayName("Moe-Display");
        user.setPassword("P4ssword!");//8 letters, 1 caps, 1 digit, 1 punctuation
        return user;
    }
}
