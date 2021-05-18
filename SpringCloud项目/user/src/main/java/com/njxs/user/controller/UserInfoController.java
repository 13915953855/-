package com.njxs.user.controller;

import com.njxs.user.entity.UserInfo;
import com.njxs.user.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (UserInfo)表控制层
 *
 * @author makejava
 * @since 2021-05-18 14:26:07
 */
@RestController
@RequestMapping("userInfo")
public class UserInfoController {
    /**
     * 服务对象
     */
    @Resource
    private UserInfoService userInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public UserInfo selectOne(@RequestParam(name = "id") Integer id) {
        return this.userInfoService.queryById(id);
    }

}