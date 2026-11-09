package co.edu.uniquindio.poo.parcial3.model;

public class panaderoArtesanal extends Empleado implements IPanadero{
    public panaderoArtesanal(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento, codigo, minutosDisponibles);
    }


    @Override
    public int calcularMinutos(Pan tipoPan, int cantidad) {

        int bandejas = (cantidad + 11) / 12;

        if (tipoPan == Pan.TRADICIONAL) {
            return bandejas * 30;
        }

        return bandejas * 40;
    }
}

