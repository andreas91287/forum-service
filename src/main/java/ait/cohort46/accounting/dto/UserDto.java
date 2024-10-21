package ait.cohort46.accounting.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDto {
    private String login;
    private String firstName;
    private String lastName;
    @Singular
    private Set<String> roles;
}
