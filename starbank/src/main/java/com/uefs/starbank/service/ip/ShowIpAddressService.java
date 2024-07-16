package com.uefs.starbank.service.ip;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Slf4j
@Service
public class ShowIpAddressService implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Running in address: {}", InetAddress.getLocalHost().getHostAddress());
    }
}
