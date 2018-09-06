package kr.co.eceris.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FcmApplicationTests {
//    private ResourceLoader resourceLoader;
//
//    public FcmApplicationTests(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }

    static final RestTemplate REST_TEMPLATE = new RestTemplate();
    static final String URL = "https://fcm.googleapis.com/v1/projects/fcm-test-d3b8e/messages:send";

    private HttpEntity<HttpHeaders> getHttpHeadersHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + getAccessToken());
        return new HttpEntity<>(httpHeaders);
    }

    // TODO createScoped에 들어가는건 뭔지 확실히 알기
    private String getAccessToken() {
//        Resource resource = resourceLoader.getResource("classpath:static/fcm-test-d3b8e-firebase-adminsdk-cwcn6-1d2adf6b0a.json");
        try (FileInputStream credentialStream = new FileInputStream("/fcm-test-d3b8e-firebase-adminsdk-cwcn6-1d2adf6b0a.json")) {
//        try (InputStream credentialStream = resource.getInputStream()) {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(credentialStream)
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("private key file not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("failed to refresh token");
        }
    }

    @Test
    public void sendTest() throws JsonProcessingException {
        HashMap<String, Object> params = new HashMap<>();
        String token = "ckXAqN-7e3w:APA91bHMPlH4ylU3rGY6uFIQD8E3c8SIVJmdQvbI_Ta_LIFlVgYEXX1UmH9mtfrVml0N_TkD91DLYvpYpREqMbwZdc9PRmH3j-BowyU7Ms_X5LTRH6F2lUGGX5pmpLEmteMzymwLCdqK";
        Notification notification = Notification.of("MESSAGE_TITLE", "MESSAGE_BODY");

        HashMap<String, Object> androidData = new HashMap<>();
        androidData.put("android data", "what is data");
        Android android = Android.of("", AndroidMessagePriority.HIGH, "60s", androidData, Notification.of("ANDROID_MESSAGE_TITLE", "ANDROID_MESSAGE_BODY"));


        Message message = Message.of("FCM TEST MESSAGE", token, null, notification, android);
        params.put("message", message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(params);
        System.out.println(jsonString);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(jsonString, httpHeaders);

        ResponseEntity<String> result = REST_TEMPLATE.postForEntity(URL, entity, String.class, jsonString);
        System.out.println(result);

    }


    @AllArgsConstructor
    @Getter
    static class Message {
        private String name; //Output Only. The identifier of the message sent, in the format of projects/*/messages/{message_id}.
        private String token; //Registration token to send a message to. // 어느 디바이스인지? 디바이스 토큰
        private HashMap<String, String> data; //Input only. Arbitrary key/value payload.
        private Notification notification; //Input only. Basic notification template to use across all platforms.
        private Android android;

        public static Message of(String name, String token, HashMap<String, String> data, Notification notification, Android android) {
            return new Message(name, token, data, notification, android);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class Android {
        private final String collapse_key;
        private final AndroidMessagePriority priority;
        private final String ttl; //How long (in seconds) the message should be kept in FCM storage if the device is offline.
        private final String restricted_package_name;
        private final HashMap<String, Object> data; //Input only. Arbitrary key/value payload.
        private final Notification notification;

        public static Android of(String collapse_key, AndroidMessagePriority priority, String ttl, HashMap<String, Object> data, Notification notification) {
            String restricted_package_name = "kr.co.eceris.fcm.test";
            return new Android(collapse_key, priority, ttl, restricted_package_name, data, notification);
        }
    }

    enum AndroidMessagePriority {
        NORMAL, HIGH;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class Notification {
        private final String title;
        private final String body;

        public static Notification of(String title, String body) {
            return new Notification(title, body);
        }
    }


}
