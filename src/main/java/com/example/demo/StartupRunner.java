package com.example.demo;

import com.example.demo.model.WebhookResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Yashwanth N");
        requestBody.put("regNo", "22BCE7240");
        requestBody.put("email", "yashwanthneelapu03@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        WebhookResponse response = restTemplate.postForObject(
                url,
                requestEntity,
                WebhookResponse.class
        );

        System.out.println("Webhook URL: " + response.getWebhookUrl());
        System.out.println("Access Token: " + response.getAccessToken());


        
        String finalSqlQuery = "SELECT d.DEPARTMENT_NAME AS DEPARTMENT_NAME, AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) ORDER BY e.EMP_ID SEPARATOR ', ') AS EMPLOYEE_LIST FROM EMPLOYEE e JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID JOIN PAYMENTS p ON p.EMP_ID = e.EMP_ID WHERE p.AMOUNT > 70000 GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME ORDER BY d.DEPARTMENT_ID DESC LIMIT 10;";

        String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        HttpHeaders submitHeaders = new HttpHeaders();
        submitHeaders.setContentType(MediaType.APPLICATION_JSON);
        submitHeaders.set("Authorization", response.getAccessToken());

        Map<String, Object> finalBody = new HashMap<>();
        finalBody.put("finalQuery", finalSqlQuery);

        HttpEntity<Map<String, Object>> submitEntity =
                new HttpEntity<>(finalBody, submitHeaders);

        ResponseEntity<String> submitResponse = restTemplate.exchange(
                submitUrl,
                HttpMethod.POST,
                submitEntity,
                String.class
        );

        System.out.println("Submission Response: " + submitResponse.getBody());
    }
}
