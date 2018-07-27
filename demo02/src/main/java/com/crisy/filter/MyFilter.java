package com.crisy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * 自定义filter
 * 实现Filter接口，实现Filter方法
 * Title:MyFilter
 * Description:
 * @author wangchenxin
 * @date 2018-7-26 下午5:13:03
 */
public class MyFilter implements Filter {

	public void destroy() {
		System.out.println("destroy");
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		System.out.println("this is MyFilter,url:" + request.getRequestURI());
		filterChain.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("init");
	}

}
