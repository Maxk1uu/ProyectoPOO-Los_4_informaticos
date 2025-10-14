// Hecho por Ricardo Quintana
// Revisado por: Gabriel Rojas

import java.util.ArrayList;

public class Huerto {
    // Atributos
    private String nombre;
    private float superficie;
    private String ubicacion;

    // Relacion singular
    private Propietario propietario;
    // Relacion Multiple
    private final ArrayList <Cuartel> cuarteles;

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

    public boolean addCuartel(int id, float sup, Cultivo cult){
        // Verifico si el cuartel pasado como parametro no existe en la lista
        for(Cuartel c:cuarteles){
            if(c.getId() == id){ // si es true ya existe
                return false;
            }
        }
        return cuarteles.add(new Cuartel(id,sup,cult,this));
    }

    public Cuartel getCuartel(int id){
        // Verifico si el cuartel pertence al Huerto
        for(Cuartel c:cuarteles){
            if(c.getId() == id){
                return c;
            }
        }
        return null; // si no lo encuentra retorna null
    }

    public Cuartel [] getCuartels(){
        Cuartel [] cuartelesArr = new Cuartel [cuarteles.size()];
        int cont = 0; // contador para guardar en la posicion correcta
        for(Cuartel c:cuarteles){
            cuartelesArr[cont] = c;
            cont++;
        }
        return cuartelesArr;
    }
}