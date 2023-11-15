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
        String resultURL = null;
        if (imageName != null)
            resultURL = profileBaseUri + imageName;
        return resultURL;
    }

    public String generatePostImageURL(String imageName) {
        String resultURL = null;
        if (imageName != null)
            resultURL = postBaseUri + imageName;
        return resultURL;
    }

    public String generateReviewImageURL(String imageName) {
        String resultURL = null;
        if (imageName != null)
            resultURL = reviewBaseUri + imageName;
        return resultURL;
    }
}
