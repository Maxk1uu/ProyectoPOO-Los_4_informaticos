// Hecho por Ricardo Quintana
package vista;
import controlador.ControlProduccion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class GUIListarCosechadores extends JDialog {
    private JPanel contentPane;
    private JButton buttonVolver;
    private JLabel labelCosechadores;
    private JPanel panel1;
    private JScrollPane jscrollPaneListaCosechadores;
    private JTable tablaDatos;

    // Relaciones
    ControlProduccion controlProduccion = ControlProduccion.getInstance();
    GUIMsg guiMsg = new GUIMsg();

    public GUIListarCosechadores() {
            setTitle("Listar Cosechadores");
            setContentPane(contentPane);
            setAlwaysOnTop(true);
            pack();
            setLocationRelativeTo(null); //Esto centra la ventana

            buttonVolver.addActionListener(new ActionListener() {
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

            // Creamos la tabla
            String [] datosCosechadores = controlProduccion.listCosechadores();
            String[][] datosListado = new String[controlProduccion.listCosechadores().length][datosCosechadores[0].split(";").length];
            for (int i = 0; i < controlProduccion.listCosechadores().length; i++) {
                datosListado[i] = controlProduccion.listCosechadores()[i].split(";");
            }
            String[] columnas = {"Rut", "Nombre", "Dirección", "Email", "Fecha Nac.", "Nr.Cuadrillas"
                    , "Monto impago $", "Monto pagado $"};
            TableModel tableModel = new DefaultTableModel(datosListado, columnas);
            tablaDatos.setModel(tableModel);
            DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
            defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < tablaDatos.getColumnCount(); i++) {
                tablaDatos.getColumnModel().getColumn(i)
                        .setCellRenderer(defaultTableCellRenderer);
            }
    }

    private void onCancel() {
        dispose();
    }
}
