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
        return cantidadKg * precioKg;
    }
    public void setPago(PagoPesaje pagoPesaje){
        this.pagoPesaje = pagoPesaje;
    }
    public PagoPesaje getPago(){
        return pagoPesaje;
    }
    public boolean isPagado(){
        if (pagoPesaje == null) return false;
        // La fecha asociada a PagoPesaje es como una fecha límite.
        // Esta fecha se compara con LocalDate.now porque aquí no se especifica con que fecha compararse.
        // Se podría decir que se compará con la fecha asignada de este clase.
        // Pero esta clase solamente representa el comienzo del pesaje.
        //Retornará true si es que la fecha limite ya ha pasado.
        //Falso en lo contrario.
        return pagoPesaje.getFecha().isBefore(LocalDate.now());
    }
}
