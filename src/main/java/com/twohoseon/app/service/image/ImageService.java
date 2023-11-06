package com.twohoseon.app.service.image;

import com.twohoseon.app.service.CommonService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ImageService
 * @date : 2023/11/01
 * @modifyed : $
 **/
public interface ImageService extends CommonService {
    String uploadImage(MultipartFile file, String path);

    String updateImage(MultipartFile file, String path, Long postId);

}
