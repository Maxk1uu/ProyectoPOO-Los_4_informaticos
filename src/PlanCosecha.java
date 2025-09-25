//Creado por: Gabriel
//ültima revisión:

import java.util.Date;
import java.util.ArrayList;
public class PlanCosecha {

    private int id;
    private String nombre;
    private Date inicio;
    private Date finEstimado;
    private Date finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;
    //Relacion
    private Cuartel cuartel;
    // Relacion multiple
    private final ArrayList<Cuadrilla> cuadrillas;
    //Constructor
    public PlanCosecha(int id, String nombre, Date inicio, Date finEstimado, double metaKilos, double precioBaseKilo, Cuartel cuartel) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.finEstimado = finEstimado;
        this.metaKilos = metaKilos;
        this.precioBaseKilo = precioBaseKilo;
        this.cuartel = cuartel;
        //Relacion singular, la cual añade este objeto a la coleccion del objeto creado a partir de la clase Cuartel.
        cuartel.addPlanCosecha(this);
        cuadrillas = new ArrayList<>();
        estado = EstadoPlan.PLANIFICADO;
    }
    //Metodos
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getInicio() {
        return inicio;
    }
    public Date getFinEstimado() {
        return finEstimado;
    }
    public Date getFinReal() {
        return finReal;
    }
    public void setFinReal(Date finReal) {
        this.finReal = finReal;
    }
    public double getMetaKilos() {
        return metaKilos;
    }
    public void setMetaKilos(double metaKilos) {
        this.metaKilos = metaKilos;
    }
    public double getPrecioBaseKilo() {
        return precioBaseKilo;
    }
    public void setPrecioBaseKilo(double precioBaseKilo) {
        this.precioBaseKilo = precioBaseKilo;
    }
    public EstadoPlan getEstado() {
        return estado;
    }
    public void setEstado(EstadoPlan estado) {
        this.estado = estado;
    }
    public Cuartel getCuartel() {
        return cuartel;
    }
    // Asociacion Multiple de esta clase.
    public boolean addCuadrilla(int idCuadrilla, String nombreCuadrilla, Supervisor supervisor) {
        //se utiliza el metodo private findCuadrilla para asegurar que la cuadrilla no existe, si no existe, crea una nueva.
        //De lo contrario, retorna false.
        if (findCuadrilla(idCuadrilla) == null) {
            //Asegura que el supervisor no está asignado a una cuadrila, a través de llamar al metodo getCuadrillaAsignada.
            //Si lo está, el objeto retornado no será null, por cual se asume que el supervisor ya tiene una cuadrilla asignada.
            if (supervisor.getCuadrillaAsignada() == null) {
                return cuadrillas.add(new Cuadrilla(idCuadrilla, nombreCuadrilla, supervisor, this));
            }
        }
        return false;
    }
    //Asigna una cosechadora con una cuadrilla.
    public boolean addCosechadorToCuadrilla(int idCuadrilla, Date fechaInicio, Date fechaFin, double meta, Cosechador cosechador) {
        //Asegura que el objeto encontrado no es null, si no lo es, llama al metodo addCosechador.
        if (findCuadrilla(idCuadrilla) != null) {
            return findCuadrilla(idCuadrilla).addCosechador(fechaInicio, fechaFin, meta, cosechador);
        }
        return false;
    }
    /*Retorna los objetos asociados, chequear en el si es que funciona.
      Advertencia: Si esto no funciona, colocar cuadrillas.size() en lugar de 0 no resolvera el problema.
     */
    public Cuadrilla[] getCuadrillas() {
        return cuadrillas.toArray(new Cuadrilla[0]);
    }
    //Metodo private que encuentra la cuadrilla deseada(Se pueden agregar metodos extras, pero solamente privados).
    private Cuadrilla findCuadrilla(int idCuadrilla) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getId() == idCuadrilla) {
                return cuadrilla;
            }
        }
        return null;
    }





}
