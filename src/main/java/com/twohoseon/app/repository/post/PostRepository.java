package com.twohoseon.app.repository.post;

import com.twohoseon.app.entity.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostRepository
 * @date : 10/17/23 8:17 PM
 * @modifyed : $
 **/

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
//    Page<Post> findAllBy(Pageable pageable);

//    List<PostDto> findAllBy();

//    List<PostInfo> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = "author")
    List<Post> findAllBy();
}
