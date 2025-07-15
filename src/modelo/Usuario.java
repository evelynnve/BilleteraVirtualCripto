package modelo;

public class Usuario {
	private int id;
	private int idPersona;
	private String email;
	private String contraseña;
	private boolean aceptarTerminos;
	public Usuario(int id, int idPersona, String email, String contraseña, boolean aceptarTerminos) {
		super();
		this.id = id;
		this.idPersona = idPersona;
		this.email = email;
		this.contraseña = contraseña;
		this.aceptarTerminos = aceptarTerminos;
	}
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public boolean isAceptarTerminos() {
		return aceptarTerminos;
	}
	public void setAceptarTerminos(boolean aceptarTerminos) {
		this.aceptarTerminos = aceptarTerminos;
	}
	
}
