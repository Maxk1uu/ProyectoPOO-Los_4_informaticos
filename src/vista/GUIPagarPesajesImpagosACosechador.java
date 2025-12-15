package vista;
import controlador.ControlProduccion;
import utilidades.EstadoPlan;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class GUIPagarPesajesImpagosACosechador extends JDialog {

    private JPanel panel1;
    private JButton cancelarButton;
    private JButton aceptarButton;
    private JTextField iDpago;
    private JTextField rutCosechador;
    private JTable tableCosechador;
    private DefaultTableModel tableModel;


    GUIMsg guiMsg = new GUIMsg();
    ControlProduccion controlProduccion = ControlProduccion.getInstance();

    public GUIPagarPesajesImpagosACosechador(){
        setContentPane(panel1);
        setTitle("Pagar Impagos");
        setModalityType(DEFAULT_MODALITY_TYPE);
        pack();
        setAlwaysOnTop(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        String[] columnas = {"Id","Fecha","Calidad","Kilos","Precio Kg","Monto", "Pagado"};

        tableModel = new DefaultTableModel(columnas, 0);
        tableCosechador.setModel(tableModel);

        rutCosechador.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
            if(!rutCosechador.getText().isBlank()){
                   cargaDatos();
                }
            }
        });


        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelar();
            }
        });

    }
    public static void main(String[] args) {
        GUIPagarPesajesImpagosACosechador dialog = new GUIPagarPesajesImpagosACosechador();
        dialog.setVisible(true);
    }

    private boolean hayCamposVacios() {
        return rutCosechador.getText().isEmpty() && iDpago.getText().isEmpty();

    }

    private void onOk(){

    }

    private  void onCancelar(){
        dispose();
    }
    private void cargaDatos(){
        Rut rutCos = Rut.of(rutCosechador.getText());
        try{
            String[] datos = controlProduccion.listPesajesCosechador(rutCos);
            //tableModel;


        }catch(GestionHuertosException | IllegalArgumentException e){
            guiMsg.error(e.getMessage());
        }
    }

}
