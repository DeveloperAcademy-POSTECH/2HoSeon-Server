package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.PostCommentInfoDTO;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
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
public interface PostCommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findPostCommentsByPost(Post post);

    List<PostCommentInfoDTO> findByPostAndId(Post post, Long id);

}
