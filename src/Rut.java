//Codigo hecho por: Maximiliano Maureira
//Revisado por:

import java.util.ArrayList;
public class Rut {
    //Atributos
    private String rut;
    private String codIdentificador;

    //Registro de ruts por Tipo de Persona
    public static ArrayList<Rut> rutsPropietarios = new ArrayList<>();
    public static ArrayList<Rut> rutsSupervisores = new ArrayList<>();
    public static ArrayList<Rut> rutsCosechadores = new ArrayList<>();

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
