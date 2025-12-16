package vista;

import controlador.ControlProduccion;
import utilidades.Calidad;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;

public class GUIAgregarPesajeACosechador extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idPesajeTextField;
    private JComboBox<String> cosechadorListaComboBox;
    private JComboBox<String> cuadrillaDeCosComboBox;
    private JTextField cantidadKilosTextField;
    private JComboBox<Calidad> calidadComboBox;
    private final GUIMsg errorMessage = new GUIMsg();
    public GUIAgregarPesajeACosechador() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setAlwaysOnTop(true);
        pack();
        setLocationRelativeTo(null);
        //Las siguientes lineas llaman a cada list de cosechadores que existen a través del controlador
        for (String listaCos : ControlProduccion.getInstance().listCosechadores()) {
            String[] listOfCos = listaCos.split(";");
            String onlyRutAndNombre = listOfCos[0] +";"+ listOfCos[1];
            cosechadorListaComboBox.addItem(onlyRutAndNombre);
        }
        //ActionListener que cambiá las cuadrillas mostradas según el cosechador seleccionado
        ActionListener changeComboBoxOfCuadrillas = (event) -> {
            if (cosechadorListaComboBox.getSelectedIndex() != -1) {
                Rut rutCos;
                String selectedCosechador = (String) cosechadorListaComboBox.getSelectedItem();
                String[] list = selectedCosechador.split(";");
                rutCos = Rut.of(list[0]);
                cuadrillaDeCosComboBox.removeAllItems();
                for (String cuadrillaOFCos : ControlProduccion.getInstance().getCuadrillasDeCosechadorDePlan(rutCos)){
                    cuadrillaDeCosComboBox.addItem(cuadrillaOFCos);
                }
            }
        };
        //Esto asegura que cuando se eliga un cosechador, el combo box de cuadrillas cambiará para mostrar sus cuadrillas.
        cosechadorListaComboBox.addActionListener(changeComboBoxOfCuadrillas);

        //Los mismo que los cosechadores, esta vez con los Enum de calidad
        Calidad[] calidadArray = Calidad.values();
        for (Calidad calidad : calidadArray) {
            calidadComboBox.addItem(calidad);
        }

        buttonOK.addActionListener(actionEvent -> onOK());

        buttonCancel.addActionListener(actionEvent -> onCancel());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        setAlwaysOnTop(false);
        String idStr =  idPesajeTextField.getText().trim();
        String cantidadKilos = cantidadKilosTextField.getText().trim();
        int calidadIndex = calidadComboBox.getSelectedIndex();
        if (cosechadorListaComboBox.getSelectedIndex() == -1
                || cuadrillaDeCosComboBox.getSelectedIndex() == -1
                || calidadComboBox.getSelectedIndex() == -1
        || idStr.isBlank() || cantidadKilos.isBlank()) {
            errorMessage.error("Existen datos faltantes, incorrectos o no seleccionados");
        } else {
            try {
                int idPesaje =  Integer.parseInt(idStr);
                float cantidadKiloFloat = Float.parseFloat(cantidadKilos);
                String cos =  cosechadorListaComboBox.getSelectedItem().toString();
                String cuadrilla = cuadrillaDeCosComboBox.getSelectedItem().toString();
                String[] listaCos = cos.split(";");
                String[]  listaCuadrilla = cuadrilla.split(";");
                Rut rutCos =  Rut.of(listaCos[0]);
                int idCuadrilla = Integer.parseInt(listaCuadrilla[0]);
                int idPlanCosechaDeCuadrilla = Integer.parseInt(listaCuadrilla[2].trim());
                Calidad calidad = calidadComboBox.getItemAt(calidadIndex);
                ControlProduccion.getInstance().addPesaje(idPesaje, rutCos, idPlanCosechaDeCuadrilla, idCuadrilla, cantidadKiloFloat, calidad);
                errorMessage.informacion("Pesaje registrado con exito");
                idPesajeTextField.setText("");
                cantidadKilosTextField.setText("");
                cosechadorListaComboBox.setSelectedIndex(-1);
                calidadComboBox.setSelectedIndex(-1);
                cuadrillaDeCosComboBox.setSelectedIndex(-1);

            } catch (NumberFormatException e) {
                errorMessage.error("Un formato númerico indicado es incorrecto");
            } catch(GestionHuertosException e){
                errorMessage.error(e.getMessage());
            }
        }
    }

    private void onCancel() {
        dispose();
    }
}
