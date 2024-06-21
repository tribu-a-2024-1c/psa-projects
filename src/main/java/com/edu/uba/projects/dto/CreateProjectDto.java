package com.edu.uba.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {

    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private String description;
    private Long productId;
}