package com.xc.springbootpractice.Controller;

import com.xc.springbootpractice.Entity.Result;
import com.xc.springbootpractice.Entity.Student;
import com.xc.springbootpractice.Service.StudentService;
import com.xc.springbootpractice.Utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/listAll")
    public List<Student> listAllStudent() {
        logger.info("method");
        List<Student> list = studentService.getAllStudentList();
        return list;
    }

    @PostMapping(value = "/student")
    public Result addStudent(@Valid Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResultUtil().error(101, bindingResult.getFieldError().getDefaultMessage());
        }
        return studentService.addStudent(student);
    }
}
