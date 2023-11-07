package com.twohoseon.app.service.image;

import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.InvalidFileTypeException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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

    private final PostRepository postRepository;

    @Override
    public String uploadImage(MultipartFile file, String path) {

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));

        String saveName = fileDir + path + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            file.transferTo(savePath);

            String thumbnailSaveName = fileDir + path + File.separator + "thumb_" + fileName;
            File thumbnailFile = new File(thumbnailSaveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 50, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    public String updateImage(MultipartFile file, String path, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        if (!file.getContentType().startsWith("image")) {
            throw new InvalidFileTypeException();
        }

        File removeFile = null;

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        String saveName = fileDir + path + File.separator + fileName;
        Path savePath = Paths.get(saveName);

        try {
            if (post.getImage() != null) {
                removeFile = new File(fileDir + path + File.separator + URLDecoder.decode("thumb_" + post.getImage(), "UTF-8"));
                removeFile.delete();
                removeFile = new File(fileDir + path + File.separator + URLDecoder.decode(post.getImage(), "UTF-8"));
                removeFile.delete();
            }

            file.transferTo(savePath);

            String thumbnailSaveName = fileDir + path + File.separator + "thumb_" + fileName;
            File thumbnailFile = new File(thumbnailSaveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        postRepository.save(post);
        return fileName;
    }
}
