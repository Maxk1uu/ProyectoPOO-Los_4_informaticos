//ültima revisión: Gabriel Rojas
package modelo;

import utilidades.EstadoPlan;
import utilidades.GestionHuertosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PlanCosecha {

    private int id;
    private String nombre;
    private LocalDate inicio;
    private LocalDate finEstimado;
    private LocalDate finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;

    //Relacion
    private Cuartel cuartel;
    // Relacion multiple
    private final ArrayList<Cuadrilla> cuadrillas;

    //Constructor
    public PlanCosecha(int id, String nombre, LocalDate inicio, LocalDate finEstimado, double metaKilos, double precioBaseKilo, Cuartel cuartel) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.finEstimado = finEstimado;
        this.metaKilos = metaKilos;
        this.precioBaseKilo = precioBaseKilo;
        this.cuartel = cuartel;
        //Relacion singular, la cual añade este objeto a la coleccion del objeto creado a partir de la clase modelo.Cuartel.
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

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getFinEstimado() {
        return finEstimado;
    }

    public LocalDate getFinReal() {
        return finReal;
    }

    public void setFinReal(LocalDate finReal) {
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

    public boolean setEstado(EstadoPlan estado) {
        //Condicional bien largo.
        // Si el estado indicado por parametros es el mismo que el del plan, entonces tirara una excepcion.
        // Si el estado del plan es cancelado, entonces no se podra cambiar su estado nuevamente.
        // si su estado es Cerrado y se quiere cambiar a ejecutando, entonces no se podra, porque el plan cosecha ya ha concluido.
        // si el Estado es Cerrado y se quiere cambiar a planificado, entonces no se podra.
        // si el estado es Ejecutando y se quiere cambiar a planificado, entonces no se podra porque el plan ya esta en ejecucion.
        // Disclaimer: Estas son condiciones que talvez no sean correctas, hablar de estas condiciones con la profesora.

        if (getEstado().equals(estado) || getEstado().equals(EstadoPlan.CANCELADO)
                || getEstado().equals(EstadoPlan.CERRADO)
                || getEstado().equals(EstadoPlan.PLANIFICADO) && estado.equals(EstadoPlan.CERRADO)
                || getEstado().equals(EstadoPlan.EJECUTANDO) && estado.equals(EstadoPlan.PLANIFICADO))
            return false;
        this.estado = estado;
        return true;
    }

    public Cuartel getCuartel() {
        return cuartel;
    }

    // Asociacion Multiple de esta clase.
    public void addCuadrilla(int idCuadrilla, String nombreCuadrilla, Supervisor supervisor) {
        //se utiliza el metodo private findCuadrilla para asegurar que la cuadrilla no existe, si no existe, crea una nueva.
        //De lo contrario, retorna null.
        if (findCuadrilla(idCuadrilla).isEmpty()) {
            //Asegura que el supervisor no está asignado a una cuadrila, a través de llamar al metodo getCuadrillaAsignada.
            //Si lo está, el objeto retornado no será null, por lo cual se asume que el supervisor ya tiene una cuadrilla asignada.
            if (supervisor.getCuadrillaAsignada() == null) {
                cuadrillas.add(new Cuadrilla(idCuadrilla, nombreCuadrilla, supervisor, this));
            }
        } else {
            throw new GestionHuertosException("Ya existe en el plan una cuadrilla con el id pasado como parametro");
        }
    }

    //Asigna una cosechadora con una cuadrilla.
    public void addCosechadorToCuadrilla(int idCuadrilla, LocalDate fechaInicio, LocalDate fechaFin, double meta, Cosechador cosechador) {
        //Asegura que el objeto encontrado no es null, si no lo es, llama al metodo addCosechador.
        if (findCuadrilla(idCuadrilla).isPresent()) {
            findCuadrilla(idCuadrilla).get().addCosechador(fechaInicio, fechaFin, meta, cosechador);
        } else {
            throw new GestionHuertosException("No existe en el plan una cuadrilla con el id pasado como parametro");
        }
    }

    public Cuadrilla[] getCuadrillas() {
        if (!cuadrillas.isEmpty()) return cuadrillas.toArray(new Cuadrilla[0]);
        return new Cuadrilla[0];
    }

    //Metodo private que encuentra la cuadrilla deseada.
    private Optional<Cuadrilla> findCuadrilla(int idCuadrilla) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getId() == idCuadrilla) {
                return Optional.of(cuadrilla);
            }
        }
        return Optional.empty();
    }

    public double getCumplimientoMeta() {
        double suma = 0;
        for (Cuadrilla cuadrilla : cuadrillas) {
            suma += cuadrilla.getKilosPesados();
        }
        // Calculo el porcentaje de kilos que lleva en total las cuadrillas con respecto a la meta de kilos
        return (suma * 100) / this.metaKilos;
    }
}
