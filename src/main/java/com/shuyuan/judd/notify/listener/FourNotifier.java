package com.shuyuan.judd.notify.listener;

import com.alibaba.fastjson.JSONObject;
import com.shuyuan.judd.client.constants.BizProtocalFieldConstants;
import com.shuyuan.judd.notify.feignservice.PlatCertificationFeignService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RocketMQMessageListener(topic = "${mq-topic.result-notify-r3}", consumerGroup = "FourNotifier",consumeMode = ConsumeMode.CONCURRENTLY)
public class FourNotifier implements RocketMQListener<String> {
    
    @Autowired
    private PlatCertificationFeignService platCertificationFeignService;
    private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    @Override
    public void onMessage(String s) {
        log.info("message: {}", s);
        JSONObject msg = JSONObject.parseObject(s);
        JSONObject notification = JSONObject.parseObject(msg.getBytes("body"), JSONObject.class);
        log.debug("notification: {}", notification.toJSONString());
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject data = notification.getJSONObject("data");
        String url = data.getString(BizProtocalFieldConstants.NOTIFY_URL);
        data.remove(BizProtocalFieldConstants.NOTIFY_URL);
        log.debug("new notification: {}", notification);
        String notificationStr = notification.toJSONString();
        spring.shuyuan.judd.base.model.Response<String> res = platCertificationFeignService.sign(notificationStr);
        String signature;
        if(res.getCode().equals(spring.shuyuan.judd.base.model.Response.Code.SUCCESS.getCode())){
            signature = res.getData();
        }else{
            log.error("通知加钱失败{}",notificationStr);
            return;
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Signature", signature)
                .post(RequestBody.create(notification.toJSONString(), mediaType))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error(call.toString(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200) {
                    final ResponseBody body = response.body();
                    String resStr = body.string();
                    body.close();
                    log.info("商户通知成功，响应: {}", resStr);
                } else {
                    final ResponseBody body = response.body();
                    String resStr = body.string();
                    body.close();
                    log.warn("商户通知失败，放弃通知！商户响应码：{}，响应: {}", response.code(), resStr);
                }
            }
        });
    }
}
