// Hecho por: Ricardo Quintana
// Revisado por: Gabriel Rojas
package modelo;

import utilidades.EstadoFenologico;
import utilidades.GestionHuertosException;

import java.util.ArrayList;
import java.util.Optional;

public class Huerto {

    // Atributos
    private String nombre;
    private float superficie;
    private String ubicacion;

    // Relacion singular
    private Propietario propietario;
    // Relacion Multiple
    private final ArrayList<Cuartel> cuarteles;

    //Constructor
    public Huerto(String nombre, float superficie, String ubicacion, Propietario propietario) {
        this.nombre = nombre;
        this.superficie = superficie;
        this.ubicacion = ubicacion;
        this.propietario = propietario;
        cuarteles = new ArrayList<>();
        //Relacion se aplica.
        propietario.addHuerto(this);
    }

    //Metodos

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = (Propietario) propietario;
    }

    public void addCuartel(int id, float sup, Cultivo cult) throws GestionHuertosException {
        // Verifico si el cuartel pasado como parametro no existe en la lista
        if(getCuartelById(id).isEmpty()){
            // Verifico que la superficie del cuartel + los cuarteles que tiene el huerto, no supere la superficie del huerto
            float suma = 0;
            for (Cuartel c : cuarteles) {
                suma += c.getSuperficie();
            }
            suma += sup;
            if (suma <= superficie) {
                cuarteles.add(new Cuartel(id, sup, cult, this));
            } else {
                throw new GestionHuertosException("La superficie del cuartel supera el limite del huerto, al sumarle la superficie de sus cuarteles actuales");
            }
        } else {
            throw new GestionHuertosException("Ya existe en el huerto un cuartel con el id indicado");
        }
    }

    public Cuartel getCuartel(int id) {
        // Verifico si el cuartel pertence al modelo.Huerto
        if(getCuartelById(id).isPresent()){
            return getCuartelById(id).get();
        }
        return null; // si no lo encuentra retorna null
    }

    public Cuartel[] getCuartels() {
        return cuarteles.toArray(new Cuartel[0]);
    }

    public void setEstadoCuartel(int id, EstadoFenologico estadoFenologico) throws GestionHuertosException {
        if (getCuartelById(id).isEmpty()) {
            throw new GestionHuertosException("No existe un cuartel con el id indicado");
        }
        if (!getCuartelById(id).get().setEstado(estadoFenologico)) {
            throw new GestionHuertosException("No esta permitido el cambio de estado solicitado");
        }
    }

    public Optional<Cuartel> getCuartelById(int id) {
        for (Cuartel c : cuarteles) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
