package com.twohoseon.app.enums;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ConsumerType
 * @date : 11/2/23 3:12 PM
 * @modifyed : $
 **/

public enum ConsumerType {
    TRENDSETTER,       // 유행선도자: 유행에 제일 민감한 사람들
    FLEXER,            // Flexer킹/플렉스왕: 가성비보다 과시욕이 먼저인 사람들
    BUDGET_KEEPER,     // 지갑지킴이: 가격에 제일 민감한 사람들
    ECO_WARRIOR,       // 지구지킴이: 환경을 중요시하는 사람들
    BEAUTY_LOVER,      // 예쁜게최고야짜릿해: 무조건 예쁜 거!!!!!!
    IMPULSE_BUYER,     // 지름신강림러: 충동소비하는 사람들
    ADVENTURER,        // 프로도전러: 다양한 것을 추구하는 사람들
    RISK_AVERSE        // 실패줄임러: 신뢰도가 있는 제품만 사는 안전러
    ;

    /**
     * @author : hyunwoopark
     * @version : 1.0.0
     * @package : twohoseon
     * @name : VoteResult
     * @date : 11/7/23 1:37 AM
     * @modifyed : $
     **/
    public enum VoteResult {
        BUY, NOT_BUY, DRAW
    }
}
