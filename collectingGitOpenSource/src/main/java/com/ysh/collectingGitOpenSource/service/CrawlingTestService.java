package com.ysh.collectingGitOpenSource.service;

import com.ysh.collectingGitOpenSource.controller.component.JsoupComponent;
import com.ysh.collectingGitOpenSource.dto.KospiStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingTestService {

    private final JsoupComponent jsoupComponet;

    public List<KospiStockDto> getKosPiStockList(){
        return jsoupComponet.getKosPiStockList();
    }

}
