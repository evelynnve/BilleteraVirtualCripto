package modelo;

public class Activo {
	private int id;
	private int ID_MONEDA;
	private int ID_USUARIO;
	private double cantidad;
	
	public Activo(int id, int iD_MONEDA, int iD_USUARIO, double cantidad) {
		super();
		this.id = id;
		ID_MONEDA = iD_MONEDA;
		ID_USUARIO = iD_USUARIO;
		this.cantidad = cantidad;
	}
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public int getID_MONEDA() {
		return ID_MONEDA;
	}
	public void setID_MONEDA(int iD_MONEDA) {
		ID_MONEDA = iD_MONEDA;
	}
	public int getID_USUARIO() {
		return ID_USUARIO;
	}
	public void setID_USUARIO(int iD_USUARIO) {
		ID_USUARIO = iD_USUARIO;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	
}
