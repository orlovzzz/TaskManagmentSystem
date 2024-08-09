package org.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangeTaskDTO {
    private String id;
    private String title;
    private String description;
    private String priority;
}
