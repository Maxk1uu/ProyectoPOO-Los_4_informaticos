package vista;
//Codigo hecho por: Maximiliano Maureira (V2)
//Revisado por: Gabriel Rojas (V2)

public class GestionHuertosApp {
    //Relaciones
    private static final GestionHuertosUI gestionHuertosUI = GestionHuertosUI.getInstance();

    //Main
    public static void main(String[] args) {
        System.out.println("\n*** SISTEMA DE GESTION DE HUERTOS ***");
        gestionHuertosUI.menu();
    }
}