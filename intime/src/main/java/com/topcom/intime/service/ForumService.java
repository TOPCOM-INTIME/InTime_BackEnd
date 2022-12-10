package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ForumReqDto;
import com.topcom.intime.Dto.ForumResDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.Forum;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.ForumRepository;

@Service
public class ForumService {

	@Autowired
	private ForumRepository forumRepository;
	
	@Transactional
	public int saveForum(ForumReqDto forumReqDto) {
		Forum forum = Forum.builder()
				.title(forumReqDto.getTitle())
				.content(forumReqDto.getContent())
				.writer(getPrincipal())
				.build();
		
		return forumRepository.save(forum).getId();
	}
	
	@Transactional
	public List<ForumResDto> getAllForumsByUser() {
		List<Forum> forums = forumRepository.findAllBywriterId(getPrincipal().getId());
		List<ForumResDto> forumResDtos = new ArrayList<>();
		for(Forum forum : forums) {
			ForumResDto forumResDto = new ForumResDto(forum.getId(),
					forum.getTitle(), forum.getContent(), 
					forum.getCreateDate(), forum.getWriter());
			forumResDtos.add(forumResDto);
		}
		return forumResDtos;
	}
	
	@Transactional
	public List<ForumResDto> getAllForums() {
		List<Forum> forums = forumRepository.findAll();
		List<ForumResDto> forumResDtos = new ArrayList<>();
		for(Forum forum : forums) {
			ForumResDto forumResDto = new ForumResDto(forum.getId(),
					forum.getTitle(), forum.getContent(), 
					forum.getCreateDate(), forum.getWriter());
			forumResDtos.add(forumResDto);
		}
		return forumResDtos;
	}
	
	@Transactional
	public void updateForumById(int id, ForumReqDto forumDto) {
		Forum forum = forumRepository.findById(id)
		.orElseThrow(()->{
			return new IllegalArgumentException("Failed to find Forum By Id : " + id);
		});
		forum.setTitle(forumDto.getTitle());
		forum.setContent(forumDto.getContent());
	}
	
	@Transactional
	public void deleteForumById(int id) {
		forumRepository.deleteById(id);
	}
	
	public User getPrincipal() {
		Object principalObject = 
				SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser();
	}
	
}
