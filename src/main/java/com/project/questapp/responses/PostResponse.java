package com.project.questapp.responses;
import java.util.List;
import com.project.questapp.entities.Post;
import lombok.Data;

@Data
public class PostResponse {
	
	public PostResponse(Post entity,List<LikeResponse> likes) {
		this.id=entity.getId();
		this.userId=entity.getUser().getId();
		this.userName=entity.getUser().getUserName();
		this.text=entity.getText();
		this.title=entity.getTitle();
		this.postLikes=likes;
	}
	
	Long id;
	Long userId;
	String userName;
	String text;
	String title;
	List<LikeResponse> postLikes;
}
