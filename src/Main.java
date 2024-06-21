import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Person> listPerson = new ArrayList<>();
        List<Department> listDepartment = new ArrayList<>();
        String[] names = {"Иван", "Вася", "Петя", "Саша", "Андрей", "Денис", "Света", "Маша", "Даша", "Максим"};
        Random random = new Random();
        List<Person> list = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            Department department = new Department();
            department.setName("Департамент " + i);
            listDepartment.add(department);
        }

        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setName(names[random.nextInt(10)]);
            person.setAge(random.nextInt(18, 65));
            person.setSalary(random.nextInt(20000, 200000));
            person.setDepart(listDepartment.get(random.nextInt(5)));
            listPerson.add(person);
        }

        for(Person p : listPerson){
            System.out.println(p);
        }

        System.out.println();

        System.out.println(findMostYoungestPerson(listPerson));
        System.out.println();
        System.out.println(findMostExpensiveDepartment(listPerson));
        System.out.println();
        System.out.println(groupByDepartment(listPerson));
        System.out.println();
        System.out.println(groupByDepartmentName(listPerson));
        System.out.println();
        System.out.println(getDepartmentOldestPerson(listPerson));
        System.out.println();
        System.out.println(cheapPersonsInDepartment(listPerson));
        System.out.println();


    }

    private static class Department {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Person {
        private String name;
        private int age;
        private double salary;
        private Main.Department depart;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public Department getDepart() {
            return depart;
        }

        public void setDepart(Department depart) {
            this.depart = depart;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", depart=" + depart +
                    '}';
        }
    }

    /**
     * Найти самого молодого сотрудника
     */
    static Optional<Person> findMostYoungestPerson(List<Main.Person> people) {
        return people.stream().min((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()));
    }

    /**
     * Найти департамент, в котором работает сотрудник с самой большой зарплатой
     */
    static Optional<Department>  findMostExpensiveDepartment(List<Main.Person> people) {
        return people.stream().max((o1, o2) -> Double.compare(o1.getSalary(), o2.getSalary()))
                .map(Person::getDepart);
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    static Map<Department, List<Person>> groupByDepartment(List<Main.Person> people) {
        return people.stream().collect(Collectors.groupingBy(Person::getDepart));
    }

    /**
     * Сгруппировать сотрудников по названиям департаментов
     */
    static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
        return people.stream().collect(Collectors.groupingBy(p -> p.getDepart().getName()));
    }

    /**
     * В каждом департаменте найти самого старшего сотрудника
     */
    static Map<String, Main.Person> getDepartmentOldestPerson(List<Main.Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDepart().getName(),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Person::getAge)),
                                Optional::get
                        )
                ));
    }

    /**
     * *Найти сотрудников с минимальными зарплатами в своем отделе
     * (прим. можно реализовать в два запроса)
     */
    static List<Person> cheapPersonsInDepartment(List<Person> people) {
        Map<String, Optional<Person>> minSalaryByDepartment = people.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDepart().getName(),
                        Collectors.minBy(Comparator.comparingDouble(Person::getSalary))
                ));

        return minSalaryByDepartment.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}