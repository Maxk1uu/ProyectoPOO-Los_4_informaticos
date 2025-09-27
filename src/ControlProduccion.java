//Hecho por: Gabriel Rojas
//Ultima revision:
import java.util.ArrayList;
import java.time.LocalDate;
public class ControlProduccion {
    //Asociaciones.
    //Advertencia: no dividir esta asociacion con sus multiples hijos, ya que se pueden guardar sin problema en esta coleccion.
    //Llamar y utilizar condiciones de instanceof y casting para llamar a clases hijas.
    private ArrayList<Persona> personas;
    private ArrayList<Huerto> huertos;
    private ArrayList<Cultivo> cultivos;

    public boolean createPropietario(Rut rut, String nombre, String email, String direccionParticular, String direccionComercial) {
        //Asegura que esta persona no existe,  si existe, retorna false.
        if (findPersona(rut, 3) == null) return false;

        // De lo contrario, lo agrega a la colección.
        return personas.add(new Propietario(rut, nombre, email, direccionParticular, direccionComercial));
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) {
        //Check para asegurar que esta persona no existe.
        if (findPersona(rut, 2) == null) return false;
        //Agrega el supervisor a la coleccion.
        return personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public boolean createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) {
        //Asegura que un cosechador con el mismo rut pasado por el parametro no exista.
        if (findPersona(rut, 1) == null) return false;
        //Agrega el cosechador a la coleccion.
        return personas.add(new Cosechador(rut, nombre, email, direccion, fechaNacimiento));
    }

    public boolean createCultivo(int id, String nombre, String periodo, float rendimiento) {
        if (findCultivo(id) == null) return false;
        return cultivos.add(new Cultivo(id, nombre, periodo, rendimiento));
    }

    //Necesidad de la clase huerto.
    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) {
    }

    //Necesidad de la clase huerto.
    public boolean addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {
    }

    //Necesidad de la clase cuartel.
    public boolean createPlanCosecha(int id, String nombrePlan, LocalDate fechaInicio, LocalDate fechaFin, double metaKilos, double precioBaseKilo, String nomHuerto, int idCuartel) {
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
            if (plan.addCuadrilla(idCuadrilla, nombreCuadrilla, supervisorEncontrado)){ //Ignorar advertencia, condicion que no sea null existe.
                //Retorna true si es que esta operación puede realizarse.
                return true;
            }
        }
        //De lo contrario, retorna false.
        return false;

    }
    public boolean addCosechadorToCuadrilla(int idPlanCosecha, int idCuadrilla, LocalDate fechaIniCosechador,LocalDate fechaFinCosechador, double metaKilosCosechador, Rut rutCosechador){
        //Asigna los metodos findPlanCosecha y findPersona a variables para mejor legibilidad
        PlanCosecha plan = findPlanCosecha(idPlanCosecha);
        Cosechador cosechadorEncontrado = (Cosechador)findPersona(rutCosechador, 1);
        //Condiciones que aseguran que estas variables existan, de lo contrario, devuelven null.
        if (plan == null) return false;
        if (findCuadrilla(idCuadrilla, plan.getId()) == null) return false;
        if (cosechadorEncontrado == null) return false;
        //Condiciones más complejas con LocalDate: por hacer.

    }

    //Metodo private que encuentra a una persona deseada a través de su rut.
    private Persona findPersona(Rut rut, int tipo) {
        //Por tener 3 colecciones estaticas, es necesario recorrer los 3 arraylist cada uno.
        //Recomendable encontrar una mejor implementación, talvez juntar estas 3 colecciones es una.
        //tipo es una variable pasada por parametro para elegir que rut se quiere encontrar.
        //esta variable es automatica y elegida en el metodo correspondiente.
        //1 = Cosechador, 2 = Supervisor, 3 = Propietario.
        //mejor implementacion: por hacer.
        switch (tipo) {
            case 1:
                for (Rut ruts : Rut.rutsCosechadores) {
                    if (rut.toString().equals(ruts.toString())) {
                        for (Persona personas : personas) {
                            if (personas.toString().equals(ruts.toString())) return personas;
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
}

