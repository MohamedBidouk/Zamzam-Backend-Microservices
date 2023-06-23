package org.zamzam.notificationservice.service;

import org.zamzam.notificationservice.model.Notification;
import org.zamzam.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void saveNotification(String orderPlacedEvent){
        Notification notification = new Notification();

        notification.setMessage(orderPlacedEvent);
        notificationRepository.save(notification);
    }
    public List<Notification> getAllNotification(){
        return notificationRepository.findAll();
    }

    public String deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
        return "Deleted Successfully";
    }
}
