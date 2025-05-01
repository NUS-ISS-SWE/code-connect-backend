package com.nus.iss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeConnectUserServiceApplicationTest {
    @Test
    public void applicationContextLoad() {
    }

    @Test
    public void testMain() {
        CodeConnectUserServiceApplication.main(new String[]{});
    }

}
