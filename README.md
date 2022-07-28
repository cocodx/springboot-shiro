# springboot-shiro
shiro 各种用法姿势

##### shirodemo1
在主函数，测试性质的跑一下shiro的登录流程。从配置shiro.ini加载用户信息
***
##### shirodemo2
自定义realm，在校验用户身份，从数据库中取用户信息对比
***
##### shirodemo3
加密，以及shiro中，怎么使用加密，realm中配置加密算法，salt，迭代次数
***
##### shirodemo4
用户授权，前置条件是：已经登录的用户，在realm中实现授权方法。添加用户角色与用户权限

建表语句
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

```

初始化sql
```sql
INSERT INTO `sys_permission` VALUES (1, 'user:create', '用户创建权限');
INSERT INTO `sys_permission` VALUES (2, 'user:delete', '用户删除权限');
INSERT INTO `sys_permission` VALUES (3, 'user:query', '用户查询权限');
INSERT INTO `sys_permission` VALUES (4, 'user:update', '用户修改权限');

INSERT INTO `sys_role` VALUES (1, 'role1');
INSERT INTO `sys_role` VALUES (2, 'role2');
INSERT INTO `sys_role` VALUES (3, 'role3');

INSERT INTO `sys_role_permission` VALUES (1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 2);
INSERT INTO `sys_role_permission` VALUES (3, 3);

INSERT INTO `sys_user` VALUES (1, 'root', 'b2793335f43645fd8e00c7d18e14e05f');

INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 2);
INSERT INTO `sys_user_role` VALUES (1, 3);

```

查询语句
```sql
-------查询用户的角色
select r.* from sys_user u 
LEFT JOIN sys_user_role ur on u.id=ur.user_id
LEFT JOIN sys_role r on r.id=ur.role_id
where u.user_name="root";

-------查询用户的权限
select p.* from sys_role r
LEFT JOIN sys_role_permission rp on r.id=rp.role_id
LEFT JOIN sys_permission p on rp.permission_id=p.id
where r.role_name in (
	select r.role_name from sys_user u 
LEFT JOIN sys_user_role ur on u.id=ur.user_id
LEFT JOIN sys_role r on r.id=ur.role_id
where u.user_name="root"
);
```
***