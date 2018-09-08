package com.xc.springbootpractice.Controller;

import com.xc.springbootpractice.Entity.Result;
import com.xc.springbootpractice.Entity.Student;
import com.xc.springbootpractice.Service.StudentService;
import com.xc.springbootpractice.Utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    /**
     * 查询所有的学生列表
     * @param model
     * @return
     */
    @GetMapping(value = "/students")
    public ModelAndView listAllStudent(Model model) {
        logger.info("method");
        List<Student> list = studentService.getAllStudentList();

        //将集合添加到视图中
        model.addAttribute("students", list);
        ModelAndView mav = new ModelAndView("listing");
        return mav;
    }

    /**
     * 添加一个学生
     * @param student
     * @param bindingResult
     * @return
     */
    @GetMapping(value = "/student")
    public String addStudent(@Valid Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
//            return new ResultUtil().error(101, bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/students";
        }
        studentService.addStudent(student);
        return "redirect:/students";
    }

    /**
     * 编辑页回显
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/student/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model) {
        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);
        return "add";
    }

    /**
     * 修改学生
     * @param student
     * @return
     */
    @PutMapping(value = "/student")
    public String addStudent(Student student) {
        studentService.updateStudent(student);

        return "redirect:/students";
    }

    /**
     * 删除学生
     * @param id
     * @return
     */
    @DeleteMapping(value = "/student/{id}")
    public String deleteStudent(@PathVariable("id") Integer id) {
        Student studentById = studentService.getStudentById(id);
        studentService.deleteStudent(studentById);

        return "redirect:/students";
    }
}
