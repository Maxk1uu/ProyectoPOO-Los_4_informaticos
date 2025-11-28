package vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUICrearCultivoMsg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public GUICrearCultivoMsg() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        // add your code here
        dispose();
    }
    public void errorMessageEmpty() {
        JOptionPane.showMessageDialog(this, "Existen datos incorrectos o faltantes", "", JOptionPane.ERROR_MESSAGE);
    }
    public void errorMessageInvalid() {
        JOptionPane.showMessageDialog(this, "Existen datos negativos o con un formato incorrecto", "", JOptionPane.ERROR_MESSAGE);
    }
    public void cultivoAlreadyExists(String message) {
        JOptionPane.showMessageDialog(this, message, "", JOptionPane.ERROR_MESSAGE);
    }
    public void successMessage() {
        JOptionPane.showMessageDialog(this, "Cultivo creado con exito", "", JOptionPane.INFORMATION_MESSAGE);
    }
}
