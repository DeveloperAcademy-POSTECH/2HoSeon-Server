package com.twohoseon.app.dto.apns;

import com.eatthepath.json.JsonSerializer;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.twohoseon.app.entity.post.Post;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomApnsPayloadBuilder
 * @date : 11/1/23 12:07 AM
 * @modifyed : $
 **/
public class CustomApnsPayloadBuilder extends ApnsPayloadBuilder {

    private static final String DESTINATION_KEY = "destination";
    private static final String POST_ID_KEY = "post_id";
    private static final String POST_STATUS = "post_status";
    private static final String CONSUMER_TYPE_EXIST_KEY = "consumer_type_exist";

    @Override
    public String build() {
        return JsonSerializer.writeJsonTextAsString(this.buildPayloadMap());
    }

    @Override
    public String buildMdmPayload(final String pushMagicValue) {
        return JsonSerializer.writeJsonTextAsString(this.buildMdmPayloadMap(pushMagicValue));
    }

    public ApnsPayloadBuilder setPostDetails(final String postId) {
        super.addCustomProperty(DESTINATION_KEY, "post_detail");
        super.addCustomProperty(POST_ID_KEY, postId);
        return this;
    }

    public ApnsPayloadBuilder setPostDetails(final Post post) {
        super.addCustomProperty(POST_ID_KEY, post.getId());
        super.addCustomProperty(POST_STATUS, post.getPostStatus().name());
        return this;
    }

    public ApnsPayloadBuilder setConsumerTypeExist(final boolean consumerTypeExist) {
        super.addCustomProperty(CONSUMER_TYPE_EXIST_KEY, consumerTypeExist);
        return this;
    }
}
