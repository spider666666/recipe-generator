# JPA迁移到MyBatis-Plus完成

## 迁移总结

已成功将项目从Spring Data JPA迁移到MyBatis-Plus。

## 主要变更

### 1. 依赖变更 (pom.xml)
- ❌ 移除：`spring-boot-starter-data-jpa`
- ✅ 添加：`mybatis-plus-boot-starter` (3.5.5)

### 2. 配置变更 (application.yml)
- ❌ 移除：JPA相关配置 (hibernate, ddl-auto等)
- ✅ 添加：MyBatis-Plus配置
  - 驼峰命名转换
  - SQL日志输出
  - 主键自动增长
  - 逻辑删除配置
  - Mapper文件位置

### 3. 启动类变更
- ✅ 添加：`@MapperScan("com.recipe.mapper")` 注解

### 4. Entity类变更
所有实体类的注解从JPA改为MyBatis-Plus：

| JPA注解 | MyBatis-Plus注解 | 说明 |
|---------|------------------|------|
| `@Entity` | `@TableName` | 指定表名 |
| `@Table` | `@TableName` | 指定表名 |
| `@Id` | `@TableId` | 主键标识 |
| `@GeneratedValue` | `IdType.AUTO` | 主键自增 |
| `@Column` | 无需注解 | 自动映射 |
| `@CreationTimestamp` | `@TableField(fill = FieldFill.INSERT)` | 插入时填充 |
| `@UpdateTimestamp` | `@TableField(fill = FieldFill.INSERT_UPDATE)` | 更新时填充 |
| `@OneToMany/@ManyToOne` | `@TableField(exist = false)` | 关联对象不映射 |

**字段名变更：**
- `createdAt` → `createTime`
- `updatedAt` → `updateTime`

### 5. Repository → Mapper
创建了新的Mapper接口替代Repository：

```
repository/ (已删除)
├── UserRepository.java
├── RecipeRepository.java
├── IngredientRepository.java
├── FavoriteRepository.java
├── HistoryRepository.java
└── ShoppingListRepository.java

mapper/ (新建)
├── UserMapper.java
├── RecipeMapper.java
├── IngredientMapper.java
├── FavoriteMapper.java
├── HistoryMapper.java
└── ShoppingListMapper.java
```

所有Mapper接口继承 `BaseMapper<T>`，自动获得CRUD方法。

### 6. Service层变更

#### AuthServiceImpl
```java
// JPA方式
userRepository.existsByUsername(username)
userRepository.save(user)

// MyBatis-Plus方式
userMapper.selectCount(new LambdaQueryWrapper<User>()
    .eq(User::getUsername, username))
userMapper.insert(user)
```

#### ClaudeRecipeGeneratorServiceImpl
```java
// JPA方式
recipeRepository.save(recipe)

// MyBatis-Plus方式
recipeMapper.insert(recipe)
recipeIngredientMapper.insert(ri)
recipeStepMapper.insert(step)
```

#### CustomUserDetailsService
```java
// JPA方式
userRepository.findByUsername(username)

// MyBatis-Plus方式
userMapper.selectOne(new LambdaQueryWrapper<User>()
    .eq(User::getUsername, username))
```

### 7. 新增配置类

#### MyBatisPlusConfig.java
- 配置分页插件

#### MyBatisPlusMetaObjectHandler.java
- 自动填充 `createTime` 和 `updateTime`

## MyBatis-Plus常用方法

### 查询
```java
// 根据ID查询
User user = userMapper.selectById(1L);

// 条件查询
User user = userMapper.selectOne(
    new LambdaQueryWrapper<User>()
        .eq(User::getUsername, "admin")
);

// 列表查询
List<User> users = userMapper.selectList(
    new LambdaQueryWrapper<User>()
        .like(User::getUsername, "test")
);

// 分页查询
Page<User> page = new Page<>(1, 10);
userMapper.selectPage(page,
    new LambdaQueryWrapper<User>()
        .orderByDesc(User::getCreateTime)
);

// 统计
Long count = userMapper.selectCount(
    new LambdaQueryWrapper<User>()
        .eq(User::getEnabled, true)
);
```

### 插入
```java
User user = new User();
user.setUsername("test");
userMapper.insert(user);  // 自动填充createTime和updateTime
```

### 更新
```java
// 根据ID更新
User user = new User();
user.setId(1L);
user.setNickname("新昵称");
userMapper.updateById(user);  // 自动填充updateTime

// 条件更新
userMapper.update(null,
    new LambdaUpdateWrapper<User>()
        .set(User::getEnabled, false)
        .eq(User::getUsername, "test")
);
```

### 删除
```java
// 根据ID删除
userMapper.deleteById(1L);

// 条件删除
userMapper.delete(
    new LambdaQueryWrapper<User>()
        .eq(User::getUsername, "test")
);
```

## 优势

1. **性能更好** - 直接SQL操作，无ORM转换开销
2. **更灵活** - 支持复杂SQL，可自定义XML
3. **代码更简洁** - Lambda表达式，类型安全
4. **功能丰富** - 内置分页、逻辑删除、自动填充等
5. **易于调试** - SQL日志清晰可见

## 注意事项

1. **数据库表必须存在** - MyBatis-Plus不会自动创建表，需要手动执行 `init.sql`
2. **字段映射** - 默认驼峰转下划线 (createTime → create_time)
3. **关联查询** - 需要手动处理或使用XML配置
4. **事务管理** - 使用 `@Transactional` 注解

## 测试建议

1. 先执行 `init.sql` 创建数据库表
2. 测试用户注册登录功能
3. 测试食谱生成功能
4. 检查SQL日志确认执行正确

## 后续优化

- [ ] 添加XML Mapper文件处理复杂查询
- [ ] 实现关联查询（食谱+食材+步骤）
- [ ] 添加缓存注解优化性能
- [ ] 完善其他Service层的CRUD操作
