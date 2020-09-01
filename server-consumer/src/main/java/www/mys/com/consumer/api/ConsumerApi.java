package www.mys.com.consumer.api;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;

import javax.validation.Valid;

@RequestMapping("/api/provider")
public interface ConsumerApi {

    @PostMapping(value = "/saveData")
    public Result<String> saveData(@RequestBody @Valid RequestData<String> requestData
            , BindingResult bindingResult) throws Exception;

    @GetMapping(value = "/getData/{key}")
    public Result<String> getData(@PathVariable("key") String key);

}
