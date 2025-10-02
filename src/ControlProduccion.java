//Hecho por: Gabriel Rojas y Ricardo Quintana
//Ultima revision:
// Error encontrado el arreglo debe ser del tamaño exacto de la cantidad de personas que tengan dicho rol
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;

public class ControlProduccion {

    //Asociaciones.
    //Advertencia: no dividir esta asociacion con sus multiples hijos, ya que se pueden guardar sin problema en esta coleccion.
    //Llamar y utilizar condiciones de instanceof y casting para llamar a clases hijas.

    private final ArrayList<Persona> personas = new ArrayList<>();
    private final ArrayList<Huerto> huertos = new ArrayList<>();
    private final ArrayList<Cultivo> cultivos = new ArrayList<>();
    private final ArrayList<PlanCosecha>  planCosechas = new ArrayList<>();

    public boolean createPropietario(Rut rut, String nombre, String email, String direccionParticular, String direccionComercial) {
        //Asegura que esta persona no existe,  si existe, retorna false.
        if (findPersona(rut) != null) return false;

        // De lo contrario, lo agrega a la colección.
        return personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut) != null) return false;
        //Agrega el supervisor a la coleccion.
        return personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public boolean createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut) != null) return false;
        //Agrega el cosechador a la coleccion.
        return personas.add(new Cosechador(rut, nombre, email, direccion, fechaNacimiento));
    }

    public boolean createCultivo(int id, String nombre, String periodo, float rendimiento) {
        if (findCultivo(id) != null) return false;
        return cultivos.add(new Cultivo(id, nombre, periodo, rendimiento));
    }

    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) {
        // Primero verifico que no exista el huerto
        if(findHuerto(nombre) == null){

            // Verifico si es que el rut es de un propetario
            Propietario propietario = (Propietario) findPersona(rutPropietario);

            if (propietario != null ){

                // Creo el huerto
                return huertos.add(new Huerto(nombre,superficie,ubicacion,propietario));
            }
        }
        return false;
    }

    public boolean addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {

        // Verificar que el huerto exista y que el cuartel no tenga un huerto asociado
        if( findCuartel(idCuartel, nombreHuerto) == null){
            return findHuerto(nombreHuerto).addCuartel(idCuartel,superficie,findCultivo(idCultivo));
        }
        return false;
    }

    public boolean createPlanCosecha(int id, String nombrePlan, LocalDate fechaInicio, LocalDate fechaFin, double metaKilos, double precioBaseKilo, String nomHuerto, int idCuartel) {
        //Asigna una variable al huerto que se encuentra a traves del metodo findHuerto.
        Huerto huertoEncontrado = findHuerto(nomHuerto);
        //Se asegura que el plan cosecha no exista, a traves de su id.
        //Si encuentra un plan cosecha existente, retorna false.
        if (findPlanCosecha(id) != null) return false;
        //Asegura que la fecha de inicio no sea supeerior o igual a la fecha de fin.
        if (fechaInicio.isAfter(fechaFin) && fechaInicio.isEqual(fechaFin)) return false;
        //Si no existe el huerto pasado por parametros, retorna false.
        if (huertoEncontrado == null) return false;
        //Condicion que asegura que el cuartel pasado por parametros exista y sea parte del huerto.
        Cuartel cuartelEncontrado = findCuartel(idCuartel, nomHuerto);
        if (cuartelEncontrado != null){
            //Crea el nuevo plan de cosecha.
            return planCosechas.add(new PlanCosecha(id,nombrePlan,fechaInicio, fechaFin, metaKilos, precioBaseKilo, cuartelEncontrado));
        }
        // De lo contrario, retorna false.
        return false;
    }

    public boolean addCuadrillaToPlan(int idPlan, int idCuadrilla, String nombreCuadrilla, Rut rutSupervisor) {
        //Asigna el resultado de findPersona a una variable, para mejor legibilidad de codigo.
        //se hace un casting a la clase hija Supervisor, porque el metodo  findPersona retorna un objeto Persona.
        Supervisor supervisorEncontrado = (Supervisor) findPersona(rutSupervisor);
        //Si no encuentra a ningun supervisor con el rut pasado por parametro, retorna false.
        if (supervisorEncontrado == null) return false;
        //Si encuentra una cuadrilla ya asignada al supervisor, retorna false.
        if (supervisorEncontrado.getCuadrillaAsignada() != null) return false;
        //Utiliza el metodo private findPlanCosecha, asegura que este no sea null.
        if (findPlanCosecha(idPlan) != null) {
            //Asigna este resultado a una variable.
            PlanCosecha plan = findPlanCosecha(idPlan);
            /*
            Utiliza otro metodo private llamado findCuadrilla, si ve que esta cuadrilla ya existe en su coleccion de cuadrillas
            Retornará la cuadrilla que encuentre.
            En este caso, no queremos que pase eso, porque estamos agregando una nueva cuadrilla
            que no exista en su coleccion.
            */
            if (findCuadrilla(idCuadrilla, plan.getId()) == null
                    && plan.addCuadrilla(idCuadrilla, nombreCuadrilla, supervisorEncontrado)){ //Ignorar advertencia, condicion que no sea null existe.
                //Retorna true si es que esta operación puede realizarse.
                return plan.addCuadrilla(idCuadrilla, nombreCuadrilla, supervisorEncontrado);
            }
        }
        //De lo contrario, retorna false.
        return false;
    }

    public boolean addCosechadorToCuadrilla(int idPlanCosecha, int idCuadrilla, LocalDate fechaIniCosechador,LocalDate fechaFinCosechador, double metaKilosCosechador, Rut rutCosechador){
        //Asigna los metodos findPlanCosecha, findPersona y findCuadrilla a variables para mejor legibilidad
        PlanCosecha plan = findPlanCosecha(idPlanCosecha);
        Cosechador cosechadorEncontrado = (Cosechador)findPersona(rutCosechador);
        //Condiciones que aseguran que estas variables existan, de lo contrario, devuelven false.
        if (plan == null) return false;
        Cuadrilla cuadrillaEncontrada = findCuadrilla(idCuadrilla, plan.getId());
        if (cuadrillaEncontrada == null) return false;
        if (cosechadorEncontrado == null) return false;
        //booleano isAfter, que asegura que la fecha de inicio no es superior a la fecha final. Solo puede ser Inferior o Igual.
        //Además, asegura que la fecha de inicio y final este en el intervalo de tiempo del plan de cosecha.
        if (!fechaIniCosechador.isAfter(fechaFinCosechador) && !fechaIniCosechador.isBefore(plan.getInicio()) && !fechaFinCosechador.isAfter(plan.getFinEstimado()) ) return false;
        //Retornará un valor booleano que mostrara si la acción se pudo realizar o no.
        return plan.addCosechadorToCuadrilla(idCuadrilla, fechaIniCosechador, fechaFinCosechador, metaKilosCosechador, cosechadorEncontrado);

    }

    public String[] listCultivos(){
        if (cultivos.isEmpty()) return new String[0];
        String[] listaCultivos = new String[cultivos.size()];
        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);
            //Talvez se deba usar String.format, chequear después.
            listaCultivos[i] = String.join(", ", Integer.toString(cultivo.getId()), cultivo.getEspecie(), cultivo.getVariedad(), Double.toString(cultivo.getRendimiento()), Integer.toString(cultivo.getCuarteles().length));
        }
        return listaCultivos;
    }

    public String[] listHuertos(){
        if(huertos.isEmpty()) return new String[0];
        String[] listaHuertos = new String[huertos.size()];
        for (int i = 0; i < huertos.size(); i++) {
            listaHuertos[i] = String.join(", ", huertos.get(i).getNombre(), Float.toString(huertos.get(i).getSuperficie()), huertos.get(i).getUbicacion(),
                    huertos.get(i).getPropietario().getRut().toString(), huertos.get(i).getPropietario().getNombre(), Integer.toString(huertos.get(i).getCuartels().length ));
        }
        return listaHuertos;
    }
    public String[] listPropietarios(){
        /*Cambie los parametros del for de las listas de personas, listaXXX.length -> persona.size(). Ademas, agregue otra variable(j) que avanza cada vez que encuentra un tipo de persona.
        **Este cambio permite buscar entre todas las personas y guardar en el arreglo solo las personas necesarias, ya que con el length se llenaba el arreglo de nulls, por que el for avanzaba de numero
        * y eso iba avanzando espacios en el arreglo. */
        if(personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen propietarios.
        if (findArraySize(3) == -1) return new String[0];
        String[] listaPropietarios = new String[findArraySize(3)];
        // Busco los propietarios de la lista de personas
        for (int i=0, j=0; i < personas.size(); i++) {
            if(personas.get(i) instanceof Propietario){
                listaPropietarios[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(),
                        personas.get(i).getEmail(), ((Propietario) personas.get(i)).getDirComercial(), Integer.toString(((Propietario) personas.get(i)).getHuertos().length));
                j++;
            }
        }
        return  listaPropietarios;
    }

    public String[] listSupervisores(){
        if(personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen supervisores
        if (findArraySize(1) == -1) return new String[0];
        String [] listaSupervisores = new String[findArraySize(1)];
        // Busco los supervisores de la lista de personas
        for(int i=0, j=0; i< personas.size(); i++){
            if(personas.get(i) instanceof  Supervisor) {
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

    public String [] listCosechadores(){
        if(personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio
        //El metodo findArraySize busca el tamaño del arreglo, si es -1, entonces no existen cosechadores
        if (findArraySize(2) == -1) return new String[0];
        String [] listaCosechadores = new String[findArraySize(2)];
        // Busco los cosechadores de la lista de personas
        for(int i=0, j=0; i< personas.size(); i++){
            if(personas.get(i) instanceof  Cosechador){
                int cuadrillasAsignadas = ((Cosechador)personas.get(i)).getCuadrillas().length;
                listaCosechadores[j] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(),
                        ((Cosechador)personas.get(i)).getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.toString(cuadrillasAsignadas));
                j++;
            }
        }
        return listaCosechadores;
    }

    public String [] listPlanes(){
        if(planCosechas.isEmpty()) return new String[0]; // Sino existen planes de cosechas retorna un arreglo vacío

        String [] listaPlanesCosechas = new String[planCosechas.size()];
        // Guardo los planes de Cosecha en el arreglo
        int cont = 0;
        for(PlanCosecha planCosecha : planCosechas){
            listaPlanesCosechas[cont] = String.join(", ", Integer.toString(planCosecha.getId()), planCosecha.getNombre(), planCosecha.getInicio().toString(),
                    planCosecha.getFinEstimado().toString(), Double.toString(planCosecha.getMetaKilos()), planCosecha.getEstado().toString(), Integer.toString(planCosecha.getCuartel().getId()) ,
                    planCosecha.getCuartel().getHuerto().getNombre(), Integer.toString(planCosecha.getCuadrillas().length));
            cont++;
        }
        return listaPlanesCosechas;
    }

    //Metodo private que encuentra a una persona deseada a través de su rut.
    private Persona findPersona(Rut rut) {
        //Busca a la persona.
        for (Persona persona : personas) {
            //compara rut.
            if (persona.getRut().toString().equals(rut.toString())) return persona;
        }
        //De lo contrario, no existe.
        return null;
    }

    //private method para encontrar un cultivo.
    private Cultivo findCultivo(int id) {
        for (Cultivo cultivos : cultivos) {
            //Compara el id pasado por parametro con el getId() del objeto.
            if (id == cultivos.getId()) return cultivos;
        }
        //De lo contrario, retorna null.
        return null;
    }

    //Metodo privado para encontar el plan de cosecha deseado.
    private PlanCosecha findPlanCosecha(int id) {
        //Si no existen planes de cosecha, retorna null
        if (planCosechas.isEmpty()) return null;
        //Busca entre la coleccion
        for (PlanCosecha planCosecha : planCosechas) {
            //Compara, lo devuelve.
            if (id == planCosecha.getId()) return planCosecha;
        }
        //De lo contrario.
        return null;
    }
    //Metodo private para encontrar una cuadrilla deseada
    private Cuadrilla findCuadrilla(int idCuadrilla, int idPlan) {
        //Para no repetir codigo, es necesario pasar por parametro el identificador del PlanCosecha.
        //De esta forma, se reutiliza el codigo findPlanCosecha y se ahorra lineas de codigo.
        if (findPlanCosecha(idPlan) != null) {
            if (findPlanCosecha(idPlan).getCuadrillas().length == 0) return null;
            //Se asigna el array que retorna findPlanCosecha.getCuadrillas a una variable para mejor legibilidad.
            Cuadrilla[] cuadrillas = findPlanCosecha(idPlan).getCuadrillas();//Ignorar advertencia. La linea de arriba asegura que findPlanCosecha no sea null.
            for (int i = 0; i < cuadrillas.length; i++) {
                //Recorre el array, compara identificadores con su metodo getId().
                //Lo encuentra, lo devuelve.
                if (cuadrillas[i].getId() == idCuadrilla) return cuadrillas[i];
            }
        }
        // De lo contrario, no existe.
        return null;
    }
    private Huerto findHuerto(String nombre) {
        // Verifico que exista un huerto con esos datos
        for(Huerto huerto : huertos) {
            // Si existe retorna el huerto
            if(huerto.getNombre().equals(nombre))  return huerto;
        }
        // De lo contrario retona null, por lo tanto no existe
        return null;
    }
    private Cuartel findCuartel (int idCuartel, String nombreHuerto) {
        /* Cuarteles no tiene un relacion directa con la clase, por lo que accedemos
        por medio de alguna relacion que tenga, en este caso los huertos
         */

        // Si no existe el huerto, retona null
        if (findHuerto(nombreHuerto) == null) return null;

        for(Huerto huerto : huertos) {
            //Condicion que evita NullPointerException, porque getCuartel puede ser null.
            if (huerto.getCuartel(idCuartel) != null) {
                if (huerto.getCuartel(idCuartel).getId() == idCuartel) {

                    // Devuelve el cuartel si es que lo tiene asignado
                    return huerto.getCuartel(idCuartel);
                }
            }
        }
        // Si no tiene asignado el cuartel pasado como parametro retorna null
        return null;
    }
    private int findArraySize(int opcion) {
        int supervisores = 0, cosechadores = 0, propietarios = 0;
        if (!personas.isEmpty()) {
            //dependiendo de opcion: 1 = supervisor, 2 == cosechador, 3 == propietarios;
            for (Persona persona1 : personas) {
                if (persona1 instanceof Supervisor) supervisores++;
                if (persona1 instanceof Cosechador) cosechadores++;
                if (persona1 instanceof  Propietario) propietarios++;
            }
            if (opcion == 1 && supervisores != 0) return supervisores;
            if (opcion == 2 && cosechadores != 0) return cosechadores;
            if (opcion == 3 && propietarios != 0) return propietarios;
        }
        //Si no existe, retorna -1.
        return -1;
    }
}
