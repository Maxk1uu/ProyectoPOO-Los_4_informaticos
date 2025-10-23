package modelo;//Codigo hecho por: Maximiliano Maureira
//Revisado por: Gabriel Rojas
import java.time.LocalDate;
public class CosechadorAsignado {
    //Atributos
    private LocalDate desde;
    private LocalDate hasta;
    private double metaKilos;

    //Relaciones
    private Cosechador cosechador;
    private Cuadrilla cuadrilla;

    //Constructor
    public CosechadorAsignado(LocalDate fIni, LocalDate fFin, double metaKilos, Cuadrilla cuad, Cosechador cos) {
        this.desde = fIni;
        this.hasta = fFin;
        this.metaKilos = metaKilos;
        this.cuadrilla = cuad;
        cosechador = cos;
        //La relacion se ejecuta.
        cosechador.addCuadrilla(this);
    }

    //Metodos
    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
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
