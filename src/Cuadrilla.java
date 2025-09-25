//Codigo hecho por: Maximiliano Maureira
//Revisado por:
import java.util.ArrayList;
import java.util.Date;

public class Cuadrilla {
    //Atributos
    private int id;
    private String nombre;
    private static int maximoCosechadores;

    //Relaciones
    PlanCosecha planCosecha;
    ArrayList<CosechadorAsignado> cosechadoresAsignados = new ArrayList<>();
    Supervisor supervisor;

    //Constructor
    public Cuadrilla(int id, String nom, Supervisor sup, PlanCosecha plan) {
        this.id = id;
        this.nombre = nom;
        this.planCosecha = plan;
        this.supervisor = sup;
        //Correción hecha por: Gabriel
        //Se realiza la asociación, añadiendo esta cuadrilla con el supervisor pasado por el constructor.
        supervisor.setCuadrilla(this);
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

    public boolean addCosechador(Date fIni, Date fFin, double meta, Cosechador cos) {
            //Aquí se encontraba un equals entre dos objetos con diferentes clases, se encontró una forma de arreglarlo.
            if(isEqualToAnotherCosechador(cos) != null){ //Busca al cosechador entre los cosechadores asignados para conseguir la cuadrilla en la que trabaja.
                Cuadrilla cuad = isEqualToAnotherCosechador(cos).getCuadrilla();//Ignorar la advertencia de IntelliJ, se colocó una condición para que el objeto que devuelve el metodo privado no sea null.
                CosechadorAsignado cosechador = new CosechadorAsignado(fIni, fFin, meta, cuad,  cos);
                if (cosechadoresAsignados.contains(cosechador) && cosechadoresAsignados.size() >= maximoCosechadores) {
                    return false; //Si el cosechador ya está asignado o si la cuadrilla ya está llena, retorna falso.
                }

                cosechadoresAsignados.add(cosechador);
                return true;
            }
        return false;
    }

    public Cosechador[] getCosechadores() {
        return cosechadoresAsignados.toArray(new Cosechador[0]);
    }

    public static int getMaximoCosechadores() {
        return maximoCosechadores;
    }

    public static void setMaximoCosechadores(int max) {
        maximoCosechadores = max;
    }
    private CosechadorAsignado isEqualToAnotherCosechador(Cosechador cosechador){
        for (CosechadorAsignado cosAs: cosechadoresAsignados) {
            if (cosAs.getCosechador().equals(cosechador)){
                return cosAs;
            }
        }
        return null;
    }
}
