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
    private JTextField idCultivoInt;
    private JTextField variedadText;
    private JLabel cultivoTitle;
    private JLabel Image;
    private JPanel panelWithTextFields;
    private JTextField rendimientoFloatTextField;
    private JTextField especieText;
    // mensaje emergente
    private final GUIMsg errorMessage = new GUIMsg();

    public GUICrearCultivo() {
        super((Frame)null);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        cultivoTitle.setText("Creación de cultivo");
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
        setAlwaysOnTop(false);
        String idCultivo = idCultivoInt.getText();
        String variedad = variedadText.getText();
        String especie = especieText.getText();
        String rendimiento = rendimientoFloatTextField.getText();
        if (idCultivo.isBlank() ||  variedad.isBlank() || especie.isBlank() || rendimiento.isBlank()) {
            errorMessage.error("Existen datos incorrectos o faltantes");
        } else {
            try {
                int id = Integer.parseInt(idCultivo);
                float rendimientoParsed = Float.parseFloat(rendimiento);
                if (rendimientoParsed <= 0 ) {
                    errorMessage.error("El rendimiento indicado no puede ser menor o igual a 0");
                }else if(id < 0){
                    errorMessage.error("El ID indicado no puede ser negativo");
                }else {
                    ControlProduccion.getInstance().createCultivo(id, especie, variedad, rendimientoParsed);
                    errorMessage.informacion("Cultivo registrado con exito");
                    especieText.setText("");
                    idCultivoInt.setText("");
                    variedadText.setText("");
                    rendimientoFloatTextField.setText("");
                }
            } catch(NumberFormatException e) {
                errorMessage.error("Un formato númerico indicado es inválido");
            } catch (GestionHuertosException e) {
                errorMessage.error(e.getMessage());
            }
        }
    }
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
