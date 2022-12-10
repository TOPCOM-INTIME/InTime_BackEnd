package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.NoticeReqDto;
import com.topcom.intime.Dto.NoticeResDto;
import com.topcom.intime.model.Notice;
import com.topcom.intime.repository.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepository;
	
	@Transactional
	public void saveNotice(NoticeReqDto noticeReqDto) {
		Notice notice = Notice.builder()
				.title(noticeReqDto.getTitle())
				.content(noticeReqDto.getContent())
				.build();
		
		noticeRepository.save(notice);
	}
	
	@Transactional
	public List<NoticeResDto> getAllNotices() {
		List<Notice> notices = noticeRepository.findAll();
		List<NoticeResDto> noticeResDtos = new ArrayList<>();
		for(Notice notice : notices) {
			NoticeResDto noticeResDto = new NoticeResDto(notice.getId(),
					notice.getTitle(), notice.getContent(), notice.getCreateDate());
			noticeResDtos.add(noticeResDto);
		}
		return noticeResDtos;
	}
	
	@Transactional
	public void updateNoticeById(int id, NoticeReqDto noticeDto) {
		Notice notice = noticeRepository.findById(id)
		.orElseThrow(()->{
			return new IllegalArgumentException("Failed to find Notice By Id : " + id);
		});
		notice.setTitle(noticeDto.getTitle());
		notice.setContent(noticeDto.getContent());
	}
	
	@Transactional
	public void deleteNoticeById(int id) {
		noticeRepository.deleteById(id);
	}
	
}
