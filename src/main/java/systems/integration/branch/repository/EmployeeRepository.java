package systems.integration.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.integration.branch.domain.Branch;
import systems.integration.branch.domain.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByName(String name);
    List<Employee> findByRole(String role);
    List<Employee> findByBranch(Branch branch);
}
