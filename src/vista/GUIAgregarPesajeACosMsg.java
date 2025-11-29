package vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIAgregarPesajeACosMsg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public GUIAgregarPesajeACosMsg() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }
    public void errorMessageEmpty(){
        JOptionPane.showMessageDialog(this, "Existen datos incorrectos, faltantes o no seleccionados.", "", JOptionPane.ERROR_MESSAGE);
    }
    public void successMessage(){
        JOptionPane.showMessageDialog(this, "Pesaje registrado con exito", "", JOptionPane.INFORMATION_MESSAGE);
    }
    public void errorMessageInvalid(String error){
        JOptionPane.showMessageDialog(this, "Existen datos númericos no válidos: "+ error, "", JOptionPane.ERROR_MESSAGE);
    }
    public void errorMessageGestionHuertosApp(String error){
        JOptionPane.showMessageDialog(this, error, "", JOptionPane.ERROR_MESSAGE);
    }
    private void onOK() {
        // add your code here
        dispose();
    }
}
