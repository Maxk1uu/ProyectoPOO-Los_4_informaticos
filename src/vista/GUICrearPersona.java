package vista;
import controlador.ControlProduccion;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GUICrearPersona extends JDialog {
    private JPanel panel;
    private JTextField rutField;
    private JTextField emailField;
    private JTextField dirField;
    private JRadioButton propietarioRadioButton;
    private JRadioButton supervisorRadioButton;
    private JRadioButton cosechadorRadioButton;
    private JTextField datoVariableField;
    private JTextField nameField;
    private JButton cancelarButton;
    private JButton aceptarButton;
    private JLabel iconPersona;
    private JLabel datoVariable;
    ButtonGroup roles = new ButtonGroup();//Esto es un grupo de botones para que solo se puedan seleccionar uno a la vez



    //Relacion
    ControlProduccion controlProduccion = ControlProduccion.getInstance();
    GUIMsg guiMsg = new GUIMsg();

    public GUICrearPersona() {
        setTitle("Crear Persona");
        setContentPane(panel);
        setAlwaysOnTop(true);
        pack();
        setLocationRelativeTo(null); //Esto centra la ventana
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//Esto cierra la ventana al pulsar el boton de cerrar

        //Estos son los "Nombres" de cada boton
        propietarioRadioButton.setActionCommand("Propietario");
        supervisorRadioButton.setActionCommand("Supervisor");
        cosechadorRadioButton.setActionCommand("Cosechador");

        roles.add(propietarioRadioButton);
        roles.add(supervisorRadioButton);
        roles.add(cosechadorRadioButton);
        propietarioRadioButton.setSelected(true); //Por defecto se selecciona el primer boton, asi evita que sea null

        //Este ActionListener cambia el label dependiendo de la seleccion del boton
        ActionListener labelVariable = e -> {
            String persona = roles.getSelection().getActionCommand();
            switch (persona) {
                case "Propietario" -> datoVariable.setText("DIRECCION COMERCIAL:");
                case "Supervisor" -> datoVariable.setText("PROFESION:");
                case "Cosechador" -> datoVariable.setText("FECHA DE NACIMIENTO:");
            }
        };

        //Esto activa el ActionListener
        propietarioRadioButton.addActionListener(labelVariable);
        supervisorRadioButton.addActionListener(labelVariable);
        cosechadorRadioButton.addActionListener(labelVariable);

        aceptarButton.addActionListener(e -> {
            String rut = rutField.getText();
            String email = emailField.getText();
            String dir = dirField.getText();
            String nom = nameField.getText();
            String datoVar = datoVariableField.getText();
            setAlwaysOnTop(false);
            if (hayCamposVacios()) {
                guiMsg.error("Existen datos incorrectos o faltantes");
            } else {
                try {
                    Rut rutPersona = Rut.of(rut);
                    switch (roles.getSelection().getActionCommand()) {
                        case "Propietario" -> {
                            controlProduccion.createPropietario(rutPersona, nom, email, dir, datoVar);
                        }
                        case "Supervisor" -> {
                            controlProduccion.createSupervisor(rutPersona, nom, email, dir, datoVar);
                        }
                        case "Cosechador" -> {
                            LocalDate fechaNacimiento = LocalDate.parse(datoVar, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            if (!isFechaValida(fechaNacimiento)) {
                                guiMsg.error("La fecha ingresada no es valida");
                                datoVariableField.setText("");
                            } else {
                                controlProduccion.createCosechador(rutPersona, nom, email, dir, fechaNacimiento);
                            }
                        }
                    }
                    msgPersonaCreada();
                } catch (IllegalArgumentException ex) {
                    guiMsg.error("El rut ingresado no cumple el formato 12.345.678-K");
                    rutField.setText("");
                } catch (GestionHuertosException ex) {
                    msgExisteUnaPersona();
                }
            }

        });

        cancelarButton.addActionListener(e -> dispose());
    }

    //Validaciones
    private boolean hayCamposVacios() {
        return rutField.getText().isEmpty() || emailField.getText().isEmpty() || dirField.getText().isEmpty()
                || nameField.getText().isEmpty() || datoVariableField.getText().isEmpty();
    }
    private boolean isFechaValida(LocalDate fecha) {
        LocalDate fechaActual = LocalDate.now();
        return fecha.isBefore(fechaActual);
    }
    public void msgExisteUnaPersona() {
        String persona = roles.getSelection().getActionCommand();
        guiMsg.error("Ya existe un "+persona+" con el rut ingresado");
        rutField.setText("");
        emailField.setText("");
        dirField.setText("");
        datoVariableField.setText("");
        nameField.setText("");
    }
    public void msgPersonaCreada() {
        String persona = roles.getSelection().getActionCommand();
        guiMsg.informacion(persona + " creado correctamente");
        dispose();
    }
}
