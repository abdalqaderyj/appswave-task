package com.appswave.appswaveapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Date;

@Data
@Entity
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User user;
	private String title;
	private String titleInArabic;
	private String description;
	private String descriptionInArabic;
	private Date publishDate;
	private String imageUrl;
	private Status status;
	private Boolean isActive;
}


