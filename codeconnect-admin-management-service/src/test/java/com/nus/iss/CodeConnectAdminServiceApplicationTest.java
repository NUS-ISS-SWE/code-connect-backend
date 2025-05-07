package com.nus.iss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeConnectAdminServiceApplicationTest {
    @Test
    public void applicationContextLoad() {
    }

    @Test
    public void testMain() {
        CodeConnectAdminServiceApplication.main(new String[]{});
    }


}