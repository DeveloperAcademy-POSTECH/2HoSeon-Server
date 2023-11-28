package com.twohoseon.app.repository.banlist;

import com.twohoseon.app.entity.BanList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : BanListRepository
 * @date : 11/28/23 1:12 AM
 * @modifyed : $
 **/
@Repository
public interface BanListRepository extends JpaRepository<BanList, Long> {
    boolean existsByProviderId(String providerId);
}
