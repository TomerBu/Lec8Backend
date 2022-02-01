package edu.tomerbu.lec4tdd.user;

import edu.tomerbu.lec4tdd.errors.APIErrorMessageDTO;
import edu.tomerbu.lec4tdd.errors.UserNotValidException;
import edu.tomerbu.lec4tdd.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@RequestMapping("/api/1/users")
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public GenericResponse createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        return GenericResponse.builder().message("User Saved").build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})//the exception that we catch
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        //status after catching an exception
    APIErrorMessageDTO handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        var hashMap = new HashMap<String, String>();

        var eResult = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : eResult) {

            String currentValue = hashMap.get(fieldError.getField());

            if (currentValue == null)
                hashMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            else {
                hashMap.put(fieldError.getField(), currentValue + ";" + fieldError.getDefaultMessage());
            }
        }

        return new APIErrorMessageDTO(
                "Validation Error",
                HttpStatus.BAD_REQUEST.value(),
                request.getServletPath(),
                hashMap
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})//the exception that we catch
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        //status after catching an exception
    APIErrorMessageDTO handleDataIntegrityViolationException(
            DataIntegrityViolationException e, HttpServletRequest request) {

            return new APIErrorMessageDTO(
                    "The UserName already exists",
                    HttpStatus.BAD_REQUEST.value(),
                    request.getServletPath()
            );
    }
}
