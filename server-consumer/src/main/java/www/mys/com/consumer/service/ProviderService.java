package www.mys.com.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;

@FeignClient(name = "server-provider")
public interface ProviderService {

    @PostMapping(value = {"/api/provider/saveData"})
    public Result<String> saveData(@RequestBody RequestData<String> data);

    @GetMapping(value = {"/api/provider/getData/{key}"})
    public Result<String> getData(@PathVariable("key") String key);

}
