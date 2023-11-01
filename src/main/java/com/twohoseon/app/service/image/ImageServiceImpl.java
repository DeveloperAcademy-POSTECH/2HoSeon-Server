package com.twohoseon.app.service.image;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.InvalidFileTypeException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ImageServiceImpl
 * @date : 2023/11/01
 * @modifyed : $
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${file.dir}")
    private String fileDir;

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void uploadProfileImage(MultipartFile file) {

        Member member = getMemberFromRequest();

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        log.info(fileName);
        String saveName = fileDir + "profiles" + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            file.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        member.setUserProfileImage(fileName);
        memberRepository.save(member);

    }

    @Override
    public void uploadPostImage(MultipartFile file, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        log.info(fileName);
        String saveName = fileDir + "posts" + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            file.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        post.setImage(fileName);
        postRepository.save(post);
    }

    @Override
    public void uploadReviewImage(MultipartFile file) {

    }

    @Override
    public byte[] searchProfileImage(String imageName) throws IOException {
        String image = fileDir + "profiles" + File.separator + imageName;
        InputStream imageStream = new FileInputStream(image);
        log.info(imageStream.toString());
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        log.info(imageStream.toString());

        return imageByteArray;
    }

    @Override
    public byte[] searchPostImage(String imageName) throws IOException {
        String image = fileDir + "posts" + File.separator + imageName;
        InputStream imageStream = new FileInputStream(image);
        log.info(imageStream.toString());
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        log.info(imageStream.toString());

        return imageByteArray;
    }

    @Override
    public byte[] searchReviewImage(String imageName) throws IOException {
        String image = fileDir + "reviews" + File.separator + imageName;
        InputStream imageStream = new FileInputStream(image);
        log.info(imageStream.toString());
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        log.info(imageStream.toString());

        return imageByteArray;
    }
}
