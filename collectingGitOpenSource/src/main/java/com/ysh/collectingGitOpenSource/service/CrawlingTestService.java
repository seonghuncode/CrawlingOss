package com.ysh.collectingGitOpenSource.service;

import com.ysh.collectingGitOpenSource.controller.component.JsoupComponent;
import com.ysh.collectingGitOpenSource.dto.KospiStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingTestService {

    private final JsoupComponent jsoupComponent;

    //네이버 주식 가격 크롤링 테스트
    public List<KospiStockDto> getKosPiStockList(){
        return jsoupComponent.getKosPiStockList();
    }

}
