package com.ysh.collectingGitOpenSource.controller;


import com.ysh.collectingGitOpenSource.dto.GitOssDataDto;
import com.ysh.collectingGitOpenSource.dto.KospiStockDto;
import com.ysh.collectingGitOpenSource.service.CrawlingTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//하위 클래스에 @ResponseBody어노테이션을 붙이지 않아도 문자열과 JSON등을 전송 가능
//controller : mvc패턴을 사용할 경우 view(화면)과 model(비즈니스 로직)을 연결하는 것(요청을 받으면 어디로 갈지 보내주는 열할)
@RestController
@RequiredArgsConstructor //lombok으로 생성자 주입을 자동으로 설정(초기화 되지 않은 final필드, @NonNull이 붙은 필드에 대해 생성자 생성)
public class CrawlingTest {

    private final CrawlingTestService crawlingTestService;


    //네이버 주식가격 크롤링 테스트
    @GetMapping("/kospi/all")
    public List<KospiStockDto> getKosPiStockList(HttpServletRequest request){
        return crawlingTestService.getKosPiStockList();
    }

    //깃 허브에서 Oss정보 크롤링 테스트
    @GetMapping("/getOssByGitHub")
    public List<GitOssDataDto> getOssByGitHub(HttpServletRequest request){
        return crawlingTestService.getOssByGitHub();
    }

    //깃 허브에서 Repository리스트를 보여주는 페이지를 크롤링 하는 부분
    @GetMapping("/getGitRepositoryPage")
    public List<GitOssDataDto> getGitRepositoryPage(HttpServletRequest request){
        return crawlingTestService.getGitRepositoryPage();
    }





}
