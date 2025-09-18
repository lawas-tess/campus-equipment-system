package edu.cit.lawas.joseraphael.campusequipmentloan.service;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.EquipmentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentEntity getById(Long id) {
        return equipmentRepository.findById(id).orElseThrow();
    }

    public List<EquipmentEntity> getAll() {
        return equipmentRepository.findAll();
    }

    public void save(EquipmentEntity equipment) {
        boolean exists = equipmentRepository.findAll().stream()
                .anyMatch(e -> e.getSerialNumber().equalsIgnoreCase(equipment.getSerialNumber()));
        if (exists) {
            throw new IllegalStateException("Equipment already exists with serial number: " + equipment.getSerialNumber());
        }
        equipmentRepository.save(equipment);
    }


    public void updateAvailability(Long id, boolean available) {
        EquipmentEntity eq = getById(id);
        eq.setAvailability(available);
        equipmentRepository.save(eq);
    }
}
