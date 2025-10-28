package modelo;//Revisado por: Gabriel Rojas

import utilidades.Rut;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

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
    //necesita revision.
    public Optional<CosechadorAsignado> getAsignacion(int idCuadrilla,int idPlanCosecha){
        for(CosechadorAsignado cosAs : cosechadoresAsignados) {
            if(cosAs.getCuadrilla().getPlanCosecha().getId() == idPlanCosecha){
                return Optional.of(cosAs);
            }
        }
        return Optional.empty();
    }
    public CosechadorAsignado[] getAsignaciones() {
        return cosechadoresAsignados.toArray(new CosechadorAsignado[0]);
    }
}