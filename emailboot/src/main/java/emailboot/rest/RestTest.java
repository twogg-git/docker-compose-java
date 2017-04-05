package emailboot.rest;

import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTest {

    @RequestMapping("/test")
    public String test() {
        return "EmailBoot Rest Service - Test Succeeded! "+new Date().toString();
    }

}
