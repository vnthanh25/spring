package com.rpicloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bim.server.SbGatewayApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SbGatewayApplication.class)
@WebAppConfiguration
public class SbGatewayApplicationTests {

	@Test
	public void contextLoads() {
	}

}
