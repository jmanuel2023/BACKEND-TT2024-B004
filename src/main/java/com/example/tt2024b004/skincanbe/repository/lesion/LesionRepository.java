package com.example.tt2024b004.skincanbe.repository.lesion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;

@Repository
public interface LesionRepository extends JpaRepository<Lesion,Long> {

    
} 
