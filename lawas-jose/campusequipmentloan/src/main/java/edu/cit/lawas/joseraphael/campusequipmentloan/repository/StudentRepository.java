package edu.cit.lawas.joseraphael.campusequipmentloan.repository;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StudentEntity> rowMapper = (rs, rowNum) -> {
        StudentEntity st = new StudentEntity();
        st.setId(rs.getLong("id"));
        st.setStudentNo(rs.getString("student_no"));
        st.setName(rs.getString("name"));
        st.setEmail(rs.getString("email"));
        return st;
    };

    public List<StudentEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM student", rowMapper);
    }

    public StudentEntity findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM student WHERE id = ?",
                rowMapper,
                id
        );
    }

    public int save(StudentEntity student) {
        return jdbcTemplate.update(
                "INSERT INTO student(student_no, name, email) VALUES(?, ?, ?)",
                student.getStudentNo(), student.getName(), student.getEmail()
        );
    }

    public StudentEntity findByStudentNo(String studentNo) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM student WHERE student_no = ?",
                rowMapper,
                studentNo
        );
    }
}
