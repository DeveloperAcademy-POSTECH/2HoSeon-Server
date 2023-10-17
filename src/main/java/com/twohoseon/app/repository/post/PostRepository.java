package com.twohoseon.app.repository.post;

import com.twohoseon.app.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostRepository
 * @date : 10/17/23 8:17 PM
 * @modifyed : $
 **/
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
