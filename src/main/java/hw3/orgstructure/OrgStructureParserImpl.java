package hw3.orgstructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrgStructureParserImpl implements OrgStructureParser {

    @Override
    public Employee parseStructure(File csvFile) throws IOException {

        Map<Long, Employee> employees = readCsvToMap(csvFile);

        Employee boss = null;
        for (Long id : employees.keySet()) {
            Employee currentEmployee = employees.get(id);
            Employee currentBoss = employees.get(currentEmployee.getBossId());
            currentEmployee.setBoss(currentBoss);
            if (currentBoss != null) {
                currentBoss.getSubordinate().add(currentEmployee);
            } else  {
                boss = currentEmployee;
            }
        }
        return boss;
    }

    private Map<Long, Employee> readCsvToMap(File csvFile) throws IOException {
        Map<Long, Employee> employeesMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String s = reader.readLine();
            if (!s.equals("id;boss_id;name;position")) {
                throw new FileStructureNotValidException("illegal first line");
            }

            while ((s = reader.readLine()) != null) {
                String[] employeeArr = s.split(";");
                validate(employeeArr);
                Employee employee = createEmployee(employeeArr);
                employeesMap.put(employee.getId(), employee);
            }
        }
        return employeesMap;
    }

    private Employee createEmployee(String[] employeeArr) {
        Employee employee = new Employee();
        employee.setId(convertStringToLong(employeeArr[0]));
        employee.setBossId(convertStringToLong(employeeArr[1]));
        employee.setName(employeeArr[2]);
        employee.setPosition(employeeArr[3]);

        return employee;
    }

    private void validate(String[] employeeArr) {
        if (employeeArr.length != 4) {
            throw new FileStructureNotValidException("employee has less parameters than it should");
        }
        if (employeeArr[0].isEmpty()) {
            throw new FileStructureNotValidException("all employees should have an id");
        }
    }

    private Long convertStringToLong(String s) {
        try {
            return s.isEmpty() ? null : Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new FileStructureNotValidException(s + " should be a number");
        }
    }
}
