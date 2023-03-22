package com.project.questapp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.questapp.entities.*;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.requests.PostCreateRequest;
import com.project.questapp.requests.PostUpdateRequest;
import com.project.questapp.responses.LikeResponse;
import com.project.questapp.responses.PostResponse;

@Service
public class PostService {
	private PostRepository postRepository;
	private LikeService likeService;
	private UserService userService;
	
	public PostService(PostRepository postRepository, UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
	}
	
	@Autowired
	public void setLikeService(LikeService likeService) {
		this.likeService=likeService;
	}

	public List<PostResponse> getAllPosts(Optional<Long> userId) {
		List<Post> list;
		if(userId.isPresent()) {
			list = postRepository.findByUserId(userId.get());
		}else {
			list = postRepository.findAll();
		}
		return list.stream().map(b->{
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(b.getId()));
			return new PostResponse(b,likes);}).collect(Collectors.toList());
	}
	
	public PostResponse getOnePostByIdWithLikes(Long postId) {
		Post post = postRepository.findById(postId).orElse(null);
		List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
		return new PostResponse(post,likes);
	}

	public Post getOnePostById(Long postId) {
		return postRepository.findById(postId).orElse(null);
	}

	public Post createOnePost(PostCreateRequest newPostRequest) {
		User user = userService.getOneUserById(newPostRequest.getUserId());
		if(user==null)
			return null;
		Post toSave=new Post();
		toSave.setId(newPostRequest.getId());
		toSave.setText(newPostRequest.getText());
		toSave.setTitle(newPostRequest.getTitle());
		toSave.setCreateDate(new Date());
		toSave.setUser(user);
		return postRepository.save(toSave);
	}
	public void deleteOnePostById(Long postId) {
		postRepository.deleteById(postId);
		
	}
	public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
		Optional<Post> post=postRepository.findById(postId);
		if(post.isPresent()) {
			Post toUpdate=post.get();
			toUpdate.setText(updatePost.getText());
			toUpdate.setTitle(updatePost.getTitle());
			postRepository.save(toUpdate);
			return toUpdate;
		}
		return null;
	}
}
