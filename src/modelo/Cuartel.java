package modelo;//Última revisión: Gabriel Rojas

import utilidades.EstadoFenologico;

import java.util.ArrayList;

public class Cuartel {

    private int id;
    private float superficie;
    private EstadoFenologico estado;
    //Asociacion multiple
    private final ArrayList<PlanCosecha> planCosechas = new ArrayList<>();
    //Asociaciones Singulares
    private Huerto huerto;
    private Cultivo cultivo;
    //Constructor
    public Cuartel(int id, float superficie, Cultivo cultivo, Huerto huerto) {
        this.id = id;
        this.superficie = superficie;
        this.cultivo = cultivo;
        this.huerto = huerto;
        //Se ejecutan las asociaciones
        cultivo.addCuartel(this);
        this.estado = EstadoFenologico.REPOSO_INVERNAL;

    }
    //Metodos
    public int getId() {
        return id;
    }
    public float getSuperficie() {
        return superficie;
    }
    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }
    public float getRendimientoEsperado(){ //Posible solucion, no es 100% seguro que sea esto.
        return cultivo.getRendimiento();
    }
    public EstadoFenologico getEstado() {
        return estado;
    }
    public boolean setEstado(EstadoFenologico est) {
        EstadoFenologico est1 = EstadoFenologico.REPOSO_INVERNAL;
        EstadoFenologico est2 = EstadoFenologico.FLORACION;
        EstadoFenologico est3 = EstadoFenologico.CUAJA;
        EstadoFenologico est4 = EstadoFenologico.FRUCTIFICACION;
        EstadoFenologico est5 = EstadoFenologico.MADURACION;
        EstadoFenologico est6 = EstadoFenologico.COSECHA;
        EstadoFenologico est7 = EstadoFenologico.POSTCOSECHA;
        if(estado == est) {
            return false;
        }

        if((estado == est2 && est == est1) || //El cambio de estado solo puede avanzar, no retroceder.
                (estado == est3 && (est == est2 || est == est1)) ||
                (estado == est4 && (est == est3 || est == est2 || est == est1)) ||
                (estado == est5 && (est == est4 || est == est3 || est == est2 || est == est1)) ||
                (estado == est6 && (est == est5 || est == est4 || est == est3 || est == est2 || est == est1)) ||
                (estado == est7 && (est == est6 || est == est5 || est == est4 || est == est3 || est == est2 || est == est1))) {
            return false;
        }

        estado = est;
        return true;
    }
    public Huerto getHuerto() {
        return huerto;
    }
    public Cultivo getCultivo(){
        return cultivo;
    }
    //Se añade el objeto a la coleccion
    public void addPlanCosecha(PlanCosecha planCosecha){
        //Asegura que el objeto pasado por parametro no es el mismo
        if (!planCosechas.contains(planCosecha)) {
            planCosechas.add(planCosecha);
        }
    }
    public PlanCosecha[] getPlanCosechas(){
        return planCosechas.toArray(new PlanCosecha[0]);
    }




}
