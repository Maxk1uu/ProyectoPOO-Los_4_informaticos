
import com.sun.security.jgss.GSSUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GestionHuertosApp {
    //Atributos
    private Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n\\r]+"); //Si esto no funciona, hay que agregar el delimitador a cada metodo.

    //Relaciones
    private final ControlProduccion controlProduccion = new ControlProduccion();

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
            case 2: creaCultivo(); break;
            case 3: creaHuerto(); break;
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
            case 3 -> { //Cosechador
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

    private void creaCultivo() {
        boolean cultivoCreado;
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
        cultivoCreado = controlProduccion.createCultivo(id, especie, variedad, rendimiento);
        if (cultivoCreado) {
            System.out.println("El Cultivo a sido creado exitosamente.");
        } else {
            System.out.println("Ya existe un Cultivo con esa identificacion.");
        }
    }

    private void creaHuerto() {
        boolean huertoCreado, cuartelCreado;
        String nombreHuerto, ubicacion;
        float superficieHuerto, superficieCuartel;
        Rut rutPropietario;
        int nroCuarteles, idCuartel, idCultivo;
        Cultivo cultivo;
        System.out.println("---Creando Huerto---");
        System.out.print("> Nombre: ");
        nombreHuerto = sc.next();
        System.out.print("> Ubicacion: ");
        ubicacion = sc.next();
        System.out.print("> Superficie (metros cuadrados): ");
        superficieHuerto = sc.nextFloat();
        System.out.print("> Rut Propietario: ");
        rutPropietario = new Rut(sc.next());
        huertoCreado = controlProduccion.createHuerto(nombreHuerto, superficieHuerto, ubicacion, rutPropietario);
        if (!huertoCreado) {
            System.out.println("\nNo fue posible crear el Huerto. Verifique que el Rut existe y que el nombre del Huerto sea unico.");
        } else {
            System.out.println("\nEl Huerto a sido creado exitosamente.");
            System.out.println("\n-Agregando Cuarteles al Huerto-");
            System.out.print("> Nro. de Cuarteles: ");
            nroCuarteles = sc.nextInt();
            for (int i = 1; i <= nroCuarteles; i++) {
                System.out.print("\n> ID del cuartel: ");
                idCuartel = sc.nextInt();
                System.out.print("> Superficie del cuartel: ");
                superficieCuartel = sc.nextFloat();
                System.out.print("ID del cultivo del cuartel: ");
                idCultivo = sc.nextInt();
                cuartelCreado = controlProduccion.addCuartelToHuerto(nombreHuerto, idCuartel, superficieCuartel, idCultivo);
                if(cuartelCreado){
                    System.out.println("\nCuartel agregado exitosamente al huerto.");
                } else {
                    System.out.println("\nNo fue posible crear el Cuartel. Verifique que el Cultivo con id: "+idCultivo+" existe y que el id del Cuartel no se repita en el Huerto.");
                }
            }
        }
    }

    private void creaPlanDeCosecha() {
        boolean planDeCosechaCreado, cuadrillaCreada;
        double metaKilos, precioBaseKilos;
        int idPlanDeCosecha, idCuartel, nroCuadrillas, idCuadrilla;
        String nombrePlanDeCosecha, nombreHuerto, nombreCuadrilla;
        LocalDate fechaInicio, fechaTermino;
        Rut rutSupervisor;
        System.out.println("---Creando Plan de Cosecha---");
        System.out.print("> ID del plan : ");
        idPlanDeCosecha = sc.nextInt();
        System.out.print("> Nombre del plan: ");
        nombrePlanDeCosecha = sc.next();
        System.out.print("> Fecha de inicio (dd/mm/yyyy): ");
        fechaInicio = LocalDate.parse(sc.next());
        System.out.print("> Fecha de termino (dd/mm/yyyy): ");
        fechaTermino = LocalDate.parse(sc.next());
        System.out.print("> Meta (Kilos): ");
        metaKilos = sc.nextDouble();
        System.out.print("> Precio de Base por Kilo: ");
        precioBaseKilos = sc.nextDouble();
        System.out.print("> Nombre del Huerto: ");
        nombreHuerto = sc.next();
        System.out.print("> ID del Cuartel: ");
        idCuartel = sc.nextInt();
        planDeCosechaCreado = controlProduccion.createPlanCosecha(idPlanDeCosecha, nombrePlanDeCosecha, fechaInicio, fechaTermino, metaKilos, precioBaseKilos, nombreHuerto, idCuartel);
        if(!planDeCosechaCreado){
            System.out.println("\nNo fue posible crear el Plan de Cosecha. Posibles razones:");
            System.out.println("1. El ID del plan de Cosecha ya está registrado.");
            System.out.println("2. No existe el Huerto.");
            System.out.println("3. No existe el Cuartel");
        } else {
            System.out.println("\nPlan de Cosecha creado exitosamente.");
            System.out.println("\n-Agregando Cuadrillas al Plan de Cosecha-");
            System.out.print("Nro. de Cuadrillas: ");
            nroCuadrillas =  sc.nextInt();
            for (int i = 1; i <= nroCuadrillas; i++) {
                System.out.print("> ID de la cuadrilla: ");
                idCuadrilla = sc.nextInt();
                System.out.print("> Nombre de la cuadrilla: ");
                nombreCuadrilla = sc.next();
                System.out.print("> Rut del Supervisor: ");
                rutSupervisor = new Rut(sc.next());
                cuadrillaCreada = controlProduccion.addCuadrillaToPlan(idPlanDeCosecha, idCuadrilla, nombreCuadrilla, rutSupervisor);
                if(cuadrillaCreada){
                    System.out.println("\nCuadrilla agregada exitosamente al Plan de Cosecha.");
                } else {
                    System.out.println("\nNo fue posible realizar la accion. Posibles razones:");
                    System.out.println("1. Ya existe una Cuadrilla con ese ID");
                    System.out.println("2. No se encontro a ningún Supervisor con el rut dado.");
                    System.out.println("3. El Supervisor ya tiene asignada una Cuadrilla.");
                }
            }
        }
    }
    private void asignaCosechadoresAPlan() {
        boolean cosechadorAsignado;
        int idPlan, idCuadrilla, nroCosechadores;
        LocalDate fechaInicioAsignacion, fechaTerminoAsignacion;
        double metaKilos;
        Rut rutCosechador;
        System.out.println("---Asignando Cosechadores a Plan de Cosecha---");
        System.out.print("> ID del Plan: ");
        idPlan = sc.nextInt();
        System.out.print("> ID de la Cuadrilla: ");
        idCuadrilla = sc.nextInt();
        System.out.print("> Nro. de Cosechadores a asignar: ");
        nroCosechadores = sc.nextInt();
        for (int i = 1; i <= nroCosechadores; i++) {
            System.out.print("\n> Fecha de Inicio de asignacion (dd/mm/yyyy): ");
            fechaInicioAsignacion = LocalDate.parse(sc.next());
            System.out.print("> Fecha de Termino de asignacion (dd/mm/yyyy): ");
            fechaTerminoAsignacion = LocalDate.parse(sc.next());
            System.out.print("> Meta (Kilos): ");
            metaKilos = sc.nextDouble();
            System.out.print("> Rut del Cosechador: ");
            rutCosechador = new Rut(sc.next());
            cosechadorAsignado = controlProduccion.addCosechadorToCuadrilla(idPlan, idCuadrilla, fechaInicioAsignacion, fechaTerminoAsignacion, metaKilos, rutCosechador);
            if(cosechadorAsignado){
                System.out.println("\nCosechador asignado exitosamente a la cuadrilla del plan de Cosecha");
            } else {
                System.out.println("\nNo fue posible realizar la accion. Posibles razones:");
                System.out.println("1. No existe un Plan de cosecha con el ID dado.");
                System.out.println("2. No existe Cuadrilla con el ID dado.");
                System.out.println("3. No existe un Cosechador con el rut dado.");
                System.out.println("4. Las fechas de asignacion estan fuera del rango de las fechas del Plan de Cosecha");
            }
        }
    }

    private void listaCultivos() { //Falta agregar nroCuarteles, pero no entiendo a que se refierexdd
        String id, especie, variedad, rendimiento, nroCuarteles=null;
        String[] listaDeCultivos = controlProduccion.listCultivos();
        if(!listaDeCultivos[0].isEmpty()){
            System.out.printf("%-6s%-15s%-20s%-15s%-16s", "ID", "Especie", "Variedad", "Rendimiento", "Nro. Cuarteles");
            for(String cultivo: listaDeCultivos){
                String[] infoCultivo = cultivo.split(", ");
                id = infoCultivo[0];
                especie = infoCultivo[1];
                variedad = infoCultivo[2];
                rendimiento = infoCultivo[3];
                System.out.printf("%-6s%-15s%-20s%-15s%-16s", id, especie, variedad, rendimiento, nroCuarteles);
            }
        } else {
            System.out.println("No hay cultivos regitrados.");
        }
    }

}