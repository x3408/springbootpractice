package com.xc.springbootpractice.Repository;

import com.xc.springbootpractice.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
