package com.rsschool.sps.web.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.rsschool.sps.domain.User;
import com.rsschool.sps.service.utils.HttpClient;
import com.rsschool.sps.web.domain.BaseUserDetails;
import kong.unirest.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RedirectFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        BaseUserDetails principal = (BaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            User user = (User) principal.getPrincipal();
            ctx.addZuulRequestHeader(HttpClient.getTokenHeaderName(), user.getToken());
        } else {
            ctx.setResponseBody("Forbidden");
            ctx.setResponseStatusCode(HttpStatus.FORBIDDEN);
            ctx.setSendZuulResponse(false);
        }
        return null;
    }
}
