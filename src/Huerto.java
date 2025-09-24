import java.util.ArrayList;

public class Huerto {
    //Atributos
    private String nombre;
    private float superficie;
    private String ubicacion;

    //hola
    //Relaciones
    private ArrayList<Propetario>propetarios; //Clase propetario por implementar

    //Constructor
    public Huerto(String nombre, float superficie, String ubicacion) {
        this.nombre = nombre;
        this.superficie = superficie;
        this.ubicacion = ubicacion;
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


}
