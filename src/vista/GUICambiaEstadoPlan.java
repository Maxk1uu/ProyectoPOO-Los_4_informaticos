package vista;

import controlador.ControlProduccion;
import utilidades.EstadoPlan;
import utilidades.GestionHuertosException;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class GUICambiaEstadoPlan extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox nuevoEstadoComboBox;
    private JTextField idPlanField;
    private JLabel nombrePlanLabel;
    private JLabel cumplimientoMetaLabel;
    private JLabel estadoActualLabel;

    //Relaciones
    GUIMsg guiMsg = new GUIMsg();
    ControlProduccion controlProduccion = ControlProduccion.getInstance();

    public GUICambiaEstadoPlan() {
        setContentPane(contentPane);
        setTitle("Cambiar estado de un plan");
        setModal(true);
        pack();
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);


        idPlanField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setAlwaysOnTop(false);
                cargaDatos(e);
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
        if (hayCamposVacios()) {
            guiMsg.error("Existen datos incorrectos o faltantes");
        } else {
            try {
                int idPlan = Integer.parseInt(idPlanField.getText());
                EstadoPlan nuevoEstado = EstadoPlan.valueOf(nuevoEstadoComboBox.getSelectedItem().toString());
                controlProduccion.changeEstadoPlan(idPlan,nuevoEstado);
                guiMsg.informacion("Estado del plan cambiado exitosamente.");
                dispose();
            } catch (GestionHuertosException e) {
                guiMsg.error(e.getMessage());
                idPlanField.setText("");
                nombrePlanLabel.setText("");
                cumplimientoMetaLabel.setText("");
                estadoActualLabel.setText("");
                nuevoEstadoComboBox.setSelectedIndex(0);
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    private void cargaDatos(FocusEvent event) {
        boolean planEncontrado = false;
        if (!idPlanField.getText().isBlank()) {
            try {
                int idPlan = Integer.parseInt(idPlanField.getText());
                String[] listPlanes = controlProduccion.listPlanes();
                for (String plan : listPlanes) {
                    String[] infoPlanes = plan.split("; ");
                    if(Integer.parseInt(infoPlanes[0]) == idPlan) {
                        nombrePlanLabel.setText(infoPlanes[1]);
                        cumplimientoMetaLabel.setText(setFormatoMeta(infoPlanes[10]));
                        estadoActualLabel.setText(infoPlanes[6]);
                        planEncontrado = true;
                    }
                }
                if(!planEncontrado) {
                    nombrePlanLabel.setText("");
                    cumplimientoMetaLabel.setText("");
                    estadoActualLabel.setText("");
                    guiMsg.error("No existe un plan con ese id");
                }
            } catch (GestionHuertosException e) {
                guiMsg.error(e.getMessage());
            } catch (NumberFormatException e) {
                guiMsg.error("El id del plan debe ser numerico");
            }
        }
    }

    //Validaciones
    private boolean hayCamposVacios() {
        return idPlanField.getText().isEmpty();
    }

    //Formateo
    private String setFormatoMeta(String meta) {
        meta = meta.replace(",",".");
        Double metaDouble = Double.parseDouble(meta);
        return String.format("%.2f %%", metaDouble);
    }

    //Mensaje
    private void sucessMsg(String mensaje) {
        guiMsg.informacion(mensaje);
    }


}
