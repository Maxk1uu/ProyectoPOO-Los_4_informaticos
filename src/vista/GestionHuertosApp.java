package vista;//Codigo hecho por: Maximiliano Maureira
//Revisado por: Gabriel Rojas y Ricardo Quintana

import utilidades.*;

import java.util.Scanner;

public class GestionHuertosApp {
    //Atributos
    private final Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n\\r]+"); //Si esto no funciona, hay que agregar el delimitador a cada metodo.

    //Relaciones
    private static final GestionHuertosUI gestionHuertosUI = GestionHuertosUI.getInstance();

    //Main
    public static void main(String[] args) {
        GestionHuertosApp app = new GestionHuertosApp();
        gestionHuertosUI.menu();
    }
}