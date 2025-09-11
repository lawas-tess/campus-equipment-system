package edu.cit.lawas.joseraphael.campusequipmentloan.repository;
import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


@Repository
public class LoanRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<LoanEntity> rowMapper = (rs, rowNum) -> {
        LoanEntity loan = new LoanEntity();
        loan.setId(rs.getLong("id"));
        loan.setStartDate(rs.getObject("start_date", LocalDate.class));
        loan.setDueDate(rs.getObject("due_date", LocalDate.class));
        loan.setReturnDate(rs.getObject("return_date", LocalDate.class));
        loan.setStatus(LoanEntity.Status.valueOf(rs.getString("status")));
        return loan;
    };

    public List<LoanEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM loan", rowMapper);
    }

    public LoanEntity findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM loan WHERE id = ?",
                rowMapper,
                id
        );
    }

    public int save(LoanEntity loan) {
        return jdbcTemplate.update(
                "INSERT INTO loan(equipment_id, student_id, start_date, due_date, return_date, status) VALUES(?, ?, ?, ?, ?, ?)",
                loan.getEquipment().getId(),
                loan.getStudent().getId(),
                loan.getStartDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus().name()
        );
    }

    public long countActiveLoans(Long studentId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM loan WHERE student_id = ? AND status IN ('ONGOING','OVERDUE')",
                Long.class,
                studentId
        );
    }

    public int updateStatus(Long id, LoanEntity.Status status, LocalDate returnDate) {
        return jdbcTemplate.update(
                "UPDATE loan SET status = ?, return_date = ? WHERE id = ?",
                status.name(), returnDate, id
        );
    }
}

