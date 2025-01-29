package com.drinkeg.drinkeg.domain.member.login.oauth2.controller;


import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO.AppleDeleteDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.ApplePrivateKeyGenerator;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.AppleClientSecretGenerator;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoLoginDTO.KakaoLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoService.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleService.AppleService;


import java.security.PrivateKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final AppleService appleService;
    private final KakaoLoginService kakaoLoginService;
    private final ApplePrivateKeyGenerator privateKeyGenerator;
    private final AppleClientSecretGenerator appleClientSecretGenerator;

    @Value("${spring.servlet.social-login.provider.apple.private-key-path}")
    private String privateKeyPath;


    @PostMapping("/login/apple")
    @Operation(summary = "애플 로그인", description = "클라이언트에게 Identity Token을 전달 받아 유저 정보를 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");
        LoginResponseDTO loginResponseDTO = appleService.appleLogin(appleLoginRequestDTO, response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인", description = "클라이언트에게 카카오 유저 정보를 전달 받아 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody KakaoLoginRequestDTO kakaoLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start kakao login controller============");

        LoginResponseDTO loginResponseDTO = kakaoLoginService.kakaoLogin(kakaoLoginRequestDTO , response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }

    @DeleteMapping("member/delete/apple")
    @Operation(summary = "애플 ", description = "애플 회원 탈퇴하고 유저 정보를 삭제합니다.")
    public ApiResponse<?> deleteApple(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody AppleDeleteDTO appleDeleteDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple delete controller============");

        appleService.unlinkApple(principalDetail.getUsername(),appleDeleteDTO.getAuthorizationCode(),response);

        return ApiResponse.onSuccess("애플 회원 탈퇴 성공");

    }
    @GetMapping("/private-key")
    @Operation(summary = "애플 private key Test ", description = "애플 private-key를 확인합니다.")
    public String loadPrivateKey() {
        try {
            // Private Key 가져오기
            PrivateKey privateKey = privateKeyGenerator.getPrivateKey();
            String algorithm = privateKey.getAlgorithm();

            return "Private key successfully loaded: " + algorithm;
        } catch (Exception e) {
            return "Failed to load private key: " + e.getMessage();
        }
    }


    @GetMapping("/clientSecret")
    public ResponseEntity<String> getAppleClientSecret() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String clientSecret = appleClientSecretGenerator.generateClientSecret();
        return ResponseEntity.ok(clientSecret);
    }

    @GetMapping("/check-environment")
    public String checkEnvironment() {
        StringBuilder result = new StringBuilder();


        try {
            // 출력 환경변수 값 확인
            result.append("Private Key Path: ").append(privateKeyPath).append("\n");

            // 경로가 비어 있는지 확인
            if (privateKeyPath == null || privateKeyPath.isEmpty()) {
                result.append("Error: Private Key Path is empty or null.\n");
                return result.toString();
            }

            // 절대 경로 확인
            String absolutePath = Paths.get(privateKeyPath).toAbsolutePath().toString();
            result.append("Absolute Path: ").append(absolutePath).append("\n");

            // 파일 존재 여부 확인
            if (Files.exists(Paths.get(privateKeyPath))) {
                result.append("File exists at the path.\n");
            } else {
                result.append("File does NOT exist at the specified path.\n");
            }

            // 파일 읽기 권한 확인
            if (Files.isReadable(Paths.get(privateKeyPath))) {
                result.append("File is readable.\n");
            } else {
                result.append("File is NOT readable. Check file permissions.\n");
            }

        } catch (Exception e) {
            // 예외 발생 시 상세 오류 메시지 추가
            result.append("Error occurred while checking environment or file: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }

        return result.toString();


}
}
