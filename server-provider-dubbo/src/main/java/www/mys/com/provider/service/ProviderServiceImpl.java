package www.mys.com.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import www.mys.com.common.service.ProviderService;
import www.mys.com.provider.base.ProducerRunner;

import javax.annotation.Resource;
import java.util.HashMap;

@Service(value = "providerService")
@DubboService(version = "1.0.0", interfaceClass = ProviderService.class, interfaceName = "providerService")
public class ProviderServiceImpl implements ProviderService {

    private static final HashMap<String, String> data = new HashMap<>();

    @Resource(name = "producerRunner")
    private ProducerRunner producerRunner;

    @Override
    public String saveData(String key, String value) {
        data.put(key, value);
        producerRunner.sendMessage("start saveData key=" + key + ";value=" + value);
        return key;
    }

    @Override
    public String getData(String key) {
        String result = data.get(key);
        producerRunner.sendMessage("start getData key=" + key + ";result=" + result);
        return result;
    }
}
