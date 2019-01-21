package com.ssaw.commons.util.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuSen.
 * @date 2019/1/13 3:33.
 */
public class AliYunSmsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliYunSmsUtil.class);

    /** 短信客户端容器 */
    private static final Map<String, IAcsClient> I_ACS_CLIENT_MAP = new ConcurrentHashMap<>(1);

    /** 成功编码 */
    private static final String SUCCESS_CODE = "OK";

    /**
     * 根据accessKeyId获取Client
     * @param accessKeyId accessKeyId
     * @param accessKeySecret accessKeySecret
     * @return Client
     */
    private static IAcsClient getIAcsClient(String accessKeyId, String accessKeySecret) {
        IAcsClient iAcsClient = I_ACS_CLIENT_MAP.get(accessKeyId);
        if (null == iAcsClient) {
            synchronized (AliYunSmsUtil.class) {
                System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
                System.setProperty("sun.net.client.defaultReadTimeout", "10000");
                // 短信API产品名称（短信产品名固定，无需修改）
                final String product = "Dysmsapi";
                // 短信API产品域名（接口地址固定，无需修改）
                final String domain = "dysmsapi.aliyuncs.com";
                // 初始化ascClient,暂时不支持多region（请勿修改）
                IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
                try {
                    DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
                } catch (ClientException e) {
                    e.printStackTrace();
                    LOGGER.error("短信客户端错误:", e);
                }
                iAcsClient = new DefaultAcsClient(profile);
                I_ACS_CLIENT_MAP.put(accessKeyId, iAcsClient);
            }
        }
        return iAcsClient;
    }

    /**
     * 发送短信
     * @param accessKeyId accessKeyId
     * @param accessKeySecret accessKeySecret
     * @param phone 手信手机号
     * @param signName 签名
     * @param templateCode 模版编码
     * @param param 参数
     */
    public static void send(String accessKeyId, String accessKeySecret, String phone, String signName, String templateCode, String param) {
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用POST提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(param);
        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = getIAcsClient(accessKeyId, accessKeySecret).getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals(SUCCESS_CODE)) {
                LOGGER.info("短信发送成功，收信人:{},签名:{},模版:{},参数:{}", phone, signName, templateCode, param);
            } else {
                LOGGER.info("短信发送失败, 收信人:{},签名:{},模版:{},参数:{},错误:{}", phone, signName, templateCode, param, sendSmsResponse.getMessage());
            }
        } catch (ClientException e) {
            e.printStackTrace();
            LOGGER.error("短信客户端错误:", e);
        }
    }
}
