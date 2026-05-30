package cl.pymetrack.msproductos.producto.controller;

import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    void getProductosByPyme_DeberiaRetornarListaDeDtos() {
        // Preparar
        Long pymeId = 1L;
        List<ProductoDTO> listaFalsa = new ArrayList<>();
        ProductoDTO dto = new ProductoDTO();
        // Asumiendo que ProductoDTO tiene estos setters básicos, si no, puedes quitarlos
        // dto.setNombre("Producto Test"); 
        listaFalsa.add(dto);

        when(productoService.getProductosByPymeAsDTO(pymeId)).thenReturn(listaFalsa);

        // Actuar
        ResponseEntity<?> response = productoController.getProductosByPyme(pymeId);

        // Afirmar
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<ProductoDTO> cuerpo = (List<ProductoDTO>) response.getBody();
        assertEquals(1, cuerpo.size());
    }
}