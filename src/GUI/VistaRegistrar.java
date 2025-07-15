package GUI;

import javax.swing.*;
import java.awt.*;

public class VistaRegistrar extends JFrame {
private JTextField campo1 = new JTextField(50); 
private JTextField campo2 = new JTextField(20);
private JTextField campo3 = new JTextField(50); 
private JTextField campo4 = new JTextField(20);
private JButton botonVolver = new JButton ("Cancelar");
private JButton botonRegistrar = new JButton("Registrarse");
private JCheckBox checkTC = new JCheckBox ("Aceptar Terminos y condiciones");

public VistaRegistrar() {
setTitle("Registro");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 3));
        
        add(new JLabel("   Nombre: ")); add(campo1); add(new JLabel(""));
        add(new JLabel("   Apellido: ")); add(campo2); add(new JLabel(""));
        add(new JLabel("   Email: ")); add(campo3); add(new JLabel(""));
        add(new JLabel("   password: ")); add(campo4); add(new JLabel(""));
        add(new JLabel ("")); add(checkTC); add(new JLabel (""));
        add(new JLabel("")); add(botonRegistrar); add(new JLabel(""));
        add(new JLabel("")); add (botonVolver); add(new JLabel (""));
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

public String getCampo3() {
return campo3.getText();
}

public void setCampo3(JTextField campo3) {
this.campo3 = campo3;
}
    
public String getCampo4() {
return campo4.getText();
}

public void setCampo4(JTextField campo4) {
this.campo4 = campo4;
}

public JButton getBotonRegistrar() {
return botonRegistrar;
}

public void setBotonRegistrar(JButton botonRegistrar) {
this.botonRegistrar = botonRegistrar;
}

public JCheckBox getCheckTC() {
	return checkTC;
}

public void setCheckTC (JCheckBox checkTC) {
	this.checkTC = checkTC;
}

public void mostrarMensajeError(String mensaje) {
JOptionPane.showMessageDialog (this, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
}

public JButton getBotonVolver() {
	return botonVolver;
}

public void setBotonVolver(JButton botonVolver) {
	this.botonVolver = botonVolver;
}


}