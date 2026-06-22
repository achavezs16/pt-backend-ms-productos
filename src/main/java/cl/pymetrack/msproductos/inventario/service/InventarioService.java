package cl.pymetrack.msproductos.inventario.service;

import cl.pymetrack.msproductos.inventario.entity.Inventario;
import cl.pymetrack.msproductos.inventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    public Optional<Inventario> findByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    public boolean existsByProductoId(Long productoId) {
        return inventarioRepository.existsByProductoId(productoId);
    }

    @Transactional
    public Inventario actualizarStock(Long productoId, int stockDisponible, int stockReservado) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe inventario para el producto ID: " + productoId));

        inventario.setStockDisponible(stockDisponible);
        inventario.setStockReservado(stockReservado);

        return inventarioRepository.save(inventario);
    }

    @Transactional
    public boolean reservarStock(Long productoId, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe inventario para el producto ID: " + productoId));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (inventario.getStockDisponible() < cantidad) {
            return false;
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        inventario.setStockReservado(inventario.getStockReservado() + cantidad);
        inventarioRepository.save(inventario);

        return true;
    }

    @Transactional
    public boolean liberarStock(Long productoId, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe inventario para el producto ID: " + productoId));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (inventario.getStockReservado() < cantidad) {
            return false;
        }

        inventario.setStockDisponible(inventario.getStockDisponible() + cantidad);
        inventario.setStockReservado(inventario.getStockReservado() - cantidad);
        inventarioRepository.save(inventario);

        return true;
    }

    @Transactional
    public boolean confirmarDespacho(Long productoId, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe inventario para el producto ID: " + productoId));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (inventario.getStockReservado() >= cantidad) {
            inventario.setStockReservado(inventario.getStockReservado() - cantidad);
            inventarioRepository.save(inventario);
            return true;
        }

        if (inventario.getStockDisponible() >= cantidad) {
            inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
            inventarioRepository.save(inventario);
            return true;
        }

        return false;
    }
}
