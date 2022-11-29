package com.topcom.intime.firebase;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {


    private final String API_URL = "https://fcm.googleapis.com/v1/projects/intime-fcm/messages:send";
    private final ObjectMapper objectMapper;

    public int sendMessageTo(RequestForFcmDto requestDto) throws IOException {
        
    	if (requestDto.getType().equals("schedule")) {//schedule invitation
    		System.out.println("T1");
    	}
    	else if(requestDto.getType().equals("member")) {//member invitation
    		System.out.println("T2");
    	}
    	else {
    		System.out.println("Failed because type is not proper. : " + requestDto.getType());
    		return 0;
    	}
    	String slink = "Intime://write";
    	//intime://pattern //test
    	//intime://schedule
    	//intime://friend
    	
    	String message = makeMessageForSchdule(requestDto, slink);

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

    private String makeMessageForSchdule(RequestForFcmDto requestDto, String slink) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                    .token(requestDto.getTargetToken())
                    .notification(FcmMessage.Notification.builder()
                            .title(requestDto.getTitle())
                            .body(requestDto.getBody())
                            .image(null)
                            .build()
                    )
                    .data(FcmMessage.Data.builder()
                    		.link("Intime://write")
                    		.userName(requestDto.getUserName())
                    		.scheduleName(requestDto.getScheduleName())
                    		.scheduleTime(requestDto.getScheduleTime())
                    		.destName(requestDto.getDestName())
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
