package controlador;//Ultima revision:
// Error encontrado el arreglo debe ser del tamaño exacto de la cantidad de personas que tengan dicho rol

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
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
    private static ControlProduccion instance = null;
    private ControlProduccion() {
        generateTestData();
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
        if (findPersona(rut).isPresent()) throw new GestionHuertosException("Ya existe un propietario con el rut indicado");

        // De lo contrario, lo agrega a la colección.
        personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }
    //Creado por Gabriel Rojas
    public void createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) throws GestionHuertosException {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut).isPresent()) throw new GestionHuertosException("Ya existe un supervisor con el rut indicado") ;
        //Agrega el supervisor a la coleccion.
        personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }
    //Creado por Gabriel Rojas
    public void createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) throws  GestionHuertosException {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut).isPresent()) throw new GestionHuertosException("Ya existe un cosechador con el rut indicado");
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
        if (findHuerto(nombre).isPresent()) throw new GestionHuertosException("Ya existe un huerto con el nombre indicado");
        if (findPersona(rutPropietario).isEmpty()) throw new GestionHuertosException("No existe un propietario con el rut indicado");
            // Verifico si es que el rut es de un propetario
        if (findPersona(rutPropietario).get() instanceof Propietario propietario) {
            // Creo el huerto
            huertos.add(new Huerto(nombre, superficie, ubicacion, propietario));
        } else {
            throw new GestionHuertosException("ERROR: Rut " + rutPropietario + " no pertenece a un Propietario.");
        }

    }
    // Hecho por Ricardo Quintana
    public void addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {

        // Verificar que el huerto exista y que el cuartel no tenga un huerto asociado
        if (findHuerto(nombreHuerto).isEmpty()) throw new GestionHuertosException("No existe un huerto con el huerto indicado");
        if (findCultivo(idCultivo).isEmpty()) throw new GestionHuertosException("No existe un cultivo con el id indicado");
        //Condición que no aparece en el enunciado, igual dejarla aquí porsiacaso.
        // if (findCuartel(idCuartel,nombreHuerto).isPresent()) throw new GestionHuertosException("ERROR: EL ID del Cuartel " + idCuartel+ " ya está asignado al Huerto.");
        findHuerto(nombreHuerto).get().addCuartel(idCuartel, superficie, findCultivo(idCultivo).get());
    }
    //Creado por Gabriel Rojas
    public void createPlanCosecha(int id, String nombrePlan, LocalDate fechaInicio, LocalDate fechaFin, double metaKilos, double precioBaseKilo, String nomHuerto, int idCuartel) throws GestionHuertosException {
        //Se asegura que el plan cosecha no exista, a traves de su id.
        //Si encuentra un plan cosecha existente, retorna false.
        if (findPlanCosecha(id).isPresent()) throw new GestionHuertosException("Ya existe un pla cosecha con el id indicado");
        //Asegura que la fecha de inicio no sea supeerior o igual a la fecha de fin.
        if (fechaInicio.isAfter(fechaFin) || fechaInicio.isEqual(fechaFin)) throw new GestionHuertosException("Intervalo de fechas no permitido.");
        //Si no existe el huerto pasado por parametros, retorna false.
        if (findHuerto(nomHuerto).isEmpty()) throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        Huerto huertoEncontrado = findHuerto(nomHuerto).get();
        //Condicion que asegura que el cuartel pasado por parametros exista y sea parte del huerto.
        if (findCuartel(idCuartel,nomHuerto).isPresent()) {
            Cuartel cuartelEncontrado = findCuartel(idCuartel, nomHuerto).get();
            //Crea el nuevo plan de cosecha.
            planCosechas.add(new PlanCosecha(id, nombrePlan, fechaInicio, fechaFin, metaKilos, precioBaseKilo, cuartelEncontrado));
        } else {
            // Condicion que no se especifica en el enunciado, dejarlo aquí porsiacaso.
            // throw new GestionHuertosException("ERROR: El cuartel con el ID " +idCuartel + " no existe o no forma parte del Huerto asignado.");
        }
    }
    //Creado por Gabriel Rojas
    public void addCuadrillaToPlan(int idPlan, int idCuadrilla, String nombreCuadrilla, Rut rutSupervisor) throws GestionHuertosException {
        //se hace un casting a la clase hija modelo.Supervisor, porque el metodo  findPersona retorna un objeto modelo.Persona.
        //Ademas, se asegura que el objeto recibido sea el objeto modelo.Supervisor.
        if (findPersona(rutSupervisor).isEmpty()) throw new GestionHuertosException("No existe un supervisor con el rut indicado");
        if (findPersona(rutSupervisor).get() instanceof Supervisor supervisorEncontrado) {
            //Si encuentra una cuadrilla ya asignada al supervisor, retorna false.
            if (supervisorEncontrado.getCuadrillaAsignada() != null) throw new GestionHuertosException("El supervisor ya tiene asignada una cuadrilla a a su cargo");
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
        if (findPlanCosecha(idPlanCosecha).isEmpty()) throw new GestionHuertosException("No existe un plan cosecha con el id indicado");
        //Asigna los metodos findPlanCosecha, findPersona y findCuadrilla a variables para mejor legibilidad
        PlanCosecha plan = findPlanCosecha(idPlanCosecha).get();
        // Condicion que no se especifica en el enunciado.
        // if (findCuadrilla(idCuadrilla, plan.getId()).isEmpty()) throw new GestionHuertosException("ERROR: Cuadrilla con el ID "+ idCuadrilla+" no existe o no está asignada al Plan de Cosecha con el ID " + idPlanCosecha + ".");
        Cuadrilla cuadrillaEncontrada = findCuadrilla(idCuadrilla, plan.getId()).get();
        if (findPersona(rutCosechador).isEmpty()) throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        //Condicion que asegura  que el objeto encontrado sea modelo.Cosechador, para asi no
        // castear un objeto modelo.Supervisor o modelo.Propietario, si es que se introduce un rut erroneo pero que existe
        if (findPersona(rutCosechador).get() instanceof Cosechador cosechadorEncontrado) {
            //booleano isAfter, que asegura que la fecha de inicio no es superior a la fecha final. Solo puede ser Inferior o Igual.
            //Además, asegura que la fecha de inicio y final este en el intervalo de tiempo del plan de cosecha.
            if (fechaIniCosechador.isAfter(fechaFinCosechador) || fechaIniCosechador.isBefore(plan.getInicio()) || fechaIniCosechador.isAfter(plan.getFinEstimado())
                    || fechaFinCosechador.isAfter(plan.getFinEstimado()) || fechaFinCosechador.isBefore(plan.getInicio()))
                throw new GestionHuertosException("El rango de fechas de asignación del cosechador a la cuadrilla está fuera del rango de fechas del plan");
            //Retornará un valor booleano que mostrara si la acción se pudo realizar o no.
            plan.addCosechadorToCuadrilla(idCuadrilla, fechaIniCosechador, fechaFinCosechador, metaKilosCosechador, cosechadorEncontrado);
        }
    }
    // Hecho por Ricardo Quintana
    public String[] listCultivos() {
        if (cultivos.isEmpty()) return new String[0];
        String[] listaCultivos = new String[cultivos.size()];
        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);
            listaCultivos[i] = String.join(", ", Integer.toString(cultivo.getId()), cultivo.getEspecie(), cultivo.getVariedad(), Double.toString(cultivo.getRendimiento()), Integer.toString(cultivo.getCuarteles().length));
        }
        return listaCultivos;
    }
    // Hecho por Ricardo Quintana
    public String[] listHuertos() {
        if (huertos.isEmpty()) return new String[0];
        String[] listaHuertos = new String[huertos.size()];
        for (int i = 0; i < huertos.size(); i++) {
            listaHuertos[i] = String.join(", ", huertos.get(i).getNombre(), Float.toString(huertos.get(i).getSuperficie()), huertos.get(i).getUbicacion(),
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
                listaPropietarios[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(),
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
                    listaSupervisores[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
                            ((Supervisor) personas.get(i)).getProfesion(), "S/A");

                } else {
                    listaSupervisores[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
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
                listaCosechadores[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
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
            listaPlanesCosechas[cont] = String.join(", ", Integer.toString(planCosecha.getId()), planCosecha.getNombre(), planCosecha.getInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    planCosecha.getFinEstimado().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Double.toString(planCosecha.getMetaKilos()), Double.toString(planCosecha.getPrecioBaseKilo()), planCosecha.getEstado().toString(), Integer.toString(planCosecha.getCuartel().getId()),
                    planCosecha.getCuartel().getHuerto().getNombre(), Integer.toString(planCosecha.getCuadrillas().length));
            cont++;
        }
        return listaPlanesCosechas;
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
        if (findPlanCosecha(idPlan).isEmpty()) return Optional.empty(); {
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
        for(Huerto huerto : huertos) {
            // Si existe retorna el huerto
            if(huerto.getNombre().equals(nombre))  return Optional.of(huerto);
        }
        // De lo contrario retona null, por lo tanto no existe
        return Optional.empty();
    }
    private Optional<Cuartel> findCuartel (int idCuartel, String nombreHuerto) {
        /* Cuarteles no tiene un relacion directa con la clase, por lo que accedemos
        por medio de alguna relacion que tenga, en este caso los huertos
         */

        // Si no existe el huerto, retona null
        if (findHuerto(nombreHuerto).isEmpty()) return Optional.empty();

        for(Huerto huerto : huertos) {
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

    private void generateTestData() {
        LocalDate born;
        LocalDate fechaIni, fechaFin;
        LocalDate fechaIniCos, fechaFinCos;
        createPropietario(new Rut("22.222.222-2"), "Test", "Email@gmail.com", "Direccion 1200", "DireccionCo 1200");
        createSupervisor(new Rut("33.333.333-3"), "Test", "Email.com", "Direccion 1000", "Ingeniero Comercial");
        createCosechador(new Rut("44.444.444-4"), "Test", "Email.com", "Direccion 101", born = LocalDate.parse("20/10/1999", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        createCultivo(123, "Manzana", "Fuji", 0.05f);
        createHuerto("Los Vasquez", 2000, "Los Aramos", findPersona(new Rut("22.222.222-2")).get().getRut());
        addCuartelToHuerto("Los Vasquez", 124, 500, 123);
        createPlanCosecha(0003, "NombrePlan", fechaIni = LocalDate.parse("20/10/2025",DateTimeFormatter.ofPattern("dd/MM/yyyy")), fechaFin =
                LocalDate.parse("20/03/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy")), 1000, 500, "Los Vasquez", 124);
        addCuadrillaToPlan(0003, 456, "Max Steel", new Rut("33.333.333-3"));
        addCosechadorToCuadrilla(0003, 456, fechaIniCos = LocalDate.parse("10/11/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy")), fechaFinCos =
                LocalDate.parse("02/01/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy")), 300, new Rut("44.444.444-4"));
    }
}
