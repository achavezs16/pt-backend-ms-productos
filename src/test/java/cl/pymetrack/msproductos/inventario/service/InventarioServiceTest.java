package cl.pymetrack.msproductos.inventario.service;

import cl.pymetrack.msproductos.inventario.entity.Inventario;
import cl.pymetrack.msproductos.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void findAll_Success() {
        when(inventarioRepository.findAll()).thenReturn(List.of(new Inventario()));
        List<Inventario> result = inventarioService.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void findById_Success() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(new Inventario()));
        Optional<Inventario> result = inventarioService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void findByProductoId_Success() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(new Inventario()));
        Optional<Inventario> result = inventarioService.findByProductoId(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void save_Success() {
        Inventario i = new Inventario();
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(i);
        Inventario result = inventarioService.save(i);
        assertNotNull(result);
    }

    @Test
    void deleteById_Success() {
        doNothing().when(inventarioRepository).deleteById(1L);
        inventarioService.deleteById(1L);
        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void existsByProductoId_Success() {
        when(inventarioRepository.existsByProductoId(1L)).thenReturn(true);
        boolean result = inventarioService.existsByProductoId(1L);
        assertTrue(result);
    }

    // --- Tests de Lógica de Negocio (actualizarStock) ---
    @Test
    void actualizarStock_Success() {
        Inventario inv = new Inventario();
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inv);

        Inventario result = inventarioService.actualizarStock(1L, 10, 5);
        assertEquals(10, result.getStockDisponible());
        assertEquals(5, result.getStockReservado());
    }

    @Test
    void actualizarStock_NotFound() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> inventarioService.actualizarStock(1L, 10, 5));
    }

    // --- Tests de Lógica de Negocio (reservarStock) ---
    @Test
    void reservarStock_Success() {
        Inventario inv = new Inventario();
        inv.setStockDisponible(10);
        inv.setStockReservado(0);
        
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.reservarStock(1L, 5);
        assertTrue(result);
        assertEquals(5, inv.getStockDisponible());
        assertEquals(5, inv.getStockReservado());
    }

    @Test
    void reservarStock_InsufficientStock() {
        Inventario inv = new Inventario();
        inv.setStockDisponible(2); // Menos que lo solicitado
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.reservarStock(1L, 5);
        assertFalse(result);
    }

    @Test
    void reservarStock_NegativeQuantity() {
        Inventario inv = new Inventario();
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.reservarStock(1L, -5));
    }

    // --- Tests de Lógica de Negocio (liberarStock) ---
    @Test
    void liberarStock_Success() {
        Inventario inv = new Inventario();
        inv.setStockDisponible(5);
        inv.setStockReservado(5);
        
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.liberarStock(1L, 5);
        assertTrue(result);
        assertEquals(10, inv.getStockDisponible());
        assertEquals(0, inv.getStockReservado());
    }

    @Test
    void liberarStock_InsufficientReservedStock() {
        Inventario inv = new Inventario();
        inv.setStockReservado(2); // Menos que lo solicitado
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.liberarStock(1L, 5);
        assertFalse(result);
    }

    @Test
    void liberarStock_NegativeQuantity() {
        Inventario inv = new Inventario();
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.liberarStock(1L, -5));
    }

    // --- Tests de Lógica de Negocio (confirmarDespacho) ---
    @Test
    void confirmarDespacho_Success() {
        Inventario inv = new Inventario();
        inv.setStockReservado(5);
        
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.confirmarDespacho(1L, 5);
        assertTrue(result);
        assertEquals(0, inv.getStockReservado());
    }

    @Test
    void confirmarDespacho_InsufficientReservedStock() {
        Inventario inv = new Inventario();
        inv.setStockReservado(2); // Menos que lo solicitado
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        
        boolean result = inventarioService.confirmarDespacho(1L, 5);
        assertFalse(result);
    }

    @Test
    void confirmarDespacho_NegativeQuantity() {
        Inventario inv = new Inventario();
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inv));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.confirmarDespacho(1L, -5));
    }
}