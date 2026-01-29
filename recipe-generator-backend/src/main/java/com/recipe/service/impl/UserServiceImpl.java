package com.recipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recipe.dto.response.UserInfoResponse;
import com.recipe.entity.User;
import com.recipe.mapper.UserMapper;
import com.recipe.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Override
    public UserInfoResponse getCurrentUserInfo(String username) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setEnabled(user.getEnabled());
        response.setCreateTime(user.getCreateTime());

        return response;
    }
}
