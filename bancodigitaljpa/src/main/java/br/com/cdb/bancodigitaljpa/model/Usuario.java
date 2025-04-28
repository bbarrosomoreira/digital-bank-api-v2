package br.com.cdb.bancodigitaljpa.model;

import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.cdb.bancodigitaljpa.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String email;
	private String senha;
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
	}
	@Override
	public String getPassword() {
		return this.senha;
	}
	@Override
	public String getUsername() {
		return this.email;
	}
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}
	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}
	@Override
	public boolean isEnabled() {
	    return true;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario() {}
}
