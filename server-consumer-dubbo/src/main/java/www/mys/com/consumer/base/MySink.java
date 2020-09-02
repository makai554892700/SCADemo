package www.mys.com.consumer.base;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MySink {

    String INPUT = "input";

    @Input(INPUT)
    SubscribableChannel input();

}