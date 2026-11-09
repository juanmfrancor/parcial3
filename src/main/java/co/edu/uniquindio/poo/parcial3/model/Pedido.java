package co.edu.uniquindio.poo.parcial3.model;

public class Pedido {
    public String codigo;
    public String nombreCliente;
    public Pan tipoPan;
    public int cantidadPan;
    public Estado estado;
    public String codigoPanaderoAsignado;
    public int minutosReservados;
    public Panaderia ownedByPanaderia;


    public Pedido(String codigo, String nombreCliente, Pan tipoPan, int cantidadPan) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.tipoPan = tipoPan;
        this.cantidadPan = cantidadPan;
        this.estado = Estado.PENDIENTE;
        this.codigoPanaderoAsignado = null;
        this.minutosReservados = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Pan getTipoPan() {
        return tipoPan;
    }

    public void setTipoPan(Pan tipoPan) {
        this.tipoPan = tipoPan;
    }

    public int getCantidadPan() {
        return cantidadPan;
    }

    public void setCantidadPan(int cantidadPan) {
        this.cantidadPan = cantidadPan;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getCodigoPanaderoAsignado() {
        return codigoPanaderoAsignado;
    }

    public void setCodigoPanaderoAsignado(String codigoPanaderoAsignado) {
        this.codigoPanaderoAsignado = codigoPanaderoAsignado;
    }

    public int getMinutosReservados() {
        return minutosReservados;
    }

    public void setMinutosReservados(int minutosReservados) {
        this.minutosReservados = minutosReservados;
    }

    public void asignar(String codigoPanadero, int minutos) {
        this.codigoPanaderoAsignado = codigoPanadero;
        this.minutosReservados = minutos;
        this.estado = Estado.ASIGNADO;
    }

    public void cancelar() {
        this.estado = Estado.CANCELADO;
    }
}

