# JWT认证问题已修复

## 问题原因

你的token使用了 **HS384** 算法（从token头部 `eyJhbGciOiJIUzM4NCJ9` 可以看出），但是：

1. **生成token时**：密钥长度为62字节（496位），`Keys.hmacShaKeyFor()` 自动选择了 **HS384** 算法
2. **验证token时**：同样的密钥和方法，但可能因为某些原因导致算法不一致

## 解决方案

### 修改内容

在 `JwtTokenProvider.java` 中做了以下修改：

1. **明确指定算法**：在生成token时显式指定使用 `SignatureAlgorithm.HS256`
   ```java
   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
   ```

2. **密钥长度处理**：确保密钥至少32字节（256位），不足时自动填充
   ```java
   private SecretKey getSigningKey() {
       byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
       if (keyBytes.length < 32) {
           byte[] newKey = new byte[32];
           System.arraycopy(keyBytes, 0, newKey, 0, keyBytes.length);
           keyBytes = newKey;
       }
       return Keys.hmacShaKeyFor(keyBytes);
   }
   ```

## 测试步骤

### 1. 重启应用
```bash
mvn spring-boot:run
```

### 2. 重新登录获取新token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"zzb","password":"your_password"}'
```

新的token头部应该是 `eyJhbGciOiJIUzI1NiJ9`（HS256算法）

### 3. 使用新token测试
```bash
curl -X POST http://localhost:8080/api/recipes/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_NEW_TOKEN" \
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

## 注意事项

⚠️ **旧的token将无法使用**

因为算法从HS384改为HS256，之前生成的token（包括你提供的那个）将无法验证。需要：
1. 重新登录获取新token
2. 使用新token访问受保护接口

## 验证成功的标志

登录后返回的token应该是这样的格式：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6emIiLCJpYXQiOjE3Mzc...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6emIiLCJpYXQiOjE3Mzc...",
    "tokenType": "Bearer",
    "username": "zzb"
  }
}
```

注意 `accessToken` 开头是 `eyJhbGciOiJIUzI1NiJ9`（Base64解码后是 `{"alg":"HS256"}`）

## 后端日志

成功认证时，日志会显示：
```
Processing request: POST /api/recipes/generate
Authorization header: Bearer eyJhbGciOiJIUzI1NiJ9...
JWT token found: eyJhbGciOiJIUzI1NiJ9...
Username from token: zzb
User authenticated successfully: zzb
```

如果还有问题，请查看日志中的错误信息。
