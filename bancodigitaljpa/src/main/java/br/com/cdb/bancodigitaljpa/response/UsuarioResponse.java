package br.com.cdb.bancodigitaljpa.response;

import br.com.cdb.bancodigitaljpa.entity.Usuario;
import br.com.cdb.bancodigitaljpa.enums.Role;

public class UsuarioResponse {
    private String email;
    private Role role;

    public UsuarioResponse(Usuario usuario) {
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
    }
    public UsuarioResponse() {}
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

    // Getters e Setters (ou use Lombok @Getter/@Setter)
}
