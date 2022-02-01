package edu.tomerbu.lec4tdd.user;

import lombok.*;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLoginResponseDTO {
    private String userName;
    private String displayName;
    private Long id;
}
