package edu.tomerbu.lec4tdd.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.tomerbu.lec4tdd.shared.CurrentUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RequestMapping("/api/1/login")
@RestController
public class LoginController {
    @PostMapping

    public UserLoginResponseDTO handleLogin(@CurrentUser User user) {

        return UserLoginResponseDTO
                .builder()
                .userName(user.getUserName())
                .displayName(user.getDisplayName())
                .id(user.getId())
                .build();
    }
}
