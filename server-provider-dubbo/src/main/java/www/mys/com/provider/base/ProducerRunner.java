package www.mys.com.provider.base;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "producerRunner")
public class ProducerRunner {

    @Resource
    private MessageChannel output; // 获取name为output的binding

    public void sendMessage(String message) {
        output.send(MessageBuilder.withPayload(message).build());
    }

}