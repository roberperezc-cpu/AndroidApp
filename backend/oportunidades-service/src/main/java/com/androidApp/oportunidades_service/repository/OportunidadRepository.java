package com.androidApp.oportunidades_service.repository;

import com.androidApp.oportunidades_service.model.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OportunidadRepository extends JpaRepository<Oportunidad, Integer> {
}