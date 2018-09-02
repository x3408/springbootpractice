package com.xc.springbootpractice.Service;

import com.xc.springbootpractice.Entity.Result;
import com.xc.springbootpractice.Entity.Student;
import com.xc.springbootpractice.Repository.StudentRepository;
import com.xc.springbootpractice.Utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudentList() {
        List<Student> all = studentRepository.findAll();
        return all;
    }

    public Result addStudent(@Valid Student student) {
        studentRepository.save(student);
        return ResultUtil.success(student);
    }
}
