# Claude API 500错误排查指南

## 问题描述

后端调用Claude API时返回 500 错误，可能的原因：
1. **请求头配置错误**（最常见）
2. 模型不支持
3. API Key认证失败
4. 请求体格式错误

## 已做的修改

### 修改前的请求头（可能有问题）：
```java
.addHeader("x-api-key", claudeConfig.getApiKey())
.addHeader("anthropic-version", "2023-06-01")  // 官方API特有
.addHeader("content-type", "application/json")
```

### 修改后的请求头（更通用）：
```java
.addHeader("Authorization", "Bearer " + claudeConfig.getApiKey())
.addHeader("Content-Type", "application/json")
```

**关键变化**：
- ❌ 移除了 `x-api-key` 头（代理可能不支持）
- ❌ 移除了 `anthropic-version` 头（只有官方API需要）
- ✅ 使用标准的 `Authorization: Bearer` 认证
- ✅ 增强了错误日志输出

## 测试步骤

### 步骤1：重启应用

```bash
# 停止当前运行的应用（Ctrl+C）
# 重新启动
mvn spring-boot:run
```

### 步骤2：使用curl直接测试你的代理API

测试1：标准Authorization头（推荐）
```bash
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "Authorization: Bearer cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-3-5-sonnet-20240620",
    "max_tokens": 100,
    "messages": [
      {
        "role": "user",
        "content": "Hello"
      }
    ]
  }'
```

测试2：使用x-api-key头
```bash
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "x-api-key: cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-3-5-sonnet-20240620",
    "max_tokens": 100,
    "messages": [
      {
        "role": "user",
        "content": "Hello"
      }
    ]
  }'
```

测试3：同时使用两个认证头
```bash
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "Authorization: Bearer cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "x-api-key: cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-3-5-sonnet-20240620",
    "max_tokens": 100,
    "messages": [
      {
        "role": "user",
        "content": "Hello"
      }
    ]
  }'
```

### 步骤3：测试不同的模型

```bash
# 测试Claude 3 Sonnet
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "Authorization: Bearer cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-3-sonnet-20240229",
    "max_tokens": 100,
    "messages": [{"role": "user", "content": "Hello"}]
  }'

# 测试Claude 3 Haiku（最便宜，最可能支持）
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "Authorization: Bearer cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-3-haiku-20240307",
    "max_tokens": 100,
    "messages": [{"role": "user", "content": "Hello"}]
  }'

# 测试Claude 2.1
curl -X POST https://crs-external.myrightone.com/api/v1/messages \
  -H "Authorization: Bearer cr_b20992fdba86fa7a405392fa5754631085eac3d9432ed526095e33519278aa53" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "claude-2.1",
    "max_tokens": 100,
    "messages": [{"role": "user", "content": "Hello"}]
  }'
```

### 步骤4：查看详细的后端日志

重启应用后，调用生成食谱接口，查看日志输出：

```
2026-01-29 15:XX:XX - 调用Claude API: https://crs-external.myrightone.com/api/v1/messages
2026-01-29 15:XX:XX - 使用模型: claude-3-5-sonnet-20240620
2026-01-29 15:XX:XX - 请求头: Authorization=Bearer cr_b20992..., Content-Type=application/json
2026-01-29 15:XX:XX - Claude API响应状态: 500
2026-01-29 15:XX:XX - 响应消息: Internal Server Error
2026-01-29 15:XX:XX - 响应体: {具体错误信息}
```

**重点关注响应体中的错误信息！**

## 常见错误及解决方案

### 错误1：认证方式不对

**错误信息**：
```json
{
  "error": "unauthorized",
  "message": "Invalid API key"
}
```

**解决方案**：
1. 确认API Key是否正确
2. 尝试使用 `x-api-key` 头而不是 `Authorization` 头
3. 联系代理服务商确认正确的认证方式

**修改代码**（如果需要x-api-key）：
```java
Request request = new Request.Builder()
    .url(claudeConfig.getApiUrl())
    .addHeader("x-api-key", claudeConfig.getApiKey())  // 使用x-api-key
    .addHeader("Content-Type", "application/json")
    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
    .build();
```

### 错误2：模型不支持

**错误信息**：
```json
{
  "error": "model_not_found",
  "message": "No available Claude accounts support the requested model"
}
```

**解决方案**：
按顺序尝试以下模型：

1. `claude-3-haiku-20240307` （最便宜，最可能支持）
2. `claude-3-sonnet-20240229` （标准版）
3. `claude-2.1` （旧版本，兼容性好）
4. `claude-2.0`

修改 `application.yml`：
```yaml
claude:
  model: claude-3-haiku-20240307  # 改为这个试试
```

### 错误3：请求体格式错误

**错误信息**：
```json
{
  "error": "invalid_request",
  "message": "Invalid request body"
}
```

**可能原因**：
- 代理API的请求格式与标准Claude API不同
- 需要额外的参数

**排查方法**：
联系代理服务商，询问：
1. 正确的请求体格式
2. 是否需要额外参数（如 `stream: false`）
3. 是否支持标准的Claude API格式

### 错误4：代理服务内部错误

**错误信息**：
```json
{
  "error": "internal_error",
  "message": "Relay service error"
}
```

**可能原因**：
1. 代理服务本身有问题
2. 上游Claude API调用失败
3. 账户余额不足

**解决方案**：
1. 检查代理服务的状态页面
2. 联系代理服务商
3. 查看账户余额

## 如果还是500错误

### 方案1：联系代理服务商

询问以下信息：
1. ✅ 支持哪些Claude模型？
2. ✅ 正确的API URL是什么？（完整路径）
3. ✅ 认证方式是什么？（Authorization? x-api-key? 其他？）
4. ✅ 请求头需要哪些字段？
5. ✅ 请求体格式是否与官方API一致？
6. ✅ 是否有示例代码？
7. ✅ 账户状态正常吗？有余额吗？

### 方案2：使用模拟数据

暂时不调用Claude API，使用模拟数据测试其他功能：

修改 `ClaudeRecipeGeneratorServiceImpl.java`：

```java
@Override
@Transactional
public Recipe generateRecipeWithClaude(GenerateRecipeRequest request) {
    try {
        // 临时使用模拟数据
        boolean useMock = true; // 设为true使用模拟数据

        String claudeResponse;
        if (useMock) {
            log.info("使用模拟数据生成食谱");
            claudeResponse = getMockRecipeResponse(request);
        } else {
            String prompt = buildPrompt(request);
            claudeResponse = callClaudeAPI(prompt);
        }

        return parseClaudeResponse(claudeResponse, request);
    } catch (Exception e) {
        log.error("生成食谱失败", e);
        throw new RuntimeException("生成食谱失败: " + e.getMessage());
    }
}

// 添加模拟数据方法
private String getMockRecipeResponse(GenerateRecipeRequest request) {
    return """
    {
      "name": "番茄炒蛋",
      "description": "经典家常菜，色香味俱全",
      "servings": 2,
      "ingredients": [
        {"name": "鸡蛋", "quantity": "3个", "isRequired": true},
        {"name": "番茄", "quantity": "2个", "isRequired": true},
        {"name": "盐", "quantity": "适量", "isRequired": true},
        {"name": "糖", "quantity": "少许", "isRequired": false},
        {"name": "葱", "quantity": "1根", "isRequired": false}
      ],
      "steps": [
        {"stepNumber": 1, "description": "鸡蛋打散，加入少许盐", "duration": 2},
        {"stepNumber": 2, "description": "番茄洗净切块", "duration": 3},
        {"stepNumber": 3, "description": "热油，倒入蛋液，炒至凝固盛出", "duration": 3},
        {"stepNumber": 4, "description": "锅中留油，炒番茄至软烂出汁", "duration": 5},
        {"stepNumber": 5, "description": "加入炒好的鸡蛋，加盐和糖调味", "duration": 2},
        {"stepNumber": 6, "description": "翻炒均匀，撒上葱花即可", "duration": 1}
      ]
    }
    """;
}
```

### 方案3：尝试其他Claude API代理

如果当前代理一直有问题，可以考虑：
1. 使用官方Claude API（api.anthropic.com）
2. 使用其他可靠的代理服务

## 下一步

1. **重启应用**，观察日志中的详细错误信息
2. **使用curl测试**，确认哪种请求头格式可以工作
3. **把curl测试结果告诉我**，包括：
   - 哪个测试成功了？
   - 返回的完整响应是什么？
   - 错误信息的详细内容
4. 根据测试结果，我会帮你调整Java代码

## 重要提示

500错误通常意味着：
- ❌ 不是认证问题（那会是401）
- ❌ 不是找不到路由（那会是404）
- ✅ 可能是请求头格式不对
- ✅ 可能是请求体参数不对
- ✅ 可能是代理服务本身的问题

**一定要看详细的错误响应体！**
