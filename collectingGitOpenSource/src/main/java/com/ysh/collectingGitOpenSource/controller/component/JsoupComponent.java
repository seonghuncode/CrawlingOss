package com.ysh.collectingGitOpenSource.controller.component;

import com.ysh.collectingGitOpenSource.dto.KospiStockDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//클라이언트의 요청에 대한 비즈니스 로직을 수행하는 컴포넌트
//구성요소로 독립적인 단위 모듈, 작성한 class를 IoC컨테이너에 등록 가능
@Component
public class JsoupComponent {

    public List<KospiStockDto> getKosPiStockList() {
        final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1"; //타켓 url을 기준으로 크롤링 하기 위한 커넥션을 생성
        Connection conn = Jsoup.connect(stockList);
        try {
            Document document = conn.get();
            return getKosPiStockList(document);
        } catch (IOException ignored) {
        }
        return null;
    }

    //전체 결과값을 1차적으로 전달 받는다
    //전달 받은 객체를  반복문을 통해 KospiStockDto객체로 변환
    public List<KospiStockDto> getKosPiStockList(Document document) {
        Elements kosPiTable = document.select("table.type_2 tbody tr");
        List<KospiStockDto> list = new ArrayList<>();
        for (Element element : kosPiTable) {
            if (element.attr("onmouseover").isEmpty()) {
                continue;
            }
            list.add(createKosPiStockDto(element.select("td")));
        }
        return list;
    }

    //전달 받은 객체를 dto객체로 변환
    public KospiStockDto createKosPiStockDto(Elements td) {
        KospiStockDto kospiStockDto = KospiStockDto.builder().build(); //기본 객체 하나 생성
        Class<?> clazz = kospiStockDto.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < td.size(); i++) {
            String text;
            if(td.get(i).select(".center a").attr("href").isEmpty()){
                text = td.get(i).text();
            }else{
                text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
            }
            fields[i].setAccessible(true);
            try{
                fields[i].set(kospiStockDto,text);
            }catch (Exception ignored){
            }
        }
        return kospiStockDto;
    }

}
