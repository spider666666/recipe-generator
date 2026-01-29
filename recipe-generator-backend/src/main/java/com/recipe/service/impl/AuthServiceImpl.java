package com.recipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recipe.dto.request.LoginRequest;
import com.recipe.dto.request.RegisterRequest;
import com.recipe.dto.response.JwtResponse;
import com.recipe.entity.User;
import com.recipe.mapper.UserMapper;
import com.recipe.security.JwtTokenProvider;
import com.recipe.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername())
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null) {
            Long emailCount = userMapper.selectCount(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getEmail, request.getEmail())
            );
            if (emailCount > 0) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setEnabled(true);

        userMapper.insert(user);

        // 生成JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = tokenProvider.generateToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);

        return new JwtResponse(accessToken, refreshToken, user.getUsername());
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 生成JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);

        return new JwtResponse(accessToken, refreshToken, userDetails.getUsername());
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (tokenProvider.validateToken(refreshToken, userDetails)) {
            String newAccessToken = tokenProvider.generateToken(userDetails);
            return new JwtResponse(newAccessToken, refreshToken, username);
        }

        throw new RuntimeException("刷新token无效");
    }
}
