package com.twohoseon.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    //TODO 서치를 위한 dto, controller, service layer 설계 및 구현
    //TODO 투표 완료시 Vote에 대한 결과를 계산하여 Enum으로 저장한다(스케쥴링)
    //TODO 서치 전용 DTO 설계하고 만들 것
    //TODO 사진 여러장 업로드 폐지
    //TODO Post fetch 수정
    //TODO 좋아요 삭제
    //TODO ReviewRequestDTO Controller, Service에서 변경점 수정
    //TODO PostInfoDTO 내에 VoteInfo(모든 fetch에서 쓸 수 있는 정보들을 가지고 있어야 함) 정의
}
