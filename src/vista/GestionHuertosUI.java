package vista;
import controlador.ControlProduccion;
import utilidades.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GestionHuertosUI {
    //Atributos
    private final Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n\\r]+");

    //Singleton
    private static GestionHuertosUI instance;

    private GestionHuertosUI() {
    }

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
            System.out.println("::: Menú de Opciones :::");
            System.out.println("1. Crear Personas");
            System.out.println("2. Menu Huertos");
            System.out.println("3. Menu Planes de Cosecha");
            System.out.println("4. Menu Listados");
            System.out.println("5. Salir.");
            opcion = leerNumeroPositivo("\t> Opcion: ");
            switch (opcion) {
                case 1 -> creaPersona();
                case 2 -> menuHuertos();
                case 3 -> menuPlanesDeCosecha();
                case 4 -> menuListados();
                case 5 -> System.out.println("\nCerrando Sistema de Gestion de Huertos...\n");
                default -> System.out.println("\nX Error: La opcion seleccionada no existe.\n");
            }
        } while (opcion != 5);
    }

    private void menuHuertos() {
        int opcion;
        do {
            System.out.println("\n<< SUBMENU HUERTOS >>");
            System.out.println("1. Crear Cultivo");
            System.out.println("2. Crear Huerto");
            System.out.println("3. Agregar Cuarteles a Huertos");
            System.out.println("4. Cambiar Estado del Cuartel");
            System.out.println("5. Volver");
            opcion = leerNumeroPositivo("\t> Opcion: ");
            switch (opcion) {
                case 1 -> creaCultivo();
                case 2 -> creaHuerto();
                case 3 -> agregaCuartelesAHuerto();
                case 4 -> cambiaEstadoCuartel();
                case 5 -> {
                    System.out.println("\nVolviendo al menu...\n");
                    menu();
                }
                default -> System.out.println("\nX Error: La opcion seleccionada no existe.\n");
            }
        } while (opcion != 5);
    }

    private void menuPlanesDeCosecha() {
        int opcion;
        do {
            System.out.println("\n<< SUBMENU PLANES DE COSECHA >>");
            System.out.println("1. Crear Plan de Cosecha");
            System.out.println("2. Cambiar Estado de un Plan");
            System.out.println("3. Agregar Cuadrillas a un Plan");
            System.out.println("4. Agregar Cosechadores a un Plan");
            System.out.println("5. Agregar Pesajes a un Cosechador");
            System.out.println("6. Pagar Pesajes Impagos de un Cosechador");
            System.out.println("7. Volver");
            opcion = leerNumeroPositivo("\t> Opcion: ");
            switch (opcion) {
                case 1 -> creaPlanDeCosecha();
                case 2 -> cambiaEstadoPlan();
                case 3 -> agregaCuadrillasAPLan();
                case 4 -> asignaCosechadoresAPlan();
                case 5 -> agregaPesajeACosechador();
                case 6 -> pagaPesajesPendientesACosechador();
                case 7 -> {
                    System.out.println("\nVolviendo al menu...\n");
                    menu();
                }
                default -> System.out.println("\nX Error: La opcion seleccionada no existe.\n");
            }
        } while (opcion != 7);
    }

    private void menuListados() {
        int opcion;
        System.out.println("\n<< SUBMENU LISTADOS >>");
        System.out.println("1. Listado de Propietarios");
        System.out.println("2. Listado de Supervisores");
        System.out.println("3. Listado de Cosechadores");
        System.out.println("4. Listado de Cultivos");
        System.out.println("5. Listado de Huertos");
        System.out.println("6. Listado de Planes de Cosecha");
        System.out.println("7. Listado de Pesajes");
        System.out.println("8. Listado de Pesajes de un Cosechador");
        System.out.println("9. Listado de Pagos");
        System.out.println("10. Volver");
        opcion = leerNumeroPositivo("> Opcion: ");
        switch (opcion) {
            case 1 -> listaPropietarios();
            case 2 -> listaSupervisores();
            case 3 -> listaCosechadores();
            case 4 -> listaCultivos();
            case 5 -> listaHuertos();
            case 6 -> listaPlanesCosecha();
            case 7 -> listaPesajes();
            case 8 -> listaPesajesCosechadores();
            case 9 -> listaPagosPesajes();
            case 10 -> {
                System.out.println("\nVolviendo al menu...\n");
                menu();
            }
            default -> System.out.println("\nX Error: La opcion seleccionada no existe.\n");
        }
    }

    private void creaPersona() {
        int rol;
        String nombre, email, direccion;
        Rut rut;
        System.out.println("\n--Creando una Persona---");
        System.out.print("> Rol Persona ([1] Propietario ; [2] Supervisor ; [3] Cosechador): ");
        rol = leerNumeroPositivo("> Rol: ");
        rut = leerRutValido("> Rut: ");
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
                    System.err.println("\nX Error: " + e.getMessage() + "\n");
                }
            }
            case 2 -> { //Supervisor
                String profesion = leerTextoNoVacio("> Profesion: ");
                try {
                    controlProduccion.createSupervisor(rut, nombre, email, direccion, profesion);
                    System.out.println("\nSupervisor creado exitosamente.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: " + e.getMessage() + "\n");
                }
            }
            case 3 -> { //Cosechador
                LocalDate fNac = leerFechaExistente("> Fecha de Nacimiento (dd/mm/aaaa): ");
                try {
                    controlProduccion.createCosechador(rut, nombre, email, direccion, fNac);
                    System.out.println("\nEl Cosechador a sido creado exitosamente.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: " + e.getMessage() + "\n");
                }

            }
            default -> {
                System.err.println("\nX Error: Rol de Persona no valido.\n");
            }
        }
    }

    private void creaCultivo() {
        int id;
        String especie, variedad;
        float rendimiento;
        System.out.println("\n---Creando un Cultivo---");
        id = leerNumeroPositivo("> Identificacion: ");
        especie = leerTextoNoVacio("> Especie: ");
        variedad = leerTextoNoVacio("> Variedad: ");
        rendimiento = leerNumeroPositivo("> Rendimiento : ");
        try {
            controlProduccion.createCultivo(id, especie, variedad, rendimiento); //En el controlador se verifica que no exista un cultivo con ese ID.
            System.out.println("\nEl Cultivo a sido creado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void creaHuerto() {
        String nombreHuerto, ubicacion;
        float superficieHuerto;
        Rut rutPropietario;
        System.out.println("\n---Creando Huerto---");
        nombreHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        ubicacion = leerTextoNoVacio("> Ubicacion: ");
        superficieHuerto = leerFloatPositivo("> Superficie (metros cuadrados): ");
        rutPropietario = leerRutValido("> Rut Propietario: ");
        try {
            controlProduccion.createHuerto(nombreHuerto, superficieHuerto, ubicacion, rutPropietario);
            System.out.println("\nEl Huerto a sido creado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void agregaCuartelesAHuerto() {
        String nombreHuerto;
        int nroCuarteles, idCuartel, idCultivo;
        float superficieCuartel;
        try { //Captura el error si el nombre del Huerto no existe.
            System.out.println("\n---Agregando Cuarteles al Huerto---");
            nombreHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
            nroCuarteles = leerNumeroPositivo("> Nro. de Cuarteles: ");
            for (int i = 1; i <= nroCuarteles; i++) {
                idCuartel = leerNumeroPositivo("\n> ID del cuartel: ");
                superficieCuartel = leerFloatPositivo("> Superficie del cuartel: ");
                idCultivo = leerNumeroPositivo("> ID del cultivo del cuartel: ");
                try { //Captura el error si no se puede agregar el cuartel al huerto.
                    controlProduccion.addCuartelToHuerto(nombreHuerto, idCuartel, superficieCuartel, idCultivo);
                    System.out.println("\nCuartel agregado exitosamente al huerto.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: " + e.getMessage() + "\n");
                }
            }
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void cambiaEstadoCuartel() {
        String nomHuerto;
        int idCuartel, opcion;
        EstadoFenologico newEstadoCuartel = null;
        boolean error;
        System.out.println("\n---Cambiando Estado del Cuartel---");
        idCuartel = leerNumeroPositivo("> ID del Cuartel: ");
        nomHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        do { //Eso hace que si o si la opcion sea un numero entre 1 y 7
            error = false;
            System.out.println("> Nuevo estado del Cuartel ");
            System.out.printf("%-20s%-20s%n%-20s%-20s%n%-20s%-20s%n%-20s%n", "[1] Reposo Invernal",
                    "[2] Floracion", "[3] Cuaja", "[4] Fructificacion", "[5] Maduracion", "[6] Cosecha",
                    "[7] Postcosecha");
            opcion = leerNumeroPositivo("> Opcion: ");
            if (opcion < 1 || opcion > 7) {
                error = true;
                System.err.println("\nX Error: La opcion seleccionada no existe. \n");
            }
        } while (error);
        switch (opcion) {
            case 1 -> newEstadoCuartel = EstadoFenologico.REPOSO_INVERNAL;
            case 2 -> newEstadoCuartel = EstadoFenologico.FLORACION;
            case 3 -> newEstadoCuartel = EstadoFenologico.CUAJA;
            case 4 -> newEstadoCuartel = EstadoFenologico.FRUCTIFICACION;
            case 5 -> newEstadoCuartel = EstadoFenologico.MADURACION;
            case 6 -> newEstadoCuartel = EstadoFenologico.COSECHA;
            case 7 -> newEstadoCuartel = EstadoFenologico.POSTCOSECHA;
        }
        try {
            controlProduccion.changeEstadoCuartel(nomHuerto, idCuartel, newEstadoCuartel);
            System.out.println("\nEstado del Cuartel cambiado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void creaPlanDeCosecha() {
        double metaKilos, precioBaseKilos;
        int idPlanDeCosecha, idCuartel;
        String nombrePlanDeCosecha, nombreHuerto;
        LocalDate fechaInicio, fechaTermino;
        System.out.println("\n---Creando Plan de Cosecha---");
        idPlanDeCosecha = leerNumeroPositivo("> ID del plan : ");
        nombrePlanDeCosecha = leerTextoNoVacio("> Nombre del Plan de Cosecha: ");
        do {
            fechaInicio = leerFechaExistente("> Fecha de inicio (dd/mm/yyyy): ");
            fechaTermino = leerFechaExistente("> Fecha de termino (dd/mm/yyyy): ");
        } while (comparaFechas(fechaInicio, fechaTermino));
        metaKilos = leerDoublePositivo("> Meta (Kilos): ");
        precioBaseKilos = leerDoublePositivo("> Precio de Base por Kilo: ");
        nombreHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        idCuartel = leerNumeroPositivo("> ID del Cuartel: ");
        try {
            controlProduccion.createPlanCosecha(idPlanDeCosecha, nombrePlanDeCosecha, fechaInicio, fechaTermino,
                    metaKilos, precioBaseKilos, nombreHuerto, idCuartel);
            System.out.println("\nPlan de Cosecha creado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void cambiaEstadoPlan() {
        int idPlan, opcion;
        EstadoPlan newEstadoPlan = null;
        boolean error;
        System.out.println("\n---Cambiando Estado de un Plan---");
        idPlan = leerNumeroPositivo("> ID del Plan de Cosecha: ");
        do {
            error = false;
            System.out.println("> Nuevo Estado del Plan");
            System.out.printf("%-20s%-20s%n%-20s%-20s%n", "[1] Planificado", "[2] Ejecutando", "[3] Cerrado", "[4] Cancelado");
            opcion = leerNumeroPositivo("> Opcion: ");
            if (opcion < 1 || opcion > 4) {
                System.err.println("\nX Error: La opcion seleccionada no existe.\n");
                error = true;
            }
        } while (error);
        switch (opcion) {
            case 1 -> newEstadoPlan = EstadoPlan.PLANIFICADO;
            case 2 -> newEstadoPlan = EstadoPlan.EJECUTANDO;
            case 3 -> newEstadoPlan = EstadoPlan.CERRADO;
            case 4 -> newEstadoPlan = EstadoPlan.CANCELADO;
        }
        try {
            controlProduccion.changeEstadoPlan(idPlan, newEstadoPlan);
            System.out.println("\nEstado del Plan cambiado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void agregaCuadrillasAPLan() {
        String nombreCuadrilla;
        int idPlanDeCosecha, nroCuadrillas, idCuadrilla;
        Rut rutSupervisor;
        try {
            System.out.println("\n-Agregando Cuadrillas al Plan de Cosecha-");
            idPlanDeCosecha = leerNumeroPositivo("> ID del Plan de Cosecha: ");
            nroCuadrillas = leerNumeroPositivo("Nro. de Cuadrillas: ");
            for (int i = 1; i <= nroCuadrillas; i++) {
                idCuadrilla = leerNumeroPositivo("> ID de la cuadrilla: ");
                nombreCuadrilla = leerTextoNoVacio("> Nombre de la Cuadrilla: ");
                rutSupervisor = leerRutValido("> Rut del Supervisor: ");
                try { //Captura el error si no se puede agregar la cuadrilla al plan de cosecha.
                    controlProduccion.addCuadrillaToPlan(idPlanDeCosecha, idCuadrilla, nombreCuadrilla, rutSupervisor);
                    System.out.println("\nCuadrilla agregada exitosamente al Plan de Cosecha.");
                } catch (GestionHuertosException e) {
                    System.err.println("\nX Error: " + e.getMessage() + "\n");
                }
            }
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void asignaCosechadoresAPlan() {
        int idPlan, idCuadrilla, nroCosechadores;
        LocalDate fechaInicioAsignacion, fechaTerminoAsignacion;
        double metaKilos;
        Rut rutCosechador;
        System.out.println("\n---Asignando Cosechadores a Plan de Cosecha---");
        idPlan = leerNumeroPositivo("> ID del Plan: ");
        idCuadrilla = leerNumeroPositivo("> ID de la Cuadrilla: ");
        nroCosechadores = leerNumeroPositivo("> Nro. de Cosechadores a asignar: ");
        for (int i = 1; i <= nroCosechadores; i++) {
            do {
                fechaInicioAsignacion = leerFechaExistente("\n> Fecha de Inicio de asignacion (dd/mm/yyyy): ");
                fechaTerminoAsignacion = leerFechaExistente("> Fecha de Termino de asignacion (dd/mm/yyyy): ");
            } while (comparaFechas(fechaInicioAsignacion, fechaTerminoAsignacion));
            metaKilos = leerDoublePositivo("> Meta (Kilos): ");
            rutCosechador = leerRutValido("> Rut del Cosechador: ");
            try {
                controlProduccion.addCosechadorToCuadrilla(idPlan, idCuadrilla, fechaInicioAsignacion,
                        fechaTerminoAsignacion, metaKilos, rutCosechador);
                System.out.println("\nCosechador asignado exitosamente a la cuadrilla del plan de Cosecha");
            } catch (GestionHuertosException e) {
                System.err.println("\nX Error: " + e.getMessage() + "\n");
            }
        }
    }


    private void agregaPesajeACosechador() {
        int idPesaje, idPlan, idCuadrilla, opcion;
        float cantKilos;
        Rut rutCosechador;
        Calidad calidad = null;
        boolean error;
        System.out.println("\n---Agregando Pesaje a un Cosechador---");
        idPesaje = leerNumeroPositivo("> ID del Pesaje: ");
        rutCosechador = leerRutValido("> Rut del Cosechador: ");
        idPlan = leerNumeroPositivo("> ID del Plan: ");
        idCuadrilla = leerNumeroPositivo("> ID de la Cuadrilla: ");
        cantKilos = leerFloatPositivo("> Cantidad de Kilos: ");
        do {
            error = false;
            System.out.println("> Calidad del Pesaje ");
            System.out.println("> [1] Excelente   [2] Suficiente   [3] Deficiente");
            opcion = leerNumeroPositivo("> Opcion: ");
            if (opcion < 1 || opcion > 3) {
                System.err.println("\nX Error: Opcion no valida. Por favor ingrese una opcion valida.\n");
                error = true;
            }
        } while (error);
        switch (opcion) {
            case 1 -> calidad = Calidad.EXCELENTE;
            case 2 -> calidad = Calidad.SUFICIENTE;
            case 3 -> calidad = Calidad.DEFICIENTE;
        }
        try {
            controlProduccion.addPesaje(idPesaje, rutCosechador, idPlan, idCuadrilla, cantKilos, calidad);
            System.out.println("\nPesaje agregado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void pagaPesajesPendientesACosechador() {
        int idPagoPesaje;
        Rut rutCosechador;
        System.out.println("\n---Pagando Pesaje a un Cosechador---");
        idPagoPesaje = leerNumeroPositivo("> ID del Pago de Pesaje: ");
        rutCosechador = leerRutValido("> Rut del Cosechador: ");
        try {
            System.out.println("\nMonto Pagado al Cosechador: $" + controlProduccion.addPagoPesaje(idPagoPesaje, rutCosechador));
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: " + e.getMessage() + "\n");
        }
    }

    private void listaCultivos() {
        String[] listaDeCultivos = controlProduccion.listCultivos();
        if (listaDeCultivos.length != 0) {
            System.out.println("\nLISTA DE CULTIVOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%n", "ID", "Especie", "Variedad", "Rendimiento",
                    "Nro. Cuarteles");
            for (String cultivo : listaDeCultivos) {
                String[] infoCultivo = cultivo.split("; ");
                System.out.printf("%-20s%-25s%-25s%-30s%-30s%n", infoCultivo[0], infoCultivo[1], infoCultivo[2],
                        infoCultivo[3], infoCultivo[4]);
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay cultivos registrados.");
        }
    }

    private void listaHuertos() {
        String[] listaDeHuertos = controlProduccion.listHuertos();
        if (listaDeHuertos.length != 0) {
            System.out.println("\nLISTA DE HUERTOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Nombre", "Superficie", "Ubicacion",
                    "Rut del Propietario", "Nombre del Propietario", "Nro. Cuarteles");
            for (String huerto : listaDeHuertos) {
                String[] infoHuerto = huerto.split("; ");
                System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoHuerto[0], infoHuerto[1], infoHuerto[2],
                        infoHuerto[3], infoHuerto[4], infoHuerto[5]);
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay huertos registrados.");
        }
    }

    private void listaPropietarios() {
        if (controlProduccion.listPropietarios().length != 0) {
            System.out.println("\nLISTA DE PROPIETARIOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email",
                    "Direccion Comercial", "Nro. de Huertos");
            for (String propietario : controlProduccion.listPropietarios()) {
                if (propietario != null) {
                    String[] infoPropietario = propietario.split("; ");
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoPropietario[0], infoPropietario[1],
                            infoPropietario[2], infoPropietario[3], infoPropietario[4], infoPropietario[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay propietarios registrados.");
        }
    }

    private void listaSupervisores() {
        if (controlProduccion.listSupervisores().length != 0) {
            System.out.println("\nLISTA DE SUPERVISORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email", "Profesion",
                    "Nombre Cuadrilla");
            for (String supervisor : controlProduccion.listSupervisores()) {
                if (supervisor != null) {
                    String[] infoSupervisor = supervisor.split("; ");
                    if (infoSupervisor[5] == null) { //Esto es por si el supervisor no tiene cuadrilla asignada.
                        infoSupervisor[5] = "S/A";
                    }
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoSupervisor[0], infoSupervisor[1],
                            infoSupervisor[2], infoSupervisor[3], infoSupervisor[4], infoSupervisor[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay supervisores registrados.");
        }
    }

    private void listaCosechadores() {
        if (controlProduccion.listCosechadores().length != 0) {
            System.out.println("\nLISTA DE COSECHADORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email",
                    "Fecha De Nacimiento", "Nro. de Cuadrillas");
            for (String cosechador : controlProduccion.listCosechadores()) {
                if (cosechador != null) {
                    String[] infoCosechador = cosechador.split("; ");
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoCosechador[0], infoCosechador[1],
                            infoCosechador[2], infoCosechador[3], infoCosechador[4], infoCosechador[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay cosechadores registrados.");
        }
    }

    private void listaPlanesCosecha() {
        if (controlProduccion.listPlanes().length != 0) {
            System.out.println("\nLISTA DE PLANES DE COSECHA");
            System.out.println("-----------------------------");
            System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", "ID", "Nombre",
                    "Fecha de inicio", "Fecha de Termino", "Meta (kg)", "Precio Base (kg)", "Estado",
                    "ID Cuartel", "Nombre del Huerto", "Nro. Cuadrillas");
            for (String plan : controlProduccion.listPlanes()) {
                String[] infoPlan = plan.split("; ");
                System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", infoPlan[0], infoPlan[1],
                        infoPlan[2], infoPlan[3], infoPlan[4], infoPlan[5], infoPlan[6], infoPlan[7], infoPlan[8],
                        infoPlan[9]);
            }
            System.out.println("-----------------------------");
        } else {
            System.out.println("\nNo hay planes de cosecha registrados.");
        }
    }

    private void listaPesajes() {
        if (controlProduccion.listPesajes().length != 0) {
            System.out.println("\nLISTA DE PESAJES");
            System.out.println("----------------------------");
            System.out.printf("%-8s%-15s%-20s%-15s%-18s%-18s%-16s%-15s%n", "ID", "Fecha", "Rut Cosechador",
                    "Calidad", "Cantidad (kg)", "Precio por Kg", "Monto Total", "Fecha Pago");
            for (String pesaje : controlProduccion.listPesajes()) {
                String[] infoPesaje = pesaje.split("; ");
                System.out.printf("%-8s%-15s%-20s%-15s%-18s%-18s%-16s%-15s%n", infoPesaje[0], infoPesaje[1],
                        infoPesaje[2], infoPesaje[3], infoPesaje[4], infoPesaje[5], infoPesaje[6], infoPesaje[7]);
            }
            System.out.println("----------------------------");
        } else {
            System.err.println("\nNo hay pesajes registrados.");
        }
    }

    private void listaPesajesCosechadores() {
        System.out.println("\nLISTA DE PESAJES DEL COSECHADOR");
        Rut rutCosechador = leerRutValido("> Rut del Cosechador: ");
        if (controlProduccion.listPesajesCosechador(rutCosechador).length != 0) {
            System.out.println("--------------------------");
            System.out.printf("%-8s%-15s%-15s%-18s%-18s%-16s%-15s%n", "ID", "Fecha",
                    "Calidad", "Cantidad (kg)", "Precio por Kg", "Monto Total", "Fecha Pago");
            for (String pesajeCos : controlProduccion.listPesajesCosechador(rutCosechador)) {
                String[] infoPesajeCos = pesajeCos.split("; ");
                System.out.printf("%-8s%-15s%-15s%-18s%-18s%-16s%-15s%n", infoPesajeCos[0], infoPesajeCos[1],
                        infoPesajeCos[2], infoPesajeCos[3], infoPesajeCos[4], infoPesajeCos[5], infoPesajeCos[6]);
            }
            System.out.println("--------------------------");
        } else {
            System.err.println("\nNo hay pesajes registrados para el cosechador ingresado.");
        }
    }

    private void listaPagosPesajes() {
        System.out.println("\nLISTA DE PAGOS DE PESAJES");
        System.out.println("----------------------------");
        if (controlProduccion.listPagoPesajes().length != 0) {
            System.out.printf("%-8s%-15s%-16s%-17s%-20s%n", "ID", "Fecha Pago", "Monto Total", "Nro. Pesajes",
                    "Rut Cosechador");
            for (String pagos : controlProduccion.listPagoPesajes()) {
                String[] infoPagos = pagos.split("; ");
                System.out.printf("%-8s%-15s%-16s%-17s%-20s%n", infoPagos[0], infoPagos[1], infoPagos[2], infoPagos[3],
                        infoPagos[4]);
            }
            System.out.println("----------------------------");
        } else {
            System.err.println("\nNo hay pagos registrados.");
        }
    }

    private String leerTextoNoVacio(String mensaje) {
        String texto = "";
        boolean textoVacio = true;
        do { //Bucle hasta que el usuario ingrese algo no vacio.
            System.out.print(mensaje);
            texto = sc.next();
            if (!texto.isEmpty()) {
                textoVacio = false;
            }
        } while (textoVacio);
        return texto;
    }

    private int leerNumeroPositivo(String mensaje) { //Lee y devuelve un Entero.
        int numero = 0;
        boolean numeroNegativo = true;
        do { //Bucle hasta que el usuario ingrese un numero positivo.
            System.out.print(mensaje);
            try {
                numero = sc.nextInt();
                if (numero > 0) {
                    numeroNegativo = false;
                }
            } catch (
                    InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
                System.out.println("\nX Error: Solo se permiten numeros enteros.\n");
            }
        } while (numeroNegativo);
        return numero;
    }

    private float leerFloatPositivo(String mensaje) { //Lee y devuelve un Float.
        float nro = 0f;
        boolean nroNegativo = true;
        do {
            System.out.print(mensaje);
            try {
                nro = sc.nextFloat();
                if (nro > 0) {
                    nroNegativo = false;
                }
            } catch (
                    InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
                System.out.println("\nX Error: Solo se permiten numeros, no caracteres.\n");
            }
        } while (nroNegativo);
        return nro;
    }

    private double leerDoublePositivo(String mensaje) { //Lee y devuelve un Double.
        double nro = 0;
        boolean nroNegativo = true;
        do {
            try {
                System.out.print(mensaje);
                nro = sc.nextDouble();
                if (nro > 0) {
                    nroNegativo = false;
                }
            } catch (
                    InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
                System.out.println("\nX Error: Solo se permiten numeros, no caracteres.\n");
            }
        } while (nroNegativo);
        return nro;
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

    private Rut leerRutValido(String mensaje) {
        boolean rutInvalido = true;
        String rutStr = "";
        String formatoValido = "^[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]$";
        //^: Inicia la cadena
        //[0-9]: Cadena de numeros
        //{3}: Cadena de 3 digitos
        //\\. : Indica que hay un punto
        //[kK]: Indica que hay una k minus o mayus.
        //$: Finaliza la cadena
        do {
            System.out.print(mensaje);
            rutStr = sc.next();
            if (rutStr.matches(formatoValido)) {
                rutInvalido = false;
            } else {
                System.err.println("\nX Error: El rut ingresado no cumple el formato XX.XXX.XXX-X.\n");
            }
        } while (rutInvalido);
        return Rut.of(rutStr);
    }

    private boolean comparaFechas(LocalDate fechaInicio, LocalDate fechaTermino) {
        if(fechaInicio.isAfter(fechaTermino)){
            System.err.println("\nX Error: La fecha de inicio es posterior a la fecha de termino.\n");
            return false;
        }
        return true;
    }
}
