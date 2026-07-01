package cl.pymetrack.msproductos.inventario.controller;

import cl.pymetrack.msproductos.inventario.entity.Inventario;
import cl.pymetrack.msproductos.inventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // <--- EL IMPORT SALVAVIDAS
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class InventarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventarioController).build();
        objectMapper = new ObjectMapper();
        // ¡AQUÍ ESTÁ LA MAGIA PARA LAS FECHAS!
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllInventario_Success() throws Exception {
        when(inventarioService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk());
    }

    @Test
    void getInventarioById_Found() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(Optional.of(new Inventario()));
        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getInventarioById_NotFound() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInventarioByProductoId_Found() throws Exception {
        when(inventarioService.findByProductoId(1L)).thenReturn(Optional.of(new Inventario()));
        mockMvc.perform(get("/inventario/producto/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getInventarioByProductoId_NotFound() throws Exception {
        when(inventarioService.findByProductoId(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/inventario/producto/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createInventario_Success() throws Exception {
        Inventario inv = new Inventario();
        when(inventarioService.save(any(Inventario.class))).thenReturn(inv);

        mockMvc.perform(post("/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk());
    }

    @Test
    void updateInventario_Found() throws Exception {
        Inventario inv = new Inventario();
        when(inventarioService.findById(1L)).thenReturn(Optional.of(inv));
        when(inventarioService.save(any(Inventario.class))).thenReturn(inv);

        mockMvc.perform(put("/inventario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk());
    }

    @Test
    void updateInventario_NotFound() throws Exception {
        Inventario inv = new Inventario();
        when(inventarioService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/inventario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarStock_Success() throws Exception {
        Inventario inv = new Inventario();
        when(inventarioService.actualizarStock(1L, 10, 5)).thenReturn(inv);

        mockMvc.perform(put("/inventario/producto/1/stock")
                .param("stockDisponible", "10")
                .param("stockReservado", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarStock_NotFound() throws Exception {
        when(inventarioService.actualizarStock(1L, 10, 5)).thenReturn(null);

        mockMvc.perform(put("/inventario/producto/1/stock")
                .param("stockDisponible", "10")
                .param("stockReservado", "5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInventario_Found() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(Optional.of(new Inventario()));
        doNothing().when(inventarioService).deleteById(1L);

        mockMvc.perform(delete("/inventario/1"))
                .andExpect(status().isOk());
        
        verify(inventarioService, times(1)).deleteById(1L);
    }

    @Test
    void deleteInventario_NotFound() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/inventario/1"))
                .andExpect(status().isNotFound());
        
        verify(inventarioService, never()).deleteById(anyLong());
    }
}