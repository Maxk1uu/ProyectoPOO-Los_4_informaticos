//Codigo hecho por: Maximiliano Maureira
//Revisado por:
import java.util.Date;
public class CosechadorAsignado {
    //Atributos
    private Date desde;
    private Date hasta;
    private double metaKilos;

    //Relaciones
    Cosechador cosechador;
    Cuadrilla cuadrilla;

    //Constructor
    public CosechadorAsignado(Date fIni, Date fFin, double meta, Cuadrilla cuad, Cosechador cos) {
        this.desde = fIni;
        this.hasta = fFin;
        this.metaKilos = metaKilos;
        this.cuadrilla = cuad;
        cosechador = cos;
    }

    //Metodos
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public double getMetaKilos() {
        return metaKilos;
    }

    public void setMetaKilos(double metaKilos) {
        this.metaKilos = metaKilos;
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }
    public Cosechador getCosechador() {
        return cosechador;
    }
}
