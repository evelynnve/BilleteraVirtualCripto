package modelo;

public class Transacciones {
	private int ID;
	private String resumen;
	private String fecha_hora;
	private int idUsuario;
	
	
	public Transacciones(int iD, String resumen, String fecha_hora, int idUsuario) {
		super();
		ID = iD;
		this.resumen = resumen;
		this.fecha_hora = fecha_hora;
		this.idUsuario = idUsuario;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getResumen() {
		return resumen;
	}
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	public String getFecha_hora() {
		return fecha_hora;
	}
	public void setFecha_hora(String fecha_hora) {
		this.fecha_hora = fecha_hora;
	}
	public int getID_USUARIO() {
		return idUsuario;
	}
	public void setID_USUARIO(int iD_USUARIO) {
		idUsuario = iD_USUARIO;
	}

}
