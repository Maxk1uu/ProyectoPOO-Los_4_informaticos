package modelo;//Revisado por: Gabriel Rojas
import java.time.LocalDate;
import java.util.ArrayList;

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

    public boolean addCosechador(LocalDate fIni, LocalDate fFin, double meta, Cosechador cos) {
            //Aquí se encontraba un equals entre dos objetos con diferentes clases, se encontró una forma de arreglarlo.
            if(findCosechadorByRut(cos) == null){ //Busca al cosechador entre los cosechadores asignados para asegurar que no existe un cosechador repetido.
                CosechadorAsignado cosechador = new CosechadorAsignado(fIni, fFin, meta, this,  cos);
                if (!cosechadoresAsignados.contains(cosechador) && cosechadoresAsignados.size() < maximoCosechadores) {
                    return cosechadoresAsignados.add(cosechador);
                }
            }
        return false;
    }

    public Cosechador[] getCosechadores() {
        Cosechador[] listaCos = new Cosechador[cosechadoresAsignados.size()];
        for (int i = 0; i < cosechadoresAsignados.size(); i++) {
            listaCos[i] = cosechadoresAsignados.get(i).getCosechador();
        }
        return listaCos;
    }

    public static int getMaximoCosechadores() {
        return maximoCosechadores;
    }

    public static void setMaximoCosechadores(int max) {
        maximoCosechadores = max;
    }

    private CosechadorAsignado findCosechadorByRut(Cosechador cosechador){
        for (CosechadorAsignado cosAs: cosechadoresAsignados) {
            if (cosAs.getCosechador().getRut().equals(cosechador.getRut())){
                return cosAs;
            }
        }
        return null;
    }
}
