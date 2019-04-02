package com.ssaw.rocketmq.proxy;

import lombok.Data;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author HuSen
 * @date 2019/4/2 10:55
 */
@Data
public class RocketMqProxyFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws IllegalAccessException, MQClientException, InstantiationException {
        return (T) new RocketMqProxy().bind(interfaceClass);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}