package com.project.questapp.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.project.questapp.entities.*;
import com.project.questapp.repos.*;
import com.project.questapp.responses.UserResponse;

@Service
public class UserService {
	private UserRepository userRepository;
	private LikeRepository likeRepository;
	private CommentRepository commentRepository;
	private PostRepository postRepository;

	public UserService(UserRepository userRepository, LikeRepository likeRepository,
			CommentRepository commentRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User saveOneUser(User newUser) {
		return userRepository.save(newUser);
	}

	public User getOneUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User updateOneUserById(Long userId, User newUser) {
		Optional<User> user=userRepository.findById(userId);
		if(user.isPresent()) {
			User foundUser=user.get();
			foundUser.setUserName(newUser.getUserName());
			foundUser.setPassword(newUser.getPassword());
			foundUser.setAvatar(newUser.getAvatar());
			return foundUser;
		}else {
			return null;
		}
	}

	public void deleteOneUserById(Long userId) {
		userRepository.deleteById(userId);
	}

	public User getOneUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public List<Object> getUserActivity(Long userId) {
		List<Long> postIds = postRepository.findTopByUserId(userId);
		if(postIds.isEmpty()) {
			return null;
		}
		List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
		List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
		List<Object> result = new ArrayList<>();
		result.addAll(comments);
		result.addAll(likes);
		return result;
		
	}
}
