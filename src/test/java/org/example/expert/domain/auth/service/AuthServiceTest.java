package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    public void signup_회원가입_중_이메일_중복으로_예외처리한다() {
        // given 사전 조건
        SignupRequest request = new SignupRequest("test@naver.com", "1234", "USER");

        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when 실행 동작
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.signup(request));

        // then 결과 검증
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    public void signup_회원가입에_성공한다() {

        // given 사전 조건
        SignupRequest request = new SignupRequest("test@naver.com", "1234", "USER");

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);

        String encodedPassword = "encodedPassword";
        given(passwordEncoder.encode(request.getPassword())).willReturn(encodedPassword);

        UserRole userRole = UserRole.of(request.getUserRole());
        User user = new User(request.getEmail(), encodedPassword, userRole);

        given(userRepository.save(any())).willReturn(user);

        // when 실행 동작

        SignupResponse response = authService.signup(request);


        // then 결과 검증
        assertNotNull(response);
    }
}