package com.ysh.collectingGitOpenSource.service;

import com.ysh.collectingGitOpenSource.component.JsoupComponent;
import com.ysh.collectingGitOpenSource.dto.GitOssDataDto;
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


    //깃 허브에서 oss 정보 크롤링 테스트
    public List<GitOssDataDto> getOssByGitHub(){
        return jsoupComponent.getOssByGitHub();
    }

    //깃 허브에서 Repository리스트를 보여주는 페이지를 크롤링 하는 부분
    public List<GitOssDataDto> getGitRepositoryPage(){
        return jsoupComponent.getGitRepositoryPage();
    }





}
