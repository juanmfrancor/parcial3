package co.edu.uniquindio.poo.parcial3.model;

import java.util.ArrayList;
import java.util.List;

public class Panaderia {
    public String nombre;
    public List<Empleado> listaEmpleados = new ArrayList<>();
    public List<Pedido> listaPedidos = new ArrayList<>();

    public Panaderia(String nombre) {
        this.nombre = nombre;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public void agregarEmpleado(Empleado empleado) {
        listaEmpleados.add(empleado);
    }

    public Pedido buscarPedido(String codigo) {
        return listaPedidos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // REGISTRAR PEDIDO

    public void registrarPedido(String codigo, String nombreCliente,
                                Pan tipoPan, int cantidadPan) {

        if (cantidadPan <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        boolean existe = listaPedidos.stream()
                .anyMatch(p -> p.codigo.equals(codigo));

        if (existe) {
            throw new IllegalArgumentException("El código ya existe");
        }

        Pedido pedido = new Pedido(
                codigo,
                nombreCliente,
                tipoPan,
                cantidadPan

        );

        listaPedidos.add(pedido);
    }

    // ASIGNAR PEDIDO

    public void asignarPedido(String codigo) {

        Pedido pedido = buscarPedido(codigo);

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido no existe");
        }

        if (pedido.getEstado() != Estado.PENDIENTE) {
            throw new IllegalStateException(
                    "El pedido no está pendiente");
        }

        Empleado mejorEmpleado = listaEmpleados.stream()

                .filter(e -> e instanceof IPanadero)

                .filter(e -> ((IPanadero) e).calcularMinutos(
                        pedido.getTipoPan(),
                        pedido.getCantidadPan())
                        <= e.getMinutosDisponibles())

                .min((e1, e2) -> Integer.compare(
                        ((IPanadero) e1).calcularMinutos(
                                pedido.getTipoPan(), pedido.getCantidadPan()),

                        ((IPanadero) e2).calcularMinutos(
                                pedido.getTipoPan(), pedido.getCantidadPan())
                ))

                .orElse(null);

        if (mejorEmpleado == null) {
            throw new IllegalStateException(
                    "No hay panaderos disponibles para este pedido");
        }

        int minutos = ((IPanadero) mejorEmpleado).calcularMinutos(
                pedido.getTipoPan(),
                pedido.getCantidadPan());

        mejorEmpleado.descontarMinutos(minutos);

        pedido.asignar(mejorEmpleado.getCodigo(), minutos);
    }

    // CANCELAR PEDIDO

    public void cancelarPedido(String codigo) {

        Pedido pedido = buscarPedido(codigo);

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido no existe");
        }

        if (pedido.getEstado() ==Estado.CANCELADO) {
            throw new IllegalStateException(
                    "El pedido ya está cancelado");
        }

        if (pedido.getEstado() == Estado.ASIGNADO) {

            Empleado empleado = listaEmpleados.stream()

                    .filter(e -> e.getCodigo().equals(
                            pedido.getCodigoPanaderoAsignado()))

                    .findFirst()
                    .orElse(null);

            if (empleado == null) {
                throw new IllegalStateException(
                        "No se encontró el panadero asignado");
            }

            empleado.devolverMinutos(
                    pedido.getMinutosReservados());
        }

        pedido.cancelar();
    }
}