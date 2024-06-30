package com.edu.uba.projects.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectDto {
	private String title;
	private Date startDate;
	private Date endDate;
	private String status;
	private String description;
	private Leader leader;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Leader {
		private long legajo;
		private String nombre;
		private String apellido;
	}
}
