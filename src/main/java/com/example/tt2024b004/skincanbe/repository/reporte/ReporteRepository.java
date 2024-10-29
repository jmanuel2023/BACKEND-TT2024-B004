package com.example.tt2024b004.skincanbe.repository.reporte;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Reporte.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long>{
    Optional<Reporte> findByLesion(Lesion lesion);
}

