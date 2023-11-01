package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.GeneralResponseDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : UploadController
 * @date : 2023/11/01
 * @modifyed : $
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Upload", description = "사진 업로드 관련 API")
@RequestMapping("/api/images")
public class FileUploaderController {

    private final ImageService imageService;

    @Operation(summary = "프로필 사진 업로드")
    @PostMapping("/profiles/upload")
    public ResponseEntity<GeneralResponseDTO> uploadProfileImage(@RequestParam("imageFile") MultipartFile imageFile) {

        imageService.uploadProfileImage(imageFile);

        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ok(responseDTO);
    }

    @Operation(summary = "게시글 사진 업로드")
    @PostMapping("/posts/upload")
    public ResponseEntity<GeneralResponseDTO> uploadPostImage(@RequestParam("imageFile") MultipartFile imageFile, @RequestParam("postId") Long postId) {

        imageService.uploadPostImage(imageFile, postId);

        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ok(responseDTO);
    }

    @Operation(summary = "후기 사진 업로드")
    @PostMapping("/reviews/upload")
    public ResponseEntity<GeneralResponseDTO> uploadReviewImage(@RequestParam("imageFile") MultipartFile imageFile) {


        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ok(responseDTO);
    }

    @Operation(summary = "프로필 사진 조회")
    @GetMapping(value = "/profiles/{imagename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> searchProfileImage(@PathVariable("imageName") String imageName) throws IOException {

        byte[] imageByteArray = imageService.searchProfileImage(imageName);

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    @Operation(summary = "포스트 사진 조회")
    @GetMapping(value = "/posts/{imagename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> searchPostImage(@PathVariable("imageName") String imageName) throws IOException {

        byte[] imageByteArray = imageService.searchPostImage(imageName);

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    @Operation(summary = "후기 사진 조회")
    @GetMapping(value = "/reviews/{imagename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> searchReviewImage(@PathVariable("imageName") String imageName) throws IOException {

        byte[] imageByteArray = imageService.searchReviewImage(imageName);

        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
