package edu.cit.lawas.joseraphael.campusequipmentloan.controller;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.EquipmentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<?> addEquipment(@RequestBody EquipmentEntity equipment) {
        try {
            equipmentService.save(equipment);
            return ResponseEntity.ok(Map.of(
                    "message", "Equipment added successfully.",
                    "id", equipment.getId(),
                    "name", equipment.getName(),
                    "type", equipment.getType(),
                    "serialNumber", equipment.getSerialNumber(),
                    "availability", equipment.isAvailability()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(Map.of(
                    "error", e.getMessage(),
                    "name", equipment.getName(),
                    "type", equipment.getType(),
                    "serialNumber", equipment.getSerialNumber()
            ));
        }
    }


    @GetMapping
    public ResponseEntity<List<EquipmentEntity>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<EquipmentEntity>> getAvailableEquipment() {
        List<EquipmentEntity> available = equipmentService.getAll().stream()
                .filter(EquipmentEntity::isAvailability)
                .collect(Collectors.toList());

        return ResponseEntity.ok(available);
    }
}
