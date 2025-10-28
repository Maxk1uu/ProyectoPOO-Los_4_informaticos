package modelo;//Revisado por: Gabriel Rojas
import java.time.LocalDate;
import java.util.ArrayList;

public class CosechadorAsignado {
    //Atributos
    private LocalDate desde;
    private LocalDate hasta;
    private double metaKilos;

    //Relaciones
    private Cosechador cosechador;
    private Cuadrilla cuadrilla;
    private ArrayList<Pesaje> pesajes = new ArrayList<>();

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

    //necesita revision
    public double getCumplimientoMeta(){
        double sumKilos = 0;
        for(Pesaje p : pesajes){
            sumKilos += p.getCantidadKg();
        }
        return (sumKilos / metaKilos)*100;
    }
    public int getNroPesajesImpagos() {
        int nroPesajeImpagos = 0;
        for(Pesaje p : pesajes){
            if(!p.isPagado()){
                nroPesajeImpagos++;
            }
        }
        return nroPesajeImpagos;
    }
    public double getMontoPesajesImpagos(){
        double montoTotal = 0;
        for(Pesaje p : pesajes){
            if(!p.isPagado()){
                montoTotal += p.getMonto();
            }
        }
    return montoTotal;
    }
    public int getNroPesajesPagados(){
        int nroPesajePagados = 0;
        for(Pesaje p : pesajes){
            if(p.isPagado()){
                nroPesajePagados++;
            }
        }
        return nroPesajePagados;
    }

}
