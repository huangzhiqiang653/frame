package com.zx.rts.controller;

import com.zx.common.common.ResponseBean;
import com.zx.rts.entity.RtOrganization;
import com.zx.rts.service.IRtOrganizationService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rts/index")
public class RtsIndexController {
    @Resource
    IRtOrganizationService organizationService;

    /**
     * 登陆校验
     *
     * @param request
     */
    @GetMapping("/toLogin")
    public ResponseBean loginValidate(HttpServletRequest request) {
        Map<String, RtOrganization> orgMap = null;
        List<RtOrganization> orgList = organizationService.list();
        if (!CollectionUtils.isEmpty(orgList)) {
            orgMap = new HashMap<>();
            for (RtOrganization org : orgList) {
                orgMap.put(org.getCode(), org);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("orgInfoMap", orgMap);
        return new ResponseBean(map);
    }
}
