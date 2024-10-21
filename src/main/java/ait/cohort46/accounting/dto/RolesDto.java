package ait.cohort46.accounting.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RolesDto {
    private String login;
    @Singular
    private Set<String> roles;
}
