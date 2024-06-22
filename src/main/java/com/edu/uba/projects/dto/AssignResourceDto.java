package com.edu.uba.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignResourceDto {
    private String name;
    private String address;
    private String phone;
}
