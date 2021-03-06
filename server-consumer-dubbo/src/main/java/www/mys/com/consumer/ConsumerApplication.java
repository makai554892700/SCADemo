package www.mys.com.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.PropertySource;
import www.mys.com.common.base.MyPropertySourceFactory;
import www.mys.com.consumer.base.MySink;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({MySink.class})
@EnableFeignClients
@PropertySource(value = "file:${mys.config.file}", ignoreResourceNotFound = true, factory = MyPropertySourceFactory.class)
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
