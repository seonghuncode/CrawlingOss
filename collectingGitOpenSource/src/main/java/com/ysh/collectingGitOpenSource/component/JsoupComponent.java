package com.ysh.collectingGitOpenSource.component;

import com.ysh.collectingGitOpenSource.dto.GitOssDataDto;
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
            if (td.get(i).select(".center a").attr("href").isEmpty()) {
                text = td.get(i).text();
            } else {
                text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
            }
            fields[i].setAccessible(true); //필드에 대한 접근 권한을 설정
            try {
                //객체의 각 필드에 출=출한 텍스트를 설정, 객체의 필드 값을 설정하고 text변수의 값을 해당 필드에 설정
                fields[i].set(kospiStockDto, text);
            } catch (Exception ignored) { //필드가 null값이 아니고 텍스트 값이 올바르게 설정되어 있지 않으면 예외 무시
            }
        }
        return kospiStockDto;
    }
    //네이버 주식가격 크롤링 테스트=====================================================================================





    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================





    //깃 허브에서 크롤링 테스트(url을 수집)=============================================================================
    public List<GitOssDataDto> getOssByGitHub() {
        //깃 허브의 Repository만 모아놓은 페이지
        final String stockList = "https://github.com/search?o=desc&q=stars%3A%3E10&s=stars&type=Repositories";
        //해당 url에 연결하기 위한 connection객체 생성
        Connection conn = Jsoup.connect(stockList);

        //conn.get 메서드를 호출하여 웹 페이지에 연결하고, 해당 페이지의 html문서를 가져와 document객체로 저장장
        try {
            Document document = conn.get();
            //특정 페이지의 전체 HTML을 받아온다.
//            System.out.println(document);
            return getOssByGitHub(document); //해당 함수를 호출해 결과를 받아 리턴 (해당 페이지 에서 특정 태그만 조건을 주어 해당 정보만 수집하도록 거르는 과정)
        } catch (IOException ignored) {
        }
        return null;
    }


    public List<GitOssDataDto> getOssByGitHub(Document document) {
        //Document객체에서 원하는 html요소를 선택하기 위해 css선택자를 사용
        //HTML에서 table요소중 class 속성이  "div.Box-sc-g0xbh4-0.bBwPjs.search-title a"인 것만 추출해 낸다 -> 해당 oss 주소를 나타낸다.
        Elements ossUrl = document.select("div.Box-sc-g0xbh4-0.bBwPjs.search-title a"); //Box-sc-g0xbh4-0 hDWxXB
        System.out.println("ossUrl" + ossUrl);
        List<GitOssDataDto> list = new ArrayList<>();
        //선택된 정보 테이블의 각 행 tr에 대한 반복문을 수행
        for (Element element : ossUrl) {
//            //각 행의 onmouseover속성이 비어있지 않은 경우 (주로 주식정보가 표시되는 행)
//            if (element.attr("onmouseover").isEmpty()) {
//                continue;
//            }
            //createKosPiStockDto메서드를 호출하여 해당 행의 데이터를 KosPiStockDto객체로 변화하고 리스트에 추가
            list.add(createGitOssDataDto(element.select("a")));
        }
        return list;
    }

    //태그 안에서 원하는 데이터 값을 추출해서 GitOssDataDto객체에 값을 넣어 [{}, {}, {}로 저장하는 형식으로 값을 저장해서 반환해 준다.]
    public GitOssDataDto createGitOssDataDto(Elements a) {
        GitOssDataDto gitOssDataDto = GitOssDataDto.builder().build(); //기본 객체 하나 생성 (OSS정보를 담을 때 사용할 객체)
        Class<?> clazz = gitOssDataDto.getClass(); //GitOssDataDto 정보를 가져온다
        Field[] fields = clazz.getDeclaredFields(); //GitOssDataDto 클래스의 모든 필드를 가지고 온다

        System.out.println("a : " + a);
        //HTML 테이블 열 a반복 (현재 ossUrl만 추출하기 위해 위에서 a태그만 추출해서 모아 데이터가 주소 정보만 있기 때문에 단 한번 밖에 반복이 되지 않는다.)
        for (int i = 0; i < a.size(); i++) {
            String text;
            //추출한 a태그에서 만약 href값이 없다면 text변수에 그냥 빈값을 저장한다.
            if (a.get(i).select("a").attr("href").isEmpty()) {
                text = a.get(i).text();
                System.out.println("첫 번째 if문 : " + text);
            } else { //만약 추출한 정보중 a태그안에 href값이 있다면 깃 허브 주로로 만들어서 저장해준다.
                text = "https://github.com" + a.get(i).select("a").attr("href");
                System.out.println("두번째 번째 else문 : " + text);
            }
            fields[i].setAccessible(true); //필드에 대한 접근 권한을 설정
            try {
                System.out.println("i = " + i);
                //객체의 각 필드에 추출한 텍스트를 설정, 객체의 필드 값을 설정하고 text변수의 값을 해당 필드에 설정
                //현재는 주소만 알기 위해 a태그만 추출하여 gitOssDataDto에 ossUrl변수에만 값을 저장해주지만 만약 다른 값들도 값을 한 객체에 추가하고 싶다면 해당 정보들로 정보를 추출해서 반복문을 통해 추가해 주면된다
//                fields[i].set(gitOssDataDto, text);
                gitOssDataDto.setOssUrl(text); //현재는 ossUrl변수에 값만 넣어주기 때문에 index를 순회하지 않고 해당 변수에만 직접 데이터를 넣어준다.
            } catch (Exception ignored) { //필드가 null값이 아니고 텍스트 값이 올바르게 설정되어 있지 않으면 예외 무시
            }
        }
        System.out.println("gitOssDataDto : " + gitOssDataDto);
        return gitOssDataDto;
    }

    //깃 허브에서 크롤링 테스트(url을 수집)=============================================================================






    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================







//    //원하는 결과가 나오지 않고 참고 하는 용도의 로직======================================================================================
//    //깃 허브에서 Repsitory의 전체 페이지의 html을 크롤링 해오는 로직(얻을 수 있는것 Repository명, start갯수, Repository링크, update날짜)
//    public List<GitOssDataDto> getGitRepositoryPage() {
//
//        List<GitOssDataDto> projects = new ArrayList<>();
//        int projectsCount = 0; // 찾은 프로젝트 수를 세는 변수
//
//        try {
//            // GitHub stars가 10 이상인 페이지를 크롤링합니다.
//            String url = "https://github.com/search?o=desc&q=stars%3A%3E10&s=stars&type=Repositories";
//            Document doc = Jsoup.connect(url).get();
//            System.out.println(doc);
//            // 검색 결과에서 각 프로젝트를 나타내는 요소를 선택합니다.
//            Elements projectElements = doc.select("li.repo-list-item");
//            System.out.println(projectElements);
//            for (Element projectElement : projectElements) {
//                System.out.println(projectElement);
//                // 프로젝트의 URL을 추출합니다.
//                Element linkElement = projectElement.selectFirst("h3 a");
//                String projectUrl = "https://github.com" + linkElement.attr("href");
//
//                // 프로젝트의 이름을 추출합니다.
//                String projectName = linkElement.text();
//
//                // 프로젝트의 stars 수를 추출합니다.
//                Element starsElement = projectElement.selectFirst("a.muted-link.mr-3");
//                String starsCount = starsElement.text().trim();
//
//                // stars 수가 10 이상인 프로젝트만 리스트에 추가합니다.
//                int stars = Integer.parseInt(starsCount);
//                if (stars >= 10) {
//                    GitOssDataDto project = GitOssDataDto.builder()
//                            .name(projectName)
//                            .stars(stars)
//                            .ossUrl(projectUrl)
//                            .build();
//                    projects.add(project);
//                    projectsCount++;
//
//                    // 3개의 프로젝트를 찾으면 반복 종료
//                    if (projectsCount == 3) {
//                        break;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return projects;
//    }
//
////원하는 결과가 나오지 않고 참고 하는 용도의 로직======================================================================================



}
