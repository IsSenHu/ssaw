package com.ssaw.commons.aspect;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.exceptions.ParamException;
import com.ssaw.commons.util.json.jack.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author HuSen.
 * @date 2018/11/28 17:27.
 */
@Slf4j
@Aspect
public class AutoRequestResolve {

    /**
     * 打印请求参数
     * @param joinPoint 切点
     * @param requestLog RequestLog注解
     */
    @Before(value = "@annotation(requestLog)", argNames = "joinPoint,requestLog")
    public void log(JoinPoint joinPoint, RequestLog requestLog) {
        Object[] args = joinPoint.getArgs();
        // 过滤掉不用打印的参数
        List<Object> objects = argsFilter(args);
        log.info("请求方法:{},参数:{}", requestLog.method(), JsonUtils.object2JsonString(objects));
    }

    /**
     * 参数校验
     * @param joinPoint 切点
     * @param validating Validating注解
     */
    @Before(value = "@annotation(validating)", argNames = "joinPoint,validating")
    public void validating(JoinPoint joinPoint, Validating validating) throws ParamException {
        Object[] args = joinPoint.getArgs();
        if(ArrayUtils.isNotEmpty(args)) {
            List<Object> collect = Arrays.stream(args)
                    // 过滤出BindingResult实现类的参数
                    .filter(arg -> BindingResult.class.isAssignableFrom(arg.getClass()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)) {
                BindingResult result = (BindingResult) collect.get(0);
                if(result.hasErrors()) {
                    throw new ParamException(result.getFieldErrors());
                }
            }
        }
    }

    /**
     * 参数过滤，以排除HttpServletRequest等类型参数，便于转为json串.
     * @param args 未过滤的参数
     * @return 过滤后的参数
     */
    private List<Object> argsFilter(Object[] args) {
        List<Object> list = new ArrayList<>();
        int length = args.length;
        for (Object obj : args) {
            if (!(obj instanceof HttpSession)
                    && !(ServletRequest.class.isAssignableFrom(obj.getClass()))
                    && !(ServletResponse.class.isAssignableFrom(obj.getClass()))
                    && !(WebRequest.class.isAssignableFrom(obj.getClass()))
                    && !(InputStream.class.isAssignableFrom(obj.getClass()))
                    && !(OutputStream.class.isAssignableFrom(obj.getClass()))
                    && !(Reader.class.isAssignableFrom(obj.getClass()))
                    && !(Writer.class.isAssignableFrom(obj.getClass()))
                    && !(Locale.class.isAssignableFrom(obj.getClass()))
                    && !(Principal.class.isAssignableFrom(obj.getClass()))
                    && !(Model.class.isAssignableFrom(obj.getClass()))
                    && !(ModelMap.class.isAssignableFrom(obj.getClass()))
                    && !(Errors.class.isAssignableFrom(obj.getClass()))
                    && !BindingResult.class.isAssignableFrom(obj.getClass())
                    && !(SessionStatus.class.isAssignableFrom(obj.getClass()))
                    && !RedirectAttributes.class.isAssignableFrom(obj.getClass())
                    && !(UriComponentsBuilder.class.isAssignableFrom(obj.getClass()))
                    && !(HttpEntity.class.isAssignableFrom(obj.getClass()))) {
                list.add(obj);
            }
        }
        return list;
    }
}
