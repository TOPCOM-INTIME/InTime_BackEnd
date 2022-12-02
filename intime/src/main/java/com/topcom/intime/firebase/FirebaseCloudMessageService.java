package com.topcom.intime.firebase;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

	@Autowired
	private UserRepository userRepository;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/intime-fcm/messages:send";
    private final ObjectMapper objectMapper;
    private final String schedule_link = "intime://schedule";
    private final String friend_link = "intime://community";
    
    
    public int sendMessageForSchedule(InviteDto inviteDto, String uName) throws IOException {
    	String message = makeMessageForSchdule(inviteDto, uName);

    	return requestToFcmServer(message);
    }
    
    public int sendMessageForFriend(InviteDto inviteDto, String uName) throws IOException {
    	String message = makeMessageForFriend(inviteDto, uName);

    	return requestToFcmServer(message);
    }
    
    public int requestToFcmServer(String message) throws IOException {
       
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + getAccessToken())
                .addHeader("Content-Type", "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("RESPONSE : " + response.body().string());
        
        return 1;
    }

    private String makeMessageForSchdule(InviteDto inviteDto, String senderName) throws JsonParseException, JsonProcessingException {
    	String title = "단체 일정 초대";
        String body = senderName + "님으로 부터 " + " 일정을 초대받았습니다.";
        
        String recieverName = inviteDto.getUserName();
        User reciever = userRepository.findByUsername(recieverName)
        		.orElseThrow(()->{
        			return new IllegalArgumentException("Failed to find User by username: " + recieverName);
        		});
        String deviceToken = reciever.getDeviceToken();
        
    	FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                    .token(deviceToken)
                    .notification(FcmMessage.Notification.builder()
                            .title(title)
                            .body(body)
                            .image(null)
                            .build()
                    )
                    .data(FcmMessage.Data.builder()
                    		.link(friend_link)
                    		.userName(senderName)
                    		.scheduleName(inviteDto.getScheduleName())
                    		.scheduleTime(inviteDto.getScheduleTime())
                    		.destName(inviteDto.getDestName())
                    		.build()
                    )
                    .build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
    
    private String makeMessageForFriend(InviteDto inviteDto, String uName) throws JsonParseException, JsonProcessingException {
    	String title = "친구 요청";
        String body = uName + "님으로 부터 친구요청을 받았습니다.";
    	FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                    .token(inviteDto.getTargetToken())
                    .notification(FcmMessage.Notification.builder()
                            .title(title)
                            .body(body)
                            .image(null)
                            .build()
                    )
                    .data(FcmMessage.Data.builder()
                    		.link(schedule_link)
                    		.userName(uName)
                    		.build()
                    )
                    .build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
    

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
	
}
