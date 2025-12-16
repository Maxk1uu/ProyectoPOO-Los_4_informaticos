
package controlador;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import persistencia.GestionHuertosIO;
import utilidades.*;
import modelo.*;

public class ControlProduccion {
    //Asociaciones.
    //Advertencia: no dividir esta asociacion con sus multiples hijos, ya que se pueden guardar sin problema en esta coleccion.
    //Llamar y utilizar condiciones de instanceof y casting para llamar a clases hijas.

    private final ArrayList<Persona> personas = new ArrayList<>();
    private final ArrayList<Huerto> huertos = new ArrayList<>();
    private final ArrayList<Cultivo> cultivos = new ArrayList<>();
    private final ArrayList<PlanCosecha> planCosechas = new ArrayList<>();
    private final ArrayList<Pesaje> pesajes = new ArrayList<>();
    private final ArrayList<PagoPesaje> pagosPesajes = new ArrayList<>();
    private static ControlProduccion instance = null;

    private ControlProduccion() {
        try {
            readDataFromTextFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (GestionHuertosException e) {
            System.out.println("\nX Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static ControlProduccion getInstance() {
        if (instance == null) {
            instance = new ControlProduccion();
        }
        return instance;
    }

    //Creado por Gabriel Rojas
    public void createPropietario(Rut rut, String nombre, String email, String direccionParticular, String direccionComercial) throws GestionHuertosException {
        //Asegura que esta persona no existe,  si existe, retorna false.
        if (findPersona(rut).isPresent() && findPersona(rut).get() instanceof Propietario)
            throw new GestionHuertosException("Ya existe un propietario con el rut indicado");

        // De lo contrario, lo agrega a la colección.
        personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }

    //Creado por Gabriel Rojas
    public void createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) throws GestionHuertosException {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut).isPresent() && findPersona(rut).get() instanceof Supervisor)
            throw new GestionHuertosException("Ya existe un supervisor con el rut indicado");
        //Agrega el supervisor a la coleccion.
        personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    //Creado por Gabriel Rojas
    public void createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) throws GestionHuertosException {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut).isPresent() && findPersona(rut).get() instanceof Cosechador)
            throw new GestionHuertosException("Ya existe un cosechador con el rut indicado");
        //Agrega el cosechador a la coleccion.
        personas.add(new Cosechador(rut, nombre, email, direccion, fechaNacimiento));
    }

    //Creado por Gabriel Rojas
    public void createCultivo(int id, String nombre, String periodo, float rendimiento) throws GestionHuertosException {
        if (findCultivo(id).isPresent()) throw new GestionHuertosException("Ya existe un cultivo con el id indicado");
        cultivos.add(new Cultivo(id, nombre, periodo, rendimiento));
    }

    // Hecho por Ricardo Quintana
    public void createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) throws GestionHuertosException {
        // Primero verifico que no exista el huerto
        if (findHuerto(nombre).isPresent())
            throw new GestionHuertosException("Ya existe un huerto con el nombre indicado");
        if (findPersona(rutPropietario).isEmpty() || !(findPersona(rutPropietario).get() instanceof Propietario propietario))
            throw new GestionHuertosException("No existe un propietario con el rut indicado");
        // Verifico si es que el rut es de un propetario
        // Creo el huerto
        huertos.add(new Huerto(nombre, superficie, ubicacion, propietario));
        // Condicion que no se especifica en el uml
        // throw new GestionHuertosException("ERROR: Rut " + rutPropietario + " no pertenece a un Propietario.");
    }

    // Hecho por Ricardo Quintana
    public void addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {

        // Verificar que el huerto exista y que el cuartel no tenga un huerto asociado
        if (findHuerto(nombreHuerto).isEmpty())
            throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        if (findCultivo(idCultivo).isEmpty())
            throw new GestionHuertosException("No existe un cultivo con el id indicado");
        //Condición que no aparece en el enunciado, igual dejarla aquí porsiacaso.
        // if (findCuartel(idCuartel,nombreHuerto).isPresent()) throw new GestionHuertosException("ERROR: EL ID del Cuartel " + idCuartel+ " ya está asignado al Huerto.");
        findHuerto(nombreHuerto).get().addCuartel(idCuartel, superficie, findCultivo(idCultivo).get());
    }

    //Creado por Gabriel Rojas
    public void changeEstadoCuartel(String nombreHuerto, int idCuartel, EstadoFenologico estado) throws GestionHuertosException {
        //Excepciones
        if (findHuerto(nombreHuerto).isEmpty())
            throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        if (findCuartel(idCuartel, nombreHuerto).isEmpty())
            throw new GestionHuertosException("No existe en el huerto un cuartel con el id indicado");
        //Cambio de estado
        findHuerto(nombreHuerto).get().setEstadoCuartel(idCuartel, estado);
    }

    //Creado por Gabriel Rojas
    public void createPlanCosecha(int id, String nombrePlan, LocalDate fechaInicio, LocalDate fechaFin, double metaKilos, double precioBaseKilo, String nomHuerto, int idCuartel) throws GestionHuertosException {
        //Se asegura que el plan cosecha no exista, a traves de su id.
        //Si encuentra un plan cosecha existente, retorna false.
        if (findPlanCosecha(id).isPresent())
            throw new GestionHuertosException("Ya existe un plan cosecha con el id indicado");
        //Asegura que la fecha de inicio no sea supeerior o igual a la fecha de fin.
        //Condicion que se deja en standby
        //if (fechaInicio.isAfter(fechaFin) || fechaInicio.isEqual(fechaFin)) throw new GestionHuertosException("Intervalo de fechas no permitido.");
        //Si no existe el huerto pasado por parametros, retorna false.
        if (findHuerto(nomHuerto).isEmpty())
            throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        Huerto huertoEncontrado = findHuerto(nomHuerto).get();
        //Condicion que asegura que el cuartel pasado por parametros exista y sea parte del huerto.
        if (findCuartel(idCuartel, nomHuerto).isPresent()) {
            Cuartel cuartelEncontrado = findCuartel(idCuartel, nomHuerto).get();
            //Crea el nuevo plan de cosecha.
            planCosechas.add(new PlanCosecha(id, nombrePlan, fechaInicio, fechaFin, metaKilos, precioBaseKilo, cuartelEncontrado));
        } else {
            throw new GestionHuertosException("No existe en el huerto un cuartel con el id indicado");
        }
    }

    //Creado por Gabriel Rojas
    public void changeEstadoPlan(int idPlan, EstadoPlan estado) throws GestionHuertosException {
        if (findPlanCosecha(idPlan).isEmpty())
            throw new GestionHuertosException("No existe un plan cosecha con el id indicado");
        PlanCosecha plan = findPlanCosecha(idPlan).get();
        if (!plan.setEstado(estado))
            throw new GestionHuertosException("No esta permitido el cambio de estado solicitado");
    }

    //Creado por Gabriel Rojas
    public void addCuadrillaToPlan(int idPlan, int idCuadrilla, String nombreCuadrilla, Rut rutSupervisor) throws GestionHuertosException {
        //se hace un casting a la clase hija modelo.Supervisor, porque el metodo  findPersona retorna un objeto modelo.Persona.
        //Ademas, se asegura que el objeto recibido sea el objeto modelo.Supervisor.
        if (findPersona(rutSupervisor).isEmpty())
            throw new GestionHuertosException("No existe un supervisor con el rut indicado");
        if (findPersona(rutSupervisor).get() instanceof Supervisor supervisorEncontrado) {
            //Si encuentra una cuadrilla ya asignada al supervisor, retorna false.
            if (supervisorEncontrado.getCuadrillaAsignada() != null)
                throw new GestionHuertosException("El supervisor ya tiene asignada una cuadrilla a su cargo");
            //Utiliza el metodo private findPlanCosecha, asegura que este no sea null.
            if (findPlanCosecha(idPlan).isPresent()) {
                //Asigna este resultado a una variable.
                PlanCosecha plan = findPlanCosecha(idPlan).get();
            /*
            Utiliza otro metodo private llamado findCuadrilla, si ve que esta cuadrilla ya existe en su coleccion de cuadrillas
            Retornará la cuadrilla que encuentre.
            En este caso, no queremos que pase eso, porque estamos agregando una nueva cuadrilla
            que no exista en su coleccion.
            */
                if (findCuadrilla(idCuadrilla, plan.getId()).isEmpty()) {
                    plan.addCuadrilla(idCuadrilla, nombreCuadrilla, supervisorEncontrado);
                } else {
                    // Condicion que no se usa, dejar aquí porsiacaso
                    // throw new GestionHuertosException("ERROR: La Cuadrilla con el ID " + idCuadrilla +" ya está asignada a este Plan de Cosecha.");
                }
            } else {
                throw new GestionHuertosException("No existe un plan cosecha con el id indicado");
            }
        }
    }

    //Creado por Gabriel Rojas
    public void addCosechadorToCuadrilla(int idPlanCosecha, int idCuadrilla, LocalDate fechaIniCosechador, LocalDate fechaFinCosechador, double metaKilosCosechador, Rut rutCosechador) {

        //Condiciones que aseguran que estas variables existan, de lo contrario, devuelven false.
        if (findPlanCosecha(idPlanCosecha).isEmpty())
            throw new GestionHuertosException("No existe un plan cosecha con el id indicado");
        //Asigna los metodos findPlanCosecha, findPersona y findCuadrilla a variables para mejor legibilidad
        PlanCosecha plan = findPlanCosecha(idPlanCosecha).get();
        // Condicion que no se especifica en el enunciado.
        // if (findCuadrilla(idCuadrilla, plan.getId()).isEmpty()) throw new GestionHuertosException("ERROR: Cuadrilla con el ID "+ idCuadrilla+" no existe o no está asignada al Plan de Cosecha con el ID " + idPlanCosecha + ".");
        if (findPersona(rutCosechador).isEmpty())
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        //Condicion que asegura  que el objeto encontrado sea modelo.Cosechador, para asi no
        // castear un objeto modelo.Supervisor o modelo.Propietario, si es que se introduce un rut erroneo pero que existe
        if (findPersona(rutCosechador).get() instanceof Cosechador cosechadorEncontrado) {
            //booleano isAfter, que asegura que la fecha de inicio no es superior a la fecha final. Solo puede ser Inferior o Igual.
            //Además, asegura que la fecha de inicio y final este en el intervalo de tiempo del plan de cosecha.
            if (fechaIniCosechador.isAfter(fechaFinCosechador) || fechaIniCosechador.isBefore(plan.getInicio()) || fechaIniCosechador.isAfter(plan.getFinEstimado())
                    || fechaFinCosechador.isAfter(plan.getFinEstimado()) || fechaFinCosechador.isBefore(plan.getInicio()))
                throw new GestionHuertosException("El rango de fechas de asignación del cosechador a la cuadrilla está fuera del rango de fechas del plan");
            if (findCuadrilla(idCuadrilla, idPlanCosecha).isPresent() && findCuadrilla(idCuadrilla, idPlanCosecha).get().getCosechadores().length >= Cuadrilla.getMaximoCosechadores())
                throw new GestionHuertosException("El número de cosechadores ya alcanzó el máximo permitido");
            plan.addCosechadorToCuadrilla(idCuadrilla, fechaIniCosechador, fechaFinCosechador, metaKilosCosechador, cosechadorEncontrado);
        }
    }

    //Creado por Gabriel Rojas.
    public void addPesaje(int id, Rut rutCosechador, int idPlan, int idCuadrilla, float cantidadKg, Calidad calidad) throws GestionHuertosException {
        //Condiciones que tiraran la excepcion GestionHuertosException
        if (findPesajeById(id).isPresent()) throw new GestionHuertosException("Ya existe un pesaje con el id indicado");
        if (findPersona(rutCosechador).isEmpty() || !(findPersona(rutCosechador).get() instanceof Cosechador cosechador))
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        if (findPlanCosecha(idPlan).isEmpty())
            throw new GestionHuertosException("No existe un plan con el id indicado");
        if (!findPlanCosecha(idPlan).get().getEstado().equals(EstadoPlan.EJECUTANDO))
            throw new GestionHuertosException("El plan no se encuentra en estado \"en ejecucion\"");
        //Utiliza el metodo getCosAsig para asegurarse de que este cosechador este asignado a una cuadrilla de un plna
        if (cosechador.getAsignacion(idCuadrilla, idPlan).isEmpty())
            throw new GestionHuertosException("El cosechador no tiene una asignacion a una cuadrilla con el id indicado en el plan con el id señalado");
        //Compara las fechas de asignación del cosechador asignado en esa cuadrilla con LocalDate.now()
        if (LocalDate.now().isBefore(cosechador.getAsignacion(idCuadrilla, idPlan).get().getDesde()) || LocalDate.now().isAfter(cosechador.getAsignacion(idCuadrilla, idPlan).get().getHasta()))
            throw new GestionHuertosException("La fecha no esta en el rango de la asignacion del cosechador");
        if (!findPlanCosecha(idPlan).get().getCuartel().getEstado().equals(EstadoFenologico.COSECHA))
            throw new GestionHuertosException("El cultivo no se encuentra en estado fenológico cosecha");
        //La fecha del pesaje es LocalDateTime.now, ver enunciado si es que hay dudas con eso.
        pesajes.add(new Pesaje(id, cantidadKg, calidad, LocalDateTime.now(), cosechador.getAsignacion(idCuadrilla, idPlan).get()));
    }

    //Creado por Gabriel Rojas
    public double addPagoPesaje(int id, Rut rutCosechador) throws GestionHuertosException {
        if (findPagoPesajeById(id).isPresent())
            throw new GestionHuertosException("Ya existe un pago de pesaje con el id indicado");
        if (findPersona(rutCosechador).isEmpty() || !(findPersona(rutCosechador).get() instanceof Cosechador cosechador))
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        //Utiliza los metodos cosAsignadoTienePesajes y findCosPesajesImpagos
        if (cosechador.getCuadrillas().length == 0 || findCosPesajesImpagos(cosechador).isEmpty())
            throw new GestionHuertosException("El cosechador no tiene pesajes impagos");
        pagosPesajes.add(new PagoPesaje(id, LocalDate.now(), findCosPesajesImpagos(cosechador)));
        return findPagoPesajeById(id).get().getMonto();
    }

    // Hecho por Ricardo Quintana
    public String[] listCultivos() {
        Comparator<Cultivo> ordenarPorEspecieThenVariedad = Comparator.comparing(Cultivo::getEspecie).thenComparing(Cultivo::getVariedad);
        return cultivos.stream()
                .sorted(ordenarPorEspecieThenVariedad)
                .map( cultivo -> String.join("; ", Integer.toString(cultivo.getId()),
                        cultivo.getEspecie(), cultivo.getVariedad(), Double.toString(cultivo.getRendimiento()), Integer.toString(cultivo.getCuarteles().length)))
                .toArray(String[]::new);
    }

    // Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listHuertos() {
        return huertos.stream()
                .map(huertos ->String.join("; ", huertos.getNombre(), Float.toString(huertos.getSuperficie()), huertos.getUbicacion(),
                        reconstruyeRut(huertos.getPropietario().getRut().toString()), huertos.getPropietario().getNombre(), Integer.toString(huertos.getCuartels().length)))
                .toArray(String[]::new);
    }
    // Hecho por Ricardo Quintana, programación funcional por Gabriel Rojas
    public String[] listPropietarios() {
        Comparator<Propietario> ordenarPorNombre = Comparator.comparing(Propietario::getNombre, String.CASE_INSENSITIVE_ORDER);
        return personas.stream()
                .filter(persona -> persona.getClass() == Propietario.class)
                .map(Propietario.class::cast)
                .sorted(ordenarPorNombre)
                .map( propietario ->  String.join("; ",reconstruyeRut(propietario.getRut().toString()), propietario.getNombre(), propietario.getDireccion(),
                        propietario.getEmail(), propietario.getDirComercial(), Integer.toString((propietario).getHuertos().length)))
                .toArray(String[]::new);
    }

    // Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listSupervisores() {
        Comparator<Supervisor> ordenarPorKilosPesados = Comparator.comparing(supervisor -> supervisor.getCuadrillaAsignada().getKilosPesados());
        return personas.stream()
                .filter(persona -> persona.getClass() == Supervisor.class)
                .map(Supervisor.class::cast)
                .sorted(ordenarPorKilosPesados.reversed())
                .map(supervisor -> String.join("; ", reconstruyeRut(supervisor.getRut().toString()), supervisor.getNombre(), supervisor.getDireccion(), supervisor.getEmail(),
                        supervisor.getProfesion(), (supervisor.getCuadrillaAsignada() == null) ? "S/A" : supervisor.getCuadrillaAsignada().getNombre(),
                        (supervisor.getCuadrillaAsignada() == null) ? "N/A" :  (Double.toString(supervisor.getCuadrillaAsignada().getKilosPesados())), getNroPesajesImpagos(supervisor) ))
                .toArray(String[]::new);
    }

    // Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listCosechadores() {
        Comparator<Cosechador> ordenarPorMontoImpago =
                Comparator.comparing(cosechador ->
                Arrays.stream(cosechador.getAsignaciones())
                .mapToDouble(CosechadorAsignado::getMontoPesajesImpagos)
                        .sum());
        return personas.stream()
                .filter(persona -> persona.getClass() == Cosechador.class)
                .map(Cosechador.class::cast)
                .sorted(ordenarPorMontoImpago.reversed())
                .map(cosechador -> String.join("; ", reconstruyeRut(cosechador.getRut().toString()), cosechador.getNombre(), cosechador.getDireccion(), cosechador.getEmail(),
                        cosechador.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.toString(cosechador.getCuadrillas().length), String.format("%.1f",getMontoImpago(cosechador)), String.format("%.1f",getMontoPagado(cosechador))))
                .toArray(String[]::new);
    }

    // Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listPlanes() {
        if (planCosechas.isEmpty()) return new String[0]; // Sino existen planes de cosechas retorna un arreglo vacío
        Comparator<PlanCosecha> ordenarPorEspecieThenVariedad =
                Comparator.comparing(planCosecha -> planCosecha.getCuartel().getCultivo().getEspecie());
        return planCosechas.stream()
                .sorted(ordenarPorEspecieThenVariedad
                        .thenComparing(planCosecha ->  planCosecha.getCuartel().getCultivo().getVariedad())) // Por alguna razón no me deja hacer el thenComparing en la misma variable.
                .map(planCosecha -> String.join("; ", Integer.toString(planCosecha.getId()), planCosecha.getNombre(), planCosecha.getInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        planCosecha.getFinEstimado().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Double.toString(planCosecha.getMetaKilos()), Double.toString(planCosecha.getPrecioBaseKilo()), planCosecha.getEstado().toString(), Integer.toString(planCosecha.getCuartel().getId()),
                        planCosecha.getCuartel().getHuerto().getNombre(), Integer.toString(planCosecha.getCuadrillas().length), Double.toString(planCosecha.getCumplimientoMeta())))
                .toArray(String[]::new);
    }
    //Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listPesajes() {
        return pesajes.stream()
                .map(pesaje -> String.join("; ",
                        Integer.toString(pesaje.getId()),
                        pesaje.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        pesaje.getCosechadorAsignado().getCosechador().getRut().toString(),
                        String.valueOf(pesaje.getCalidad()), String.valueOf(pesaje.getCantidadKg()),
                        String.valueOf(pesaje.getPrecioKg()), String.valueOf(pesaje.getMonto()), (pesaje.getPagoPesaje() == null) ? "Impago" : pesaje.getPagoPesaje().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .toArray(String[]::new);
    }
    //Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listPesajesCosechador(Rut rut) {
        if(findPersona(rut).isPresent() && findPersona(rut).get() instanceof Cosechador cosechador) {
            if(cosechador.getAsignaciones().length > 0){
                return pesajes.stream()
                        .filter(pesaje -> pesaje.getCosechadorAsignado().getCosechador().getRut().equals(rut))
                        .map(pesaje -> String.join("; ", Integer.toString(pesaje.getId()), pesaje.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                pesaje.getCalidad().name(), String.format("%.1f",pesaje.getCantidadKg()), String.format("%.1f",pesaje.getPrecioKg()),
                                String.format("%.1f",pesaje.getMonto()), (pesaje.getPagoPesaje() == null) ? "Impago" : pesaje.getPagoPesaje().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                        .toArray(String[]::new);

            } else {
                throw new GestionHuertosException("El cosechador no ha sido asignado a una cuadrilla");
            }
        } else {
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        }
    }

    // Hecho por Ricardo Quintana, Programación funcional por Gabriel Rojas
    public String[] listPagoPesajes() {
        return pagosPesajes.stream()
                .map(pagoPesaje -> String.join("; ", Integer.toString(pagoPesaje.getId()), pagoPesaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Double.toString(pagoPesaje.getMonto()), Integer.toString(pagoPesaje.getPesajes().length), reconstruyeRut(pagoPesaje.getPesajes()[0].getCosechadorAsignado().getCosechador().getRut().toString())))
                .toArray(String[]::new);
    }
    //Hecho por Gabriel Rojas
    public String[] getCuadrillasDeCosechadorDePlan(Rut rutCosechador) throws GestionHuertosException {
        //Asegura que la fecha de hoy, que será la del pesaje, esta dentro de cada intervalo de asignación de su plan de cosecha correspondiente
        Predicate<Cuadrilla> filterCuadrillaByDate = (cuadrilla ->
                        LocalDate.now().isAfter(cuadrilla.getPlanCosecha().getInicio())
                        && LocalDate.now().isBefore(cuadrilla.getPlanCosecha().getFinEstimado())
                        || LocalDate.now().isEqual(cuadrilla.getPlanCosecha().getInicio())
                        || LocalDate.now().isEqual(cuadrilla.getPlanCosecha().getFinEstimado()));

        List<Cuadrilla> filteredCuadrillas = Arrays.stream(Optional.of(personas.stream()
                                .filter(persona -> persona.getRut().equals(rutCosechador))
                                .filter(persona -> persona.getClass() == Cosechador.class)
                                .findFirst()
                                .orElseThrow(() -> new GestionHuertosException("No existe un cosechador con el rut indicado")))//Evita un nullPointerException
                        .map(Cosechador.class::cast)
                        .get()
                        .getCuadrillas())
                        .filter(cuadrilla -> (cuadrilla.getPlanCosecha().getEstado() == EstadoPlan.EJECUTANDO))
                        .filter(filterCuadrillaByDate)
                        .toList();

        if (filteredCuadrillas.isEmpty()) throw new GestionHuertosException("El cosechador no tiene cuadrillas disponibles para pesaje");

        return filteredCuadrillas.stream()
                .map(cuadrilla ->
                        String.join("; ", Integer.toString(cuadrilla.getId()), cuadrilla.getNombre(), Integer.toString(cuadrilla.getPlanCosecha().getId())))
                .toArray(String[]::new);

    }



    // Hecho por Ricardo Quintana
    private void readDataFromTextFile() throws FileNotFoundException {
        // creo el Scanner asociado al archivoDeTexto
        try {
            Scanner scGestionHuertos = new Scanner(new File("InputDataGestionHuertos.txt")).useLocale(Locale.UK);
            scGestionHuertos.useDelimiter("[\\n\\r|;]+");
            while (scGestionHuertos.hasNextLine()) {
                String linea = scGestionHuertos.nextLine().trim();
                int nroDeLineas = 0;
                if (!linea.startsWith("#") && !linea.isEmpty()) {
                    String[] operacion = linea.split(";");
                    switch (operacion[0]) {
                        case "createPropietario":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                Rut rut = Rut.of(scGestionHuertos.next().trim());
                                String nombre = scGestionHuertos.next().trim();
                                String email = scGestionHuertos.next().trim();
                                String direccion = scGestionHuertos.next().trim();
                                String direccionComercial = scGestionHuertos.next().trim();
                                createPropietario(rut, nombre, email, direccion, direccionComercial);
                            }
                            break;
                        case "createSupervisor":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                Rut rut = Rut.of(scGestionHuertos.next().trim());
                                String nombre = scGestionHuertos.next().trim();
                                String email = scGestionHuertos.next().trim();
                                String direccion = scGestionHuertos.next().trim();
                                String profesion = scGestionHuertos.next().trim();
                                createSupervisor(rut, nombre, email, direccion, profesion);
                            }
                            break;
                        case "createCosechador":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                Rut rut = Rut.of(scGestionHuertos.next().trim());
                                String nombre = scGestionHuertos.next().trim();
                                String email = scGestionHuertos.next().trim();
                                String direccion = scGestionHuertos.next().trim();
                                LocalDate fechaNacimiento = LocalDate.parse(scGestionHuertos.next().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                createCosechador(rut, nombre, email, direccion, fechaNacimiento);
                            }
                            break;
                        case "createCultivo":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                String id = scGestionHuertos.next().trim();
                                String especie = scGestionHuertos.next().trim();
                                String variedad = scGestionHuertos.next().trim();
                                float rendimiento = scGestionHuertos.nextFloat();
                                createCultivo(Integer.parseInt(id), especie, variedad, rendimiento);
                            }
                            break;
                        case "createHuerto":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                String nombre = scGestionHuertos.next().trim();
                                float superficie = scGestionHuertos.nextFloat();
                                String ubicacion = scGestionHuertos.next().trim();
                                Rut rut = Rut.of(scGestionHuertos.next().trim());
                                createHuerto(nombre, superficie, ubicacion, rut);
                            }
                            break;
                        case "addCuartelToHuerto":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                String nombreHuerto = scGestionHuertos.next().trim();
                                int idCuartel = scGestionHuertos.nextInt();
                                float superficie = scGestionHuertos.nextFloat();
                                int idCultivo = scGestionHuertos.nextInt();
                                addCuartelToHuerto(nombreHuerto, idCuartel, superficie, idCultivo);
                            }
                            break;
                        case "createPlanCosecha":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idPlanCosecha = scGestionHuertos.nextInt();
                                String nombre = scGestionHuertos.next().trim();
                                LocalDate fechaInicio = LocalDate.parse(scGestionHuertos.next().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                LocalDate fechaTermino = LocalDate.parse(scGestionHuertos.next().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                double meta = scGestionHuertos.nextDouble();
                                float precioBase = scGestionHuertos.nextFloat();
                                String nombreHuerto = scGestionHuertos.next().trim();
                                int idCuartel = scGestionHuertos.nextInt();
                                createPlanCosecha(idPlanCosecha, nombre, fechaInicio, fechaTermino, meta, precioBase, nombreHuerto, idCuartel);
                            }
                            break;
                        case "addCuadrillaToPlan":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idPlanCosecha = scGestionHuertos.nextInt();
                                int idCuadrilla = scGestionHuertos.nextInt();
                                String nombreCuadrilla = scGestionHuertos.next().trim();
                                Rut rutSupervisor = Rut.of(scGestionHuertos.next().trim());
                                addCuadrillaToPlan(idPlanCosecha, idCuadrilla, nombreCuadrilla, rutSupervisor);
                            }
                            break;
                        case "addCosechadorToCuadrilla":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idPlanCosecha = scGestionHuertos.nextInt();
                                int idCuadrilla = scGestionHuertos.nextInt();
                                LocalDate fechaInicio = LocalDate.parse(scGestionHuertos.next().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                LocalDate fechaFinal = LocalDate.parse(scGestionHuertos.next().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                double meta = scGestionHuertos.nextDouble();
                                Rut rutCosechador = Rut.of(scGestionHuertos.next().trim());
                                addCosechadorToCuadrilla(idPlanCosecha, idCuadrilla, fechaInicio, fechaFinal, meta, rutCosechador);
                            }
                            break;
                        case "changeEstadoPlan":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idPlanCosecha = scGestionHuertos.nextInt();
                                EstadoPlan estadoPlan = EstadoPlan.valueOf(scGestionHuertos.next().trim());
                                changeEstadoPlan(idPlanCosecha, estadoPlan);
                            }
                            break;
                        case "changeEstadoCuartel":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idCuartel = scGestionHuertos.nextInt();
                                String nombreHuerto = scGestionHuertos.next().trim();
                                EstadoFenologico estado = EstadoFenologico.valueOf(scGestionHuertos.next().trim());
                                changeEstadoCuartel(nombreHuerto, idCuartel, estado);
                            }
                            break;
                        case "addPesaje":
                            nroDeLineas = Integer.parseInt(operacion[1]);
                            for (int i = 0; i < nroDeLineas; i++) {
                                int idPesaje = scGestionHuertos.nextInt();
                                Rut rut = Rut.of(scGestionHuertos.next().trim());
                                int idPlan = scGestionHuertos.nextInt();
                                int idCuadrilla = scGestionHuertos.nextInt();
                                float ctdKilos = scGestionHuertos.nextFloat();
                                Calidad calidad = Calidad.valueOf(scGestionHuertos.next().trim());
                                addPesaje(idPesaje, rut, idPlan, idCuadrilla, ctdKilos, calidad);
                            }
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // Hecho por Ricardo Quintana
    public void saveSystemData() throws GestionHuertosException {
        GestionHuertosIO persistencia = GestionHuertosIO.getInstance();

        // Guardar personas
        Persona[] personasObj = personas.toArray(new Persona[0]);
        persistencia.savePersonas(personasObj);

        // Guardar cultivos
        ArrayList<Cultivo> cultivosList = new ArrayList<>();
        for (Cultivo cultivo : cultivos) {
            if (cultivo.getCuarteles().length == 0) {
                cultivosList.add(cultivo);
            }
        }
        Cultivo[] cultivosObj = cultivosList.toArray(new Cultivo[0]);
        persistencia.saveCultivos(cultivosObj);

        // Guardar planes de cosecha
        ArrayList<PlanCosecha> planesCosechasList = new ArrayList<>();
        for (PlanCosecha planCosecha: planCosechas) {
            if(planCosecha.getCuadrillas().length == 0) {
                planesCosechasList.add(planCosecha);
            }
        }
        PlanCosecha[] planCosechasObj = planesCosechasList.toArray(new PlanCosecha[0]);
        persistencia.savePlanesCosecha(planCosechasObj);
    }

    // Hecho por Ricardo Quintana
    public void readSystemData() throws GestionHuertosException {
        GestionHuertosIO persistencia = GestionHuertosIO.getInstance();
        // primero limpio las colecciones para evitar objetos duplicados
        personas.clear();
        cultivos.clear();
        planCosechas.clear();

        personas.addAll(Arrays.asList(persistencia.readPersonas()));
        cultivos.addAll(Arrays.asList(persistencia.readCultivos()));
        planCosechas.addAll(Arrays.asList(persistencia.readPlanesCosecha()));
    }

    //Metodo private que encuentra a una persona deseada a través de su rut.
    private Optional<Persona> findPersona(Rut rut) {
       return personas.stream()
               .filter(persona -> persona.getRut().equals(rut))
               .findFirst();
    }

    //private method para encontrar un cultivo.
    private Optional<Cultivo> findCultivo(int id) {
        return cultivos.stream()
                .filter(cultivo -> cultivo.getId() == id)
                .findFirst();
    }

    //Metodo privado para encontar el plan de cosecha deseado.
    private Optional<PlanCosecha> findPlanCosecha(int id) {
       return planCosechas.stream()
               .filter(planCosecha -> planCosecha.getId() == id)
               .findFirst();
    }

    //Metodo private para encontrar una cuadrilla deseada
    private Optional<Cuadrilla> findCuadrilla(int idCuadrilla, int idPlan) {
        if (findPlanCosecha(idPlan).isEmpty()) return Optional.empty();
        return Arrays.stream(planCosechas.stream()
                .filter(planCosecha -> planCosecha.getId() == idPlan)
                .findFirst()
                .orElseThrow(()-> new GestionHuertosException("No existe el plan cosecha con el id indicado"))
                .getCuadrillas())
                .filter(planCosecha -> planCosecha.getId() == idCuadrilla)
            .findFirst();
    }

    // Hecho por Ricardo Quintana
    private Optional<Huerto> findHuerto(String nombre) {
        return huertos.stream()
                .filter(huerto -> huerto.getNombre().equals(nombre))
                .findFirst();
    }

    private Optional<Cuartel> findCuartel(int idCuartel, String nombreHuerto) {
        return huertos.stream()
                .filter(huerto -> huerto.getNombre().equals(nombreHuerto))
                .findFirst()
                .orElseThrow(()->new GestionHuertosException("No existe un huerto con el nombre indicado"))
                .getCuartelById(idCuartel);
    }

    private Optional<Pesaje> findPesajeById(int id) {
        return pesajes.stream()
                .filter(pesaje -> pesaje.getId() == id)
                .findFirst();
    }

    private Optional<PagoPesaje> findPagoPesajeById(int id) {
        return pagosPesajes.stream()
                .filter(pagoPesaje -> pagoPesaje.getId() == id)
                .findFirst();
    }

    //Verifica que tiene pesajes impagos
    private List<Pesaje> findCosPesajesImpagos(Cosechador cosechador) {
        return Arrays.stream(cosechador.getAsignaciones())
                .map(CosechadorAsignado::getPesajes)
                .flatMap(Arrays::stream)
                .filter(pesaje -> !pesaje.isPagado())
                .toList();
    }

    private String reconstruyeRut(String rut) {
        String dosNumeros = rut.substring(0,2);
        String tresNumeros = rut.substring(2,5);
        String resto = rut.substring(5);
        return dosNumeros + "." + tresNumeros + "." + resto;
    }

    private double getMontoImpago(Cosechador cosechador) {
        return Arrays.stream(cosechador.getAsignaciones())
                .map(CosechadorAsignado::getPesajes)
                .flatMap(Arrays::stream)
                .filter(pesaje -> !pesaje.isPagado())
                .mapToDouble(Pesaje::getMonto)
                .sum();
    }

    private double getMontoPagado(Cosechador cosechador) {
        return Arrays.stream(cosechador.getAsignaciones())
                .map(CosechadorAsignado::getPesajes)
                .flatMap(Arrays::stream)
                .filter(Pesaje::isPagado)
                .mapToDouble(Pesaje::getMonto)
                .sum();
    }
    private String getNroPesajesImpagos(Supervisor supervisor) {
        int nroPesajesImpagos = 0;
        for (CosechadorAsignado cosasig : supervisor.getCuadrillaAsignada().getAsignaciones()) {
            nroPesajesImpagos += cosasig.getNroPesajesImpagos();
        }
        return String.valueOf(nroPesajesImpagos);
    }
}
