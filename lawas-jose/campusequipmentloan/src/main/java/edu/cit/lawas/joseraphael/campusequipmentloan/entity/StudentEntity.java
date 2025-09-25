package edu.cit.lawas.joseraphael.campusequipmentloan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_no", nullable = false, unique = true)
    private String studentNo;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private String lname;

    @Column(nullable = false, unique = true)
    private String uname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
