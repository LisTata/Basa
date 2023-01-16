package com.company;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ProductsDemo {

    static Connection conn = null;
    static String url = "jdbc:sqlite:C:\\Users\\gk\\Downloads\\data.db" ;
    static List<Product> products = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) throws SQLException {
        createConnection();
        System.out.println();
        createConnection();
        DatabaseMetaData metaData = conn.getMetaData();
        String[] types = {"TABLE"};//ищем таблицы
        //Retrieving the columns in the database
        ResultSet tables = metaData.getTables(null, null, "%", types);
        while (
                tables.next
                        ()) {
            System.out.println(tables.getString("TABLE_NAME"));
        }
      // readAll();
     //   addProduct();
     //   for(Product product: products) System.out.println(product);
        task3();
    }
    public static void createConnection() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void readAll(){
        String sql = "select * from product";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (
                    rs.next
                            ())
                products.add(new Product(
                        rs.getLong("id"),
                        rs.getString("category"),
                        rs.getString("name"),
                        rs.getDouble("price")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //for(Product product😛roducts) System.out.println(product);

        sql = "SELECT * FROM customer";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (
                    rs.next
                            ()) {
                customers.add(new Customer(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("tier")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT o.id as oid, o.order_date, o.delivery_date, o.status,c.idas cid FROM order_customer o " +
        "inner join customer c on (o.customer_id= c.id)";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (
                    rs.next
                            ()) {
                Order order  = new Order(rs.getLong("oid"),
                        rs.getString("order_date"),
                        rs.getString("delivery_date"),
                        rs.getString("status"));
                order.setCustomer(findCustomerById(customers,rs.getLong("cid")));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Наполнение заказов продуктами
        sql = "SELECT * from order_product_relationship";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // loop through the result set
            while (
                    rs.next
                            ()) {
                Long order_id = rs.getLong("order_id");
                Long product_id = rs.getLong("product_id");
                Product product = findProductById(products, product_id);
                if (product!=null)
                    findOrderById(orders, order_id).addProduct(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Customer findCustomerById(Collection<Customer> customers, Long id) {
        return
                customers.stream
                        ()
                        .filter(c -> id==c.getId())
                        .findFirst()
                        .orElse(null);
    }

    public static Product findProductById(Collection<Product> products, Long id) {
        return
                products.stream
                        ()
                        .filter(c -> id==c.getId())
                        .findFirst()
                        .orElse(null);
    }

    public static Order findOrderById(Collection<Order> orders, Long id) {
        return
                orders.stream
                        ()
                        .filter(c -> id==c.getId())
                        .findFirst()
                        .orElse(null);
    }

    static void addProduct(){
        String sql = "INSERT INTO product (name, category, price) VALUES ('name', 'category', 1);";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //Вернуть все книги (Books)
    static void task1(){
        List<Product> result =
                products.stream()
                        .filter(p->p.getCategory().equalsIgnoreCase("Books"))
                        .collect(Collectors.toList());
        for (Product product:result)
            System.out.println(product);
    }
    //Получить все заказы , вкоторых есть хотя бы одна игрушка(Toys)
    static void task2(){
        List<Order> result= orders.stream()
                .filter(o->o.getProducts().stream()
                .anyMatch(p->p.getCategory().equalsIgnoreCase("Toys"))
                )
                .collect(Collectors.toList());
        for (Order order :result)
            System.out.println(order);
    }

    //Найти покупателя на букву "A"
    static void task3(){
        List<Customer> result= customers.stream()
                .filter(c->c.getName().charAt(0)=='A'

                )
                .collect(Collectors.toList());
        for (Customer customer :result)
            System.out.println(customer);
    }

    //Найти товар с максимальной ценой
    static void task4(){
        Optional<Product> result = products.stream()
                .filter((p->p.getCategory().equalsIgnoreCase("Toys")))
                        .max(Comparator.comparing(Product::getPrice));
        System.out.println(result.get().getName());
    }
    //Три последних заказа
    static void task6() {
        List<Order> result =
                orders.stream
                        ()
                        .sorted(Comparator.comparing(Order::getOrderDate))
                        .limit(3)
                        .collect(Collectors.toList());
        for (Order order:result)
            System.out.println(order);
    }


    //Извлечь все продукты из заказов, выполненные 2021-02-28
    static void task7() {
        List<Product> result =
                orders.stream
                        ()
                        .filter(o->o.getOrderDate().equals("2021-02-28"))
                        .flatMap(o->o.getProducts().stream()
                                .distinct())
                        .collect(Collectors.toList());
        for (Product product:result)
            System.out.println(product);
    }


    //Вычислить сумму покупок 2021-02-28
    static void task8() {
        Double result =
                orders.stream
                        ()
                        .filter(o->o.getOrderDate().equals("2021-02-28"))
                        .flatMap(o->o.getProducts().stream())
                        .mapToDouble(p->p.getPrice())
                        .sum();
        System.out.println(result);
    }

    //Получить номера заказов и количество продуктов в каждом
    static void task9() {
        Map<Long, Integer> result =
                orders.stream
                        ()
                        .collect(Collectors.toMap(o->o.getId(), o->o.getProducts().size()));

        for (Map.Entry<Long, Integer> entry:result.entrySet())
            System.out.println(entry.getKey()+" "+entry.getValue());
    }

}
