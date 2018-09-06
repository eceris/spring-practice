package kr.co.eceris.fcm;

public abstract class Sender {
    public static final String PRIVATE_KEY_PATH = "classpath:static/fcm-test-d3b8e-firebase-adminsdk-cwcn6-1d2adf6b0a.json";
    public static final String SEND_URL = "https://fcm.googleapis.com/v1/projects/fcm-test-d3b8e/messages:send";
    public static final String FIREBASE_MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    public static final String DEVICE_PACKAGE_NAME = "kr.co.eceris.fcm.test";
}
