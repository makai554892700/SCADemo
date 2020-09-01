package www.mys.com.provider.base;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MySink {

    String OUTPUT = "output";

    @Output(OUTPUT)
    MessageChannel output();

}