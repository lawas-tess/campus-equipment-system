package edu.cit.lawas.joseraphael.campusequipmentloan.repository;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.EquipmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EquipmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public EquipmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EquipmentEntity> rowMapper = (rs, rowNum) -> {
        EquipmentEntity eq = new EquipmentEntity();
        eq.setId(rs.getLong("id"));
        eq.setName(rs.getString("name"));
        eq.setType(rs.getString("type"));
        eq.setSerialNumber(rs.getString("serial_number"));
        eq.setAvailability(rs.getBoolean("availability"));
        return eq;
    };

    public List<EquipmentEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM equipment", rowMapper);
    }

    public EquipmentEntity findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM equipment WHERE id = ?",
                rowMapper,
                id
        );
    }

    public int save(EquipmentEntity equipment) {
        return jdbcTemplate.update(
                "INSERT INTO equipment(name, type, serial_number, availability) VALUES(?, ?, ?, ?)",
                equipment.getName(), equipment.getType(), equipment.getSerialNumber(), equipment.isAvailability()
        );
    }

    public int updateAvailability(Long id, boolean available) {
        return jdbcTemplate.update(
                "UPDATE equipment SET availability = ? WHERE id = ?",
                available, id
        );
    }
}
