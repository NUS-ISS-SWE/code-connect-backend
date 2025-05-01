package com.nus.iss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeConnectApiGatewayApplicationTest {
    @Test
    public void applicationContextLoad() {
    }

    @Test
    public void testMain() {
        CodeConnectApiGatewayApplication.main(new String[]{});
    }

}
