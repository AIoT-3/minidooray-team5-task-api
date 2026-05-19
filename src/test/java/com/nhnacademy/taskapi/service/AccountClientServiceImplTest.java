package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.user.UserExistsResponse;
import com.nhnacademy.taskapi.service.impl.AccountClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountClientServiceImplTest {

    @InjectMocks
    private AccountClientServiceImpl accountClientService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(accountClientService, "accountUrl", "http://localhost:8080");
    }

    @Test
    @DisplayName("exists - true")
    void checkUserExists() {
        // given
        String userId = "testUser";

        // when
        when(restTemplate.getForEntity(any(URI.class), eq(UserExistsResponse.class)))
                .thenReturn(ResponseEntity.ok(new UserExistsResponse(true)));

        boolean resp=accountClientService.checkUserExists(userId);

        // then
        // RestTemplate의 getForEntity 메서드가 올바르게 호출되었는지 검증
        verify(restTemplate).getForEntity(
                any(URI.class),
                eq(UserExistsResponse.class)
        );
        assertTrue(resp);
    }

    @Test
    @DisplayName("exists - false")
    void checkUserExistsFalse() {
        // given
        String userId = "testUser";

        // when
        when(restTemplate.getForEntity(any(URI.class), eq(UserExistsResponse.class)))
                .thenReturn(ResponseEntity.ok(new UserExistsResponse(false)));

        boolean resp=accountClientService.checkUserExists(userId);

        // then
        // RestTemplate의 getForEntity 메서드가 올바르게 호출되었는지 검증
        verify(restTemplate).getForEntity(
                any(URI.class),
                eq(UserExistsResponse.class)
        );
        assertFalse(resp);
    }
}
