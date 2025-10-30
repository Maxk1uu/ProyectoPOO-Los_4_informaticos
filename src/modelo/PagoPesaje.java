package modelo;
import java.time.LocalDate;
import java.util.List;

public class PagoPesaje {
    private final int id;
    LocalDate fecha;
    //Relacion
    private List<Pesaje> pesajes;
    //Constructor
    public PagoPesaje(int id, LocalDate fecha, List<Pesaje> pesajes) {
        this.id = id;
        this.fecha = fecha;
        this.pesajes = pesajes;
        //Se realiza la relacion, añadiendo este PagoPesaje a cada Pesaje pasado por parametro.
        for (Pesaje p : pesajes) {
            p.setPago(this);
        }
    }
    public int getId() {
        return id;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public double getMonto(){
        //Se calcula el Monto total de todos los pesajes.
        double montoTotal = 0;
        for (Pesaje p: pesajes){
            montoTotal += p.getMonto();
        }
        return montoTotal;
    }
    public Pesaje[] getPesajes() {
        return  pesajes.toArray(new Pesaje[0]);
    }
}
