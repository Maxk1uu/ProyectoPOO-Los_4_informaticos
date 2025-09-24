//Codigo hecho por: Maximiliano Maureira
//Revisado por:
public class Rut {
    //Atributos
    private String rut;
    private String codIdentificador;

    //Constructor
    public Rut(String rut) {
        this.rut = rut;
        codIdentificador = rut.split("-")[1];
    }

    //Metodos
    @Override
    public String toString() {
        return rut;
    }
}
