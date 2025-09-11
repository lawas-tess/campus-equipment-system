package edu.cit.lawas.joseraphael.campusequipmentloan.controller;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.EquipmentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping("/addEquipment")
    public ResponseEntity<EquipmentEntity> addEquipment(@RequestBody EquipmentEntity equipment) {
        equipmentService.save(equipment);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/getAllEquipment")
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
