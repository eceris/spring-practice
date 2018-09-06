package kr.co.eceris.fcm;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 * REST API를 이용한 SENDER
 */
@Service
@RequiredArgsConstructor
public class HttpSender extends Sender{
    private static final Logger logger = LoggerFactory.getLogger(HttpSender.class);

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private final ResourceLoader resourceLoader;

    /**
     * 정보를 fcm을 통해 보낸다.
     *
     * @param title 제목
     * @param content 컨텐츠
     * @param token 메시지를 보낼 타겟 디바이스의 토큰
     */
    public void send(String title, String content, @NonNull String token) {
        Message message = assembleMessage(title, content, token);
        logger.debug("message assembled : {}", message);
        send(message);
    }

    /**
     * message 를 보내기 위한 인증토큰을 조회 혹은 갱신
     *
     * @return
     */
    private String getAccessToken() {
        Resource resource = resourceLoader.getResource(PRIVATE_KEY_PATH);
        try (InputStream credentialStream = resource.getInputStream()) {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(credentialStream)
                    .createScoped(Arrays.asList(FIREBASE_MESSAGING_SCOPE));
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (FileNotFoundException e) {
            logger.error("private key file not found : ", e);
            throw new IllegalStateException();
        } catch (IOException e) {
            logger.error("failed to refresh token : ", e);
            throw new IllegalStateException();
        }
    }

    /**
     * 주어진 정보를 토대로 FCM 서버와 약속한 형태로 데이터를 조립
     *
     * @param title
     * @param message
     * @param token
     * @return
     */
    private Message assembleMessage(String title, String message, String token) {
        HashMap<String, Object> androidData = new HashMap<>();
        androidData.put("android data", "what is data");

        Notification notification = Notification.of("MESSAGE_TITLE", "MESSAGE_BODY");
        AndroidMessage android = AndroidMessage.builder()
                .collapse_key("")
                .priority(AndroidMessagePriority.HIGH)
                .ttl("60s")
                .restricted_package_name(DEVICE_PACKAGE_NAME)
                .data(androidData)
                .notification(Notification.of(title, message))
                .build();

        return Message.builder().token(token)
                .data(null)
                .notification(notification)
                .android(android).build();
    }




    /**
     * 메시지를 FCM 서버에 전송
     *
     * @param body
     */
    private ResponseEntity send(@NonNull Message body) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("message", body);
        ResponseEntity<String> result = REST_TEMPLATE.postForEntity(SEND_URL, new HttpEntity<>(params, assembleHttpHeader()), String.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            logger.debug("succeed to send via FCM, result message : {}", result.getBody());
        } else {
            logger.debug("failed to send via FCM, result code : {}, result message : {}", result.getStatusCode(), result.getBody());
        }
        return result;
    }

    /**
     * FCM 서버와 약속한 Header 조립
     *
     * @return
     */
    private HttpHeaders assembleHttpHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.add("Authorization", "Bearer " + getAccessToken());
        return httpHeaders;
    }

    @Value
    @Builder
    static class Message {
        @NonNull
        private String token; //Registration token to send a message to. // 어느 디바이스인지? 디바이스 토큰
        private HashMap<String, String> data; //Input only. Arbitrary key/value payload.
        private Notification notification; //Input only. Basic notification template to use across all platforms.
        private AndroidMessage android;
        //TODO
        //private IosMessage ios;
    }

    @Value
    @Builder
    static class AndroidMessage {
        private final String collapse_key; // An identifier of a group of messages that can be collapsed
        private final AndroidMessagePriority priority;
        private final String ttl; //How long (in seconds) the message should be kept in FCM storage if the device is offline.
        @NonNull
        private final String restricted_package_name; // Package name of the application where the registration tokens must match in order to receive the message.
        private final HashMap<String, Object> data; //Input only. Arbitrary key/value payload.
        private final Notification notification;
    }

    @Value(staticConstructor = "of")
    static class Notification {
        private final String title;
        private final String body;
    }

    enum AndroidMessagePriority {
        NORMAL, HIGH
    }


//    https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html#//apple_ref/doc/uid/TP40008194-CH10-SW1



}

