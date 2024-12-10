package com.hun.market.letter;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleService {

//    private static final Logger logger = LoggerFactory.getLogger(GoogleService.class); // Google Sheets와 상호작용하기 위한 메서드를 포함
    private static final String APPLICATION_NAME = "Google Sheets Application"; // Google Sheets 애플리케이션의 이름을 나타냄
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance(); // 상수는 JSON 데이터를 처리하기 위한 JsonFactory 인스턴스를 제공
    private static final String CREDENTIALS_FILE_PATH = "/googlesheet/google.json"; // 인증에 사용되는 JSON 파링릐 경로를 지정
    private Sheets sheetsService;

    private final RedisTemplate redisCacheTemplate;

    private static final String SPREADSHEET_ID = "12-kj9LL61DMihahC4_v5kKS3Fu66rkSVrGc6pFcHglU";

    // 현재의 메소드는 Sheets 인스턴스를 얻는 데 사용
    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
            sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        return sheetsService;
    }

    public void writeToSh22tCache() {
        try {
            Sheets service = getSheetsService();

            Sheets.Spreadsheets.Values.Get request = service.spreadsheets().values().get(SPREADSHEET_ID, "RAW");
            ValueRange response = request.execute();

            List<List<Object>> sheetData = response.getValues();

            if (sheetData == null || sheetData.isEmpty()) {
                log.info("No data found.");
            } else {
                for (int i = 1; i < sheetData.size(); i++) {
                    SetOperations setOperations = redisCacheTemplate.opsForSet();

                    List<Object> row = sheetData.get(i);

                    String employeeNum = String.valueOf(row.get(6));
                    String sender = String.valueOf(row.get(2));
                    String receiver = String.valueOf(row.get(4));
                    String senderDepart = String.valueOf(row.get(1));
                    String message = String.valueOf(row.get(5));

                    Letter letter = Letter.from()
                            .sender(sender)
                            .senderDepart(senderDepart)
                            .message(message)
                            .build();

                    setOperations.add(employeeNum, letter);
                    redisCacheTemplate.opsForValue().set("name:"+employeeNum, receiver);
                }
            }
        } catch (Exception e) {
            throw new RedisPostException("Failed to write data to the redis: " + e.getMessage());
        }
    }

}
