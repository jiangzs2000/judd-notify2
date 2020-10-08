package com.shuyuan.judd.notify;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.shuyuan.judd.client.model.merchant.Merchant;
import spring.shuyuan.judd.base.cache.CacheService;

/**
 * @author Sting
 * create 2017/05/18

 **/
public class CacheServiceTest extends BaseIntegrationTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void testHash() throws Exception {
        Merchant merchant=new Merchant();
        merchant.setIdName("hudanliqi");
//        cacheService.putStringToHash("hash","a", JSONObject.toJSONString(merchant));
//        String stringFromHash = cacheService.getStringFromHash("hash", "a");
//        System.out.println(stringFromHash);
    }
}
