package com.nzgreens.console.realm;

import com.nzgreens.console.permission.BitCode;
import com.nzgreens.console.permission.PermissionConfig;
import com.nzgreens.dal.console.example.AdminRole;
import com.nzgreens.dal.console.example.AdminUser;
import com.nzgreens.dal.console.example.AdminUserExample;
import com.nzgreens.dal.console.mapper.AdminRoleMapper;
import com.nzgreens.dal.console.mapper.AdminUserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录组件。
 */
public class LoginRealm extends AuthorizingRealm {
    @Resource
    private PermissionConfig permissionConfig;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private AdminRoleMapper adminRoleMapper;
    private String hashAlgorithmName = Sha256Hash.ALGORITHM_NAME;
    private String salt = Sha256Hash.ALGORITHM_NAME;

    /**
     * 清理当前用户缓存。
     */
    public void clearCache() {
        clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

    /**
     * 检查密码是否正确。
     *
     * @param password       原密码
     * @param hashedPassword 加密后的密码
     * @return 如果密码正确返回true，否则返回false。
     */
    public Boolean checkPassword(String password, String hashedPassword) {
        return encryptPassword(password).equals(hashedPassword);
    }

    /**
     * 加密。
     *
     * @param password 待加密的密码
     * @return 返回加密后的密码。
     */
    public String encryptPassword(String password) {
        return new SimpleHash(hashAlgorithmName, password, getSaltByteSource())
                .toBase64();
    }

	public static void main(String[] args) {
		System.out.println(new SimpleHash(Sha256Hash.ALGORITHM_NAME, "cnyfdr)(*", ByteSource.Util.bytes(Sha256Hash.ALGORITHM_NAME))
				.toBase64());
	}

    @Override
    protected void onInit() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(
                hashAlgorithmName);
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        AdminUserExample example = new AdminUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<AdminUser> users = adminUserMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(users)) {
            throw new UnknownAccountException();
        }
        AdminUser user = users.get(0);
        if (user.getStatus() == 1) {
            throw new DisabledAccountException();
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user.getId(), user.getPassword(), getSaltByteSource(),
                getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        Long userId = (Long) getAvailablePrincipal(principals);
        AdminUser user = adminUserMapper.selectByPrimaryKey(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        BitCode userCode = null;
        BitCode roleCode = null;
        if (user.getRoleId() != null) {
            AdminRole role = adminRoleMapper.selectByPrimaryKey(user.getRoleId());
            if (role != null && StringUtils.isNotEmpty(role.getPermissions())) {
                roleCode = new BitCode(role.getPermissions());
            }
        }
        info.addRoles(permissionConfig.getPermissionCodes(getUserPermission(userCode, roleCode)));
        return info;
    }

    private BitCode getUserPermission(BitCode userCode, BitCode roleCode) {
        if (userCode != null && roleCode != null) {
            return userCode.or(roleCode);
        } else if (userCode == null && roleCode != null) {
            return roleCode;
        } else if (userCode != null && roleCode == null) {
            return userCode;
        }
        return new BitCode();
    }

    private ByteSource getSaltByteSource() {
        return ByteSource.Util.bytes(salt);
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithmName = hashAlgorithmName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
