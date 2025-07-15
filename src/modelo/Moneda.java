package modelo;

public class Moneda {
	private int id;
	private String tipo;
	private String nombre;
	private String nomenclatura;
	private double valorDolar;
	private double volatilidad;
	private double stock;
	private String icono;
	
	
	public Moneda(int id, String tipo, String nombre, String nomenclatura, double valorDolar, double volatilidad,
			double stock, String icono) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.nombre = nombre;
		this.nomenclatura = nomenclatura;
		this.valorDolar = valorDolar;
		this.volatilidad = volatilidad;
		this.stock = stock;
		this.icono = icono;
	}
	public Moneda() {
		// TODO Auto-generated constructor stub
	}
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNomenclatura() {
		return nomenclatura;
	}
	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}
	public double getValorDolar() {
		return valorDolar;
	}
	public void setValorDolar(double valorDolar) {
		this.valorDolar = valorDolar;
	}
	public double getVolatilidad() {
		return volatilidad;
	}
	public void setVolatilidad(double volatilidad) {
		this.volatilidad = volatilidad;
	}
	public double getStock() {
		return stock;
	}
	public void setStock(double stock) {
		this.stock = stock;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	
}
