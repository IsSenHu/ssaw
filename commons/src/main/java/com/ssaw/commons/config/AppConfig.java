package com.ssaw.commons.config;

import com.ssaw.commons.bean.StudentBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @date 2019/4/8 14:06
 */
@Configuration
public class AppConfig {

    @Bean
    public StudentBean studentBean() {
        StudentBean studentBean = new StudentBean();
        studentBean.setId(1);
        studentBean.setName("胡森");
        return studentBean;

    }
}