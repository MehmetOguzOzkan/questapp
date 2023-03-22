package com.project.questapp.entities;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="post")
@Data
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id",nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	User user;
	
	String title;
	@Lob
	@Column(columnDefinition = "text")
	String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date createDate;
}
