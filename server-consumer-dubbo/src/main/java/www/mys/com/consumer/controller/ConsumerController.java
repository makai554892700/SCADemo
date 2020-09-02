package www.mys.com.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import www.mys.com.common.service.ProviderService;
import www.mys.com.consumer.api.ConsumerApi;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;
import www.mys.com.utils.ResultUtils;
import www.mys.com.utils.StringUtils;

import javax.validation.Valid;

@RestController
public class ConsumerController implements ConsumerApi {

    @DubboReference(version = "1.0.0", loadbalance = "roundrobin", retries = 2)
    ProviderService providerService;

    @Override
    public Result<String> saveData(@Valid RequestData<String> requestData, BindingResult bindingResult) throws Exception {
        return ResultUtils.succeed(providerService.saveData(
                StringUtils.getRandomStr(16), requestData.getData()
        ));
    }

    @Override
    public Result<String> getData(String key) {
        return ResultUtils.succeed(providerService.getData(key));
    }
}
