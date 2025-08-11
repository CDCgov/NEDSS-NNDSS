package gov.cdc.nnddataexchangeservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GrantedAuthorityFinderTest {

    @Test
    void testFindAuthorities() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        List<GrantedAuthority> mockResult = Arrays.asList(
                new SimpleGrantedAuthority("ADMINISTRATE-SYSTEM"),
                new SimpleGrantedAuthority("ADD-PATIENT"),
                new SimpleGrantedAuthority("CREATE-NOTIFICATION"),
                new SimpleGrantedAuthority("ADD-INVESTIGATION")
        );

        when(jdbcTemplate.query(
                anyString(),
                any(PreparedStatementSetter.class),
                any(RowMapper.class))
        ).thenReturn(mockResult);

        GrantedAuthorityFinder finder = new GrantedAuthorityFinder(jdbcTemplate);
        Set<GrantedAuthority> authorities = finder.find("test-user-name");

        assertEquals(new HashSet<>(mockResult), authorities);
        verify(jdbcTemplate, times(1))
                .query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class));
    }

    @Test
    void testFindAuthorities_noAuthorities() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        when(jdbcTemplate.query(
                anyString(),
                any(PreparedStatementSetter.class),
                any(RowMapper.class))
        ).thenReturn(Collections.emptyList());

        GrantedAuthorityFinder finder = new GrantedAuthorityFinder(jdbcTemplate);
        Set<GrantedAuthority> authorities = finder.find("no-role-user");

        assertEquals(Collections.emptySet(), authorities);
        verify(jdbcTemplate, times(1))
                .query(anyString(), any(PreparedStatementSetter.class), any(RowMapper.class));
    }
}
