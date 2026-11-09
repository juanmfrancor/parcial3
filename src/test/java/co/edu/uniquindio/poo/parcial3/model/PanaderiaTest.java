package co.edu.uniquindio.poo.parcial3.model;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PanaderiaTest {

    private static final Logger LOG =
            Logger.getLogger(PanaderiaTest.class.getName());

    // Objetos que vamos a reutilizar
    private Panaderia panaderia;
    private panaderoArtesanal ana;
    private PanaderoIndustrial carlos;

    @BeforeEach
    public void setUp() {

        panaderia = new Panaderia("El Trigal");

        ana = new panaderoArtesanal("Ana", "123", "A01", 100);

        carlos = new PanaderoIndustrial("Carlos", "456", "I01", 120);

        panaderia.agregarEmpleado(ana);
        panaderia.agregarEmpleado(carlos);
    }

    @Test
    public void registrarPedidoTest() {

        LOG.info("Inicio de prueba registrarPedidoTest");

        // Act: registrar el pedido
        panaderia.registrarPedido(
                "P01", "Juan", Pan.TRADICIONAL, 25
        );

        // Buscar el pedido registrado
        Pedido pedido = panaderia.buscarPedido("P01");

        // Assert: comprobar los resultados
        assertNotNull(pedido);
        assertEquals(Estado.PENDIENTE, pedido.getEstado());
        assertEquals(25, pedido.getCantidadPan());
        assertEquals(100, ana.getMinutosDisponibles());

        LOG.info("Fin de prueba registrarPedidoTest");
    }

    @Test
    public void registrarPedidoCodigoDuplicadoTest() {

        LOG.info("Inicio de prueba registrarPedidoCodigoDuplicadoTest");

        // Arrange: registrar el primer pedido
        panaderia.registrarPedido(
                "P01", "Juan", Pan.TRADICIONAL, 25
        );

        // Act: cancelar el primer pedido
        panaderia.cancelarPedido("P01");

        // Assert: intentar registrar otro pedido con el mismo código
        assertThrows(IllegalArgumentException.class, () -> {
            panaderia.registrarPedido(
                    "P01", "Pedro", Pan.INTEGRAL, 30
            );
        });

        // Comprobar que solamente existe el pedido original
        assertEquals(1, panaderia.getListaPedidos().size());

        // Comprobar que el pedido original sigue cancelado
        assertEquals(
                Estado.CANCELADO,
                panaderia.buscarPedido("P01").getEstado()
        );

        LOG.info("Fin de prueba registrarPedidoCodigoDuplicadoTest");
    }
    @Test
    public void registrarPedidoCantidadInvalidaTest() {

        LOG.info("Inicio de prueba registrarPedidoCantidadInvalidaTest");

        // Act y Assert: intentar registrar un pedido con cantidad 0
        assertThrows(IllegalArgumentException.class, () -> {
            panaderia.registrarPedido(
                    "P03", "Juan", Pan.TRADICIONAL, 0
            );
        });

        // Comprobar que no se registró ningún pedido
        assertEquals(0, panaderia.getListaPedidos().size());

        LOG.info("Fin de prueba registrarPedidoCantidadInvalidaTest");
    }


    @Test
    public void asignarPedidoMenorTiempoTest() {
        LOG.info("Inicio de prueba asignarPedidoMenorTiempoTest");

        carlos.setMinutosDisponibles(45);

        panaderia.registrarPedido("P04", "Juan", Pan.TRADICIONAL, 25);
        panaderia.asignarPedido("P04");

        Pedido pedido = panaderia.buscarPedido("P04");

        assertEquals(Estado.ASIGNADO, pedido.getEstado());
        assertEquals("I01", pedido.getCodigoPanaderoAsignado());
        assertEquals(45, pedido.getMinutosReservados());
        assertEquals(100, ana.getMinutosDisponibles());
        assertEquals(0, carlos.getMinutosDisponibles());

        LOG.info("Fin de prueba asignarPedidoMenorTiempoTest");
    }

    @Test
    public void asignarPedidoEmpateTest() {
        LOG.info("Inicio de prueba asignarPedidoEmpateTest");

        ana.setMinutosDisponibles(80);

        panaderoArtesanal diana =
                new panaderoArtesanal("Diana", "789", "A02", 80);

        carlos.setMinutosDisponibles(54);
        panaderia.agregarEmpleado(diana);

        panaderia.registrarPedido("P05", "Pedro", Pan.INTEGRAL, 13);
        panaderia.asignarPedido("P05");

        Pedido pedido = panaderia.buscarPedido("P05");

        assertEquals(Estado.ASIGNADO, pedido.getEstado());
        assertEquals("A01", pedido.getCodigoPanaderoAsignado());
        assertEquals(80, pedido.getMinutosReservados());
        assertEquals(0, ana.getMinutosDisponibles());
        assertEquals(80, diana.getMinutosDisponibles());
        assertEquals(54, carlos.getMinutosDisponibles());

        LOG.info("Fin de prueba asignarPedidoEmpateTest");
    }

    @Test
    public void asignarPedidoSinTiempoDisponibleTest() {
        LOG.info("Inicio de prueba asignarPedidoSinTiempoDisponibleTest");

        carlos.setMinutosDisponibles(44);

        panaderia.getListaEmpleados().clear();
        panaderia.agregarEmpleado(carlos);

        Cajero lucia = new Cajero("Lucía", "999", "C01", 120,"8");
        panaderia.agregarEmpleado(lucia);

        panaderia.registrarPedido("P06", "Juan", Pan.TRADICIONAL, 25);

        assertThrows(IllegalStateException.class, () -> {
            panaderia.asignarPedido("P06");
        });

        assertEquals(Estado.PENDIENTE,
                panaderia.buscarPedido("P06").getEstado());
        assertEquals(44, carlos.getMinutosDisponibles());
        assertEquals(120, lucia.getMinutosDisponibles());

        LOG.info("Fin de prueba asignarPedidoSinTiempoDisponibleTest");
    }

    @Test
    public void cancelarPedidoAsignadoTest() {
        LOG.info("Inicio de prueba cancelarPedidoAsignadoTest");

        panaderia.registrarPedido("P07", "Juan", Pan.TRADICIONAL, 25);
        panaderia.registrarPedido("P08", "Pedro", Pan.TRADICIONAL, 25);

        panaderia.asignarPedido("P07");
        panaderia.asignarPedido("P08");

        assertEquals(30, carlos.getMinutosDisponibles());

        panaderia.cancelarPedido("P07");

        assertEquals(75, carlos.getMinutosDisponibles());
        assertEquals(Estado.CANCELADO,
                panaderia.buscarPedido("P07").getEstado());
        assertEquals(Estado.ASIGNADO,
                panaderia.buscarPedido("P08").getEstado());

        LOG.info("Fin de prueba cancelarPedidoAsignadoTest");
    }

    @Test
    public void cancelarPedidoPendienteTest() {
        LOG.info("Inicio de prueba cancelarPedidoPendienteTest");

        panaderia.registrarPedido("P09", "Juan", Pan.TRADICIONAL, 25);

        panaderia.cancelarPedido("P09");

        assertEquals(Estado.CANCELADO,
                panaderia.buscarPedido("P09").getEstado());
        assertEquals(1, panaderia.getListaPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());

        LOG.info("Fin de prueba cancelarPedidoPendienteTest");
    }

    @Test
    public void cancelarPedidoDosVecesTest() {
        LOG.info("Inicio de prueba cancelarPedidoDosVecesTest");

        panaderia.registrarPedido("P10", "Juan", Pan.TRADICIONAL, 25);
        panaderia.asignarPedido("P10");

        assertEquals(75, carlos.getMinutosDisponibles());

        panaderia.cancelarPedido("P10");

        assertEquals(120, carlos.getMinutosDisponibles());

        assertThrows(IllegalStateException.class, () -> {
            panaderia.cancelarPedido("P10");
        });

        assertEquals(120, carlos.getMinutosDisponibles());
        assertEquals(Estado.CANCELADO,
                panaderia.buscarPedido("P10").getEstado());

        LOG.info("Fin de prueba cancelarPedidoDosVecesTest");
    }

}
