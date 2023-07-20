package com.zeta;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zeta.system.model.entity.SysRole;
import com.zeta.system.model.entity.SysUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.zetaframework.core.utils.JSONUtil;

import java.util.List;

/**
 * JSONUtil工具类测试类
 *
 * @author gcc
 */
public class JSONUtilTests {

    private static String userListJsonStr = "";

    @BeforeAll
    public static void before() {
        SysUser user1 = new SysUser();
        user1.setId(1L);
        user1.setAccount("test1");
        SysUser user2 = new SysUser();
        user2.setId(2L);
        user2.setAccount("test2");
        List<SysUser> userList = CollUtil.newArrayList(user1, user2);

        userListJsonStr = JSONUtil.toJsonStr(userList);
    }

    /**
     * 将userListJsonStr转换成List<SysUser>测试
     */
    @Test
    public void parseUserListTest() {
        // 转成List<SysUser>对象
        List<SysUser> userList = JSONUtil.parseObject(userListJsonStr, new TypeReference<List<SysUser>>() {});
        assert(userList != null && !userList.isEmpty());
        assert(userList.size() == 2);
        userList.forEach(System.out::println);

        // 转成List<SysRole>对象
        List<SysRole> roleList = JSONUtil.parseObject(userListJsonStr, new TypeReference<List<SysRole>>() {});
        assert(roleList == null);
    }

}
