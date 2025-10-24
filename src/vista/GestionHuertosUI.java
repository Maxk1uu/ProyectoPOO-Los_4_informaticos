package vista;
import controlador.ControlProduccion;
import utilidades.Rut;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        email = leerTextoNoVacio("> Email: ");
        direccion = leerTextoNoVacio("> Direccion: ");

        switch (rol) {
            case 1 -> { //Propietario
                String dirComercial = leerTextoNoVacio("> Direccion Comercial: ");
                try {
                    controlProduccion.createPropietario(rut, nombre, email, direccion, dirComercial);
                    System.out.println("\nPropietario creado exitosamente");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: "+e.getMessage() + "\n");
                }
            }
            case 2 -> { //Supervisor
                String profesion = leerTextoNoVacio("> Profesion: ");
                try {
                    controlProduccion.createSupervisor(rut, nombre, email, direccion, profesion);
                    System.out.println("\nSupervisor creado exitosamente.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: "+e.getMessage() + "\n");
                }
            }
            case 3 -> { //Cosechador
                System.out.print("> Fecha de Nacimiento (dd/mm/aaaa): ");
                LocalDate fNac = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                try {
                controlProduccion.createCosechador(rut, nombre, email, direccion, fNac);
                System.out.println("\nEl Cosechador a sido creado exitosamente.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: "+e.getMessage() + "\n");
                }

            }
            default -> {
                System.err.println("Rol de Persona no valido. Por favor ingrese un rol valido.");}
        }
    }

    private void creaCultivo() {
        int id;
        String especie, variedad;
        float rendimiento;
        System.out.println("\n---Creando un Cultivo---");
        System.out.print("> Identificacion: ");
        id = sc.nextInt();
        especie = leerTextoNoVacio("> Especie: ");
        variedad = leerTextoNoVacio("> Variedad: ");
        System.out.print("> Rendimiento : ");
        rendimiento = sc.nextFloat();
        try {
            controlProduccion.createCultivo(id, especie, variedad, rendimiento);
            System.out.println("\nEl Cultivo a sido creado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void creaHuerto() {
        String nombreHuerto, ubicacion;
        float superficieHuerto, superficieCuartel;
        Rut rutPropietario;
        int nroCuarteles, idCuartel, idCultivo;
        System.out.println("\n---Creando Huerto---");
        nombreHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        ubicacion = leerTextoNoVacio("> Ubicacion: ");
        System.out.print("> Superficie (metros cuadrados): ");
        superficieHuerto = sc.nextFloat();
        System.out.print("> Rut Propietario: ");
        rutPropietario = new Rut(sc.next());
        try {
            controlProduccion.createHuerto(nombreHuerto, superficieHuerto, ubicacion, rutPropietario);
            System.out.println("\nEl Huerto a sido creado exitosamente.");
            System.out.println("\n-Agregando Cuarteles al Huerto-");
            System.out.print("> Nro. de Cuarteles: ");
            nroCuarteles = sc.nextInt();
            for (int i = 1; i <= nroCuarteles; i++) {
                System.out.print("\n> ID del cuartel: ");
                idCuartel = sc.nextInt();
                System.out.print("> Superficie del cuartel: ");
                superficieCuartel = sc.nextFloat();
                System.out.print("> ID del cultivo del cuartel: ");
                idCultivo = sc.nextInt();

                try {
                    controlProduccion.addCuartelToHuerto(nombreHuerto, idCuartel, superficieCuartel, idCultivo);
                    System.out.println("\nCuartel agregado exitosamente al huerto.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: "+e.getMessage() + "\n");
                }
            }
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void creaPlanDeCosecha() {
        double metaKilos, precioBaseKilos;
        int idPlanDeCosecha, idCuartel, nroCuadrillas, idCuadrilla;
        String nombrePlanDeCosecha, nombreHuerto, nombreCuadrilla;
        LocalDate fechaInicio, fechaTermino;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Rut rutSupervisor;
        System.out.println("\n---Creando Plan de Cosecha---");
        System.out.print("> ID del plan : ");
        idPlanDeCosecha = sc.nextInt();
        nombrePlanDeCosecha = leerTextoNoVacio("> Nombre del Plan de Cosecha: ");
        System.out.print("> Fecha de inicio (dd/mm/yyyy): ");
        fechaInicio = LocalDate.parse(sc.next(), formato);
        System.out.print("> Fecha de termino (dd/mm/yyyy): ");
        fechaTermino = LocalDate.parse(sc.next(), formato);
        System.out.print("> Meta (Kilos): ");
        metaKilos = sc.nextDouble();
        System.out.print("> Precio de Base por Kilo: ");
        precioBaseKilos = sc.nextDouble();
        nombreHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        System.out.print("> ID del Cuartel: ");
        idCuartel = sc.nextInt();
        try {
            controlProduccion.createPlanCosecha(idPlanDeCosecha, nombrePlanDeCosecha, fechaInicio, fechaTermino, metaKilos, precioBaseKilos, nombreHuerto, idCuartel);
            System.out.println("\nPlan de Cosecha creado exitosamente.");
            System.out.println("\n-Agregando Cuadrillas al Plan de Cosecha-");
            System.out.print("Nro. de Cuadrillas: ");
            nroCuadrillas =  sc.nextInt();
            for (int i = 1; i <= nroCuadrillas; i++) {
                System.out.print("> ID de la cuadrilla: ");
                idCuadrilla = sc.nextInt();
                nombreCuadrilla = leerTextoNoVacio("> Nombre de la Cuadrilla: ");
                System.out.print("> Rut del Supervisor: ");
                rutSupervisor = new Rut(sc.next());
                try {
                controlProduccion.addCuadrillaToPlan(idPlanDeCosecha, idCuadrilla, nombreCuadrilla, rutSupervisor);
                    System.out.println("\nCuadrilla agregada exitosamente al Plan de Cosecha.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: "+e.getMessage() + "\n");
                }
            }
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void asignaCosechadoresAPlan() {
        int idPlan, idCuadrilla, nroCosechadores;
        LocalDate fechaInicioAsignacion, fechaTerminoAsignacion;
        double metaKilos;
        Rut rutCosechador;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n---Asignando Cosechadores a Plan de Cosecha---");
        System.out.print("> ID del Plan: ");
        idPlan = sc.nextInt();
        System.out.print("> ID de la Cuadrilla: ");
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
            System.out.print("> Rut del Cosechador: ");
            rutCosechador = new Rut(sc.next());
            try {
                controlProduccion.addCosechadorToCuadrilla(idPlan, idCuadrilla, fechaInicioAsignacion, fechaTerminoAsignacion, metaKilos, rutCosechador);
                System.out.println("\nCosechador asignado exitosamente a la cuadrilla del plan de Cosecha");
            } catch (GestionHuertosException e) {
                System.err.println("\nX Error: " + e.getMessage() + "\n");
            }
        }
    }

    private void listaCultivos() {
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
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Nombre", "Superficie", "Ubicacion", "Rut del Propietario", "Nombre del Propietario", "Nro. Cuarteles");
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
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email", "Direccion Comercial", "Nro. de Huertos");
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
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email", "Profesion", "Nombre Cuadrilla");
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
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email", "Fecha De Nacimiento", "Nro. de Cuadrillas");
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
            System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", "ID", "Nombre", "Fecha de inicio", "Fecha de Termino", "Meta (kg)", "Precio Base (kg)", "Estado", "ID Cuartel", "Nombre del Huerto", "Nro. Cuadrillas");
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
        do { //Bucle hasta que el usuario ingrese algo no vacio.
            System.out.print(mensaje);
            texto = sc.next();
            if(!texto.isEmpty()){
                textoVacio = false;
            }
        } while(textoVacio);
        return texto;
    }

    private int leerNumeroPositivo(String mensaje) {
        int numero = 0;
        boolean numeroNegativo = true;
        do { //Bucle hasta que el usuario ingrese un numero positivo.
            System.out.print(mensaje);
            numero = sc.nextInt();
            if (numero > 0) {
                numeroNegativo = false;
            }
        } while (numeroNegativo);
        return numero;
    }

    private LocalDate leerFechaExistente(String mensaje) {
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean fechaExistente;
        do {
            System.out.println(mensaje);
            try {
                fecha = LocalDate.parse(sc.next(), formato);
                fechaExistente = true;
            } catch (DateTimeParseException e) {
                System.err.println("\nX Error: la fecha es invalida o no cumple el formato dd/MM/yyyy.\n");
                fechaExistente = false;
            }
        } while (!fechaExistente);
        return fecha;
    }
}
