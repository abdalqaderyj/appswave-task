package com.appswave.appswaveapi.service;

import com.appswave.appswaveapi.model.News;
import com.appswave.appswaveapi.model.Status;

import java.util.List;

public interface NewsService {
	News create(News news);
	List<News> getApproved();
	List<News> getPending();
	News getById(Long id);
	News update(News updatedNews);
	News updateStatus(Long id, Status status);
	Boolean deleteById(Long id);
}
