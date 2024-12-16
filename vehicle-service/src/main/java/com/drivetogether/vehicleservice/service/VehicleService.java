package com.drivetogether.vehicleservice.service;

import com.drivetogether.vehicleservice.dto.VehicleDTO;
import com.drivetogether.vehicleservice.model.Vehicle;
import com.drivetogether.vehicleservice.model.VehicleModel;
import com.drivetogether.vehicleservice.repository.VehicleModelRepository;
import com.drivetogether.vehicleservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public VehicleDTO createVehicle(Long userId, VehicleDTO vehicleDTO) {
        if (vehicleDTO.getId() == null) {
            vehicleDTO.setOwnerId(userId);

            if (!vehicleModelRepository.existsByModelName(vehicleDTO.getModel())) {
                VehicleModel model = new VehicleModel();
                model.setModelName(vehicleDTO.getModel());
                vehicleModelRepository.save(model);
            }

            Vehicle veh = vehicleRepository.save(mapToVehicleDTO(vehicleDTO));
            return mapToVehicle(veh);
        }

        throw new RuntimeException("Vehicle Already exists");
    }

    private Vehicle mapToVehicleDTO(VehicleDTO vehicleDTO) {
        return Vehicle.builder()
                .id(vehicleDTO.getId())
                .ownerId(vehicleDTO.getOwnerId())
                .licensePlate(vehicleDTO.getLicensePlate())
                .make(vehicleDTO.getMake())
                .model(vehicleModelRepository.findByModelName(vehicleDTO.getModel()))
                .capacity(vehicleDTO.getCapacity())
                .build();
    }

    private VehicleDTO mapToVehicle(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .ownerId(vehicle.getOwnerId())
                .licensePlate(vehicle.getLicensePlate())
                .make(vehicle.getMake())
                .model(vehicle.getModel().getModelName())
                .capacity(vehicle.getCapacity())
                .build();
    }
}
