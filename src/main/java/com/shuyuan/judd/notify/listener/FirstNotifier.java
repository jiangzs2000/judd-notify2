package com.shuyuan.judd.notify.listener;

import com.alibaba.fastjson.JSONObject;
import com.shuyuan.judd.client.constants.BizProtocalFieldConstants;
import com.shuyuan.judd.notify.feignservice.PlatCertificationFeignService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.shuyuan.judd.base.model.Response;

import java.io.IOException;

@Slf4j
@Service
@RocketMQMessageListener(topic = "${mq-topic.result-noitfy-r0}", consumerGroup = "FirstNotifier",consumeMode = ConsumeMode.CONCURRENTLY)
public class FirstNotifier implements RocketMQListener<String> {
    @Value("${mq-topic.result-notify-r1}")
    private String resultNotifyR1;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
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
        if(StringUtils.isEmpty(url)){
            log.error("非法报文,报文将被丢弃，通知报文中没有notify_url：{}", data.toString());
            return;
        }
        if(StringUtils.isEmpty(url)){
            log.error("非法报文,报文将被丢弃，通知报文中没有notify_url：{}", data.toString());
            return;
        }
        data.remove(BizProtocalFieldConstants.NOTIFY_URL);
        log.debug("new notification: {}", notification);
        String notificationStr = notification.toJSONString();
        spring.shuyuan.judd.base.model.Response<String> res = platCertificationFeignService.sign(notificationStr);
        String signature;
        if(res.getCode().equals(spring.shuyuan.judd.base.model.Response.Code.SUCCESS.getCode())){
            signature = res.getData();
        }else{
            log.error("通知加签失败{}",notificationStr);
            return;
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Signature", signature)
                .post(RequestBody.create(notificationStr, mediaType))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("通知商户失败，放下级队列", e);
                send2NextTopic(notification, url);
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.code() == 200) {
                    final ResponseBody body = response.body();
                    String resStr = body.string();
                    body.close();
                    log.info("商户通知成功，响应: {}", resStr);
                } else {
                    final ResponseBody body = response.body();
                    String resStr = body.string();
                    body.close();
                    log.warn("商户通知失败，通知放下一级，商户响应码：{}，响应: {}", response.code(), resStr);
                    send2NextTopic(notification, url);
                }
                /*
                Response res = JSONObject.parseObject(resStr, Response.class);
                if(!res.getCode().equals(Response.Code.SUCCESS.getCode())){
                    log.error(call.toString(), e);
                    Message msg = new Message(resultNotifyR1, null, null, JSONObject.toJSONBytes(notification));
                    msg.setDelayTimeLevel(3);
                }*/
            }
        });
    }

    private void send2NextTopic(JSONObject notification, String url){
        JSONObject data = notification.getJSONObject("data");
        data.put(BizProtocalFieldConstants.NOTIFY_URL, url);
        log.info("放到下级topic: {}", notification.toString());
        Message msg = new Message(resultNotifyR1, null, null, notification.toString().getBytes());
        msg.setDelayTimeLevel(3);
        rocketMQTemplate.asyncSend(resultNotifyR1, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
            }
            @Override
            public void onException(Throwable throwable) {
                log.error("发送result-notify-r1失败", throwable);
            }
        });
    }
}
