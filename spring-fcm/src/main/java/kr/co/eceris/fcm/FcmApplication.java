package kr.co.eceris.fcm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FcmApplication {

    @Autowired
    private FcmService service;

    public static void main(String[] args) {
        SpringApplication.run(FcmApplication.class, args);
    }

    @PostMapping("send/{message}")
    public void send(@PathVariable String message) {
        String token = "ckXAqN-7e3w:APA91bHMPlH4ylU3rGY6uFIQD8E3c8SIVJmdQvbI_Ta_LIFlVgYEXX1UmH9mtfrVml0N_TkD91DLYvpYpREqMbwZdc9PRmH3j-BowyU7Ms_X5LTRH6F2lUGGX5pmpLEmteMzymwLCdqK";
        service.send("title", message, token);
    }
}
