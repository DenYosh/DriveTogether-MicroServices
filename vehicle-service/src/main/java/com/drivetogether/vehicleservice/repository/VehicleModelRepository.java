package com.drivetogether.vehicleservice.repository;

import com.drivetogether.vehicleservice.model.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    VehicleModel findByModelName(String modelName);
    boolean existsByModelName(String modelName);
}
