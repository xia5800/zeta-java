package com.zeta;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zeta.system.model.entity.*;
import com.zeta.system.model.enums.MenuTypeEnum;
import com.zeta.system.model.enums.SexEnum;
import com.zeta.system.model.enums.UserStateEnum;
import com.zeta.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zetaframework.core.mybatisplus.generator.UidGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 初始化数据库
 *
 * @author gcc
 */
@SpringBootTest
public class KtZetaApplicationTests {

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysRoleMenuService roleMenuService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;


    /**
     * 初始化数据库
     */
    // @Test // 注释掉，防止maven打包的时候没有跳过测试
    public void initDatabase() {
        // 初始化系统菜单、权限
        List<Long> menuIds = initMenu();
        // 初始化角色
        Long superAdminId = initRole();
        // 初始化超级管理员菜单权限
        initRoleMenu(superAdminId, menuIds);
        // 初始化超级管理员用户
        Long userId = initAdminUser();
        // 初始化用户角色
        initUserRole(userId, superAdminId);
        // 初始化数据字典
        initSysDict();
    }


    /**
     * 初始化系统菜单、权限
     */
    public List<Long> initMenu() {
        List<SysMenu> batchList = new ArrayList<>();
        int menuSort = 1;

        // 系统管理
        int systemSort = 1;
        Long systemId = uidGenerator.getUid();
        batchList.add(buildMenu(systemId, 0L, menuSort++, "系统管理", "/system", "", "layui-icon-set"));
        // 系统管理-用户管理
        Long userId = uidGenerator.getUid();
        Long userIdR = uidGenerator.getUid();
        Long userIdC = uidGenerator.getUid();
        Long userIdU = uidGenerator.getUid();
        Long userIdD = uidGenerator.getUid();
        batchList.add(buildMenu(userId, systemId, systemSort++, "用户管理", "/system/user", "system/user"));
        batchList.add(buildButton(userIdR, userId, 1, "查看用户", "sys:user:view"));
        batchList.add(buildButton(userIdC, userId, 2, "新增用户", "sys:user:save"));
        batchList.add(buildButton(userIdU, userId, 3, "修改用户", "sys:user:update"));
        batchList.add(buildButton(userIdD, userId, 4, "删除用户", "sys:user:delete"));
        // 系统管理-角色管理
        Long roleId = uidGenerator.getUid();
        Long roleIdR = uidGenerator.getUid();
        Long roleIdC = uidGenerator.getUid();
        Long roleIdU = uidGenerator.getUid();
        Long roleIdD = uidGenerator.getUid();
        batchList.add(buildMenu(roleId, systemId, systemSort++, "角色管理", "/system/role", "system/role"));
        batchList.add(buildButton(roleIdR, roleId, 1, "查看角色", "sys:role:view"));
        batchList.add(buildButton(roleIdC, roleId, 2, "新增角色", "sys:role:save"));
        batchList.add(buildButton(roleIdU, roleId, 3, "修改角色", "sys:role:update"));
        batchList.add(buildButton(roleIdD, roleId, 4, "删除角色", "sys:role:delete"));
        // 系统管理-菜单管理
        Long menuId = uidGenerator.getUid();
        Long menuIdR = uidGenerator.getUid();
        Long menuIdC = uidGenerator.getUid();
        Long menuIdU = uidGenerator.getUid();
        Long menuIdD = uidGenerator.getUid();
        batchList.add(buildMenu(menuId, systemId, systemSort++, "菜单管理", "/system/menu", "system/menu"));
        batchList.add(buildButton(menuIdR, menuId, 1, "查看菜单", "sys:menu:view"));
        batchList.add(buildButton(menuIdC, menuId, 2, "新增菜单", "sys:menu:save"));
        batchList.add(buildButton(menuIdU, menuId, 3, "修改菜单", "sys:menu:update"));
        batchList.add(buildButton(menuIdD, menuId, 4, "删除菜单", "sys:menu:delete"));
        // 系统管理-操作日志
        Long optId = uidGenerator.getUid();
        Long optIdR = uidGenerator.getUid();
        batchList.add(buildMenu(optId, systemId, systemSort++, "操作日志", "/system/optLog", "system/optLog"));
        batchList.add(buildButton(optIdR, optId, 1, "查看操作日志", "sys:optLog:view"));
        // 系统管理-登录日志
        Long loginLogId = uidGenerator.getUid();
        Long loginLogIdR = uidGenerator.getUid();
        batchList.add(buildMenu(loginLogId, systemId, systemSort++, "登录日志", "/system/loginLog", "system/loginLog"));
        batchList.add(buildButton(loginLogIdR, loginLogId, 1, "查看登录日志", "sys:loginLog:view"));
        // 系统管理-文件管理
        Long fileId = uidGenerator.getUid();
        Long fileIdR = uidGenerator.getUid();
        Long fileIdC = uidGenerator.getUid();
        Long fileIdE = uidGenerator.getUid();
        Long fileIdD = uidGenerator.getUid();
        batchList.add(buildMenu(fileId, systemId, systemSort++, "文件管理", "/system/file", "system/file"));
        batchList.add(buildButton(fileIdR, fileId, 1, "查看文件", "sys:file:view"));
        batchList.add(buildButton(fileIdC, fileId, 2, "上传文件", "sys:file:save"));
        batchList.add(buildButton(fileIdE, fileId, 3, "下载文件", "sys:file:export"));
        batchList.add(buildButton(fileIdD, fileId, 4, "删除文件", "sys:file:delete"));
        // 系统管理-数据字典
        Long dictId = uidGenerator.getUid();
        Long dictIdR = uidGenerator.getUid();
        Long dictIdC = uidGenerator.getUid();
        Long dictIdU = uidGenerator.getUid();
        Long dictIdD = uidGenerator.getUid();
        batchList.add(buildMenu(dictId, systemId, systemSort++, "数据字典", "/system/dict", "system/dict"));
        batchList.add(buildButton(dictIdR, dictId, 1, "查看字典", "sys:dict:view"));
        batchList.add(buildButton(dictIdC, dictId, 2, "新增字典", "sys:dict:save"));
        batchList.add(buildButton(dictIdU, dictId, 3, "修改字典", "sys:dict:update"));
        batchList.add(buildButton(dictIdD, dictId, 4, "删除字典", "sys:dict:delete"));
        Long dictItemR = uidGenerator.getUid();
        Long dictItemC = uidGenerator.getUid();
        Long dictItemU = uidGenerator.getUid();
        Long dictItemD = uidGenerator.getUid();
        batchList.add(buildButton(dictItemR, dictId, 5, "查看字典项", "sys:dictItem:view"));
        batchList.add(buildButton(dictItemC, dictId, 6, "新增字典项", "sys:dictItem:save"));
        batchList.add(buildButton(dictItemU, dictId, 7, "修改字典项", "sys:dictItem:update"));
        batchList.add(buildButton(dictItemD, dictId, 8, "删除字典项", "sys:dictItem:delete"));

        menuService.saveBatch(batchList);

        return CollUtil.newArrayList(
                systemId,
                userId, userIdR ,userIdC, userIdU, userIdD,
                roleId, roleIdR, roleIdC, roleIdU, roleIdD,
                menuId, menuIdR, menuIdC, menuIdU, menuIdD,
                optId, optIdR,
                loginLogId, loginLogIdR,
                fileId, fileIdR, fileIdC, fileIdE, fileIdD,
                dictId, dictIdR, dictIdC, dictIdU, dictIdD,
                dictItemR, dictItemC, dictItemU, dictItemD
        );
    }


    /**
     * 初始化角色
     */
    public Long initRole() {
        List<SysRole> batchList = new ArrayList<>();

        Long superAdminId = uidGenerator.getUid();
        Long adminId = uidGenerator.getUid();
        Long userId = uidGenerator.getUid();
        batchList.add(new SysRole(superAdminId, "超级管理员", "SUPER_ADMIN", "超级管理员，拥有至高无上的权利", true));
        batchList.add(new SysRole(adminId, "管理员", "ADMIN", "管理员，拥有99%的权利"));
        batchList.add(new SysRole(userId, "普通用户", "USER", "普通用户，拥有管理员赋予的权利"));
        roleService.saveBatch(batchList);

        return superAdminId;
    }

    /**
     * 初始化超级管理员菜单权限
     * @param superAdminRoleId Long     超级管理员角色id
     * @param menuIds List<Long>    菜单id
     */
    public void initRoleMenu(Long superAdminRoleId, List<Long> menuIds) {
        List<SysRoleMenu> batchList = menuIds.stream()
                .map(it -> new SysRoleMenu(superAdminRoleId, it))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(batchList);
    }

    /**
     * 初始化超级管理员用户
     */
    public Long initAdminUser() {
        Long userId = uidGenerator.getUid();
        String passwordEncoder = BCrypt.hashpw("admin");
        SysUser user = new SysUser(userId, "zeta管理员", "zetaAdmin", passwordEncoder, SexEnum.MALE.getCode(), UserStateEnum.NORMAL.getCode(), true);
        userService.save(user);
        return userId;
    }

    /**
     * 初始化用户角色
     * @param userId Long           用户id
     * @param superAdminRoleId Long     （超级管理员）角色id
     */
    public void initUserRole(Long userId, Long superAdminRoleId) {
        userRoleService.save(new SysUserRole(userId, superAdminRoleId));
    }

    /**
     * 初始化数据字典
     */
    public void initSysDict() {
        // 初始化字典
        List<SysDict> dictList = new ArrayList<>();
        Long dictId = uidGenerator.getUid();
        dictList.add(new SysDict(dictId, "设备状态", "device_status", "设备运行状态", 1));
        sysDictService.saveBatch(dictList);

        // 初始化字典项
        List<SysDictItem> dictItemList = new ArrayList<>();
        dictItemList.add(new SysDictItem(uidGenerator.getUid(), dictId, "运行", "RUNNING", "设备正在运行", 1));
        dictItemList.add(new SysDictItem(uidGenerator.getUid(), dictId, "停止", "WAITING", "设备已停止", 2));
        sysDictItemService.saveBatch(dictItemList);
    }


    /**
     * 构造菜单
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param label
     * @param path
     * @return
     */
    private SysMenu buildMenu(Long id, Long parentId, Integer sortValue, String label, String path) {
        return buildMenu(id, parentId, sortValue, label, path, "", "");
    }

    /**
     * 构造菜单
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param label
     * @param path
     * @param component
     * @return
     */
    private SysMenu buildMenu(Long id, Long parentId, Integer sortValue, String label, String path, String component) {
        return buildMenu(id, parentId, sortValue, label, path, component, "");
    }

    /**
     * 构造菜单
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param label
     * @param path
     * @param icon
     */
    private SysMenu buildMenu(Long id, Long parentId, Integer sortValue, String label, String path, String component, String icon) {
        // 将"/system" => "system";  "/system/user" => "system_user";  "/system/user/123" => "system_user_123"
        String name = Arrays.stream(path.split("/")).filter(StrUtil::isNotBlank).collect(Collectors.joining("_"));

        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(id);
        sysMenu.setParentId(parentId);
        sysMenu.setSortValue(sortValue);
        sysMenu.setLabel(label);
        sysMenu.setPath(path);
        sysMenu.setName(name);
        sysMenu.setComponent(component);
        sysMenu.setIcon(icon);
        sysMenu.setIcon(icon);
        sysMenu.setType(MenuTypeEnum.MENU);
        sysMenu.setAuthority("");
        return sysMenu;
    }

    /**
     * 构造按钮
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param label
     * @param authority
     */
    private SysMenu buildButton(Long id, Long parentId, Integer sortValue, String label, String authority) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setAuthority(authority);
        sysMenu.setType(MenuTypeEnum.RESOURCE);
        sysMenu.setLabel(label);
        sysMenu.setParentId(parentId);
        sysMenu.setSortValue(sortValue);
        sysMenu.setId(id);
        return sysMenu;
    }
}
