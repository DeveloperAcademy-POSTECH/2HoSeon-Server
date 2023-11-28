package com.twohoseon.app.service.report;

import com.twohoseon.app.dto.request.member.ReportRequest;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportService
 * @date : 11/27/23 9:01 PM
 * @modifyed : $
 **/
public interface ReportService {
    void reportPost(ReportRequest reportRequest);

    void reportComment(ReportRequest reportRequest);
}
