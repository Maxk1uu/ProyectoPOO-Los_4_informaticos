import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GestionHuertosApp {
    //Atributos
    private Scanner sc = new Scanner(System.in);

    //Main
    public static void main(String[] args) {
        Scanner sc;

    }

    //Metodos
    private void menu() {
        int opcion;
        System.out.println("*** SISTEMA DE GESTION DE HUERTOS ***\n");
        System.out.println("----------Menú de Opciones----------");
        System.out.println("1. Crear Persona");
        System.out.println("2. Crear Cultivo");
        System.out.println("3. Crear Huerto");
        System.out.println("4. Crear Plan de Cosecha");
        System.out.println("5. Asignar Cosechadores a Plan");
        System.out.println("6. Listar Cultivos");
        System.out.println("7. Listar Huertos");
        System.out.println("8. Listar Personas");
        System.out.println("9. Listar Planes de Cosecha");
        System.out.println("10. Salir");
        System.out.print("-Opcion: ");
        opcion = sc.nextInt();
        switch (opcion) { //Falta terminar aqui, ir agregando los casos a medida que se crean los metodos
            case 1: creaPersona(); break;
        }
    }

    private void creaPersona() {
        sc.useDelimiter("[\\t\\n\\r]+");
        int rol;
        String nombre, email, direccion;
        System.out.println("--Creando una Persona---");
        System.out.print("> Rol Persona ([1] Propietario ; [2] Supervisor ; [3] Cosechador): ");
        rol = sc.nextInt();
        System.out.print("> Rut: ");
        Rut rut = new Rut(sc.next());
        System.out.print("> Nombre: ");
        nombre = sc.next();
        System.out.print("> Email: ");
        email = sc.next();
        System.out.print("> Direccion: ");
        direccion = sc.next();

        switch (rol) {
            case 1 -> { //Propietario
                if(!Rut.rutsPropietarios.contains(rut)) { //Si no está registrado el rut, se añade y se crea un nuevo propietario
                    Rut.rutsPropietarios.add(rut);
                    System.out.print("> Direccion Comercial: ");
                    String dirComercial = sc.next();
                    Propietario propietario = new Propietario(rut, nombre, email, direccion, dirComercial);
                    System.out.println("\nEl Propietario a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Propietario.");
                }
            }
            case 2 -> { //Supervisor
                if(!Rut.rutsSupervisores.contains(rut)) { //Si no está registrado el rut, se añade y se crea un nuevo supervisor
                    Rut.rutsSupervisores.add(rut);
                    System.out.print("> Profesion: ");
                    String profesion = sc.next();
                    Supervisor supervisor = new Supervisor(rut, nombre, email, direccion, profesion);
                    System.out.println("\nEl Supervisor a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Supervisor.");
                }
            }
            case 3 -> {
                if(!Rut.rutsCosechadores.contains(rut)) { //Si no está registrado el rut, se añade y se crea un nuevo cosechador
                    Rut.rutsCosechadores.add(rut);
                    System.out.print("> Fecha de Nacimiento (dd/mm/aaaa): ");
                    LocalDate fNac = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    Cosechador cosechador = new Cosechador(rut, nombre, email, direccion, fNac);
                    System.out.println("\nEl Cosechador a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Cosechador.");
                }
            }
        }
    }

}