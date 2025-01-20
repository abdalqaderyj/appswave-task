package com.appswave.appswaveapi.dao;

import com.appswave.appswaveapi.model.News;
import com.appswave.appswaveapi.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
	List<News> findByStatusAndIsActiveTrue(Status status);

	@Transactional
	@Modifying
	@Query(value = "UPDATE news SET is_active = false WHERE publish_date < NOW()", nativeQuery = true)
	void markExpiredNewsAsInactive();
}
