package me.vita.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.vita.domain.NotificationVO;
import me.vita.mapper.NotificationMapper;

@Service
public class NotificationServiceImpl implements NotificationService{

	
	@Autowired
	NotificationMapper mapper;

	@Override
	public boolean register(NotificationVO notificationVO) {
		return mapper.insert(notificationVO) == 1;
	}

	@Override
	public boolean remove(Integer notifyNo) {
		return mapper.delete(notifyNo) == 1;
	}

	@Override
	public boolean modify(Integer notifyNo) {
		return mapper.update(notifyNo) == 1;
	}

	@Override
	public List<NotificationVO> getList(String userId, Integer page) {
		return mapper.selectList(userId, page);
	}

	@Override
	public int getNotifyChkCount(String userId) {
		return mapper.selectNotifyChkCount(userId);
	}
	
	
	
}
