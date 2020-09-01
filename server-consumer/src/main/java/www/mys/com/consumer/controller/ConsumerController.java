package www.mys.com.consumer.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import www.mys.com.consumer.api.ConsumerApi;
import www.mys.com.consumer.service.ProviderService;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;
import www.mys.com.utils.ResultUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class ConsumerController implements ConsumerApi {

    @Resource
    private ProviderService providerService;

    @Override
    public Result<String> saveData(@Valid RequestData<String> requestData, BindingResult bindingResult) throws Exception {
        RequestData<String> dataStr = new RequestData();
        dataStr.setData(requestData.getData());
        return ResultUtils.succeed(ResultUtils.getResult(providerService.saveData(dataStr)));
    }

    @Override
    public Result<String> getData(String key) {
        return ResultUtils.succeed(ResultUtils.getResult(providerService.getData(key)));
    }
}
