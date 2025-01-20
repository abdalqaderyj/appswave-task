package com.appswave.appswaveapi.service;

import com.appswave.appswaveapi.dao.NewsRepository;
import com.appswave.appswaveapi.model.News;
import com.appswave.appswaveapi.model.Status;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NewsServiceImpl implements NewsService {
	private final NewsRepository newsRepository;

	@Autowired
	public NewsServiceImpl(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	@Override
	public News create(News news) {
		if (ObjectUtils.isEmpty(news.getPublishDate())) {
			news.setPublishDate(new Date());
		}

		news.setStatus(Status.PENDING);
		news.setIsActive(true);
		return newsRepository.save(news);
	}

	@Override
	public List<News> getApproved() {
		return newsRepository.findByStatusAndIsActiveTrue(Status.APPROVED);
	}

	@Override
	public List<News> getPending() {
		return newsRepository.findByStatusAndIsActiveTrue(Status.PENDING);
	}

	@Override
	public News getById(Long id) {
		return newsRepository.findById(id).orElse(new News());
	}

	@Override
	public News updateStatus(Long id, Status status) {
		News news = newsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("News not found"));
		news.setStatus(status);

		return newsRepository.save(news);
	}

	@Override
	public News update(News updatedNews) {
		if (newsRepository.findById(updatedNews.getId()).isPresent()) {
			return newsRepository.save(updatedNews);
		} else {
			throw new NoSuchElementException("News not found");
		}
	}

	@Override
	public Boolean deleteById(Long id) {
		newsRepository.deleteById(id);
		return newsRepository.findById(id).isEmpty();
	}

	@Scheduled(cron = "0 */1 * * * ?") // Run every minute
	public void softDeleteExpiredNews() {
		System.out.println("Soft delete expired news..........");
		newsRepository.markExpiredNewsAsInactive();
	}
}
