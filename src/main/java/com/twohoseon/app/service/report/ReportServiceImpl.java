package com.twohoseon.app.service.report;

import com.twohoseon.app.dto.request.member.ReportRequest;
import com.twohoseon.app.entity.BanList;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.member.Report;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.CommentNotFoundException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.banlist.BanListRepository;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.repository.report.ReportRepository;
import com.twohoseon.app.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportServiceImpl
 * @date : 11/27/23 9:01 PM
 * @modifyed : $
 **/
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService, CommonService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BanListRepository banListRepository;

    @Override
    @Transactional
    public void reportPost(ReportRequest reportRequest) {
        Member reqMember = getMemberFromRequest();
        Member reportedMember;
        Post post = postRepository.findById(reportRequest.getPostId())
                .orElseThrow(PostNotFoundException::new);
        reportedMember = post.getAuthor();
        Report report = Report.builder()
                .reporter(reqMember)
                .reportedMember(reportedMember)
                .post(post)
                .reason(reportRequest.getReason())
                .build();
        reportRepository.save(report);
        int reportCount = reportRepository.getReportCount(reportedMember.getId());
        if (reportCount >= 10) {
            reportedMember.setIsBaned(true);
            BanList banList = BanList.builder()
                    .providerId(reportedMember.getProviderId())
                    .build();
            banListRepository.save(banList);
        }
    }

    @Override
    @Transactional
    public void reportComment(ReportRequest reportRequest) {
        Member reqMember = getMemberFromRequest();
        Member reportedMember;
        Comment comment = commentRepository.findById(reportRequest.getPostId())
                .orElseThrow(CommentNotFoundException::new);
        reportedMember = comment.getAuthor();
        Report report = Report.builder()
                .reporter(reqMember)
                .reportedMember(reportedMember)
                .comment(comment)
                .reason(reportRequest.getReason())
                .build();
        reportRepository.save(report);
        int reportCount = reportRepository.getReportCount(reportedMember.getId());
        if (reportCount >= 10) {
            reportedMember.setIsBaned(true);
            BanList banList = BanList.builder()
                    .providerId(reportedMember.getProviderId())
                    .build();
            banListRepository.save(banList);
        }
    }
}
