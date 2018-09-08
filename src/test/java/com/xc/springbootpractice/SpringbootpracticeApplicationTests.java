package com.xc.springbootpractice;

import com.xc.springbootpractice.Entity.Result;
import com.xc.springbootpractice.Entity.Student;
import com.xc.springbootpractice.Repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootpracticeApplicationTests {

	@Autowired
	private Student student;
	@Autowired
	private StudentRepository studentRepository;

	@Test
	public void beanTest() {
		System.out.println(student);
	}

	@Test
	public void deleteTest() {
		studentRepository.deleteById(7);
	}
}
