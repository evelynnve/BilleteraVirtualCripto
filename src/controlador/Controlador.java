package controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import DAO.*;
import GUI.*;
import modelo.*;
import app.*;
import excepciones.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Controlador {
    private VistaLogin vistaL;
    private VistaRegistrar vistaR;
    private VistaBalance vistaB;
    private VistaCotizacion vistaC;
    private VistaCompra vistaComp;
    private VistaHistorial vistaH;
    private VistaSwap vistaS;
    private ActivoDAO activoDAO;
    private CompraDAO compraDAO;
    private MonedaDAO monedaDAO;
    private SwapDAO swapDAO;
    private UsuarioDAO usuarioDAO;
    
    private Convertir convertirListener;
    private BotonCompra botonCompraListener;
    private VolverACotizacion volverACotizacionListener;
    private BotonSwap botonSwapListener;
    private ConvertirSwap convertirSwapListener;
    private CerrarSesionListener cerrarSesionListener;
    private GenerarDatos generarDatosListener;
    private BalanceListenerExportarTabla balanceExportarTablaListener;
    private Cotizaciones cotizacionesListener;
    private MisOperaciones misOperacionesListener;
    private RegistrarListener registrarListener;

    public Controlador(VistaLogin vistaL, VistaRegistrar vistaR, VistaBalance vistaB, VistaCotizacion vistaC,
                       VistaCompra vistaComp, VistaHistorial vistaH, VistaSwap vistaS, ActivoDAO activoDAO, CompraDAO compraDAO,
                       MonedaDAO monedaDAO, SwapDAO swapDAO, UsuarioDAO usuarioDAO) {
        super();
        this.vistaL = vistaL;
        this.vistaR = vistaR;
        this.vistaB = vistaB;
        this.vistaC = vistaC;
        this.vistaComp = vistaComp;
        this.vistaH = vistaH;
        this.vistaS = vistaS;
        this.activoDAO = activoDAO;
        this.compraDAO = compraDAO;
        this.monedaDAO = monedaDAO;
        this.swapDAO = swapDAO;
        this.usuarioDAO = usuarioDAO;
        actualizarCotizacionesPeriodicamente();
        this.vistaL.getBotonLogin().addActionListener(new LoginListener());
        this.vistaL.getBotonRegistrarse().addActionListener(new RegistrarseListener());
    }

  
    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Usuario usuario = new Usuario();
            Persona persona = new Persona();
            usuario.setEmail(vistaL.getCampo1());
            usuario.setContraseña(vistaL.getCampo2());
            try {
                if (usuario.getEmail().isEmpty() || usuario.getContraseña().isEmpty()) {
                    throw new LoginException("Los campos no pueden estar vacíos.");
                }
                boolean resultado = usuarioDAO.checkLogin(usuario.getEmail(), usuario.getContraseña());
                if (!resultado) {
                    throw new LoginException("Credenciales incorrectas.");
                } else {
                    persona.setNombre(usuarioDAO.obtenerNombre(usuario.getEmail()));
                    persona.setApellido(usuarioDAO.obtenerApellido(usuario.getEmail()));
                    vistaB.setCampoNombreUsuario(persona.getNombre() + " " + persona.getApellido());
                    double balance = monedaDAO.balance(usuario.getEmail());
                    vistaB.setEtiquetaBalance(balance);
                    try {
                        List<Moneda> list = activoDAO.obtenerDatosTabla(usuario.getEmail());
                        Object[][] datos = vistaB.agregarImagenes(list);
                        vistaB.actualizarTabla(datos);
                    } catch (SQLException ee) {
                        System.err.println("Error al cargar los datos: " + ee.getMessage());
                    }

                   
                    if (generarDatosListener != null) {
                        vistaB.getBotonGenerarDatos().removeActionListener(generarDatosListener);
                    }
                    generarDatosListener = new GenerarDatos(usuario.getEmail());
                    vistaB.getBotonGenerarDatos().addActionListener(generarDatosListener);

                    if (cerrarSesionListener != null) {
                        vistaB.getBotonCerrarSesion().removeActionListener(cerrarSesionListener);
                    }
                    cerrarSesionListener = new CerrarSesionListener();
                    vistaB.getBotonCerrarSesion().addActionListener(cerrarSesionListener);

                    if (balanceExportarTablaListener != null) {
                        vistaB.getBotonExportarCSV().removeActionListener(balanceExportarTablaListener);
                    }
                    balanceExportarTablaListener = new BalanceListenerExportarTabla();
                    vistaB.getBotonExportarCSV().addActionListener(balanceExportarTablaListener);

                    if (cotizacionesListener != null) {
                        vistaB.getBotonCotizaciones().removeActionListener(cotizacionesListener);
                    }
                    cotizacionesListener = new Cotizaciones(usuario.getEmail());
                    vistaB.getBotonCotizaciones().addActionListener(cotizacionesListener);

                    if (misOperacionesListener != null) {
                        vistaB.getBotonOperaciones().removeActionListener(misOperacionesListener);
                    }
                    misOperacionesListener = new MisOperaciones(usuario.getEmail());
                    vistaB.getBotonOperaciones().addActionListener(misOperacionesListener);

                    vistaL.setVisible(false);
                    vistaL.dispose();
                    vistaB.setVisible(true);
                }
            } catch (LoginException ex) {
                vistaL.mostrarMensajeError(ex.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    class RegistrarseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vistaL.setVisible(false);
            vistaL.dispose();
            vistaR.setVisible(true);
            vistaR.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            if (registrarListener != null) {
                vistaR.getBotonRegistrar().removeActionListener(registrarListener);
            }
            registrarListener = new RegistrarListener();
            vistaR.getBotonRegistrar().addActionListener(registrarListener);

            if (cerrarSesionListener != null) {
                vistaR.getBotonVolver().removeActionListener(cerrarSesionListener);
            }
            cerrarSesionListener = new CerrarSesionListener();
            vistaR.getBotonVolver().addActionListener(cerrarSesionListener);
        }
    }

    class RegistrarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Usuario usuario = new Usuario();
            String nombre = vistaR.getCampo1();
            String apellido = vistaR.getCampo2();
            boolean tc = vistaR.getCheckTC().isSelected();

            usuario.setEmail(vistaR.getCampo3());
            usuario.setContraseña(vistaR.getCampo4());

            Persona persona = new Persona(nombre, apellido);
            boolean resultado;
            try {
                if (!tc) {
                    vistaR.mostrarMensajeError("No aceptaste los terminos y condiciones");
                } else {
                    if (((nombre.length() == 0) || (apellido.length() == 0) || (usuario.getEmail().length() == 0) || (usuario.getContraseña().length() == 0)))
                        vistaR.mostrarMensajeError("Debe completar todos los datos");
                    else {
                        resultado = usuarioDAO.registrar(persona, usuario.getEmail(), usuario.getContraseña(), tc);
                        if (!resultado) {
                            vistaR.mostrarMensajeError("el email ya esta registrado");
                        } else {
                            vistaR.setVisible(false);
                            vistaR.dispose();
                            vistaL.setVisible(true);
                        }
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class BalanceListenerExportarTabla implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vistaB.exportarTabla(vistaB.getTabla(), "C:\\Users\\usuario\\Documents\\tabla.csv");
        }
    }

    class CerrarSesionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vistaB.setVisible(false);
            vistaB.dispose();
            vistaC.setVisible(false);
            vistaC.dispose();
            vistaR.setVisible(false);
            vistaR.dispose();
            vistaL.setVisible(true);
        }
    }

    class GenerarDatos implements ActionListener {
        private String email;
        public GenerarDatos(String email) {
            this.email = email;
        }
        public void actionPerformed(ActionEvent e) {
            try {
                if (!activoDAO.tablaBDVacia(email)) {
                    throw new MonedaException("No se pueden generar monedas existentes.");
                } else
                    monedaDAO.generarMonedas();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (MonedaException e1) {
                vistaB.mostrarMensajeError(e1.getMessage());
            }

            try {
                activoDAO.cargarActivos(email);
                List<Moneda> list = activoDAO.obtenerDatosTabla(email);
                Object[][] datos = vistaB.agregarImagenes(list);
                vistaB.actualizarTabla(datos);
                double balance = monedaDAO.balance(email);
                vistaB.setEtiquetaBalance(balance);
            } catch (SQLException ee) {
                System.err.println("Error al cargar los datos: " + ee.getMessage());
            }
        }
    }

    class Cotizaciones implements ActionListener {
        private String email;

        public Cotizaciones(String email) {
            this.email = email;
        }

        public void actionPerformed(ActionEvent e) {
            Map<String, Double> precios = activoDAO.obtenerPreciosActuales();
            SwingUtilities.invokeLater(() -> vistaC.actualizarTabla(precios));
            vistaB.setVisible(false);
            vistaB.dispose();
            try {
                String nombreApellido = usuarioDAO.obtenerNombre(this.email) + " " + usuarioDAO.obtenerApellido(this.email);
                vistaC.setLabelUsuario(nombreApellido);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                boolean[] activos = activoDAO.checkActivos(email);
                vistaC.setActivos(activos);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if (cerrarSesionListener != null) {
                vistaC.getBotonCerrarSesion().removeActionListener(cerrarSesionListener);
            }
            cerrarSesionListener = new CerrarSesionListener();
            vistaC.getBotonCerrarSesion().addActionListener(cerrarSesionListener);

            if (volverACotizacionListener != null) {
                vistaC.getBotonVolver().removeActionListener(volverACotizacionListener);
            }
            volverACotizacionListener = new VolverACotizacion();
           
            vistaC.getBotonVolver().addActionListener(new VolverABalance());

            vistaC.setBotonListener(new verAccionTabla(email));
            vistaC.setVisible(true);
        }
    }

    class verAccionTabla implements BotonTablaListener {
        private String email;

        public verAccionTabla(String email) {
            this.email = email;
        }

        public void botonPresionado(int fila, int columna, String accion, String precio, String[] precios) throws SQLException {
            vistaC.setVisible(false);
            vistaC.dispose();
            Moneda moneda = new Moneda();

            moneda.setValorDolar(Double.parseDouble(precio.replace("$", "").replace(",", ".")));
            int opcion = fila;
            switch (opcion) {
                case 0:
                    moneda.setNomenclatura("BTC");
                    break;
                case 1:
                    moneda.setNomenclatura("ETH");
                    break;
                case 2:
                    moneda.setNomenclatura("USDC");
                    break;
                case 3:
                    moneda.setNomenclatura("USDT");
                    break;
                case 4:
                    moneda.setNomenclatura("DOGE");
                    break;
            }

            vistaComp.setCampoMonto(" ");
            vistaComp.setEtiquetaPrecio(precio);
            String stock = compraDAO.devolverStock(moneda.getNomenclatura());
            vistaComp.setEtiquetaStock(stock);

            vistaS.setCampoMonto("");
            vistaS.setEtiquetaPrecio(precio);
            String stockS = compraDAO.devolverStock(moneda.getNomenclatura());
            vistaS.setEtiquetaStock(stockS);

            if (columna == 3 && accion.equals("Comprar")) {
                if (convertirListener != null) {
                    vistaComp.getBotonConvertir().removeActionListener(convertirListener);
                }
                convertirListener = new Convertir(moneda.getNomenclatura(), moneda.getValorDolar());
                vistaComp.getBotonConvertir().addActionListener(convertirListener);

                if (botonCompraListener != null) {
                    vistaComp.getBotonComprar().removeActionListener(botonCompraListener);
                }
                botonCompraListener = new BotonCompra(moneda.getNomenclatura(), email, moneda.getValorDolar());
                vistaComp.getBotonComprar().addActionListener(botonCompraListener);

                if (volverACotizacionListener != null) {
                    vistaComp.getBotonCancelar().removeActionListener(volverACotizacionListener);
                }
                volverACotizacionListener = new VolverACotizacion();
                vistaComp.getBotonCancelar().addActionListener(volverACotizacionListener);

                vistaComp.setVisible(true);
            } else if (columna == 4 && accion.equals("Swap")) {
                if (convertirSwapListener != null) {
                    vistaS.getBotonConvertir().removeActionListener(convertirSwapListener);
                }
                convertirSwapListener = new ConvertirSwap(moneda.getNomenclatura(), moneda.getValorDolar(), precios);
                vistaS.getBotonConvertir().addActionListener(convertirSwapListener);

                if (botonSwapListener != null) {
                    vistaS.getBotonSwap().removeActionListener(botonSwapListener);
                }
                botonSwapListener = new BotonSwap(moneda.getNomenclatura(), email, moneda.getValorDolar(), precios);
                vistaS.getBotonSwap().addActionListener(botonSwapListener);

                if (volverACotizacionListener != null) {
                    vistaS.getBotonCancelar().removeActionListener(volverACotizacionListener);
                }
                volverACotizacionListener = new VolverACotizacion();
                vistaS.getBotonCancelar().addActionListener(volverACotizacionListener);

                vistaS.setVisible(true);
            }
        }
    }

    class VolverACotizacion implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vistaComp.setVisible(false);
            vistaComp.setEtiquetaEquivalencia(0, "");
            vistaComp.dispose();
            vistaS.setVisible(false);
            vistaS.setEtiquetaEquivalencia(0, "");
            vistaS.dispose();
            vistaC.setVisible(true);
        }
    }

    class BotonCompra implements ActionListener {
        private double valorCripto;
        private String nomenclatura;
        private String email;

        public BotonCompra(String nomenclatura, String email, double valorCripto) {
            this.valorCripto = valorCripto;
            this.nomenclatura = nomenclatura;
            this.email = email;
        }

        public void actionPerformed(ActionEvent e) {
            String seleccion = vistaComp.getSeleccion();
            double cantidad = vistaComp.getCampoMonto();
            try {
                double cantidadComprada = compraDAO.simularCompra(nomenclatura, seleccion, cantidad, email, valorCripto);
                if (cantidadComprada <= 0) {
                    throw new TransaccionesException("No posee suficiente " + seleccion);
                } else {
                	try {
                        List<Moneda> list = activoDAO.obtenerDatosTabla(email);
                        Object[][] datos = vistaB.agregarImagenes(list);
                        vistaB.actualizarTabla(datos);
                    } catch (SQLException ee) {
                        System.err.println("Error al cargar los datos: " + ee.getMessage());
                    }
                    double balance = monedaDAO.balance(email);
                    vistaB.setEtiquetaBalance(balance);
                    vistaComp.mostrarMensajeExito(null);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (TransaccionesException e2) {
                vistaComp.mostrarMensajeError(e2.getMessage());
            }
        }
    }

    class BotonSwap implements ActionListener {
        private double valorCripto;
        private String nomenclatura;
        private String email;
        private String[] precios;

        public BotonSwap(String nomenclatura, String email, double valorCripto, String[] precios) {
            this.valorCripto = valorCripto;
            this.nomenclatura = nomenclatura;
            this.email = email;
            this.precios = precios;
        }

        public void actionPerformed(ActionEvent e) {
            String seleccion = vistaS.getSeleccion();
            double cantidad = vistaS.getCampoMonto();
            int i = 0;
            switch (seleccion) {
                case "BTC":
                    i = 0;
                    break;
                case "ETH":
                    i = 1;
                    break;
                case "USDC":
                    i = 2;
                    break;
                case "USDT":
                    i = 3;
                    break;
                case "DOGE":
                    i = 4;
                    break;
            }
            String precioOrigenString = precios[i];
            double valorCriptoOrigen = Double.parseDouble(precioOrigenString.replace("$", "").replace(",", "."));
            try {
                double cantidadSwap = swapDAO.simularSwap(nomenclatura, seleccion, cantidad, email, valorCripto, valorCriptoOrigen);
                if (cantidadSwap <= 0) {
                    throw new TransaccionesException("No posee suficiente " + seleccion);
                } else {
                	try {
                        List<Moneda> list = activoDAO.obtenerDatosTabla(email);
                        Object[][] datos = vistaB.agregarImagenes(list);
                        vistaB.actualizarTabla(datos);
                    } catch (SQLException ee) {
                        System.err.println("Error al cargar los datos: " + ee.getMessage());
                    }
                    double balance = monedaDAO.balance(email);
                    vistaB.setEtiquetaBalance(balance);
                    vistaS.mostrarMensajeExito(null);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (TransaccionesException e2) {
                vistaS.mostrarMensajeError(e2.getMessage());
            }
        }
    }

    class Convertir implements ActionListener {
        private String nomenclatura;
        private double valorCripto;

        public Convertir(String nomenclatura, double valorCripto) {
            this.nomenclatura = nomenclatura;
            this.valorCripto = valorCripto;
        }

        public void actionPerformed(ActionEvent e) {
            String seleccion = vistaComp.getSeleccion();
            double cantidad = vistaComp.getCampoMonto();
            double equivalencia = 0;
            try {
                equivalencia = compraDAO.equivalencias(nomenclatura, seleccion, cantidad, valorCripto);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            vistaComp.setEtiquetaEquivalencia(equivalencia, nomenclatura);
        }
    }

    class ConvertirSwap implements ActionListener {
        private String nomenclatura;
        private double valorCripto;
        private String[] precios;

        public ConvertirSwap(String nomenclatura, double valorCripto, String[] precios) {
            this.nomenclatura = nomenclatura;
            this.valorCripto = valorCripto;
            this.precios = precios;
        }

        public void actionPerformed(ActionEvent e) {
            String seleccion = vistaS.getSeleccion();
            double cantidad = vistaS.getCampoMonto();
            double equivalencia = 0;
            int i = 0;
            switch (seleccion) {
                case "BTC":
                    i = 0;
                    break;
                case "ETH":
                    i = 1;
                    break;
                case "USDC":
                    i = 2;
                    break;
                case "USDT":
                    i = 3;
                    break;
                case "DOGE":
                    i = 4;
                    break;
            }
            String precioOrigenString = precios[i];
            double valorCriptoOrigen = Double.parseDouble(precioOrigenString.replace("$", "").replace(",", "."));
            try {
                equivalencia = swapDAO.equivalencias(nomenclatura, seleccion, cantidad, valorCripto, valorCriptoOrigen);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            vistaS.setEtiquetaEquivalencia(equivalencia, nomenclatura);
        }
    }

    private void actualizarCotizacionesPeriodicamente() {
        Timer timer = new Timer(5000, e -> {
            Map<String, Double> precios = activoDAO.obtenerPreciosActuales();
            SwingUtilities.invokeLater(() -> vistaC.actualizarTabla(precios));
        });
        timer.start();
    }

    class VolverABalance implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vistaC.setVisible(false);
            vistaC.dispose();
            vistaH.setVisible(false);
            vistaH.dispose();
            vistaB.setVisible(true);
        }
    }

    class MisOperaciones implements ActionListener {
        private String email;

        public MisOperaciones(String email) {
            this.email = email;
        }

        public void actionPerformed(ActionEvent e) {
            vistaB.setVisible(false);
            vistaB.dispose();
            try {
                String[] operaciones = activoDAO.misOperaciones(email);
                System.out.println("CHECK EMAIL: " + email);
                vistaH.getModelo().clear();
                for (String operacion : operaciones) {
                    vistaH.getModelo().addElement(operacion);
                }
                vistaH.getLista().setModel(vistaH.getModelo());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            vistaH.getBotonVolver().addActionListener(new VolverABalance());
            vistaH.setVisible(true);
        }
    }
}

	