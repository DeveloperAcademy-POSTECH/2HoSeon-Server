package com.twohoseon.app.common;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ImageDTO
 * @date : 2023/11/13
 * @modifyed : $
 **/
public abstract class ImageDTO {
    private final String baseUri = "https:/test.hyunwoo.tech/images/";
    private final String profileBaseUri = baseUri + "profiles/";
    private final String postBaseUri = baseUri + "posts/";
    private final String reviewBaseUri = baseUri + "reviews/";

    public String generateProfileImageURL(String imageName) {
        return profileBaseUri + imageName;
    }

    public String generatePostImageURL(String imageName) {
        return postBaseUri + imageName;
    }

    public String generateReviewImageURL(String imageName) {
        return reviewBaseUri + imageName;
    }
}
