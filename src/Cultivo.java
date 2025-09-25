import java.util.ArrayList;

public class Cultivo {
    // Atributos
    private int id;
    private String especie;
    private String variedad;
    private float rendimiento;
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
    // problema que ni idea de como arreglar
    public boolean addCuartel(Cuartel cuartel) {
        for (Cuartel Ncuartel : cuarteles) {
            if (Ncuartel.getId() == cuartel.getId()) {
                System.out.println("El id: " + cuartel.getId() + "del Cuatel a crear ya esta siendo utilizado");
                return false;
            }
        }
        cuarteles.add(cuartel);
        System.out.printf("Identificacion: %d\n Especie: %s\n Variedad: %s\n Rendimiento: %.2f\n Cultivo creado existosamente", getId(), getEspecie(), getVariedad(), getRendimiento());
        return true;
    }

    public Cuartel[] getCuarteles () {
        return cuarteles.toArray(new Cuartel[0]);
    }
}

