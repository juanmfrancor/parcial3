package co.edu.uniquindio.poo.parcial3.model;

public class PanaderoIndustrial extends Empleado implements IPanadero {
    public PanaderoIndustrial(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento, codigo, minutosDisponibles);
    }


    @Override
    public int calcularMinutos(Pan tipoPan, int cantidad) {

        int lotes = (cantidad + 29) / 30;

        if (tipoPan == Pan.TRADICIONAL) {
            return 20 + (lotes * 25);
        }

        return 20 + (lotes * 35);
    }
}

