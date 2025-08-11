package gov.cdc.nnddataexchangeservice.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GrantedAuthorityFinder {

    private static final String QUERY = """
        SELECT grantedAuthority
        FROM (
          SELECT 'ADMINISTRATE-SYSTEM' grantedAuthority
          FROM auth_user
          WHERE user_id = ? AND master_sec_admin_ind = 'T'
          UNION
          SELECT 'ADMINISTRATE-SECURITY' grantedAuthority
          FROM auth_user
          WHERE user_id = ? AND prog_area_admin_ind = 'T'
          UNION
          SELECT DISTINCT (operationType.bus_op_nm || '-' || objectType.bus_obj_nm) AS grantedAuthority
          FROM auth_user authUser
            JOIN auth_user_role role ON role.auth_user_uid = authUser.auth_user_uid
            JOIN auth_perm_set permissionSet ON role.auth_perm_set_uid = permissionSet.auth_perm_set_uid
            JOIN auth_bus_obj_rt objectRight ON objectRight.auth_perm_set_uid = permissionSet.auth_perm_set_uid
            JOIN auth_bus_obj_type objectType ON objectRight.auth_bus_obj_type_uid = objectType.auth_bus_obj_type_uid
            JOIN auth_bus_op_rt operationRight ON operationRight.auth_bus_obj_rt_uid = objectRight.auth_bus_obj_rt_uid
            JOIN auth_bus_op_type operationType ON operationType.auth_bus_op_type_uid = operationRight.auth_bus_op_type_uid
          WHERE
            authUser.user_id = ?
            AND NOT (
              role.role_guest_ind = 'T'
              AND COALESCE(operationRight.bus_op_guest_rt, 'F') = 'F'
            )
        ) AS permQuery
        WHERE grantedAuthority IS NOT NULL
        """;

    private final JdbcTemplate jdbcTemplate;

    public GrantedAuthorityFinder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Finds granted authorities for the given user_id.
     *
     * @param userId The user's application user_id
     * @return Set of GrantedAuthority
     */
    public Set<GrantedAuthority> find(String userId) {
        List<GrantedAuthority> authorities = jdbcTemplate.query(
                QUERY,
                ps -> {
                    ps.setString(1, userId);
                    ps.setString(2, userId);
                    ps.setString(3, userId);
                },
                (rs, rowNum) -> map(rs)
        );
        return new HashSet<>(authorities);
    }

    private GrantedAuthority map(ResultSet rs) throws SQLException {
        String authority = rs.getString("grantedAuthority");
        return new SimpleGrantedAuthority(authority);
    }
}