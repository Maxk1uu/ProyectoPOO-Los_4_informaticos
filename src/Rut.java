//Codigo hecho por: Maximiliano Maureira
//Revisado por: Gabriel Rojas

public class Rut {
    //Atributos
    private String rut;

    //Constructor
    public Rut(String rut) {
        this.rut = rut;
    }

    //Metodos
    @Override
    public String toString() {
        return rut;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Rut otro = (Rut) obj;
        return  this.rut.equals(otro.rut);
    }
}
