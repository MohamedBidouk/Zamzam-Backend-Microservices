package org.zamzam.notificationservice.controller;


import org.zamzam.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.notificationservice.model.Notification;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotification(){
        return new ResponseEntity<>(notificationService.getAllNotification(), HttpStatus.OK);
    }
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId")Long notificationId){
        return new ResponseEntity<>(notificationService.deleteNotification(notificationId), HttpStatus.OK);
    }
}
