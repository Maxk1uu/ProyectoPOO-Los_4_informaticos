//Codigo hecho por: Maximiliano Maureira
//Revisado por:
import java.util.ArrayList;

public class Propietario extends Persona {
    //Atributos
    private String direccionComercial;

    //Relaciones
    ArrayList<Huerto> huertos = new ArrayList<>();

    //Constructor (Creado por Generate)
    public Propietario(Rut rut, String nom, String email, String dir, String direccionComercial) {
        super(rut, nom, email, dir);
        this.direccionComercial = direccionComercial;
    }

    //Metodos
    public String getDirComercial() {
        return direccionComercial;
    }

    public void setDirComercial(String direccionComercial) {
        this.direccionComercial = direccionComercial;
    }

    public boolean addHuerto(Huerto huerto) {
        if (huertos.contains(huerto)) { //Si el huerto ya está en el ArrayList, retorna falso.
            return false;
        } else {
            huertos.add(huerto);
            return true;
        }
    }

    public Huerto[] getHuertos() {
        return huertos.toArray(Huerto[0]); //Convierte el ArrayList completo en arreglo.
    }
}
