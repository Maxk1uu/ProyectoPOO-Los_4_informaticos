//Hecho por: Gabriel Rojas
//Ultima revision:
import java.util.ArrayList;
import java.time.LocalDate;
public class ControlProduccion {

    //Asociaciones.
    //Advertencia: no dividir esta asociacion con sus multiples hijos, ya que se pueden guardar sin problema en esta coleccion.
    //Llamar y utilizar condiciones de instanceof y casting para llamar a clases hijas.

    private ArrayList<Persona> personas = new ArrayList<>();
    private ArrayList<Huerto> huertos = new ArrayList<>();
    private ArrayList<Cultivo> cultivos = new ArrayList<>();

    public boolean createPropietario(Rut rut, String nombre, String email, String direccionParticular, String direccionComercial) {
        //Asegura que esta persona no existe,  si existe, retorna false.
        if (findPersona(rut, 3) != null) return false;

        // De lo contrario, lo agrega a la colección.
        return personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut, 2) != null) return false;
        //Agrega el supervisor a la coleccion.
        return personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public boolean createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut, 1) != null) return false;
        //Agrega el cosechador a la coleccion.
        return personas.add(new Cosechador(rut, nombre, email, direccion, fechaNacimiento));
    }

    public boolean createCultivo(int id, String nombre, String periodo, float rendimiento) {
        if (findCultivo(id) == null) return false;
        return cultivos.add(new Cultivo(id, nombre, periodo, rendimiento));
    }

    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) {
        // Primero verifico que no exista el huerto
        if(findHuerto(nombre) == null){

            // Verifico si es que el rut es de un propetario
            Propietario propietario = (Propietario) findPersona(rutPropietario,3);

            if (propietario != null ){

                // Creo el huerto
                return huertos.add(new Huerto(nombre,superficie,ubicacion,propietario));
            }
        }
        return false;
    }

    public boolean addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {

        // Verificar que el huerto exista y que el cuartel no tenga un huerto asociado
        if( findCuartel(idCuartel, nombreHuerto) != null){
            return findHuerto(nombreHuerto).addCuartel(idCuartel,superficie,findCultivo(idCultivo));
        }
        return false;
    }

    //Necesidad de la clase huerto.
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
        //Condicion que asegura que el cuartel pasado por parametros exista.
        if (findCuartel(idCuartel, nomHuerto) != null){
            Cuartel cuartelEncontrado = findCuartel(idCuartel, nomHuerto);
            //Crea el nuevo plan de cosecha.
            return cuartelEncontrado.addPlanCosecha(new PlanCosecha(id, nombrePlan, fechaInicio, fechaFin, metaKilos, precioBaseKilo, cuartelEncontrado));
        }
        // De lo contrario, retorna false.
        return false;
    }

    public boolean addCuadrillaToPlan(int idPlan, int idCuadrilla, String nombreCuadrilla, Rut rutSupervisor) {
        //Asigna el resultado de findPersona a una variable, para mejor legibilidad de codigo.
        //se hace un casting a la clase hija Supervisor, porque el metodo  findPersona retorna un objeto Persona.
        Supervisor supervisorEncontrado = (Supervisor) findPersona(rutSupervisor, 2);
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
        Cosechador cosechadorEncontrado = (Cosechador)findPersona(rutCosechador, 1);
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
            listaCultivos[i] = String.join(", ", Integer.toString(cultivo.getId()), cultivo.getEspecie(),Double.toString(cultivo.getRendimiento()), cultivo.getVariedad());
        }
        return listaCultivos;
    }

    public String[] listHuertos(){
        if(huertos.isEmpty()) return new String[0];
        String[] listaHuertos = new String[huertos.size()];
        for (int i = 0; i < huertos.size(); i++) {
            listaHuertos[i] = String.join(", ", huertos.get(i).getNombre(), Float.toString(huertos.get(i).getSuperficie()), huertos.get(i).getUbicacion(), huertos.get(i).getPropietario().getRut().toString(), huertos.get(i).getPropietario().getNombre(), Integer.toString(huertos.get(i).getCuartels().length ));
        }
        return listaHuertos;
    }
    public String[] listPropietarios(){
        if(personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio

        String[] listaPropietarios = new String[personas.size()];
        // Busco los propietarios de la lista de personas
        for (int i = 0; i < personas.size(); i++) {
            if(personas.get(i) instanceof Propietario){
                listaPropietarios[i] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(), ((Propietario) personas.get(i)).getDirComercial(), Integer.toString(((Propietario) personas.get(i)).getHuertos().length));
            }
        }
        return  listaPropietarios;
    }

    public String[] listSupervisores(){
        if(personas.isEmpty()) return new String[0]; // Sino existen personas retorna un arreglo vacio

        String [] listaSupervisores = new String[personas.size()];
        // Busco los supervisores de la lista de personas
        for(int i = 0; i< personas.size(); i++){
            if(personas.get(i) instanceof  Supervisor){
                listaSupervisores[i] = String.join(", ", personas.get(i).getRut().toString(), personas.get(i).getNombre(), personas.get(i).getDireccion(), personas.get(i).getEmail(), ((Supervisor) personas.get(i)).getProfesion(), ((Supervisor) personas.get(i)).getCuadrillaAsignada().getNombre());
            }
        }
        return listaSupervisores;
    }

    //Metodo private que encuentra a una persona deseada a través de su rut.
    private Persona findPersona(Rut rut, int tipo) {
        //Por tener 3 colecciones estaticas, es necesario recorrer los 3 arraylist cada uno.
        //Recomendable encontrar una mejor implementación, talvez juntar estas 3 colecciones en una.
        //tipo es una variable pasada por parametro para elegir que rut se quiere encontrar.
        //esta variable es automatica y elegida en el metodo correspondiente.
        //1 = Cosechador, 2 = Supervisor, 3 = Propietario.
        //mejor implementacion: por hacer.
        switch (tipo) {
            case 1:
                for (Rut ruts : Rut.rutsCosechadores) {
                    if (rut.toString().equals(ruts.toString())) {
                        for (Persona personas : personas) {
                            if (personas.getRut().toString().equals(ruts.toString())) return personas;
                        }
                    }
                }
                break;
            case 2:
                for (Rut ruts : Rut.rutsSupervisores) {
                    if (rut.toString().equals(ruts.toString())) {
                        for (Persona personas : personas) {
                            if (personas.toString().equals(ruts.toString())) return  personas;
                        }
                    }
                }
                break;
            case 3:
                for (Rut ruts : Rut.rutsPropietarios) {
                    if (rut.toString().equals(ruts.toString())) {
                        for (Persona personas : personas) {
                            if (personas.toString().equals(ruts.toString())) return personas;
                        }
                    }
                }
                break;
        }
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
        //Problema: PlanCosecha no tiene ninguna asociacion con esta clase.
        //Se tiene que obtener el plan cosecha deseado a través de 3 metodos.
        for (Cultivo cultivos : cultivos) {
            //Se asigna una variable con el array obtenido por cultivos.getCuarteles.
            Cuartel[] cuarteles = cultivos.getCuarteles();
            for (int i = 0; i < cuarteles.length; i++) {
                //Recorre el array, y asigna esta variable con su metodo getPlanCosechas en la posicion i.
                PlanCosecha[] planes = cuarteles[i].getPlanCosechas();
                //Recorre el nuevo array asignado.
                for (int k = 0; k < planes.length; k++) {
                    //compara su metodo getId, el cual pertence al objeto PlanCosecha respectivo a la posicion k
                    //con el id pasado por parametro.
                    //Si lo encuentra, lo devuelve.
                    if (planes[k].getId() == id) return planes[k];
                }
            }
        }
        //De lo contrario, no existe.
        return null;
    }
    //Metodo private para encontrar una cuadrilla deseada
    private Cuadrilla findCuadrilla(int idCuadrilla, int idPlan) {
        //Para no repetir codigo, es necesario pasar por parametro el identificador del PlanCosecha.
        //De esta forma, se reutiliza el codigo findPlanCosecha y se ahorra lineas de codigo.
        if (findPlanCosecha(idPlan) != null) {
            if (findPlanCosecha(idPlan).getCuadrillas() == null) return null;
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
            if(huerto.getCuartel(idCuartel) != null){

                // Devuelve el cuartel si es que lo tiene asignado
                return huerto.getCuartel(idCuartel);
            }
        }
        // Si no tiene asignado el cuartel pasado como parametro retorna null
        return null;
    }
}
