package vista;
import controlador.ControlProduccion;
import utilidades.Rut;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GestionHuertosUI {
    //Atributos
    private final Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n\\r]+");

    //Singleton
    private static GestionHuertosUI instance;
    private GestionHuertosUI() {}
    public static GestionHuertosUI getInstance() {
        if (instance == null) {
            instance = new GestionHuertosUI();
        }
        return instance;
    }

    //Relaciones
    private final ControlProduccion controlProduccion = ControlProduccion.getInstance();

    //Metodos
    public void menu() {
        int opcion;
        do {
            System.out.println("\n*** SISTEMA DE GESTION DE HUERTOS ***\n");
            System.out.println("----------Menú de Opciones----------");
            System.out.println("1. Crear Personas");
            System.out.println("2. Menu Huertos");
            System.out.println("3. Menu Planes de Cosecha");
            System.out.println("4. Menu Listados");
            System.out.println("5. Salir.");
            System.out.print("-Opcion: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    creaPersona();
                    break;
                case 2:
                    creaCultivo();
                    break;
                case 3:
                    creaHuerto();
                    break;
                case 4:
                    creaPlanDeCosecha();
                    break;
                case 5:
                    asignaCosechadoresAPlan();
                    break;
                case 6:
                    listaCultivos();
                    break;
                case 7:
                    listaHuertos();
                    break;
                case 8:
                    listaPersonas();
                    break;
                case 9:
                    listaPlanesCosecha();
                    break;
                case 10:
                    System.out.println("Cerrando programa...");
                    break;
                default: System.out.println("La opción seleccionada no existe.");
            }
        } while (opcion != 10);
    }

    private void menuHuertos() {
        int opcion;
        System.out.println("<< SUBMENU HUERTOS >>");
        System.out.println("1. Crear Cultivo");
        System.out.println("2. Crear Huerto");
        System.out.println("3. Agregar Cuarteles a Huertos");
        System.out.println("4. Cambiar Estado del Cuartel");
        System.out.println("5. Volver");
        System.out.println("\tOpcion: ");
        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                creaCultivo();
                break;
            case 2:
                creaHuerto();
                break;
            case 3:
                creaPlanDeCosecha();
                break;
        }
    }

    private void creaPersona() {
        int rol;
        String nombre, email, direccion;
        System.out.println("\n--Creando una Persona---");
        System.out.print("> Rol Persona ([1] Propietario ; [2] Supervisor ; [3] Cosechador): ");
        rol = sc.nextInt();
        System.out.print("> Rut: ");
        Rut rut = new Rut(sc.next());
        nombre = leerTextoNoVacio("> Nombre: ");
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
                    System.out.println("\nEl modelo.Propietario a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya se encuentra registrado como modelo.Propietario.");
                }
            }
            case 2 -> { //modelo.Supervisor
                boolean supervisorCreado;
                System.out.print("> Profesion: ");
                String profesion = sc.next();
                supervisorCreado = controlProduccion.createSupervisor(rut, nombre, email, direccion, profesion);
                if(supervisorCreado) {
                    System.out.println("\nEl modelo.Supervisor a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya se encuentra registrado como modelo.Supervisor.");
                }
            }
            case 3 -> { //modelo.Cosechador
                boolean cosechadorCreado;
                System.out.print("> Fecha de Nacimiento (dd/mm/aaaa): ");
                LocalDate fNac = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                cosechadorCreado = controlProduccion.createCosechador(rut, nombre, email, direccion, fNac);
                if(cosechadorCreado) {
                    System.out.println("\nEl modelo.Cosechador a sido creado exitosamente.");
                } else {
                    System.out.println("\nEl rut ya se encuentra registrado como modelo.Cosechador.");
                }
            }
            default -> {
                System.out.println("Rol de Persona no valido. Por favor ingrese un rol valido.");}
        }
    }

    private void creaCultivo() {
        boolean cultivoCreado;
        int id;
        String especie, variedad;
        float rendimiento;
        System.out.println("\n---Creando un modelo.Cultivo---");
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
            System.out.println("\nEl modelo.Cultivo a sido creado exitosamente.");
        } else {
            System.out.println("\nYa existe un modelo.Cultivo con esa identificacion.");
        }
    }

    private void creaHuerto() {
        boolean huertoCreado, cuartelCreado;
        String nombreHuerto, ubicacion;
        float superficieHuerto, superficieCuartel;
        Rut rutPropietario;
        int nroCuarteles, idCuartel, idCultivo;
        System.out.println("\n---Creando modelo.Huerto---");
        System.out.print("> Nombre: ");
        nombreHuerto = sc.next();
        System.out.print("> Ubicacion: ");
        ubicacion = sc.next();
        System.out.print("> Superficie (metros cuadrados): ");
        superficieHuerto = sc.nextFloat();
        System.out.print("> utilidades.Rut modelo.Propietario: ");
        rutPropietario = new Rut(sc.next());
        huertoCreado = controlProduccion.createHuerto(nombreHuerto, superficieHuerto, ubicacion, rutPropietario);
        if (!huertoCreado) {
            System.out.println("\nNo fue posible crear el modelo.Huerto. Verifique que el utilidades.Rut existe y que el nombre del modelo.Huerto sea unico.");
        } else {
            System.out.println("\nEl modelo.Huerto a sido creado exitosamente.");
            System.out.println("\n-Agregando Cuarteles al modelo.Huerto-");
            System.out.print("> Nro. de Cuarteles: ");
            nroCuarteles = sc.nextInt();
            for (int i = 1; i <= nroCuarteles; i++) {
                System.out.print("\n> ID del cuartel: ");
                idCuartel = sc.nextInt();
                System.out.print("> Superficie del cuartel: ");
                superficieCuartel = sc.nextFloat();
                System.out.print("> ID del cultivo del cuartel: ");
                idCultivo = sc.nextInt();
                cuartelCreado = controlProduccion.addCuartelToHuerto(nombreHuerto, idCuartel, superficieCuartel, idCultivo);
                if(cuartelCreado){
                    System.out.println("\nmodelo.Cuartel agregado exitosamente al huerto.");
                } else {
                    System.out.println("\nNo fue posible crear el modelo.Cuartel. Verifique que el modelo.Cultivo con id: "+idCultivo+" existe y que el id del modelo.Cuartel no se repita en el modelo.Huerto.");
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
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Rut rutSupervisor;
        System.out.println("\n---Creando Plan de Cosecha---");
        System.out.print("> ID del plan : ");
        idPlanDeCosecha = sc.nextInt();
        System.out.print("> Nombre del plan: ");
        nombrePlanDeCosecha = sc.next();
        System.out.print("> Fecha de inicio (dd/mm/yyyy): ");
        fechaInicio = LocalDate.parse(sc.next(), formato);
        System.out.print("> Fecha de termino (dd/mm/yyyy): ");
        fechaTermino = LocalDate.parse(sc.next(), formato);
        System.out.print("> Meta (Kilos): ");
        metaKilos = sc.nextDouble();
        System.out.print("> Precio de Base por Kilo: ");
        precioBaseKilos = sc.nextDouble();
        System.out.print("> Nombre del modelo.Huerto: ");
        nombreHuerto = sc.next();
        System.out.print("> ID del modelo.Cuartel: ");
        idCuartel = sc.nextInt();
        planDeCosechaCreado = controlProduccion.createPlanCosecha(idPlanDeCosecha, nombrePlanDeCosecha, fechaInicio, fechaTermino, metaKilos, precioBaseKilos, nombreHuerto, idCuartel);
        if(!planDeCosechaCreado){
            System.out.println("\nNo fue posible crear el Plan de Cosecha. Posibles razones:");
            System.out.println("1. El ID del plan de Cosecha ya está registrado.");
            System.out.println("2. No existe el modelo.Huerto.");
            System.out.println("3. No existe el modelo.Cuartel");
            System.out.println("4. La fecha de inicio es posterior a la fecha de termino.");
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
                System.out.print("> utilidades.Rut del modelo.Supervisor: ");
                rutSupervisor = new Rut(sc.next());
                cuadrillaCreada = controlProduccion.addCuadrillaToPlan(idPlanDeCosecha, idCuadrilla, nombreCuadrilla, rutSupervisor);
                if(cuadrillaCreada){
                    System.out.println("\nmodelo.Cuadrilla agregada exitosamente al Plan de Cosecha.");
                } else {
                    System.out.println("\nNo fue posible agregar la modelo.Cuadrilla al Plan. Posibles razones:");
                    System.out.println("1. Ya existe una modelo.Cuadrilla con ese ID");
                    System.out.println("2. No se encontro a ningún modelo.Supervisor con el rut dado.");
                    System.out.println("3. El modelo.Supervisor ya tiene asignada una modelo.Cuadrilla.");
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
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n---Asignando Cosechadores a Plan de Cosecha---");
        System.out.print("> ID del Plan: ");
        idPlan = sc.nextInt();
        System.out.print("> ID de la modelo.Cuadrilla: ");
        idCuadrilla = sc.nextInt();
        System.out.print("> Nro. de Cosechadores a asignar: ");
        nroCosechadores = sc.nextInt();
        for (int i = 1; i <= nroCosechadores; i++) {
            System.out.print("\n> Fecha de Inicio de asignacion (dd/mm/yyyy): ");
            fechaInicioAsignacion = LocalDate.parse(sc.next(), formato);
            System.out.print("> Fecha de Termino de asignacion (dd/mm/yyyy): ");
            fechaTerminoAsignacion = LocalDate.parse(sc.next(), formato);
            System.out.print("> Meta (Kilos): ");
            metaKilos = sc.nextDouble();
            System.out.print("> utilidades.Rut del modelo.Cosechador: ");
            rutCosechador = new Rut(sc.next());
            cosechadorAsignado = controlProduccion.addCosechadorToCuadrilla(idPlan, idCuadrilla, fechaInicioAsignacion, fechaTerminoAsignacion, metaKilos, rutCosechador);
            if(cosechadorAsignado){
                System.out.println("\nmodelo.Cosechador asignado exitosamente a la cuadrilla del plan de Cosecha");
            } else {
                System.out.println("\nNo fue posible asignar el modelo.Cosechador a la modelo.Cuadrilla. Posibles razones:");
                System.out.println("1. No existe un Plan de cosecha con el ID dado.");
                System.out.println("2. No existe modelo.Cuadrilla con el ID dado.");
                System.out.println("3. No existe un modelo.Cosechador con el rut dado.");
                System.out.println("4. Las fechas de asignacion estan fuera del rango de las fechas del Plan de Cosecha");
            }
        }
    }

    private void listaCultivos() { //Falta agregar nroCuarteles, pero no entiendo a que se refierexdd
        String[] listaDeCultivos = controlProduccion.listCultivos();
        if(listaDeCultivos.length != 0){
            System.out.println("\nLISTA DE CULTIVOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%n", "ID", "Especie", "Variedad", "Rendimiento", "Nro. Cuarteles");
            for(String cultivo: listaDeCultivos){
                String[] infoCultivo = cultivo.split(", ");
                System.out.printf("%-20s%-25s%-25s%-30s%-30s%n", infoCultivo[0], infoCultivo[1], infoCultivo[2], infoCultivo[3], infoCultivo[4]);
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay cultivos registrados.");
        }
    }

    private void listaHuertos() {
        String[] listaDeHuertos = controlProduccion.listHuertos();
        if(listaDeHuertos.length != 0){
            System.out.println("\nLISTA DE HUERTOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Nombre", "Superficie", "Ubicacion", "utilidades.Rut del modelo.Propietario", "Nombre del modelo.Propietario", "Nro. Cuarteles");
            for(String huerto: listaDeHuertos){
                String[] infoHuerto = huerto.split(", ");
                System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoHuerto[0], infoHuerto[1], infoHuerto[2], infoHuerto[3], infoHuerto[4], infoHuerto[5]);
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay huertos registrados.");
        }
    }

    private void listaPersonas() {
        if( controlProduccion.listPropietarios().length != 0){
            System.out.println("\nLISTA DE PROPIETARIOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "utilidades.Rut", "Nombre", "Direccion", "Email", "Direccion Comercial", "Nro. de Huertos");
            for(String propietario: controlProduccion.listPropietarios()){
                if(propietario != null) {
                    String[] infoPropietario = propietario.split(", ");
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoPropietario[0], infoPropietario[1], infoPropietario[2], infoPropietario[3], infoPropietario[4], infoPropietario[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay propietarios registrados.");
        }
        if(controlProduccion.listSupervisores().length != 0){
            System.out.println("\nLISTA DE SUPERVISORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "utilidades.Rut", "Nombre", "Direccion", "Email", "Profesion", "Nombre modelo.Cuadrilla");
            for(String supervisor: controlProduccion.listSupervisores()){
                if(supervisor != null) {
                    String[] infoSupervisor = supervisor.split(", ");
                    if (infoSupervisor[5] == null) { //Esto es por si el supervisor no tiene cuadrilla asignada.
                        infoSupervisor[5] = "S/A";
                    }
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoSupervisor[0], infoSupervisor[1], infoSupervisor[2], infoSupervisor[3], infoSupervisor[4], infoSupervisor[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay supervisores registrados.");
        }
        if( controlProduccion.listCosechadores().length != 0){
            System.out.println("\nLISTA DE COSECHADORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "utilidades.Rut", "Nombre", "Direccion", "Email", "Fecha De Nacimiento", "Nro. de Cuadrillas");
            for(String cosechador: controlProduccion.listCosechadores()){
                if(cosechador != null) {
                    String[] infoCosechador = cosechador.split(", ");
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoCosechador[0], infoCosechador[1], infoCosechador[2], infoCosechador[3], infoCosechador[4], infoCosechador[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay cosechadores registrados.");
        }
    }
    private void listaPlanesCosecha() {
        if(controlProduccion.listPlanes().length != 0) {
            System.out.println("\nLISTA DE PLANES DE COSECHA");
            System.out.println("-----------------------------");
            System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", "ID", "Nombre", "Fecha de inicio", "Fecha de Termino", "Meta (kg)", "Precio Base (kg)", "Estado", "ID modelo.Cuartel", "Nombre del modelo.Huerto", "Nro. Cuadrillas");
            for(String plan : controlProduccion.listPlanes()){
                String[] infoPlan = plan.split(", ");
                System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", infoPlan[0], infoPlan[1], infoPlan[2], infoPlan[3], infoPlan[4], infoPlan[5], infoPlan[6], infoPlan[7],  infoPlan[8], infoPlan[9]);
            }
            System.out.println("-----------------------------");
        } else {
            System.out.println("\nNo hay planes de cosecha registrados.");
        }
    }

    private String leerTextoNoVacio(String mensaje) {
        String texto = "";
        boolean textoVacio = true;
        do {
            System.out.print(mensaje);
            texto = sc.next();
            if(!texto.isEmpty()){
                textoVacio = false;
            }
        } while(textoVacio);
        return texto;
    }
}
