package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6)
	private String senha;
	
	@NotNull
	private Role role;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public UsuarioDTO() {}
	
	

}
