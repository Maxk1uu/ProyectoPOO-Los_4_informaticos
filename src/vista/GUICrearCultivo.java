//Creado por: Gabriel Rojas
package vista;

import controlador.ControlProduccion;
import utilidades.GestionHuertosException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUICrearCultivo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField variedadText;
    private JTextField expecieText;
    private JTextField idInt;
    private JLabel cultivoTitle;
    private JLabel Image;
    private JPanel panelWithTextFields;
    private JTextField rendimientoFloatTextField;
    private final GUICrearCultivoMsg cultivoMsg;
    public GUICrearCultivo() {
        super((Frame)null);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        cultivoTitle.setText("Creación de cultivo");
        cultivoMsg = new GUICrearCultivoMsg();
        panelWithTextFields.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del cultivo"));
        buttonOK.addActionListener(actionEvent -> onOK());

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String idCultivo = idInt.getText();
        String variedad = variedadText.getText();
        String especie = expecieText.getText();
        String rendimiento = rendimientoFloatTextField.getText();
        if (idCultivo.isBlank() ||  variedad.isBlank() || especie.isBlank() || rendimiento.isBlank()) {
            setAlwaysOnTop(false);
            cultivoMsg.errorMessageEmpty();
        } else {
            try {
                setAlwaysOnTop(false);
                int id = Integer.parseInt(idCultivo);
                float rendimientoParsed =  Float.parseFloat(rendimiento);
                ControlProduccion.getInstance().createCultivo(id, especie, variedad, rendimientoParsed);
                cultivoMsg.successMessage();
                idInt.setText("");
                variedadText.setText("");
                expecieText.setText("");
                rendimientoFloatTextField.setText("");
            } catch(NumberFormatException e) {
                cultivoMsg.errorMessageInvalid();
            } catch (GestionHuertosException e) {
                cultivoMsg.cultivoAlreadyExists(e.getMessage());
            }
        }
    }
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
