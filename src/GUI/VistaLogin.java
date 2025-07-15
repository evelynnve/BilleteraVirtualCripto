package GUI;

import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {
	private JTextField campo1 = new JTextField(50); 
	private JTextField campo2 = new JTextField(20);
	private JButton botonLogin = new JButton("Login");
	private JButton botonRegistrarse = new JButton("Crear una cuenta");
	
	public VistaLogin() {
		setTitle("Bienvenido a la billetera virtual");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 3));
        
        add(new JLabel("   E-Mail: ")); add(campo1); add(new JLabel(""));
        add(new JLabel("   Password: ")); add(campo2); add(new JLabel(""));
        add(new JLabel("")); add (botonLogin); add(new JLabel(""));
        add(new JLabel("")); add(new JLabel("")); add(new JLabel(""));
        add(new JLabel("  ¿Aún no estas registrado?")); add (botonRegistrarse); add(new JLabel (""));
	}

	public String getCampo1() {
		return campo1.getText();
	}

	public void setCampo1(JTextField campo1) {
		this.campo1 = campo1;
	}

	public String getCampo2() {
		return campo2.getText();
	}

	public void setCampo2(JTextField campo2) {
		this.campo2 = campo2;
	}

	public JButton getBotonLogin() {
		return botonLogin;
	}

	public void setBotonLogin(JButton botonLogin) {
		this.botonLogin = botonLogin;
	}

	public JButton getBotonRegistrarse() {
		return botonRegistrarse;
	}

	public void setBotonRegistrarse(JButton botonRegistrarse) {
		this.botonRegistrarse = botonRegistrarse;
	}
	
	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog (this, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
	}
}
