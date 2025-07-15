package GUI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VistaSwap extends JFrame {

	    private JLabel etiquetaStock = new JLabel("Stock disponible: ");
	    private JLabel etiquetaPrecio = new JLabel("Precio de Compra: ");
	    private JLabel etiquetaEquivalencia = new JLabel("Equivale a..... ");
	    private JTextField campoMonto = new JTextField(10);
	    private JList lista = new JList();
	    private DefaultListModel modelo = new DefaultListModel();
	    private JButton botonConvertir = new JButton("Convertir");
	    private JButton botonSwap = new JButton("Realizar Swap");
	    private JButton botonCancelar = new JButton("Volver");
	    private String seleccion;

	    public String getSeleccion() {
			return seleccion;
		}

		public void setSeleccion(String seleccion) {
			this.seleccion = seleccion;
		}

		public JLabel getEtiquetaStock() {
			return etiquetaStock;
		}

		public void setEtiquetaStock(String etiquetaStock) {
			this.etiquetaStock.setText("Stock disponible: " + etiquetaStock);;
		}

		public JLabel getEtiquetaPrecio() {
			return etiquetaPrecio;
		}

		public void setEtiquetaPrecio(String etiquetaPrecio) {
			this.etiquetaPrecio.setText("Precio de Compra: " + etiquetaPrecio);
		}

		public JLabel getEtiquetaEquivalencia() {
			return etiquetaEquivalencia;
		}

		public void setEtiquetaEquivalencia(double etiquetaEquivalencia, String nomenclatura) {
			this.etiquetaEquivalencia.setText("Equivale a..... " + etiquetaEquivalencia + " " + nomenclatura);
		}

		public double getCampoMonto() {
			return Double.parseDouble(campoMonto.getText());
		}

		public void setCampoMonto(String campoMonto) {
			this.campoMonto.setText(campoMonto);
		}

		public JList getLista() {
			return lista;
		}

		public void setLista(JList lista) {
			this.lista = lista;
		}

		public DefaultListModel getModelo() {
			return modelo;
		}

		public void setModelo(DefaultListModel modelo) {
			this.modelo = modelo;
		}

		public JButton getBotonConvertir() {
			return botonConvertir;
		}

		public void setBotonConvertir(JButton botonConvertir) {
			this.botonConvertir = botonConvertir;
		}

		public JButton getBotonSwap() {
			return botonSwap;
		}

		public void setBotonSwap(JButton botonSwap) {
			this.botonSwap = botonSwap;
		}

		public JButton getBotonCancelar() {
			return botonCancelar;
		}


		public void setBotonCancelar(JButton botonCancelar) {
			this.botonCancelar = botonCancelar;
		}


		public VistaSwap() {
	        setTitle("Billetera Virtual - Compra");
	        setSize(600, 400);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(10, 10, 10, 10);
	        String[] items = { "BTC","ETH", "USDC", "USDT", "DOGE"};
	        for (String item : items) {
	        modelo.addElement(item);
	        }
	        lista.setModel(modelo);
	        lista.setVisibleRowCount(4);
	        this.add(new JScrollPane(lista));
	        lista.addListSelectionListener(new ListSelectionListener() {
	            @Override
	            public void valueChanged(ListSelectionEvent e) {
	                if (!e.getValueIsAdjusting()) {
	                    String opcionLista = (String) lista.getSelectedValue(); 
	                    setSeleccion(opcionLista);
	                }
	            }
	        });
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.gridwidth = 2;
	        add(etiquetaStock, gbc);

	        gbc.gridy = 1;
	        add(etiquetaPrecio, gbc);

	        gbc.gridy = 2;
	        gbc.gridwidth = 1;
	        add(new JLabel("Realizar SWAP con: "), gbc);

	        gbc.gridx = 1;
	        add(campoMonto, gbc);

	        gbc.gridx = 2;
	        add(lista, gbc);

	        gbc.gridx = 3;
	        add(botonConvertir, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 3;
	        gbc.gridwidth = 4;
	        add(etiquetaEquivalencia, gbc);

	        JPanel panelBotones = new JPanel(new FlowLayout());
	        panelBotones.add(botonSwap);
	        panelBotones.add(botonCancelar);

	        gbc.gridy = 4;
	        gbc.gridwidth = 4;
	        add(panelBotones, gbc);
	    }
		public void mostrarMensajeError(String mensaje) {
			JOptionPane.showMessageDialog (this, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		public void mostrarMensajeExito(String mensaje) {
			JOptionPane.showMessageDialog(null, "La operación se realizó con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		}
}
