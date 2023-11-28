package com.twohoseon.app.repository.report;

import com.twohoseon.app.entity.member.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportRepository
 * @date : 11/27/23 9:08 PM
 * @modifyed : $
 **/

public interface ReportRepository extends JpaRepository<Report, Long>, ReportCustomRepository {
}
