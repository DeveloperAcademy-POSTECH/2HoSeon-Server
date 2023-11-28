package com.twohoseon.app.repository.report;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.twohoseon.app.entity.member.QReport.report;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportCustomRepositoryImpl
 * @date : 11/27/23 9:09 PM
 * @modifyed : $
 **/

@Repository
@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int getReportCount(Long memberId) {
        LocalDateTime now = LocalDateTime.now().minusMonths(3);
        Long reportCount = jpaQueryFactory.select(report.countDistinct())
                .from(report)
                .where(report.reportedMember.id.eq(memberId)
                        .and(report.createDate.after(now)))
                .fetchOne();
        return reportCount == null ? 0 : Math.toIntExact(reportCount);
    }
}
