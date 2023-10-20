package com.twohoseon.app.repository.post;

import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentRepository
 * @date : 10/17/23 8:18 PM
 * @modifyed : $
 **/
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<List<PostComment>> findPostCommentsByPost(Post post);
}
