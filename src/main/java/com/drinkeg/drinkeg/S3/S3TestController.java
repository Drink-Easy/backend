package com.drinkeg.drinkeg.S3;


import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.securityDTO.jwtDTO.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3TestController {

    private final S3Service s3Service;
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<String> upload(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestPart(value = "request") S3TestUploadDTO testUploadDTO,
                                      @RequestPart(value = "picture", required = false) MultipartFile picture) {

        String s = s3Service.SaveImage(picture);
        return ApiResponse.onSuccess("사진 저장 완료");
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> delete(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("picture") String picture) {

        s3Service.deleteFile(picture);
        return ApiResponse.onSuccess("사진 삭제 완료");
    }

}
