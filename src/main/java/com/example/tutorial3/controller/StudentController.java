package com.example.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tutorial3.model.StudentModel;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;

@Controller
public class StudentController {
	private final StudentService studentService;
	
	public StudentController() {
		studentService = new InMemoryStudentService();
	}
	
	@RequestMapping("/student/add")
	public String add(@RequestParam(value = "npm", required = true) String npm,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "gpa", required = true) double gpa) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		return "add";
	}
	
	/*@RequestMapping("/student/view")
	public String view(Model model, @RequestParam(value = "npm", required = true) String npm) {
		StudentModel student = studentService.selectStudent(npm);
		model.addAttribute("student", student);
		return "view";
	}*/
	
	@RequestMapping("/student/viewall")
	public String viewAll(Model model) {
		List<StudentModel> students = studentService.selectAllStudents();
		model.addAttribute("students", students);
		return "viewall";
	}
	
	@RequestMapping(value = {"/student/view", "/student/view/{npm}"})
	public String studentPath(@PathVariable Optional<String> npm, Model model) {
	if (npm.isPresent()) {
	StudentModel student = studentService.selectStudent(npm.get());
	if(student == null) {
		return "errorNotFound";
	}
	else {
		model.addAttribute("name", student.getName());
		model.addAttribute("npm", student.getNpm());
		model.addAttribute("gpa", student.getGpa());
		return "view";
	}
	} else {
		return "errorPage";
	}
	}
	
	@RequestMapping(value = {"/student/delete", "/student/delete/{npm}"})
	public String studentDelete(@PathVariable Optional<String> npm, Model model) {
	if (npm.isPresent()) {
	StudentModel student = studentService.selectStudent(npm.get());
	if(student == null) {
		return "errorNotFound";
	}
	else {
		StudentModel deletedStudent = studentService.deleteStudent(npm.get());
		model.addAttribute("name", deletedStudent.getName());
		model.addAttribute("npm", deletedStudent.getNpm());
		model.addAttribute("gpa", deletedStudent.getGpa());
		return "deleteView";
	}
	} else {
		return "errorPage";
	}
	}
}
