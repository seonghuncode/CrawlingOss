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


    //네이버 주식가격 크롤링 테스트=====================================================================================
    //타켓 url을 기준으로 크롤링 하기 위한 커넥션을 생성하고 document객체 생성
    public List<KospiStockDto> getKosPiStockList() {
        final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
        //해당 url에 연결하기 위한 connection객체 생성
        Connection conn = Jsoup.connect(stockList);
//        System.out.println("stockList" + stockList);

        //conn.get 메서드를 호출하여 웹 페이지에 연결하고, 해당 페이지의 html문서를 가져와 document객체로 저장장
       try {
            Document document = conn.get();
            return getKosPiStockList(document); //해당 함수를 호출해 결과를 받아 리턴
        } catch (IOException ignored) {
        }
        return null;
    }

    //전체 결과값을 1차적으로 전달 받는다
    //전달 받은 객체를  반복문을 통해 KospiStockDto객체로 변환
    //getKosPiStockList메서드 에서 가져온 Document객체를 인자로 받습니다.
    public List<KospiStockDto> getKosPiStockList(Document document) {
        //Document객체에서 원하는 html요소를 선택하기 위해 css선택자를 사용
        //HTML에서 table요소중 classs속성이 type_@인 것 아래의 tbody요소 아래있는 tr요소를 모두 선택하라는 의미
        Elements kosPiTable = document.select("table.type_2 tbody tr");
        System.out.println("kospitable" + kosPiTable);
        List<KospiStockDto> list = new ArrayList<>();
        //선택된 정보 테이블의 각 행 tr에 대한 반복문을 수행
        for (Element element : kosPiTable) {
            //각 행의 onmouseover속성이 비어있지 않은 경우 (주로 주식정보가 표시되는 행)
            if (element.attr("onmouseover").isEmpty()) {
                continue;
            }
            //createKosPiStockDto메서드를 호출하여 해당 행의 데이터를 KosPiStockDto객체로 변화하고 리스트에 추가
            list.add(createKosPiStockDto(element.select("td")));
        }
        return list;
    }

    //전달 받은 객체를 dto객체로 변환
    //Elements 객체로 표현된 HTML테이블 행 td열을 KosPiStockDto객체로 변환하는 역할
    public KospiStockDto createKosPiStockDto(Elements td) {
        KospiStockDto kospiStockDto = KospiStockDto.builder().build(); //기본 객체 하나 생성 (주식 정보를 담을 때 사용할 객체)
        Class<?> clazz = kospiStockDto.getClass(); //KospiStockDto클래스 정보를 가져온다
        Field[] fields = clazz.getDeclaredFields(); //KospiStockDto 클래스의 모든 필드를 가지고 온다

        //HTML 테이블 열 td반복
        for (int i = 0; i < td.size(); i++) {
            String text;
            //td열에서 text출력 /  td열 내부에서 ".center a" css선택자로 링크가 있는지 확인하고 있을 경우 해당 링크를 텍스트로 설정
            //있을 경우 해당 링크를 텍스트로 설정, 없을 경우 td.get(i).text()로 td열의 텍스트를 가져온다
            if(td.get(i).select(".center a").attr("href").isEmpty()){
                text = td.get(i).text();
            }else{
                text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
            }
            fields[i].setAccessible(true); //필드에 대한 접근 권한을 설정
            try{
                //객체의 각 필드에 출=출한 텍스트를 설정, 객체의 필드 값을 설정하고 text변수의 값을 해당 필드에 설정
                fields[i].set(kospiStockDto,text);
            }catch (Exception ignored){ //필드가 null값이 아니고 텍스트 값이 올바르게 설정되어 있지 않으면 예외 무시
            }
        }
        return kospiStockDto;
    }
    //네이버 주식가격 크롤링 테스트=====================================================================================



    //깃 허브에서 크롤링 테스트=====================================================================================

    //깃 허브에서 크롤링 테스트=====================================================================================




}
