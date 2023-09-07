package com.ysh.collectingGitOpenSource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //클래스 레벨에서 모든 필드를 대상으로 접근자와 설장자가 자동으로 생성
@NoArgsConstructor //매개변수를 갖지 않는 기본 생성자를 생성 해준다.
@AllArgsConstructor //모든 필드를 인자로 받는 생성자를 만들어 준다.
@Builder //객체를 정의하고 그 객체를 생성할 때 보통 생성하는 것
public class GitOssDataDto {

    private String filename;
    private String pathname;
    private String checksum;
    private String tlshchecksum;
    private String ossname;
    private String ossversion;
    private String license;
    private String parentname;
    private String platformname;
    private String platformversion;
    private String updatedate;
    private String sourcepath;

    private int stars;
    private String name;
    private String ossUrl;


}

