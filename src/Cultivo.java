import java.util.ArrayList;

public class Cultivo {
    // Atributos
    private int id;
    private String especie;
    private String variedad;
    private float rendimiento;

    //Relaciones
    private final ArrayList<Cuartel> cuarteles = new ArrayList<>();

    // Constructor
    public Cultivo(int id, String especie, String variedad, float rendimiento) {
        this.id = id;
        this.especie = especie;
        this.variedad = variedad;
        this.rendimiento = rendimiento;
    }

    // Metodos
    public int getId() {
        return id;
    }
    public String getEspecie(){
        return especie;
    }
    public String getVariedad(){
        return variedad;
    }
    public float getRendimiento(){
        return rendimiento;
    }
    public void setRendimiento(float rendimiento) {
        this.rendimiento = rendimiento;
    }
    public boolean addCuartel(Cuartel cuartel) {
        for (Cuartel Ncuartel : cuarteles) {
            if (Ncuartel.getId() == cuartel.getId()) {
                return false;
            }
        }
        return cuarteles.add(cuartel);
    }

    public Cuartel[] getCuarteles () {
        return cuarteles.toArray(new Cuartel[0]);
    }
}

