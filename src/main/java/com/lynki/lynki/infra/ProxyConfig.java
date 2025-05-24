package com.lynki.lynki.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

@Configuration
public class ProxyConfig {

    @Bean
    public List<Proxy> proxyList() {
        return List.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("57.129.81.201",8080 )),
                       new Proxy(Proxy.Type.HTTP, new InetSocketAddress("72.10.160.170", 3897)));
    }
}
