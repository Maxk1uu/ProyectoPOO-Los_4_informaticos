package vista;
import controlador.ControlProduccion;
import utilidades.EstadoPlan;

import javax.swing.*;

public class GUIPagarPesajesImpagosACosechador extends JDialog {

    private JPanel panel1;
    private JButton cancelarButton;
    private JButton aceptarButton;
    private JTextField iDpago;
    private JTextField rutCosechador;
    private JTable tableCosechador;


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

    }

    private boolean hayCamposVacios() {
        return rutCosechador.getText().isEmpty();
    }
    private void onOk(){
        if(hayCamposVacios()){
            guiMsg.error("Datos incorrectos o faltantes");
        }else {
            try {

            }catch (RuntimeException e){

            }
        }
    }
}
