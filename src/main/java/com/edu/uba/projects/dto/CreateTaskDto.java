package com.edu.uba.projects.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {
    private Long id;
    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private String description;
    private int estimation;
}



