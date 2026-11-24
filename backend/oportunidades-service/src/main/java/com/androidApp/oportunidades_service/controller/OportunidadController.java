package com.androidApp.oportunidades_service.controller;

import com.androidApp.oportunidades_service.model.Oportunidad;
import com.androidApp.oportunidades_service.repository.OportunidadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
public class OportunidadController {

    private final OportunidadRepository repository;

    public OportunidadController(OportunidadRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Oportunidad> obtenerTodas() {
        return repository.findAll();
    }

    @PostMapping
    public Oportunidad crear(@RequestBody Oportunidad oportunidad) {
        // Android envía id = 0 en registros nuevos; lo pasamos a null para que JPA genere el ID
        if (oportunidad.getId() != null && oportunidad.getId() == 0) {
            oportunidad.setId(null);
        }
        return repository.save(oportunidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidad> actualizar(@PathVariable Integer id, @RequestBody Oportunidad datos) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNombre(datos.getNombre());
                    existente.setTipo(datos.getTipo());
                    existente.setDescripcion(datos.getDescripcion());
                    existente.setFuente(datos.getFuente());
                    existente.setPrioridad(datos.getPrioridad());
                    existente.setPotencial(datos.getPotencial());
                    existente.setEstado(datos.getEstado());
                    existente.setObservaciones(datos.getObservaciones());
                    existente.setFotoUri(datos.getFotoUri());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
