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
        return equipmentRepository.findById(id);
    }

    public List<EquipmentEntity> getAll() {
        return equipmentRepository.findAll();
    }

    public void save(EquipmentEntity equipment) {
        equipmentRepository.save(equipment);
    }

    public void updateAvailability(Long id, boolean available) {
        equipmentRepository.updateAvailability(id, available);
    }
}
