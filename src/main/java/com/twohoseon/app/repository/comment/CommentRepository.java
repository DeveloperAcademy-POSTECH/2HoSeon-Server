package com.twohoseon.app.repository.comment;

import com.twohoseon.app.dto.response.CommentInfoDTO;
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
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    Optional<List<Comment>> findPostCommentsByPost(Post post);

    List<CommentInfoDTO> findByPostAndId(Post post, Long id);

    List<Comment> findByPost_Id(Long id);


}
