package kr.co.eceris.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Admin SDK를 이용한 SENDER
 */
@Service
@RequiredArgsConstructor
public class AdminSender extends Sender {
    private static final Logger logger = LoggerFactory.getLogger(AdminSender.class);

    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        Resource resource = resourceLoader.getResource(PRIVATE_KEY_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            logger.error("Failed to initialize FirebaseApp ", e);
            throw new IllegalStateException();
        }
    }

    public void send(String title, String content, @NonNull String token) {
        // This registration token comes from the client FCM SDKs.
        String registrationToken = token;

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setAndroidConfig(getAndroid(title, content))
                .setApnsConfig(getApns())
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }

    private ApnsConfig getApns() {
        return ApnsConfig.builder()
                .putCustomData("type", "TYPE") //TODO 검증할 것
                .putCustomData("fromSeq", "FROMSEQ")
                .putCustomData("toSeq", "TOSEQ")
                .setAps(Aps.builder()
                        .setSound("sound.aif")
                        .setAlert("MESSAGE")
                        .build())
                .build();
    }


    private AndroidConfig getAndroid(String title, String body) {
        return AndroidConfig.builder()
                .setTtl(3600 * 1000) // 1 hour in milliseconds
                .setPriority(AndroidConfig.Priority.HIGH)   //required false
                .setRestrictedPackageName(DEVICE_PACKAGE_NAME) //required false
                .setNotification(AndroidNotification.builder()  //required true
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();
    }
}
