package com.ysh.collectingGitOpenSource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //클래스 레벨에서 모든 필드를 대상으로 접근자와 설장자가 자동으로 생성
@NoArgsConstructor //매개변수를 갖지 않는 기본 생성자를 생성 해준다.
@AllArgsConstructor //모든 필드를 인자로 받는 생성자를 만들어 준다.
@Builder //객체를 정의하고 그 객체를 생성할 때 보통 생성하는 것
public class KospiStockDto {

    private String no;
    private String stockName;
    private String price;                // 현재가
    private String diffAmount;           // 전일비
    private String dayRange;             // 등락률
    private String parValue;             // 액면가
    private String marketCap;            // 시가총액
    private String numberOfListedShares; // 상장 주식 수
    private String foreignOwnRate;       // 외국인 비율
    private String turnover;             // 거래량
    private String per;                  // per
    private String roe;                  // roe
    private String discussionRoomUrl;    // 토론방 url

    public String getDiscussionRoomUrl() {
        return "https://finance.naver.com"+discussionRoomUrl;
    }

}
