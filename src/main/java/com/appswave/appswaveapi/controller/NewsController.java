package com.appswave.appswaveapi.controller;

import com.appswave.appswaveapi.Util.TokenUtil;
import com.appswave.appswaveapi.model.AppsWaveCustomException;
import com.appswave.appswaveapi.model.News;
import com.appswave.appswaveapi.model.Status;

import com.appswave.appswaveapi.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;


@RestController
@RequestMapping("/v1/appswave-api/news")
public class NewsController {
	private final TokenUtil jwtUtil;
	private final NewsService newsService;

	@Autowired
	public NewsController(
			NewsService newsService,
			TokenUtil jwtUtil
	) {
		this.newsService = newsService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	public ResponseEntity<?> createNews(
			@RequestBody News news,
			HttpServletRequest request
	) throws Exception {
		News createdNews = newsService.create(news);
		return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(createdNews);
	}

	@PutMapping
	public ResponseEntity<?> updateNews(
			@RequestBody News news,
			HttpServletRequest request
	) throws Exception {
		News updatedNews = newsService.update(news);
		return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED).body(updatedNews);
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<?> approveNews(
			@PathVariable Long id,
			HttpServletRequest request
	) throws Exception {
		News updatedNews = newsService.updateStatus(id, Status.APPROVED);
		return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED).body(updatedNews);
	}

	@GetMapping("/{status}/all")
	public ResponseEntity<List<News>> getAllByStatus(
			@PathVariable String status
	) {
		if ("APPROVED".equalsIgnoreCase(status)) {
			return ResponseEntity.ok(newsService.getApproved());
		}
		return ResponseEntity.ok(newsService.getPending());
	}

	@GetMapping("/{id}")
	public ResponseEntity<News> getNewsById(
			@PathVariable Long id
	) {
		return ResponseEntity.ok(newsService.getById(id));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteNews(
			@PathVariable Long id,
			HttpServletRequest request
	) throws Exception {
		News news = newsService.getById(id);

		if (Status.APPROVED.equals(news.getStatus())) {
			String header = request.getHeader("Authorization");
			String token = header.substring(7);
			String role = jwtUtil.extractUserRole(token);
			if (!"Admin".equalsIgnoreCase(role)) {
				throw new AppsWaveCustomException("unauthorized to delete approved news", HttpServletResponse.SC_FORBIDDEN);
			}
		}

		boolean success = newsService.deleteById(id);
		if (!success) {
			throw new AppsWaveCustomException("failed to delete news", HttpServletResponse.SC_BAD_REQUEST);
		}
		return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED).body("news deleted successfully");
	}
}
