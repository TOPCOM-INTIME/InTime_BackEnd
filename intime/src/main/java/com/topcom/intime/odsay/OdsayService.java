package com.topcom.intime.odsay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcom.intime.odsay.dto.OdsayResponseDto;

@Service
public class OdsayService {

	public OdsayResponseDto requestApi() throws IOException {

		// ODsay Api Key 정보
		String apiKey = "SxaZeNIaVqLgFZIhZD72qI7y4Wrf2QRkvV5wd+SgNIk";
		
		String urlInfo = "https://api.odsay.com/v1/api/searchPubTransPathT?SX=127.0455&SY=37.2833&EX=126.7626&EY=37.6768&apiKey=" + URLEncoder.encode(apiKey, "UTF-8");

		// http 연결
		URL url = new URL(urlInfo);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader bufferedReader = 
			new BufferedReader(new InputStreamReader(conn.getInputStream()));

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		bufferedReader.close();
		conn.disconnect();

		ObjectMapper objectMapper = new ObjectMapper();
		OdsayResponseDto odsayResponseDto = null;
		try {
			odsayResponseDto = objectMapper.readValue(sb.toString(), OdsayResponseDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return odsayResponseDto;
		
	}
	
	
}
