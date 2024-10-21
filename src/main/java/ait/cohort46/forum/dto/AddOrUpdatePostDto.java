package ait.cohort46.forum.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class AddOrUpdatePostDto {
    private String title;
    private String content;
    private Set<String> tags;
}
