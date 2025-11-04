package controlador;//Ultima revision:
// Error encontrado el arreglo debe ser del tamaño exacto de la cantidad de personas que tengan dicho rol

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        if (findPersona(rut).isPresent())
            throw new GestionHuertosException("Ya existe un propietario con el rut indicado");

        // De lo contrario, lo agrega a la colección.
        personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }

    //Creado por Gabriel Rojas
    public void createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) throws GestionHuertosException {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut).isPresent())
            throw new GestionHuertosException("Ya existe un supervisor con el rut indicado");
        //Agrega el supervisor a la coleccion.
        personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    //Creado por Gabriel Rojas
    public void createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) throws GestionHuertosException {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut).isPresent())
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
    public void changeEstadoCuartel(String nombreHuerto, int idCuartel, EstadoFenologico estado) throws  GestionHuertosException {
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
                throw new GestionHuertosException("El supervisor ya tiene asignada una cuadrilla a a su cargo");
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
        Cuadrilla cuadrillaEncontrada = findCuadrilla(idCuadrilla, plan.getId()).get();
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
            if (cuadrillaEncontrada.getCosechadores().length >= Cuadrilla.getMaximoCosechadores()) throw new GestionHuertosException("El número de cosechadores ya alcanzó el máximo permitido");
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
        //ADVERTENCIA: El metodo getCosAsig todavia no existe.
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
    public double addPagoPesaje(int id, Rut rutCosechador) throws  GestionHuertosException {
        if (findPagoPesajeById(id).isPresent()) throw new GestionHuertosException("Ya existe un pago de pesaje con el id indicado");
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
        if (cultivos.isEmpty()) return new String[0];
        String[] listaCultivos = new String[cultivos.size()];
        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);
            listaCultivos[i] = String.join("; ", Integer.toString(cultivo.getId()), cultivo.getEspecie(), cultivo.getVariedad(), Double.toString(cultivo.getRendimiento()), Integer.toString(cultivo.getCuarteles().length));
        }
        return listaCultivos;
    }

    // Hecho por Ricardo Quintana
    public String[] listHuertos() {
        if (huertos.isEmpty()) return new String[0];
        String[] listaHuertos = new String[huertos.size()];
        for (int i = 0; i < huertos.size(); i++) {
            listaHuertos[i] = String.join("; ", huertos.get(i).getNombre(), Float.toString(huertos.get(i).getSuperficie()), huertos.get(i).getUbicacion(),
                    huertos.get(i).getPropietario().getRut().toString(), huertos.get(i).getPropietario().getNombre(), Integer.toString(huertos.get(i).getCuartels().length));
        }
        return listaHuertos;
    }
    // Hecho por Ricardo Quintana

    public String[] listPropietarios() {
        /*Cambie los parametros del for de las listas de personas, listaXXX.length -> persona.size(). Ademas, agregue otra variable(j) que avanza cada vez que encuentra un tipo de persona.
         **Este cambio permite buscar entre todas las personas y guardar en el arreglo solo las personas necesarias, ya que con el length se llenaba el arreglo de nulls, por que el for avanzaba de numero
         * y eso iba avanzando espacios en el arreglo. */
        if (personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen propietarios.
        if (findArraySize(3) == -1) return new String[0];
        String[] listaPropietarios = new String[findArraySize(3)];
        // Busco los propietarios de la lista de personas
        for (int i = 0, j = 0; i < personas.size(); i++) {
            if (personas.get(i) instanceof Propietario) {
                listaPropietarios[j] = String.join("; ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(),
                        personas.get(i).getEmail(), ((Propietario) personas.get(i)).getDirComercial(), Integer.toString(((Propietario) personas.get(i)).getHuertos().length));
                j++;
            }
        }
        return listaPropietarios;
    }

    // Hecho por Ricardo Quintana
    public String[] listSupervisores() {
        if (personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen supervisores
        if (findArraySize(1) == -1) return new String[0];
        String[] listaSupervisores = new String[findArraySize(1)];
        // Busco los supervisores de la lista de personas
        for (int i = 0, j = 0; i < personas.size(); i++) {
            if (personas.get(i) instanceof Supervisor) {
                if (((Supervisor) personas.get(i)).getCuadrillaAsignada() == null) {
                    listaSupervisores[j] = String.join("; ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
                            ((Supervisor) personas.get(i)).getProfesion(), "S/A");

                } else {
                    listaSupervisores[j] = String.join("; ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
                            ((Supervisor) personas.get(i)).getProfesion(), ((Supervisor) personas.get(i)).getCuadrillaAsignada().getNombre());
                }
                j++;
            }
        }
        return listaSupervisores;
    }

    // Hecho por Ricardo Quintana
    public String[] listCosechadores() {
        if (personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen cosechadores
        if (findArraySize(2) == -1) return new String[0];
        String[] listaCosechadores = new String[findArraySize(2)];
        // Busco los cosechadores de la lista de personas
        for (int i = 0, j = 0; i < personas.size(); i++) {
            if (personas.get(i) instanceof Cosechador) {
                int cuadrillasAsignadas = ((Cosechador) personas.get(i)).getCuadrillas().length;
                listaCosechadores[j] = String.join("; ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
                        ((Cosechador) personas.get(i)).getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.toString(cuadrillasAsignadas));
                j++;
            }
        }
        return listaCosechadores;
    }

    // Hecho por Ricardo Quintana
    public String[] listPlanes() {
        if (planCosechas.isEmpty()) return new String[0]; // Sino existen planes de cosechas retorna un arreglo vacío

        String[] listaPlanesCosechas = new String[planCosechas.size()];
        // Guardo los planes de Cosecha en el arreglo
        int cont = 0;
        for (PlanCosecha planCosecha : planCosechas) {
            listaPlanesCosechas[cont] = String.join("; ", Integer.toString(planCosecha.getId()), planCosecha.getNombre(), planCosecha.getInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    planCosecha.getFinEstimado().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Double.toString(planCosecha.getMetaKilos()), Double.toString(planCosecha.getPrecioBaseKilo()), planCosecha.getEstado().toString(), Integer.toString(planCosecha.getCuartel().getId()),
                    planCosecha.getCuartel().getHuerto().getNombre(), Integer.toString(planCosecha.getCuadrillas().length));
            cont++;
        }
        return listaPlanesCosechas;
    }

    public String [] listPesajes() {
        if (pesajes.isEmpty()) return new String[0];
        String[] listaPesajes = new String[pesajes.size()];
        for(Pesaje pesaje : pesajes) {
            String pagadoPesaje = String.valueOf(pesaje.getPagoPesaje().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if(pagadoPesaje == null) pagadoPesaje = "Impago"; {}
            String.join("; ", Integer.toString(pesaje.getId()), pesaje.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),pesaje.getCosechadorAsignado().getCosechador().getRut().toString(), String.valueOf(pesaje.getCalidad()), String.valueOf(pesaje.getCantidadKg()), String.valueOf(pesaje.getPrecioKg()), String.valueOf(pesaje.getMonto()), pagadoPesaje);
        }
        return listaPesajes;
    }

    public String [] listPesajesCosechador(Rut rut) {
        if (pesajes.isEmpty()) return new String[0];
        ArrayList<String> lista = new ArrayList<>();
        if(findPersona(rut).get() instanceof Cosechador cosechador) {
            if(cosechador.getAsignaciones().length > 0){
                for(CosechadorAsignado cosechadorAsignado : cosechador.getAsignaciones()) {
                    for(Pesaje pesaje : pesajes) {
                        lista.add(String.join("; ", Integer.toString(pesaje.getId()), pesaje.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), pesaje.getCalidad().name(), Double.toString(pesaje.getCantidadKg()), Double.toString(pesaje.getPrecioKg()), Double.toString(pesaje.getMonto()), pesaje.getPagoPesaje().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                    }
                }
                return lista.toArray(new String[0]);

            } else {
                throw new GestionHuertosException("El cosechador no ha sido asignado a una cuadrilla");
            }
        } else {
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        }
    }

    // Hecho por Ricardo Quintana
    public String [] listPagoPesajes() {
        if (pesajes.isEmpty()) return new String[0];
        ArrayList<String> lista = new ArrayList<>();
        for(Pesaje pesaje : pesajes) {
            if(pesaje.getPagoPesaje() != null){
                PagoPesaje pagoPesaje = pesaje.getPagoPesaje();
                lista.add(String.join("; ", Integer.toString(pagoPesaje.getId()), pagoPesaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Double.toString(pagoPesaje.getMonto()), Integer.toString(pagoPesaje.getPesajes().length), pesaje.getCosechadorAsignado().getCosechador().getRut().toString()));
            }
        }
        return lista.toArray(new String[0]);
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

    //Metodo private que encuentra a una persona deseada a través de su rut.
    private Optional<Persona> findPersona(Rut rut) {
        //Busca a la persona.
        for (Persona persona : personas) {
            //compara rut.
            if (persona.getRut().equals(rut)) return Optional.of(persona);
        }
        //De lo contrario, no existe.
        return Optional.empty();
    }

    //private method para encontrar un cultivo.
    private Optional<Cultivo> findCultivo(int id) {
        for (Cultivo cultivos : cultivos) {
            //Compara el id pasado por parametro con el getId() del objeto.
            if (id == cultivos.getId()) return Optional.of(cultivos);
        }
        //De lo contrario, retorna null.
        return Optional.empty();
    }

    //Metodo privado para encontar el plan de cosecha deseado.
    private Optional<PlanCosecha> findPlanCosecha(int id) {
        //Si no existen planes de cosecha, retorna null
        if (planCosechas.isEmpty()) return Optional.empty();
        //Busca entre la coleccion
        for (PlanCosecha planCosecha : planCosechas) {
            //Compara, lo devuelve.
            if (id == planCosecha.getId()) return Optional.of(planCosecha);
        }
        //De lo contrario.
        return Optional.empty();
    }

    //Metodo private para encontrar una cuadrilla deseada
    private Optional<Cuadrilla> findCuadrilla(int idCuadrilla, int idPlan) {
        //Para no repetir codigo, es necesario pasar por parametro el identificador del modelo.PlanCosecha.
        //De esta forma, se reutiliza el codigo findPlanCosecha y se ahorra lineas de codigo.
        if (findPlanCosecha(idPlan).isEmpty()) return Optional.empty();
        {
            if (findPlanCosecha(idPlan).get().getCuadrillas().length == 0) return Optional.empty();
            //Se asigna el array que retorna findPlanCosecha.getCuadrillas a una variable para mejor legibilidad.
            Cuadrilla[] cuadrillas = findPlanCosecha(idPlan).get().getCuadrillas();//Ignorar advertencia. La linea de arriba asegura que findPlanCosecha no sea null.
            for (Cuadrilla cuadrilla : cuadrillas) {
                //Recorre el array, compara identificadores con su metodo getId().
                //Lo encuentra, lo devuelve.
                if (cuadrilla.getId() == idCuadrilla) return Optional.of(cuadrilla);
            }
        }
        // De lo contrario, no existe.
        return Optional.empty();
    }

    // Hecho por Ricardo Quintana
    private Optional<Huerto> findHuerto(String nombre) {
        // Verifico que exista un huerto con esos datos
        for (Huerto huerto : huertos) {
            // Si existe retorna el huerto
            if (huerto.getNombre().equals(nombre)) return Optional.of(huerto);
        }
        // De lo contrario retona null, por lo tanto no existe
        return Optional.empty();
    }

    private Optional<Cuartel> findCuartel(int idCuartel, String nombreHuerto) {
        /* Cuarteles no tiene un relacion directa con la clase, por lo que accedemos
        por medio de alguna relacion que tenga, en este caso los huertos
         */

        // Si no existe el huerto, retona null
        if (findHuerto(nombreHuerto).isEmpty()) return Optional.empty();

        for (Huerto huerto : huertos) {
            //Condicion que evita NullPointerException, porque getCuartel puede ser null.
            if (huerto.getCuartel(idCuartel) != null) {
                if (huerto.getCuartel(idCuartel).getId() == idCuartel) {

                    // Devuelve el cuartel si es que lo tiene asignado
                    return Optional.of(huerto.getCuartel(idCuartel));
                }
            }
        }
        // Si no tiene asignado el cuartel pasado como parametro retorna null
        return Optional.empty();
    }

    private int findArraySize(int opcion) {
        int supervisores = 0, cosechadores = 0, propietarios = 0;
        if (!personas.isEmpty()) {
            //dependiendo de opcion: 1 = supervisor, 2 == cosechador, 3 == propietarios;
            for (Persona persona1 : personas) {
                if (persona1 instanceof Supervisor) supervisores++;
                if (persona1 instanceof Cosechador) cosechadores++;
                if (persona1 instanceof Propietario) propietarios++;
            }
            if (opcion == 1 && supervisores != 0) return supervisores;
            if (opcion == 2 && cosechadores != 0) return cosechadores;
            if (opcion == 3 && propietarios != 0) return propietarios;
        }
        //Si no existe, retorna -1.
        return -1;
    }

    private Optional<Pesaje> findPesajeById(int id) {
        for (Pesaje pesaje : pesajes) {
            if (pesaje.getId() == id) return Optional.of(pesaje);
        }
        return Optional.empty();
    }

    private Optional<PagoPesaje> findPagoPesajeById(int id) {
        for (PagoPesaje pesaje : pagosPesajes) {
            if (pesaje.getId() == id) return Optional.of(pesaje);
        }
        return Optional.empty();
    }

    //Verifica que tiene pesajes impagos
    private List<Pesaje> findCosPesajesImpagos(Cosechador cosechador) {
        List<Pesaje> pesajesConPagoPendiente = new ArrayList<>();
        if (cosechador.getAsignaciones().length == 0) return new ArrayList<>();
        for (CosechadorAsignado cosAsig : cosechador.getAsignaciones()) {
            for (Pesaje pesaje : cosAsig.getPesajes()) {
                if (!pesaje.isPagado()) pesajesConPagoPendiente.add(pesaje);
            }
        }
        return pesajesConPagoPendiente;
    }
    private String reconstruyeRut(String rut) {
        String dosNumeros = rut.substring(0,2);
        String tresNumeros = rut.substring(2,5);
        String resto = rut.substring(5);
        StringBuilder unirDosyTres = new StringBuilder();
        unirDosyTres.append(dosNumeros).append(".").append(tresNumeros).append(".").append(resto);
        return unirDosyTres.toString();
    }
}
