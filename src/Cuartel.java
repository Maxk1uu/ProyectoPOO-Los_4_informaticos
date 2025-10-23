//Creado por: Gabriel Rojas
//Última revisión: Gabriel Rojas

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
    public void setEstado(EstadoFenologico estado) {
        this.estado = estado;
    }
    public Huerto getHuerto() {
        return huerto;
    }
    public Cultivo getCultivo(){
        return cultivo;
    }
    //Se añade el objeto a la coleccion
    public boolean addPlanCosecha(PlanCosecha planCosecha){
        //Asegura que el objeto pasado por parametro no es el mismo
        if (!planCosechas.contains(planCosecha)) {
            return planCosechas.add(planCosecha);
        }
        return false;
    }
    public PlanCosecha[] getPlanCosechas(){
        return planCosechas.toArray(new PlanCosecha[0]);
    }




}
