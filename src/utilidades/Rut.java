//Creado por: Gabriel Rojas
//Revisado por: Gabriel Rojas
package utilidades;

public class Rut {
    //Atributos
    private final long numero;
    private final char dv;

    //Constructor
    private Rut(long numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }
    public static Rut of(String rutStr){
        //Recibira un String con el rut deseado, cortara en la posicion donde esta el '-' y después convertira los dos valores
        //a Long y Char respectivaente, y retornara la creacion de un nuevo objeto rut.
        //En el UML, el constructor rut es private.
        String[] cortar = rutStr.split("-");
        //Corta en los puntos.
        String[] rutNum = cortar[0].split("\\.");
        StringBuilder numero = new StringBuilder();
        for (String rut : rutNum) {
            //reconstruye el numero con un stringbuilder
           numero.append(rut);
        }
        return new Rut(Long.parseLong(numero.toString()), cortar[1].charAt(0));
    }
    //Metodos
    @Override
    public String toString() {
        return numero+"-"+dv;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Rut otro = (Rut) obj;
        return  this.toString().equals(otro.toString());
    }
}
