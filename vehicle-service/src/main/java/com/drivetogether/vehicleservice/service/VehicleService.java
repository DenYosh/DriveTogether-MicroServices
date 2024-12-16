package com.drivetogether.vehicleservice.service;

import com.drivetogether.vehicleservice.dto.VehicleModelRequestDTO;
import com.drivetogether.vehicleservice.dto.VehicleModelResponseDTO;
import com.drivetogether.vehicleservice.dto.VehicleRequestDTO;
import com.drivetogether.vehicleservice.dto.VehicleResponseDTO;
import com.drivetogether.vehicleservice.model.Vehicle;
import com.drivetogether.vehicleservice.model.VehicleModel;
import com.drivetogether.vehicleservice.repository.VehicleModelRepository;
import com.drivetogether.vehicleservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto) {
        VehicleModel model = vehicleModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));

        Vehicle vehicle = Vehicle.builder()
                .ownerId(dto.getOwnerId())
                .licensePlate(dto.getLicensePlate())
                .make(dto.getMake())
                .model(model)
                .capacity(dto.getCapacity())
                .build();

        vehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private VehicleResponseDTO mapToResponseDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .ownerId(vehicle.getOwnerId())
                .licensePlate(vehicle.getLicensePlate())
                .make(vehicle.getMake())
                .modelName(vehicle.getModel().getModelName())
                .capacity(vehicle.getCapacity())
                .build();
    }


//    Vehicle models

    public VehicleModelResponseDTO createVehicleModel(VehicleModelRequestDTO dto) {
        VehicleModel model = VehicleModel.builder()
                .modelName(dto.getModelName())
                .build();
        model = vehicleModelRepository.save(model);
        return mapToResponseDTO(model);
    }

    public List<VehicleModelResponseDTO> getAllVehicleModels() {
        return vehicleModelRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private VehicleModelResponseDTO mapToResponseDTO(VehicleModel model) {
        return VehicleModelResponseDTO.builder()
                .id(model.getId())
                .modelName(model.getModelName())
                .build();
    }
}
