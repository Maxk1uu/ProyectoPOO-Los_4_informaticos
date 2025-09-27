import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionHuertosApp {
    //Atributos
    private Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n\\r]+"); //Si esto no funciona, hay que agregar el delimitador a cada metodo.

    //Relaciones
    private final ArrayList<ControlProduccion> controlProduccion = new ArrayList<>();

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
                boolean propietarioCreado;
                System.out.print("> Direccion Comercial: ");
                String dirComercial = sc.next();
                propietarioCreado = controlProduccion.createPropietario(rut, nombre, email, direccion, dirComercial);
                if (propietarioCreado) {
                    System.out.println("\nEl Propietario a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Propietario.");
                }
            }
            case 2 -> { //Supervisor
                boolean supervisorCreado;
                System.out.print("> Profesion: ");
                String profesion = sc.next();
                supervisorCreado = controlProduccion.createSupervisor(rut, nombre, email, direccion, profesion);
                if(supervisorCreado) {
                    System.out.println("\nEl Supervisor a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Supervisor.");
                }
            }
            case 3 -> {
                boolean cosechadorCreado;
                System.out.print("> Fecha de Nacimiento (dd/mm/aaaa): ");
                LocalDate fNac = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                cosechadorCreado = controlProduccion.createCosechador(rut, nombre, email, direccion, fNac);
                if(cosechadorCreado) {
                System.out.println("\nEl Cosechador a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya está registrado como Cosechador.");
                }
            }
        }
    }

    public void creaCultivo() { //Falta agregar la verificacion de si existe el id o no.
        int id;
        String especie, variedad;
        float rendimiento;
        System.out.println("---Creando un Cultivo---");
        System.out.print("> Identificacion: ");
        id = sc.nextInt();
        System.out.print("> Especie: ");
        especie = sc.next();
        System.out.print("> Variedad: ");
        variedad = sc.next();
        System.out.print("> Rendimiento : ");
        rendimiento = sc.nextFloat();
    }
    public void creaHuerto() {
        String nombre, ubicacion;
        float superficie;
        Rut rutPropietario;

        System.out.println("---Creando Huerto---");
        System.out.print("> Nombre: ");
        nombre = sc.next();
        System.out.print("> Ubicacion: ");
        ubicacion = sc.next();
        System.out.print("> Superficie (metros cuadrados): ");
        superficie = sc.nextFloat();
        System.out.print("> Rut Propietario: ");
        rutPropietario = new Rut(sc.next());
        if(!Rut.rutsPropietarios.contains(rutPropietario)) {
            System.out.println("No existe propietario con el rut indicado.");
        }
        if()

    }

}