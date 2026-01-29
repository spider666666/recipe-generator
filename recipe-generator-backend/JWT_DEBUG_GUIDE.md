# JWT认证问题排查指南

## 问题描述
登录成功后，访问其他接口仍然提示未登录（401 Unauthorized）

## 常见原因及解决方案

### 1. 前端未正确携带Token

#### 检查登录响应
登录成功后应该返回：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "username": "testuser"
  }
}
```

#### 正确的请求方式

**方式1：使用Postman/Apifox**
1. 登录接口获取 `accessToken`
2. 在后续请求的 Headers 中添加：
   ```
   Key: Authorization
   Value: Bearer eyJhbGciOiJIUzI1NiJ9...（完整token）
   ```
   ⚠️ 注意：`Bearer` 和 token 之间有一个空格

**方式2：使用curl**
```bash
# 1. 登录获取token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# 2. 使用token访问受保护接口
curl -X POST http://localhost:8080/api/recipes/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -d '{
    "ingredients": [
      {"name": "鸡肉", "quantity": "200g"}
    ],
    "cuisineType": "CHINESE",
    "flavorTypes": ["SPICY"],
    "cookingTime": 30,
    "difficultyLevel": "HOME_COOKING"
  }'
```

**方式3：使用JavaScript/Axios**
```javascript
// 登录
const loginResponse = await axios.post('/api/auth/login', {
  username: 'testuser',
  password: '123456'
});

const token = loginResponse.data.data.accessToken;

// 保存token
localStorage.setItem('token', token);

// 后续请求携带token
axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

// 或者在每个请求中单独设置
await axios.post('/api/recipes/generate', data, {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### 2. Token格式错误

#### 常见错误
❌ 错误格式：
- `Authorization: eyJhbGciOiJIUzI1NiJ9...` （缺少 Bearer 前缀）
- `Authorization: bearer eyJhbGciOiJIUzI1NiJ9...` （bearer 小写）
- `Authorization: Bearer  eyJhbGciOiJIUzI1NiJ9...` （多个空格）
- `Authorization: BearereyJhbGciOiJIUzI1NiJ9...` （缺少空格）

✅ 正确格式：
- `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`

### 3. Token过期

检查配置文件中的过期时间：
```yaml
jwt:
  expiration: 86400000  # 24小时（毫秒）
```

如果token过期，需要重新登录或使用refreshToken刷新。

### 4. CORS问题

如果是跨域请求，确保CORS配置正确：

```yaml
cors:
  allowed-origins: http://localhost:3000,http://localhost:5500
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true
```

前端需要设置：
```javascript
axios.defaults.withCredentials = true;
```

### 5. JWT密钥配置

确保 `application.yml` 中的JWT密钥已配置：
```yaml
jwt:
  secret: your-256-bit-secret-key-change-this-in-production-environment
```

密钥长度必须至少256位（32字符）。

## 调试步骤

### 步骤1：查看日志
启动应用后，日志会显示详细的认证信息：

```
2026-01-29 14:30:00 - Processing request: POST /api/recipes/generate
2026-01-29 14:30:00 - Authorization header: Bearer eyJhbGciOiJIUzI1NiJ9...
2026-01-29 14:30:00 - JWT token found: eyJhbGciOiJIUzI1NiJ9...
2026-01-29 14:30:00 - Username from token: testuser
2026-01-29 14:30:00 - User authenticated successfully: testuser
```

如果看到：
- `No JWT token found in request` → 前端未携带token
- `Token validation failed` → token无效或过期
- `Could not set user authentication` → token解析失败

### 步骤2：使用Swagger测试

1. 访问：http://localhost:8080/api/swagger-ui.html
2. 点击右上角 `Authorize` 按钮
3. 输入：`Bearer YOUR_ACCESS_TOKEN`
4. 点击 `Authorize`
5. 测试受保护的接口

### 步骤3：检查请求头

使用浏览器开发者工具（F12）：
1. 打开 Network 标签
2. 发送请求
3. 查看请求的 Headers
4. 确认 `Authorization` 字段存在且格式正确

## 完整测试流程

### 1. 注册用户
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "nickname": "测试用户"
  }'
```

### 2. 登录获取Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

响应示例：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwNjUxMDQwMCwiZXhwIjoxNzA2NTk2ODAwfQ.xxx",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwNjUxMDQwMCwiZXhwIjoxNzA3MTE1MjAwfQ.yyy",
    "tokenType": "Bearer",
    "username": "testuser"
  }
}
```

### 3. 使用Token访问受保护接口
```bash
# 将上面获取的accessToken替换到下面的YOUR_TOKEN
curl -X POST http://localhost:8080/api/recipes/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "ingredients": [
      {"name": "鸡肉", "quantity": "200g"},
      {"name": "土豆", "quantity": "2个"}
    ],
    "cuisineType": "CHINESE",
    "flavorTypes": ["SPICY"],
    "cookingTime": 30,
    "difficultyLevel": "HOME_COOKING"
  }'
```

## 常见错误信息

### 401 Unauthorized
```json
{
  "code": 401,
  "message": "未授权，请先登录",
  "error": "Full authentication is required to access this resource"
}
```
**原因**：未携带token或token无效

### 403 Forbidden
```json
{
  "code": 403,
  "message": "访问被拒绝"
}
```
**原因**：token有效但权限不足

## 前端集成示例

### Vue 3 + Axios
```javascript
// api/request.js
import axios from 'axios';

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
});

// 请求拦截器 - 自动添加token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器 - 处理401错误
request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      // 清除token并跳转到登录页
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default request;
```

### React + Fetch
```javascript
// utils/request.js
const request = async (url, options = {}) => {
  const token = localStorage.getItem('token');

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`http://localhost:8080/api${url}`, {
    ...options,
    headers
  });

  if (response.status === 401) {
    localStorage.removeItem('token');
    window.location.href = '/login';
    throw new Error('未授权');
  }

  return response.json();
};

export default request;
```

## 检查清单

- [ ] 登录接口返回了正确的token
- [ ] token已保存（localStorage/sessionStorage）
- [ ] 请求头包含 `Authorization: Bearer {token}`
- [ ] Bearer 和 token 之间有空格
- [ ] token未过期
- [ ] JWT密钥配置正确
- [ ] 后端日志显示token被正确解析
- [ ] CORS配置允许Authorization头

## 需要帮助？

如果以上方法都无法解决问题，请提供：
1. 登录接口的完整响应
2. 后续请求的完整请求头
3. 后端日志输出
4. 使用的客户端工具（Postman/浏览器/curl等）
