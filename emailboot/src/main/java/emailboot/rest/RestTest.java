package emailboot.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTest {

    @RequestMapping("/test")
    public String test() {
        return "MailBoot Rest Service - Test Succeeded!";
    }

}
