package com.project.questapp.controllers;
import java.util.*;
import org.springframework.web.bind.annotation.*;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Like;
import com.project.questapp.requests.CommentCreateRequest;
import com.project.questapp.requests.CommentUpdateRequest;
import com.project.questapp.requests.LikeCreateRequest;
import com.project.questapp.responses.LikeResponse;
import com.project.questapp.services.LikeService;

@RestController
@RequestMapping("/likes")
public class LikeController {
	private LikeService likeService;

	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}
	@GetMapping
	public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId,@RequestParam Optional<Long> postId) {
		return likeService.getAllLikesWithParam(userId,postId);
	}
	@GetMapping("/{likeId}")
	public Like getOneLike(@PathVariable Long likeId) {
		return likeService.getOneLikeById(likeId);
	}
	@PostMapping
	public Like createOneComment(@RequestBody LikeCreateRequest request) {
		return likeService.createOneLike(request);
	}
	@DeleteMapping("/{likeId}")
	public void deleteOneComment(@PathVariable Long likeId) {
		likeService.deleteOneLikeById(likeId);
	}
	
}
