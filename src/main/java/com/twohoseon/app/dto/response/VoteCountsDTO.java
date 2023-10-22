package com.twohoseon.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteCountsDTO
 * @date : 10/22/23 4:49 AM
 * @modifyed : $
 **/
@Getter
@AllArgsConstructor
public class VoteCountsDTO {
    int agreeCount;
    int disagreeCount;
}
