package vista;
import controlador.ControlProduccion;
import modelo.PagoPesaje;
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
                    menuHuertos();
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
        rol = leerNumeroPositivo("> Rol: ");
        Rut rut = new Rut(leerTextoNoVacio("> Rut: "));
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
                LocalDate fNac = leerFechaExistente("> Fecha de Nacimiento (dd/mm/aaaa): ");
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
        id = leerNumeroPositivo("> Identificacion: ");
        especie = leerTextoNoVacio("> Especie: ");
        variedad = leerTextoNoVacio("> Variedad: ");
        rendimiento = leerNumeroPositivo("> Rendimiento : ");
        try {
            controlProduccion.createCultivo(id, especie, variedad, rendimiento); //En el controlador se verifica que no exista un cultivo con ese ID.
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
        superficieHuerto = leerFloatPositivo("> Superficie (metros cuadrados): ");
        rutPropietario = new Rut(leerTextoNoVacio("> Rut Propietario: "));
        try {
            controlProduccion.createHuerto(nombreHuerto, superficieHuerto, ubicacion, rutPropietario);
            System.out.println("\nEl Huerto a sido creado exitosamente.");
            System.out.println("\n-Agregando Cuarteles al Huerto-");
            nroCuarteles = leerNumeroPositivo("> Nro. de Cuarteles: ");
            for (int i = 1; i <= nroCuarteles; i++) {
                idCuartel = leerNumeroPositivo("\n> ID del cuartel: ");
                superficieCuartel = leerFloatPositivo("> Superficie del cuartel: ");
                idCultivo = leerNumeroPositivo("> ID del cultivo del cuartel: ");
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

    private void cambiaEstadoCuartel() {
        String nomHuerto;
        int idCuartel, opcion;
        EstadoFenologico newEstadoCuartel=null;
        boolean error;
        System.out.println("\n---Cambiando Estado del Cuartel---");
        idCuartel = leerNumeroPositivo("> ID del Cuartel: ");
        nomHuerto = leerTextoNoVacio("> Nombre del Huerto: ");
        do { //Eso hace que si o si la opcion sea un numero entre 1 y 7;
            error = false;
            System.out.println("> Nuevo estado del Cuartel ");
            System.out.printf("%-20s%-20s%n%-20s%-20s%n%-20s%-20s%n%-20s%n", "[1] Reposo Invernal",
                    "[2] Floracion", "[3] Cuaja", "[4] Fructificacion", "[5] Maduracion", "[6] Cosecha",
                    "[7] Postcosecha");
            opcion = leerNumeroPositivo("> Opcion: ");
            if(opcion < 1 || opcion > 7 ) {
                error = true;
                System.err.println("\nX Error: Opcion no valida. Por favor ingrese una opcion valida.\n");
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
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void creaPlanDeCosecha() {
        double metaKilos, precioBaseKilos;
        int idPlanDeCosecha, idCuartel, nroCuadrillas, idCuadrilla;
        String nombrePlanDeCosecha, nombreHuerto, nombreCuadrilla;
        LocalDate fechaInicio, fechaTermino;
        Rut rutSupervisor;
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
            System.out.println("\n-Agregando Cuadrillas al Plan de Cosecha-");
            nroCuadrillas = leerNumeroPositivo("Nro. de Cuadrillas: ");
            for (int i = 1; i <= nroCuadrillas; i++) {
                idCuadrilla = leerNumeroPositivo("> ID de la cuadrilla: ");
                nombreCuadrilla = leerTextoNoVacio("> Nombre de la Cuadrilla: ");
                rutSupervisor = new Rut(leerTextoNoVacio("> Rut del Supervisor: "));
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
            rutCosechador = new Rut(leerTextoNoVacio("> Rut del Cosechador: "));
            try {
                controlProduccion.addCosechadorToCuadrilla(idPlan, idCuadrilla, fechaInicioAsignacion,
                        fechaTerminoAsignacion, metaKilos, rutCosechador);
                System.out.println("\nCosechador asignado exitosamente a la cuadrilla del plan de Cosecha");
            } catch (GestionHuertosException e) {
                System.err.println("\nX Error: " + e.getMessage() + "\n");
            }
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
                System.err.println("\nX Error: Opcion no valida. Por favor ingrese una opcion valida.\n");
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
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void agregarPesajeCosechador() {
        int idPesaje, idPlan, idCuadrilla, opcion;
        float cantKilos;
        Rut rutCosechador;
        Calidad calidad = null;
        boolean error;
        System.out.println("\n---Agregando Pesaje a un Cosechador---");
        idPesaje = leerNumeroPositivo("> ID del Pesaje: ");
        rutCosechador = new Rut(leerTextoNoVacio("> Rut del Cosechador: "));
        idPlan = leerNumeroPositivo("> ID del Plan: ");
        idCuadrilla = leerNumeroPositivo("> ID de la Cuadrilla: ");
        cantKilos = leerFloatPositivo("> Cantidad de Kilos: ");
        do {
            error = false;
            System.out.println("> Calidad del Pesaje ");
            System.out.println("> [1] Excelente   [2] Suficiente   [3] Deficiente");
            opcion = leerNumeroPositivo("> Opcion: ");
            if(opcion < 1 || opcion > 3) {
                System.err.println("\nX Error: Opcion no valida. Por favor ingrese una opcion valida.\n");
                error = true;
            }
        } while (error);
        switch (opcion) {
            case 1 -> calidad = Calidad.EXCELENTE;
            case 2 -> calidad = Calidad.SUFICIENTE;
            case 3 -> calidad = Calidad.DEFICIENTE;
        }
        try{
            controlProduccion.addPesaje(idPesaje, rutCosechador, idPlan, idCuadrilla, cantKilos, calidad);
            System.out.println("\nPesaje agregado exitosamente.");
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void pagoPesajeCosechador() {
        int idPagoPesaje;
        Rut rutCosechador;
        System.out.println("\n---Pagando Pesaje a un Cosechador---");
        idPagoPesaje = leerNumeroPositivo("> ID del Pago de Pesaje: ");
        rutCosechador = new Rut(leerTextoNoVacio("> Rut del Cosechador: "));
        try {
            controlProduccion.addPagoPesaje(idPagoPesaje, rutCosechador);
            System.out.println("\nMonto Pagado al Cosechador: $"); //Aqui debe ir el monto a pagar pero aun no sé como debe ser.
        } catch (GestionHuertosException e) {
            System.err.println("\nX Error: "+e.getMessage() + "\n");
        }
    }

    private void listaCultivos() {
        String[] listaDeCultivos = controlProduccion.listCultivos();
        if(listaDeCultivos.length != 0){
            System.out.println("\nLISTA DE CULTIVOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%n", "ID", "Especie", "Variedad", "Rendimiento",
                    "Nro. Cuarteles");
            for(String cultivo: listaDeCultivos){
                String[] infoCultivo = cultivo.split(", ");
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
        if(listaDeHuertos.length != 0){
            System.out.println("\nLISTA DE HUERTOS");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Nombre", "Superficie", "Ubicacion",
                    "Rut del Propietario", "Nombre del Propietario", "Nro. Cuarteles");
            for(String huerto: listaDeHuertos){
                String[] infoHuerto = huerto.split(", ");
                System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoHuerto[0], infoHuerto[1], infoHuerto[2],
                        infoHuerto[3], infoHuerto[4], infoHuerto[5]);
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
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email",
                    "Direccion Comercial", "Nro. de Huertos");
            for(String propietario: controlProduccion.listPropietarios()){
                if(propietario != null) {
                    String[] infoPropietario = propietario.split(", ");
                    System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", infoPropietario[0], infoPropietario[1],
                            infoPropietario[2], infoPropietario[3], infoPropietario[4], infoPropietario[5]);
                }
            }
            System.out.println("----------------------");
        } else {
            System.out.println("\nNo hay propietarios registrados.");
        }
        if(controlProduccion.listSupervisores().length != 0){
            System.out.println("\nLISTA DE SUPERVISORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email", "Profesion",
                    "Nombre Cuadrilla");
            for(String supervisor: controlProduccion.listSupervisores()){
                if(supervisor != null) {
                    String[] infoSupervisor = supervisor.split(", ");
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
        if( controlProduccion.listCosechadores().length != 0){
            System.out.println("\nLISTA DE COSECHADORES");
            System.out.println("----------------------");
            System.out.printf("%-20s%-25s%-25s%-30s%-30s%-20s%n", "Rut", "Nombre", "Direccion", "Email",
                    "Fecha De Nacimiento", "Nro. de Cuadrillas");
            for(String cosechador: controlProduccion.listCosechadores()){
                if(cosechador != null) {
                    String[] infoCosechador = cosechador.split(", ");
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
        if(controlProduccion.listPlanes().length != 0) {
            System.out.println("\nLISTA DE PLANES DE COSECHA");
            System.out.println("-----------------------------");
            System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", "ID", "Nombre",
                    "Fecha de inicio", "Fecha de Termino", "Meta (kg)", "Precio Base (kg)", "Estado",
                    "ID Cuartel", "Nombre del Huerto", "Nro. Cuadrillas");
            for(String plan : controlProduccion.listPlanes()){
                String[] infoPlan = plan.split(", ");
                System.out.printf("%-8s%-25s%-20s%-20s%-15s%-20s%-15s%-15s%-25s%-16s%n", infoPlan[0], infoPlan[1],
                        infoPlan[2], infoPlan[3], infoPlan[4], infoPlan[5], infoPlan[6], infoPlan[7],  infoPlan[8],
                        infoPlan[9]);
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
            } catch (InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
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
            } catch (InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
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
            } catch (InputMismatchException e) { //Captura excepcion si el usuario ingresa un caracter en vez de un numero.
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

    private boolean comparaFechas(LocalDate fechaInicio, LocalDate fechaTermino) {
        if(fechaInicio.isAfter(fechaTermino)){
            System.err.println("\nX Error: La fecha de inicio es posterior a la fecha de termino.\n");
            return false;
        }
        return true;
    }
}
