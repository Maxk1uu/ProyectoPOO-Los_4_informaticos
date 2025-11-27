// Hecho por Ricardo Quintana
package persistencia;

import modelo.Cultivo;
import modelo.Persona;
import modelo.PlanCosecha;
import utilidades.GestionHuertosException;

import javax.smartcardio.CardChannel;
import java.io.*;
import java.util.ArrayList;

public class GestionHuertosIO {
    // Patron singleton
    private static GestionHuertosIO instance;

    private GestionHuertosIO() {
    }

    public static GestionHuertosIO getInstance() {
        if (instance == null) instance = new GestionHuertosIO();
        return instance;
    }

    // Metodos
    public Persona[] savePersonas(Persona[] personas) throws GestionHuertosException {
        try (ObjectOutputStream archObjPersonas = new ObjectOutputStream(new FileOutputStream("Personas.obj"))) {
            for (Persona persona : personas) {
                archObjPersonas.writeObject(persona);
            }
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Error al abrir/crear el archivo Personas.obj");
        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar escribir en el archivo Personas.obj");
        }
        return personas;
    }

    public Cultivo[] saveCultivos(Cultivo[] cultivos) throws GestionHuertosException {
        try (ObjectOutputStream archObjCultivos = new ObjectOutputStream(new FileOutputStream("Cultivos.obj"))) {
            for (Cultivo cultivo : cultivos) {
                archObjCultivos.writeObject(cultivo);
            }
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Error al abrir/crear el archivo Cultivos.obj");
        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar escribir en el archivo Cultivos.obj");
        }
        return cultivos;
    }

    public PlanCosecha[] savePlanesCosecha(PlanCosecha[] planCosechas) throws GestionHuertosException {
        try (ObjectOutputStream archObjPlanesCosecha = new ObjectOutputStream(new FileOutputStream("PlanCosecha.obj"))) {
            for (PlanCosecha planCosecha : planCosechas) {
                archObjPlanesCosecha.writeObject(planCosecha);
            }
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Error al abrir/crear el archivo PlanCosecha.obj");
        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar escribir en el archivo PlanesCosecha.obj");
        }
        return planCosechas;
    }

    public Persona[] readPersonas() throws GestionHuertosException {
        ArrayList<Persona> personas = new ArrayList<>();

        try (ObjectInputStream archObjPersonas =
                     new ObjectInputStream(new FileInputStream("Personas.obj"))) {

            while (true) {
                try {
                    personas.add((Persona) archObjPersonas.readObject());
                } catch (EOFException e) {
                    archObjPersonas.close();
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Archivo Personas.obj no encontrado");

        } catch (ClassCastException | ClassNotFoundException e) {
            throw new GestionHuertosException("Objeto leído no corresponde a Persona");

        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar leer datos del archivo Personas.obj");
        }

        return personas.toArray(new Persona[0]);
    }

    public Cultivo[] readCultivos() throws GestionHuertosException {
        ArrayList<Cultivo> cultivos = new ArrayList<>();

        try (ObjectInputStream archObjCultivos =
                     new ObjectInputStream(new FileInputStream("Cultivos.obj"))) {

            while (true) {
                try {
                    cultivos.add((Cultivo) archObjCultivos.readObject());
                } catch (EOFException e) {
                    archObjCultivos.close();
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Archivo Cultivos.obj no encontrado");

        } catch (ClassCastException | ClassNotFoundException e) {
            throw new GestionHuertosException("Objeto leído no corresponde a Cultivo");

        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar leer datos del archivo Cultivos.obj");
        }

        return cultivos.toArray(new Cultivo[0]);
    }

    public PlanCosecha[] readPlanesCosecha() throws GestionHuertosException {
        ArrayList<PlanCosecha> planesCosecha = new ArrayList<>();

        try (ObjectInputStream archObjPlanCosecha =
                     new ObjectInputStream(new FileInputStream("PlanesCosecha.obj"))) {

            while (true) {
                try {
                    planesCosecha.add((PlanCosecha) archObjPlanCosecha.readObject());
                } catch (EOFException e) {
                    archObjPlanCosecha.close();
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            throw new GestionHuertosException("Archivo PlanesCosecha.obj no encontrado");

        } catch (ClassCastException | ClassNotFoundException e) {
            throw new GestionHuertosException("Objeto leído no corresponde a PlanCosecha");

        } catch (IOException e) {
            throw new GestionHuertosException("Error al intentar leer datos del archivo PlanesCosecha.obj");
        }

        return planesCosecha.toArray(new PlanCosecha[0]);
    }
}