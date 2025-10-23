//Revisado por: Gabriel Rojas

import java.time.LocalDate;
import java.util.ArrayList;
public class Cosechador extends Persona {
    //Atributos
    private LocalDate fechaNacimiento;

    //Relaciones
    private final ArrayList<CosechadorAsignado> cosechadoresAsignados = new ArrayList<>();

    //Constructor (Creado por Generate)
    public Cosechador(Rut rut, String nom, String email, String dir, LocalDate fechaNacimiento) {
        super(rut, nom, email, dir);
        this.fechaNacimiento = fechaNacimiento;
    }

    //Metodos
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void addCuadrilla(CosechadorAsignado cosAs) {
        cosechadoresAsignados.add(cosAs);
    }

    public Cuadrilla[] getCuadrillas() {
        if (cosechadoresAsignados.isEmpty()) {
            return new Cuadrilla[0];
        }

        Cuadrilla[] cuadrillas = new Cuadrilla[cosechadoresAsignados.size()];
        for(int i = 0; i < cosechadoresAsignados.size(); i++) {
            cuadrillas[i] = cosechadoresAsignados.get(i).getCuadrilla(); //Guardo sólo la cuadrilla de cada cosechador.
        }
        return cuadrillas;
    }
}