package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import conexionn.*;
import app.*;
import controlador.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class VistaCotizacion extends JFrame {
    private boolean[] activos = new boolean[5];
    private JTable tabla;

    private JLabel labelUsuario = new JLabel("", JLabel.RIGHT);
    private JButton botonCerrarSesion = new JButton("Cerrar sesión");
    private JButton botonVolver = new JButton("Volver");
    private JButton botonTabla;
    private int columna;
    private int fila;
    private BotonTablaListener botonListener;
    
    public void setBotonListener(BotonTablaListener listener) {
        this.botonListener = listener;
    }
	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public JButton getBotonTabla() {
		return botonTabla;
	}

	public void setBotonTabla(JButton botonTabla) {
		this.botonTabla = botonTabla;
	}
	ImageIcon iconoBalanceOriginalBitcoin = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/Bitcoin.gif")));
    ImageIcon iconoBalanceRedimensionadoBitcoin = new ImageIcon(iconoBalanceOriginalBitcoin.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
    
    ImageIcon iconoBalanceOriginalEthereum = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/Ethereum.gif")));
    ImageIcon iconoBalanceRedimensionadoEthereum = new ImageIcon(iconoBalanceOriginalEthereum.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    ImageIcon iconoBalanceOriginalUSDC = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/Usdc.gif")));
    ImageIcon iconoBalanceRedimensionadoUSDC = new ImageIcon(iconoBalanceOriginalUSDC.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    ImageIcon iconoBalanceOriginalThether = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/Tether.gif")));
    ImageIcon iconoBalanceRedimensionadoThether = new ImageIcon(iconoBalanceOriginalThether.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    ImageIcon iconoBalanceOriginalDogecoin = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/Dogecoin.gif")));
    ImageIcon iconoBalanceRedimensionadoDogecoin = new ImageIcon(iconoBalanceOriginalDogecoin.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    String[] columnas = {"", "Cripto", "Precio de Compra", "Comprar", "Swap"};
    Object[][] datos = {
            {iconoBalanceRedimensionadoBitcoin, "Bitcoin (BTC)", "$0", "Comprar", ""},
            {iconoBalanceRedimensionadoEthereum, "Ethereum (ETH)", "$0", "Comprar", ""},
            {iconoBalanceRedimensionadoUSDC, "USDC (USDC)", "$0", "Comprar", ""},
            {iconoBalanceRedimensionadoThether, "Tether (USDT)", "$0", "Comprar", ""},
            {iconoBalanceRedimensionadoDogecoin, "Dogecoin (DOGE)", "$0", "Comprar", ""}
    };

    DefaultTableModel modeloTabla = new DefaultTableModel(datos, columnas) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 3 || column == 4;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Icon.class : Object.class;
        }
    };

    public VistaCotizacion() {
        setTitle("Billetera Virtual - Cotizaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(50);

        tabla.getColumn("").setCellRenderer(new ImageRenderer());
        tabla.getColumn("Comprar").setCellRenderer(new BotonRenderer("Comprar"));
        tabla.getColumn("Comprar").setCellEditor(new BotonEditor(tabla, "Comprar"));
        tabla.getColumn("Swap").setCellRenderer(new BotonRenderer("Swap"));
        tabla.getColumn("Swap").setCellEditor(new BotonEditor(tabla, "Swap"));

        ajustarColumnas();

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(labelUsuario, BorderLayout.WEST);
        panelSuperior.add(botonCerrarSesion, BorderLayout.EAST);

        JPanel panelInferior = new JPanel();
        panelInferior.add(botonVolver);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        actualizarBotones();
    }

    private void ajustarColumnas() {
        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
    }

    public void actualizarBotones() {
        for (int i = 0; i < activos.length; i++) {
            if (activos[i]) {
                modeloTabla.setValueAt("Swap", i, 4);
            } else {
                modeloTabla.setValueAt("", i, 4); 
            }
        }
    }

    public void actualizarTabla(Map<String, Double> precios) {
        if (precios.get("BTC") != null) modeloTabla.setValueAt("$" + String.format("%.2f", precios.get("BTC")), 0, 2);
        if (precios.get("ETH") != null) modeloTabla.setValueAt("$" + String.format("%.2f", precios.get("ETH")), 1, 2);
        if (precios.get("USDC") != null) modeloTabla.setValueAt("$" + String.format("%.2f", precios.get("USDC")), 2, 2);
        if (precios.get("USDT") != null) modeloTabla.setValueAt("$" + String.format("%.2f", precios.get("USDT")), 3, 2);
        if (precios.get("DOGE") != null) modeloTabla.setValueAt("$" + String.format("%.2f", precios.get("DOGE")), 4, 2);
    }

    public void setLabelUsuario(String usuario) {
        labelUsuario.setText(usuario);
    }

    public JButton getBotonCerrarSesion() {
        return botonCerrarSesion;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    public void setActivos(boolean[] activos) {
        this.activos = activos;
        actualizarBotones();
    }

    class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(value instanceof Icon ? (Icon) value : null);
            return this;
        }
    }

    class BotonRenderer extends JButton implements TableCellRenderer {
        private String texto;

        public BotonRenderer(String texto) {
            this.texto = texto;
            setFont(new Font("Arial", Font.BOLD, 14));
            setFocusPainted(false);
            setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(texto.equals("Swap") && (activos != null && !activos[row]) ? "" : texto);
            return this;
        }
    }

    class BotonEditor extends DefaultCellEditor {
        private JButton boton;
        private String texto;

        public BotonEditor(JTable table, String texto) {
            super(new JTextField());
            this.texto = texto;
            boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.BOLD, 14));
            boton.setFocusPainted(false);
            boton.setBackground(Color.LIGHT_GRAY);

            boton.addActionListener(e -> {
                int filaActual = table.getEditingRow();
                int columnaActual = table.getEditingColumn();
                String precio = (String) table.getValueAt(filaActual, 2);
                String [] precios = new String [5];
                for (int i = 0 ; i<5; i++) {
                	precios[i] = (String) table.getValueAt(i, 2);
                }
                if (botonListener != null) {
                    try {
						botonListener.botonPresionado(filaActual, columnaActual, texto, precio, precios);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            boton.setText(texto);
            boton.setEnabled(!texto.equals("Swap") || (activos != null && activos[row]));
            return boton;
        }

        @Override
        public Object getCellEditorValue() {
            return texto;
        }
    }
}

