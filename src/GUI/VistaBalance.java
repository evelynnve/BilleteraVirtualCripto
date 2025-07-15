package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import modelo.Moneda;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VistaBalance extends JFrame {
	private JLabel etiquetaNombreUsuario = new JLabel("", JLabel.CENTER);
    private JButton botonCerrarSesion = new JButton("Cerrar sesión");
    private JButton botonGenerarDatos = new JButton("Generar Datos de Prueba");
    private JLabel etiquetaBalance = new JLabel("Balance (USD): ", JLabel.CENTER);
    private JTable tabla = new JTable();
    private DefaultTableModel balanceModel = new DefaultTableModel(new Object[]{" ","Cripto", "Monto"}, 0); // Dejamos el monto en moneda origen (es decir, la cantidad)
    private JButton botonExportarCSV = new JButton("Exportar como CSV");
    private JButton botonOperaciones = new JButton("Mis Operaciones");
    private JButton botonCotizaciones = new JButton("Cotizaciones");

	public JTable getTabla() {
		return tabla;
	}
	public void setTabla(JTable tabla) {
		this.tabla = tabla;
	}
	public JLabel getEtiquetaBalance() {
		return etiquetaBalance;
	}
	public void setEtiquetaBalance(double balance) {
		
		ImageIcon iconoBalanceOriginal = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/balance_icon.png")));
        ImageIcon iconoBalanceRedimensionado = new ImageIcon(iconoBalanceOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
		etiquetaBalance.setText("Balance (USD): " + String.format("%.2f", balance));
		etiquetaBalance.setIcon(iconoBalanceRedimensionado);
	}
	public JButton getBotonCerrarSesion() {
		return botonCerrarSesion;
	}
	public void setBotonCerrarSesion(JButton botonCerrarSesion) {
		this.botonCerrarSesion = botonCerrarSesion;
	}
	public JButton getBotonGenerarDatos() {
		return botonGenerarDatos;
	}
	public void setBotonGenerarDatos(JButton botonGenerarDatos) {
		this.botonGenerarDatos = botonGenerarDatos;
	}
	public JButton getBotonExportarCSV() {
		return botonExportarCSV;
	}
	public void setBotonExportarCSV(JButton botonExportarCSV) {
		this.botonExportarCSV = botonExportarCSV;
	}
	public JButton getBotonOperaciones() {
		return botonOperaciones;
	}
	public void setBotonOperaciones(JButton botonOperaciones) {
		this.botonOperaciones = botonOperaciones;
	}
	public JButton getBotonCotizaciones() {
		return botonCotizaciones;
	}
	public void setBotonCotizaciones(JButton botonCotizaciones) {
		this.botonCotizaciones = botonCotizaciones;
	}
	public VistaBalance() {
        setTitle("Billetera Virtual - Mis Activos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    	
        balanceModel = new DefaultTableModel() {
        	@Override
        	public Class<?> getColumnClass(int columnIndex) {
            	if (columnIndex == 0) {
                	return Icon.class;
            	}
            	return super.getColumnClass(columnIndex);
        	}
        };
        tabla = new JTable(balanceModel);
        tabla.setDefaultEditor(Object.class, null);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(balanceModel);
        tabla.setRowSorter(sorter);

        ImageIcon iconoMisOperaciones = new ImageIcon(getClass().getResource("/imagenes/exchange.png"));
        ImageIcon iconoCotizaciones = new ImageIcon(getClass().getResource("/imagenes/cotizaciones.png"));
        Image iconoMisOperacionesEscalado = iconoMisOperaciones.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image iconoCotizacionesEscalado = iconoCotizaciones.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        botonOperaciones.setIcon(new ImageIcon(iconoMisOperacionesEscalado));
        botonCotizaciones.setIcon(new ImageIcon(iconoCotizacionesEscalado));

        JScrollPane tablaScroll = new JScrollPane(tabla);
        
        balanceModel.addColumn("");
        balanceModel.addColumn("Nombre");
        balanceModel.addColumn("Monto");

        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEncabezado.setBackground(new Color(240, 240, 240));
        panelEncabezado.add(etiquetaNombreUsuario);
        panelEncabezado.add(botonCerrarSesion);
        panelEncabezado.add(botonGenerarDatos);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelEncabezado, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(etiquetaBalance, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(tablaScroll, gbc);

        JPanel panelBotonExportar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonExportar.add(botonExportarCSV);

        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelBotonExportar, gbc);

        JPanel panelBotonesInferiores = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotonesInferiores.add(botonOperaciones);
        panelBotonesInferiores.add(botonCotizaciones);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelBotonesInferiores, gbc);
        tabla.setRowHeight(60);
    }
    public JLabel getCampoNombreUsuario() {
		return etiquetaNombreUsuario;
	}

	public void setCampoNombreUsuario(String campoNombreUsuario) {
		ImageIcon iconoBalanceOriginal = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/usuario.png")));
        ImageIcon iconoBalanceRedimensionado = new ImageIcon(iconoBalanceOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
	
		this.etiquetaNombreUsuario.setText(campoNombreUsuario);
		etiquetaNombreUsuario.setIcon(iconoBalanceRedimensionado);
	}
	
	public void actualizarTabla(Object[][] datos) {
        balanceModel.setRowCount(0);

        for (Object[] fila : datos) {
        	balanceModel.addRow(fila);
        }
    }
	
	public void exportarTabla (JTable tabla, String rutaArchivo) {
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Guardar como");
	    int userSelection = fileChooser.showSaveDialog(null);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File archivo = fileChooser.getSelectedFile();
	        try (FileWriter writer = new FileWriter(archivo)) {
	            TableModel model = tabla.getModel();
	            // encabezados
	            for (int i = 0; i < model.getColumnCount(); i++) {
	                writer.write(model.getColumnName(i) + (i < model.getColumnCount() - 1 ? "," : "\n"));
	            }
	            // filas
	            for (int i = 0; i < model.getRowCount(); i++) {
	                for (int j = 0; j < model.getColumnCount(); j++) {
	                    writer.write(model.getValueAt(i, j).toString() + (j < model.getColumnCount() - 1 ? "," : "\n"));
	                }
	            }
	        } catch (IOException e) {
	        }
	    }
	}
	
	public Object[][] agregarImagenes (List<Moneda> l) throws SQLException {
		List<Object[]> datos = new ArrayList<>();
		for (int i = 0; i < l.size(); i++) {	
            Object[] fila = new Object[3];
            ImageIcon icono0 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/"+l.get(i).getIcono())));
            ImageIcon icono = new ImageIcon(icono0.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            fila[0] = icono;
            fila[1] = l.get(i).getNombre();
            fila[2] = l.get(i).getStock();
            datos.add(fila);
        }
        return datos.toArray(new Object[0][]);
	}
	
	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog (this, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
	}
	
}