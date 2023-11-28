//package com.twohoseon.app.repository.member;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static com.twohoseon.app.entity.member.QMember.member;
//import static com.twohoseon.app.entity.post.QPost.post;
//
///**
// * @author : hyunwoopark
// * @version : 1.0.0
// * @package : twohoseon
// * @name : MemberCustomRepositoryImplTest
// * @date : 11/5/23 5:34 AM
// * @modifyed : $
// **/
//@SpringBootTest
//class MemberCustomRepositoryImplTest {
////    @Autowired
////    private JPAQueryFactory jpaQueryFactory;
////
////    @Test
////    void findMemberDeviceTokenByPostId() {
////        List<String> deviceTokens = jpaQueryFactory
////                .select(member.deviceToken)
////                .from(post)
////                .join(post.subscribers, member)
////                .where(post.id.eq(952L))
////                .fetch();
////        System.out.println(deviceTokens);
////    }
//
//}