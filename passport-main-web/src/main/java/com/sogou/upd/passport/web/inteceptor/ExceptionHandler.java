package com.sogou.upd.passport.web.inteceptor;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.HttpConstant;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于对异常的统一处理
 * User: ligang201716@sogou-inc.com
 * Date: 13-7-10
 * Time: 下午10:09
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("ExceptionHandler ",ex);
        //post请求返回json
        if(request.getMethod().toUpperCase().equals(HttpConstant.HttpMethod.POST)){
            Result result=new APIResultSupport(false);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            ModelAndView modelAndView=new ModelAndView("/exception");
            modelAndView.addObject("result",result);
            return  modelAndView;
        }
        //非post请求返回错误页面
        return new ModelAndView("/500");
    }
}