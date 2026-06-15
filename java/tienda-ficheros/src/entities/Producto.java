package entities;

import java.io.Serializable;
import java.util.Objects;

public class Producto implements Serializable{
	
	private int codigo;
	private String descripcion;
	private int stock;
	private double precio;
	
	private static int incremental = 1;
	
	
	public Producto(String descripcion, int stock, double precio) {
		this.codigo = incremental++;
		this.descripcion = descripcion;
		this.stock = stock;
		this.precio = precio;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}


	public int getCodigo() {
		return codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public int getStock() {
		return stock;
	}


	public double getPrecio() {
		return precio;
	}


	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return codigo == other.codigo;
	}


	@Override
	public String toString() {
		return "Producto [codigo=" + codigo + ", descripcion=" + descripcion + ", stock=" + stock + ", precio=" + precio
				+ "]";
	}
	
	
	
	

}
