package www.mys.com.consumer.base;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import www.mys.com.common.utils.LogUtils;

@Component
public class ReceiveTest {

    @StreamListener(MySink.INPUT)
    public void receiveInput(String receiveMsg) {
        LogUtils.log("input receive: " + receiveMsg);
    }

}
