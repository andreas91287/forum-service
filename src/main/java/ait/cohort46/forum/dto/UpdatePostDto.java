package ait.cohort46.forum.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class UpdatePostDto {
    private String title;
    private Set<String> tags;
}
