package com.twohoseon.app.dto.apns;

import com.eatthepath.json.JsonSerializer;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.twohoseon.app.entity.post.Post;

import java.time.LocalDateTime;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomApnsPayloadBuilder
 * @date : 11/1/23 12:07 AM
 * @modifyed : $
 **/
public class CustomApnsPayloadBuilder extends ApnsPayloadBuilder {
    private final String baseUri = "https://www.wote.social/images/";
    private final String profileBaseUri = baseUri + "profiles/";
    private final String postBaseUri = baseUri + "posts/";
    private final String reviewBaseUri = baseUri + "reviews/";

    private static final String DESTINATION_KEY = "destination";
    private static final String POST_ID_KEY = "post_id";
    private static final String POST_STATUS = "post_status";
    private static final String CONSUMER_TYPE_EXIST_KEY = "consumer_type_exist";

    private static final String AUTHOR_PROFILE = "author_profile";
    private static final String POST_IMAGE = "post_image";
    private static final String IS_COMMENT = "is_comment";

    private static final String NOTIFICATION_TIME = "notification_time";

    @Override
    public String build() {
        return JsonSerializer.writeJsonTextAsString(this.buildPayloadMap());
    }

    @Override
    public String buildMdmPayload(final String pushMagicValue) {
        return JsonSerializer.writeJsonTextAsString(this.buildMdmPayloadMap(pushMagicValue));
    }

    public CustomApnsPayloadBuilder setPostDetails(final String postId) {
        super.addCustomProperty(DESTINATION_KEY, "post_detail");
        super.addCustomProperty(POST_ID_KEY, postId);
        return this;
    }

    public CustomApnsPayloadBuilder setPostDetails(final Post post) {
        super.addCustomProperty(POST_ID_KEY, post.getId());
        super.addCustomProperty(NOTIFICATION_TIME, LocalDateTime.now());
        switch (post.getPostStatus()) {
            case REVIEW -> super.addCustomProperty(POST_IMAGE, generateReviewImageURL(post.getImage()));
            case ACTIVE, CLOSED -> super.addCustomProperty(POST_IMAGE, generatePostImageURL(post.getImage()));
        }

        super.addCustomProperty(POST_STATUS, post.getPostStatus().name());
        return this;
    }

    public CustomApnsPayloadBuilder setAuthor(final String memberProfileImage) {
        super.addCustomProperty(AUTHOR_PROFILE, generateProfileImageURL(memberProfileImage));
        return this;
    }

    public CustomApnsPayloadBuilder setConsumerTypeExist(final boolean consumerTypeExist) {
        super.addCustomProperty(CONSUMER_TYPE_EXIST_KEY, consumerTypeExist);
        return this;
    }

    public CustomApnsPayloadBuilder setIsComment(final boolean isComment) {
        super.addCustomProperty(IS_COMMENT, isComment);
        return this;
    }


    private String generateProfileImageURL(String imageName) {
        String resultURL = null;
        if (imageName != null)
            resultURL = profileBaseUri + imageName;
        return resultURL;
    }

    private String generatePostImageURL(String imageName) {
        String resultURL = null;
        if (imageName != null)
            resultURL = postBaseUri + imageName;
        return resultURL;
    }

    private String generateReviewImageURL(String imageName) {
        String resultURL = null;
        if (imageName != null)
            resultURL = reviewBaseUri + imageName;
        return resultURL;
    }

}
