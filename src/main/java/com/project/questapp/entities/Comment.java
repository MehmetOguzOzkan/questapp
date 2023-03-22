package com.project.questapp.entities;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="comment")
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id",nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	Post post;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	User user;
	
	@Lob
	@Column(columnDefinition="text")
	String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date createDate;
}
