package modelo;//Revisado por: Gabriel Rojas
import utilidades.GestionHuertosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class Cuadrilla {
    //Atributos
    private int id;
    private String nombre;
    private static int maximoCosechadores;

    //Relaciones
    private PlanCosecha planCosecha;
    private final ArrayList<CosechadorAsignado> cosechadoresAsignados = new ArrayList<>();
    private Supervisor supervisor;

    //Constructor
    public Cuadrilla(int id, String nom, Supervisor sup, PlanCosecha plan) {
        this.id = id;
        this.nombre = nom;
        this.planCosecha = plan;
        this.supervisor = sup;
        //Correción hecha por: Gabriel
        //Se realiza la asociación, añadiendo esta cuadrilla con el supervisor pasado por el constructor.
        supervisor.setCuadrilla(this);
        maximoCosechadores = 1;
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

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public PlanCosecha getPlanCosecha() {
        return planCosecha;
    }

    public boolean addCosechador(LocalDate fIni, LocalDate fFin, double meta, Cosechador cos) throws GestionHuertosException {
        if(findCosechadorByRut(cos).isPresent()) {
            throw new GestionHuertosException("Ya existe un cosechador en la cuadrilla con el mismo rut del cosechador recibido como parámetro");
        }
        if(cosechadoresAsignados.size() >= maximoCosechadores) {
            throw new GestionHuertosException("No es posible agregar el nuevo cosechador porque ya se alcanzó el máximo número de cosechadores en una cuadrilla");
        }
        return cosechadoresAsignados.add(new CosechadorAsignado(fIni, fFin, meta, this, cos));
    }

    public Cosechador[] getCosechadores() {
        //CORECCION
        //Este metodo devolvia cosechadores asignados:
        //return cosechadoresAsignados.toArray(new Cosechador[0])
        //se necesitaban objetos de Cosechador.
        ArrayList<Cosechador> cosechadores = new ArrayList<>();
        for (CosechadorAsignado cosechador : cosechadoresAsignados) {
            cosechadores.add(cosechador.getCosechador());
        }
        return cosechadores.toArray(new Cosechador[0]);
    }

    public double getKilosPesados() { //Revisar
        double totalKilosPesados = 0, pesajeTotalPorCosechador=0;
        for(CosechadorAsignado cosAs :  cosechadoresAsignados) {
            //CORRECION
            //Cosechador no tienen el metodo getPesajes, Cosechador asignado si.
            for(Pesaje pesaje : cosAs.getPesajes()) {
                pesajeTotalPorCosechador += pesaje.getCantidadKg();
            }
            totalKilosPesados += pesajeTotalPorCosechador;
        }
        return totalKilosPesados;
    }

    public CosechadorAsignado[] getAsignaciones() {
        return cosechadoresAsignados.toArray(new CosechadorAsignado[0]);
    }

    public static int getMaximoCosechadores() {
        return maximoCosechadores;
    }

    public static void setMaximoCosechadores(int max) {
        maximoCosechadores = max;
    }

    private Optional<CosechadorAsignado> findCosechadorByRut(Cosechador cosechador){
        for (CosechadorAsignado cosAs: cosechadoresAsignados) {
            if (cosAs.getCosechador().getRut().equals(cosechador.getRut())){
                return Optional.of(cosAs);
            }
        }
        return Optional.empty();
    }
}
