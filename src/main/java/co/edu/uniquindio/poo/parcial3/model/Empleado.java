package co.edu.uniquindio.poo.parcial3.model;

public abstract class Empleado {
    public String nombre;
    public String documento;
    public String codigo;
    public int minutosDisponibles;
    public Panaderia ownedByPanaderia;


    public Empleado(String nombre, String documento, String codigo, int minutosDisponibles) {
        this.nombre = nombre;
        this.documento = documento;
        this.codigo = codigo;
        this.minutosDisponibles = minutosDisponibles;
    }
    public void descontarMinutos(int minutos) {
        if (minutos > minutosDisponibles) {
            throw new IllegalStateException("No hay suficientes minutos");
        }

        minutosDisponibles -= minutos;
    }

    public void devolverMinutos(int minutos) {
        minutosDisponibles += minutos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getMinutosDisponibles() {
        return minutosDisponibles;
    }

    public void setMinutosDisponibles(int minutosDisponibles) {
        this.minutosDisponibles = minutosDisponibles;
    }
}
