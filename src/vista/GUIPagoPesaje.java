//Hecho por: Gabriel Rojas
package vista;

import controlador.ControlProduccion;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class GUIPagoPesaje extends JDialog {
    private JPanel contentPane;
    private JButton buttonAceptar;
    private JButton buttonCancelar;
    private JLabel totalAPagarNumber;
    private JTextField rutCosechadorTextField;
    private JTextField idPagoTextField;
    private JTable tablaDatosPesajesImpagos;

    GUIMsg guiMsg = new  GUIMsg();

    public GUIPagoPesaje() {
        setContentPane(contentPane);
        setAlwaysOnTop(true);
        setModal(true);
        getRootPane().setDefaultButton(buttonAceptar);
        setLocationRelativeTo(null);
        pack();
        setSize(590, 500);

        buttonAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        rutCosechadorTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                onFocusLostRutCosechador();
            }
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                onFocusGainedRutCosechador();
            }
        });
        String[] columnaTitles = {"ID", "Fecha", "Calidad", "Kilos", "Precio Kg.", "Monto", "Pagado"};
        TableModel tableModelInicial = new  DefaultTableModel(new String[0][0],  columnaTitles);
        tablaDatosPesajesImpagos.setModel(tableModelInicial);

    }
    private void onFocusLostRutCosechador() {
        try{
            double montoAPagar = 0;
            String[] datosPesajes = ControlProduccion.getInstance().listPesajesCosechador(Rut.of(rutCosechadorTextField.getText()));
            String[][] datosListado = new String[datosPesajes.length][datosPesajes[0].split("; ").length];
            for (int i = 0; i < datosPesajes.length; i++) {
                String[] datosDelPesaje = datosPesajes[i].split("; ");
                if (datosDelPesaje[datosDelPesaje.length - 1].equals("Impago")) { //Última posición: Estado de pago
                    // Si está impago, llama a penultima posición que contiene el monto a pagar
                    montoAPagar += reconstruyeMontoAPagar(datosDelPesaje[datosDelPesaje.length - 2 ]);
                }
                datosListado[i] = datosDelPesaje;
            }
            String[] columnaTitles = {"ID", "Fecha", "Calidad", "Kilos", "Precio Kg.", "Monto", "Pagado"};
            TableModel tableModel = new DefaultTableModel(datosListado, columnaTitles);
            tablaDatosPesajesImpagos.setModel(tableModel);
            DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
            defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < tablaDatosPesajesImpagos.getColumnCount(); i++) {
                tablaDatosPesajesImpagos.getColumnModel().getColumn(i).setCellRenderer(defaultTableCellRenderer);
            }
            totalAPagarNumber.setText(" $ "+String.format("%.1f", montoAPagar));
        } catch(GestionHuertosException | IllegalArgumentException ignored){
        }
    }


    private void onFocusGainedRutCosechador(){
        if (tablaDatosPesajesImpagos.getRowCount() > 0) {
            DefaultTableModel model = (DefaultTableModel) tablaDatosPesajesImpagos.getModel();
            model.setRowCount(0);
            totalAPagarNumber.setText("");
        }
    }
    private void onOK() {
        setAlwaysOnTop(false);
        String idPagoStr = idPagoTextField.getText();
        String rutCosStr =  rutCosechadorTextField.getText();
        if (idPagoStr.isBlank() || rutCosStr.isBlank()) {
            guiMsg.error("Existen datos incorrectos o faltantes");
        } else {
            try {
                int idPago = Integer.parseInt(idPagoStr);
                Rut rut = Rut.of(rutCosechadorTextField.getText());
                ControlProduccion.getInstance().addPagoPesaje(idPago, rut);
                guiMsg.informacion("Pago de pesajes realizado con exito");
                idPagoTextField.setText("");
                rutCosechadorTextField.setText("");
                onFocusGainedRutCosechador();
            } catch (IllegalArgumentException e) {
                guiMsg.error("Existen datos incorrectos o faltantes");
            } catch (GestionHuertosException e) {
                guiMsg.error(e.getMessage());
            }
        }

    }

    private void onCancel() {
        dispose();
    }
    public static void display(){
        SwingUtilities.invokeLater(()->{
            GUIPagoPesaje gui = new  GUIPagoPesaje();
            gui.setVisible(true);
        });
    }
    private double reconstruyeMontoAPagar(String monto) {
        String[] splittedMonto = monto.split(",");
        return Double.parseDouble(splittedMonto[0]+"."+splittedMonto[1]);
    }
}
