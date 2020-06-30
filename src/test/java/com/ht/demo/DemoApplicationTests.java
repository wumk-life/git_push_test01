package com.ht.demo;

import com.alibaba.fastjson.JSONObject;
import com.ht.utils.AESUtil;
import com.ht.utils.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
    private String orgId = "f8f8beac12a84e2ea68d528f4d54ea8f";
    private String secret = "1c6e99b74f4b4447944660b364494bcd";
    private String AESkey = "Htzc2020@!~CS1!@";
    private String AESiv = "Htzc1@2!3~4Cso.1";
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyQWdlbnQiOiJQTVNPRlQvNS4wIChXaW5kb3dzOyBMaW51eDsgSU9TO0FuZHJvaWQpICIsInVzZXJJZCI6ImVzazJNR2lDM0dNQjUwZHRLMGt5ZjJCU2YvZFA1eGQ2eGMyRThVU3RSZzV5ajQ1d0lCK21CZz09IiwianRpIjoiOWUyZDcyOWIzNzFhNGRmN2FjNDhlODBmM2JjYzMxMTQiLCJzdWIiOiJtaWNyb3NlcnZpY2UiLCJpc3MiOiJwbWluZm8iLCJpYXQiOjE1OTM0ODg0NzksImV4cCI6MTU5MzQ5NTY3OSwibmJmIjoxNTkzNDg0ODc5fQ.dvRDM4HnEZPdQfVOIu8txkAiY0Dd37zGRhAFF9ZOIVY";
    private String gatewayUrl = "http://localhost:9070/api/gateway";

    @Test
    void contextLoads() {}

    /**
     * 3.1获取AccessToken
     * */
    @Test
    public void testAccessToken(){
        try {
            String localUrl = "http://localhost:9070/api/accessToken";
            String vueUrl = "http://localhost:9061/api/api/accessToken";
            String contentType = "application/x-www-form-urlencoded";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("grant_type", "client_credential");
            paramMap.put("orgId", orgId);
            paramMap.put("secret", secret);
            String result = Post.post(vueUrl, paramMap, contentType);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用数据集接口（API接口网关）
     * */
    @Test
    public void testGateway(){
        testApplyOrder();
    }

    /**
     * 5投保申请
     * */
    @Test
    public void testApplyOrder(){
        try {
            Map<String, String> paramJsonMap = new HashMap<String, String> ();
            paramJsonMap.put("payType", "1");
            paramJsonMap.put("projectId", "b57ed21ffb46409b82aa0b7514462c91");
            paramJsonMap.put("projectNo", "W-20200630");
            paramJsonMap.put("projectName", "项目名-wu_mk-Test001");
            paramJsonMap.put("sectionId", "c678c026e373470bb71c681852b2c118");
            paramJsonMap.put("sectionNo", "W-20200630-001");
            paramJsonMap.put("sectionName", "标段-wu_mk-Test001");
            paramJsonMap.put("payEndTime", "20200711230000");
            paramJsonMap.put("kbTime", "20200620230000");
            paramJsonMap.put("bzjAmount", "50000.00");
            paramJsonMap.put("zbrName", "杭州品茗信息-wu_mk-Test001");
            paramJsonMap.put("zbrLegalCode", "92610135MA6UB2ET7A");//投标人统一社会信用代码
            paramJsonMap.put("entId", "2473975263103356");

            paramJsonMap.put("entName", "有限公司-wu_mk-Test001");
            paramJsonMap.put("legalCode", "91330201MA2AGACC9N");

            String content = JSONObject.toJSONString(paramJsonMap);
            String mapContent = AESUtil.encrypt(content, AESkey, AESiv);

            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("token", token);
            paramMap.put("orgId", orgId);
            paramMap.put("datasetCode", "applyOrder");
            paramMap.put("content", mapContent);

            String result = Post.postJson(gatewayUrl, JSONObject.toJSONString(paramMap));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 6投保申请结果查询
     * */
    @Test
    public void testQueryOrder(){
        Map<String, String> paramJsonMap = new HashMap<String, String>();
        paramJsonMap.put("payType", "1");
        paramJsonMap.put("projectId", "b57ed21f-fb46-409b-82aa-0b7514462c91");
        paramJsonMap.put("sectionId", "c678c026-e373-470b-b71c-681852b2c118");
        paramJsonMap.put("entId", "2473975263103356");
        String content = JSONObject.toJSONString(paramJsonMap);
        String mapContent = AESUtil.encrypt(content, AESkey, AESiv);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("orgId", orgId);
        paramMap.put("datasetCode", "queryOrder");
        paramMap.put("content", mapContent);
        try {
            String result = Post.postJson(gatewayUrl, JSONObject.toJSONString(paramMap));
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
