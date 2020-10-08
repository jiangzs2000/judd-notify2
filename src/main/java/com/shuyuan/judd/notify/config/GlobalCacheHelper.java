package com.shuyuan.judd.notify.config;

import com.shuyuan.judd.notify.config.constants.ServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.shuyuan.judd.base.cache.CacheService;
import spring.shuyuan.judd.base.model.TraceIdHelper;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Kevin on 2017/5/13.
 */
@Component
public class GlobalCacheHelper {

    private static final Logger logger = LoggerFactory.getLogger(GlobalCacheHelper.class);

    @Autowired private CacheService cacheService;
    private static ConcurrentSkipListSet<String> needToDeleteKeySet = new ConcurrentSkipListSet();

    @Scheduled(fixedDelay = 1000)
    private void cacheDelete(){
        if (!needToDeleteKeySet.isEmpty()){
            String key = needToDeleteKeySet.first();
            try {
                cacheService.deleteObjectByKey(key);
                logger.info("traceId:{}, cacheDelete by GlobalCacheDelete, delete key {}", TraceIdHelper.getTraceId(), key);
                needToDeleteKeySet.remove(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void putDeleteKeys( String key ) {
        if ( GlobalCacheHelper.needToDeleteKeySet.size() >= ServiceConstants.MAX_LIST_SIZE) {
            logger.warn("traceId:{} , needToDeleteKey is over MAX_LIST_SIZE", TraceIdHelper.getTraceId());
        }
        else {
            GlobalCacheHelper.needToDeleteKeySet.add(key);
        }

//        GlobalCacheDelete.needToDeleteKeySet.add(key);
    }

}
