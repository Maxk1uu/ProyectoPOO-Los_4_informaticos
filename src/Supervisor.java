//Codigo hecho por: Maximiliano Maureira
//Revisado por:
public class Supervisor extends Persona {
    //Atributos
    private String profesion;

    //Relaciones
    Cuadrilla cuadrillaAsignada; //Asumo que la relacion es asi, ya que un supervisor solo puede ser asignado a 0 o 1 cuadrilla

    //Constructor (Creado por Generate)
    public Supervisor(Rut rut, String nom, String email, String dir, String profesion) {
        super(rut, nom, email, dir);
        this.profesion = profesion;
    }

    //Metodos
    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        profesion = profesion;
    }

    public void setCuadrilla(Cuadrilla cuad) {
        if(cuadrillaAsignada == null) { //Chequea que no tenga cuadrilla asignada.
            cuadrillaAsignada = cuad;
            System.out.println("Supervisor asignado.");
        } else {
            System.out.println("El supervisor ya tiene asignada una cuadrilla.");
        }
    }

    public Cuadrilla getCuadrillaAsignada() {
        return cuadrillaAsignada;
    }
}
