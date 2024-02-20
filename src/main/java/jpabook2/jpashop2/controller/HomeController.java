package jpabook2.jpashop2.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // lombok의  어노테이션으로 Logger logger = LoggerFactory.getLogger(getClass());와 똑같은 효과
public class HomeController {
//    Logger logger = LoggerFactory.getLogger(getClass()); @Slf4j에 의해 대체됨
    @RequestMapping("/")
    public String Home(){
        log.info("home controller");
        return "home";
    }


}
