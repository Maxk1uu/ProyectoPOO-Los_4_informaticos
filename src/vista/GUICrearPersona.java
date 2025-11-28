package vista;
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
    private boolean wasAcepted = false;

    //Relacion
    GUIMsg guiMsg = new GUIMsg();

    public GUICrearPersona() {
        setTitle("Crear Persona");
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null); //Esto centra la ventana
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//Esto cierra la ventana al pulsar el boton de cerrar
        //Estos son los "Nombres" de cada boton
        propietarioRadioButton.setActionCommand("Propietario");
        supervisorRadioButton.setActionCommand("Supervisor");
        cosechadorRadioButton.setActionCommand("Cosechador");

        //Esto es un grupo de botones para que solo se puedan seleccionar uno a la vez
        ButtonGroup roles = new ButtonGroup();
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
            if (rutField.getText().isEmpty() || emailField.getText().isEmpty() || dirField.getText().isEmpty()
                    || nameField.getText().isEmpty() || datoVariableField.getText().isEmpty()) {
                guiMsg.error("Existen datos incorrectos o faltantes");
            } else {
                wasAcepted = true;
                String persona = roles.getSelection().getActionCommand();
                guiMsg.informacion(persona + " creado exitosamente");
                dispose();
            }
        });
        cancelarButton.addActionListener(e -> dispose());
    }

    //Getters
    public String getRut() {
        return rutField.getText();
    }
    public String getEmail() {
        return emailField.getText();
    }
    public String getDir() {
        return dirField.getText();
    }
    public String getNombre() {
        return nameField.getText();
    }
    public String getDatoVariable() {
        return datoVariable.getText();
    }
    public String getRol() {
        return propietarioRadioButton.getActionCommand();
    }
    public boolean wasAcepted() {
        return wasAcepted;
    }
}
