package com.nhnacademy.taskapi.service.impl;


import com.nhnacademy.taskapi.dto.user.UserExistsResponse;
import com.nhnacademy.taskapi.service.AccountClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountClientServiceImpl implements AccountClientService {

    private final RestTemplate restTemplate;

    @Value("${external.account-api.url}")
    private String accountUrl;

    // RestTemplate 사용하여 account-api의 /users/{userId}/exists 엔드포인트 호출
    @Override
    public boolean checkUserExists(String userId) {
        URI uri= UriComponentsBuilder
                .fromUriString(accountUrl)
                .path("/users/"+userId+"/exists")
                .encode()
                .build()
                .toUri();

        ResponseEntity<UserExistsResponse> resp=restTemplate.getForEntity(uri, UserExistsResponse.class);
        log.info("account-api response: {}", resp);

        return resp.getBody().exists();
    }
}
