package modelo;
import utilidades.Calidad;

import java.time.LocalDate;
import java.time.LocalDateTime;
public class Pesaje {
    private int id;
    private double cantidadKg;
    private Calidad calidad;
    LocalDateTime fechaHora;
    private double precioKg;
    //Relaciones
    private final CosechadorAsignado cosechadorAsignado;
    private PagoPesaje pagoPesaje;
    //Constructor
    public Pesaje(int id, double cant, Calidad cal, LocalDateTime fechaHora, CosechadorAsignado cosechadorAsignado) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.calidad = cal;
        this.cantidadKg = cant;
        this.cosechadorAsignado = cosechadorAsignado;
        //Por hacer, añadir addPesaje a cosechador asignado.
        this.cosechadorAsignado.addPesaje(this);
        this.precioKg = cosechadorAsignado.getCuadrilla().getPlanCosecha().getPrecioBaseKilo();
    }
    public  int getId() {
        return id;
    }
    public double getCantidadKg() {
        return cantidadKg;
    }
    public Calidad getCalidad() {
        return calidad;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public CosechadorAsignado getCosechadorAsignado() {
        return cosechadorAsignado;
    }
    public double getPrecioKg() {
        return precioKg;
    }
    public double getMonto(){
        //El monto total es cantidad por precio, ver enunciado si es que hay dudas.
        double monto = precioKg*cantidadKg;
        //Si es suficiente: 80%
        if (calidad.equals(Calidad.SUFICIENTE)) return monto*0.80;
        //Deficiente: 60%
        if (calidad.equals(Calidad.DEFICIENTE)) return monto*0.60;
        //De lo contrario: 100%
        else return cantidadKg * precioKg;
    }
    public void setPago(PagoPesaje pagoPesaje){
        this.pagoPesaje = pagoPesaje;
    }
    public PagoPesaje getPagoPesaje(){
        return pagoPesaje;
    }
    public boolean isPagado(){
        return pagoPesaje != null;
    }
}
