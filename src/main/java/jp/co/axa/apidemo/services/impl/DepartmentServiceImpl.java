package jp.co.axa.apidemo.services.impl;

import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.repositories.DepartmentRepository;
import jp.co.axa.apidemo.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Department> retrieveDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartment(Long departmentId) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        return departmentOptional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentByName(String departmentName) {
        Optional<Department> departmentOptional = departmentRepository.findByName(departmentName);
        return departmentOptional.orElse(null);
    }

    @Override
    @Transactional
    public Department createDepartment(Department department) throws ServiceException {
        if (department.getId() != null)
            throw new ServiceException("Department should not contain id.");

        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    @Transactional
    public Department updateDepartment(Department department) throws ServiceException {

        if (department.getId() == null)
            throw new ServiceException("Department not found.");

        if (!departmentRepository.existsById(department.getId()))
            throw new ServiceException("Department not found.");

        return departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> retrieveDepartmentEmployees(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(Department::getEmployees)
                .orElse(new ArrayList<>());
    }
}