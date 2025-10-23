package modelo;

import utilidades.Rut;

//Codigo hecho por: Maximiliano Maureira
//Revisado por: Gabriel Rojas
public class Supervisor extends Persona {
    //Atributos
    private String profesion;

    //Relaciones
    private Cuadrilla cuadrillaAsignada;

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
        this.profesion = profesion;
    }

    public void setCuadrilla(Cuadrilla cuad) {
        if(cuadrillaAsignada == null) { //Chequea que no tenga cuadrilla asignada.
            cuadrillaAsignada = cuad;
        }
    }

    public Cuadrilla getCuadrillaAsignada() {
        return cuadrillaAsignada;
    }
}
