package com.twohoseon.app.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportCategory
 * @date : 11/27/23 8:35 PM
 * @modifyed : $
 **/


@Schema(description = "신고 카테고리")
public enum ReportCategory {
    SPAM,           // 스팸
    OBSCENE,        // 음란
    PROFANITY,      // 욕설
    COPYRIGHT_INFRINGEMENT, // 권리침해
    SUICIDE,        // 자살
    ADVERTISING     // 광고
}
