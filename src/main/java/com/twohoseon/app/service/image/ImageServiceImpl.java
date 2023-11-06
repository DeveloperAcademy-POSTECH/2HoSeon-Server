package com.twohoseon.app.service.image;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.InvalidFileTypeException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

        String saveName = fileDir + "profiles" + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            file.transferTo(savePath);

            String thumbnailSaveName = fileDir + "profiles" + File.separator + "thumb_" + fileName;
            File thumbnailFile = new File(thumbnailSaveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 150, 150);
        } catch (IOException e) {
            e.printStackTrace();
        }

        member.setUserProfileImage(fileName);
        memberRepository.save(member);
    }

    @Override
    public void uploadPostImage(List<MultipartFile> files, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        List<String> imageList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image")) {
                throw new InvalidFileTypeException();
            }

            String originalName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
            imageList.add(fileName);

            String saveName = fileDir + "posts" + File.separator + fileName;
            Path savePath = Paths.get(saveName);

            try {
                file.transferTo(savePath);

                String thumbnailSaveName = fileDir + "posts" + File.separator + "thumb_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 150, 150);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        post.setImageList(imageList);
        postRepository.save(post);
    }

    @Override
    public void uploadReviewImage(MultipartFile file, Long reviewId) {

        Post post = postRepository.findById(reviewId)
                .orElseThrow(() -> new PostNotFoundException());

        List<String> imageList = new ArrayList<>();

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        imageList.add(fileName);

        String saveName = fileDir + "reviews" + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            file.transferTo(savePath);

            String thumbnailSaveName = fileDir + "reviews" + File.separator + "thumb_" + fileName;
            File thumbnailFile = new File(thumbnailSaveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 150, 150);
        } catch (IOException e) {
            e.printStackTrace();
        }

        post.setImageList(imageList);
        postRepository.save(post);
    }
}
