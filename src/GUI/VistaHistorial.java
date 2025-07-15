package GUI;

import java.awt.*;
import javax.swing.*;

public class VistaHistorial extends JFrame {
    private JList lista = new JList();
    private DefaultListModel modelo = new DefaultListModel();
    private JButton BotonVolver = new JButton("Volver");

    public JButton getBotonVolver() {
        return BotonVolver;
    }

    public void setBotonVolver(JButton BotonVolver) {
        this.BotonVolver = BotonVolver;
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

    public VistaHistorial() {
        setTitle("Bienvenido a la billetera virtual");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; 

        JScrollPane scrollPane = new JScrollPane(lista);
        lista.setModel(modelo);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.9; 
        add(scrollPane, gbc); 

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        add(BotonVolver, gbc);
    }
}
