package www.mys.com.provider.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import www.mys.com.common.service.ProviderService;
import www.mys.com.provider.api.ProviderApi;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;
import www.mys.com.utils.ResultUtils;
import www.mys.com.utils.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class ProviderController implements ProviderApi {

    @Resource(name = "providerService")
    private ProviderService providerService;

    @Override
    public Result<String> saveData(@Valid RequestData<String> requestData, BindingResult bindingResult) throws Exception {
        return ResultUtils.succeed(providerService.saveData(StringUtils.getRandomStr(16), requestData.getData()));
    }

    @Override
    public Result<String> getData(String key) {
        return ResultUtils.succeed(providerService.getData(key));
    }
}
