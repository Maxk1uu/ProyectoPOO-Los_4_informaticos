package vista;//Codigo hecho por: Maximiliano Maureira
//Revisado por: Gabriel Rojas y Ricardo Quintana

import utilidades.*;

import java.util.Scanner;

public class GestionHuertosApp {
    //Relaciones
    private static final GestionHuertosUI gestionHuertosUI = GestionHuertosUI.getInstance();

    //Main
    public static void main(String[] args) {
        gestionHuertosUI.menu();
    }
}