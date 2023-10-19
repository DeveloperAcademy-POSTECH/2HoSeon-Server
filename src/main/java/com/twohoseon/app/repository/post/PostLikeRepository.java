package com.twohoseon.app.repository.post;

import com.twohoseon.app.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : LikeRepository
 * @date : 10/17/23 8:18 PM
 * @modifyed : $
 **/
@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
