//import com.linkedin.AppProperties;

import com.linkedin.data.dao.*;
import com.linkedin.data.entity.Customer;
import com.linkedin.data.entity.Product;
import com.linkedin.data.entity.Service;
import com.linkedin.data.entity.Vendor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class Application {
  
  public static void main(String[] args) {
    //AppProperties appProperties = new AppProperties();
    //appProperties.loadProperties(null);
    
//    testSqlServices();
//    testSqlCustomers();
//    testSqlVendors();
    testSqlProducts();
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void testSqlServices() {
    ServiceDao serviceDao = new ServiceDao();
    Optional<Service> optionalService;
    Service service;
    
    System.out.println("**** SERVICES ****");
    //-------------------------------------------
    System.out.println("\n*** GET_ALL ***");
    List<Service> services = serviceDao.getAll();
    services.forEach(System.out::println);
    
    //-------------------------------------------
    System.out.println("\n*** GET_BY_ID ***");
    UUID uuid = UUID.fromString("baccf816-84fe-4dc3-bb51-dedbbf2f82e1");
    optionalService = serviceDao.getById(uuid);
    if (optionalService.isPresent()) {
      System.out.println(optionalService.get());
    }
    else System.out.println("No Service found for service_id=" + uuid);
    
    //-------------------------------------------
    System.out.println("\n*** DELETE ***");
    optionalService =
        services
            .stream()
            .filter(s -> s.getName().equals("Air Condition Cleaning Service"))
            .findFirst();
    if (optionalService.isPresent()) {
      UUID serviceID = optionalService.get().getServiceId();
      serviceDao.delete(serviceID);
      optionalService = serviceDao.getById(serviceID);
      if (!optionalService.isPresent()) {
        System.out.println(serviceID + " deleted successfully");
      }
    }
    
    //-------------------------------------------
    System.out.println("\n*** CREATE ***");
    service = new Service();
    service.setName("Air Condition Cleaning Service");
    service.setPrice(new BigDecimal(new Random().nextDouble((999 - 0) + 1)));
    service = serviceDao.create(service);
    System.out.println(service);
    
    //-------------------------------------------
    System.out.println("\n*** UPDATE ***");
    service.setPrice(new BigDecimal(new Random().nextDouble((499 - 0) + 1)));
    service = serviceDao.update(service);
    System.out.println(service);
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void testSqlCustomers() {
    CustomerDao customerDao = new CustomerDao();
    Optional<Customer> optionalCustomer;
    Customer customer;
    
    System.out.println("**** CUSTOMERS ****");
    //-------------------------------------------
    System.out.println("\n*** GET_ALL ***");
    List<Customer> customers = customerDao.getAll();
    customers.forEach(System.out::println);
    
    //-------------------------------------------
    System.out.println("\n*** GET_BY_ID ***");
    UUID uuid = UUID.fromString("6bf765af-4728-4bfe-b89e-50989fb3eadb");
    optionalCustomer = customerDao.getById(uuid);
    if (optionalCustomer.isPresent()) {
      System.out.println(optionalCustomer.get());
    }
    else System.out.println("No Customer found for customer_id=" + uuid);
    
    //-------------------------------------------
    System.out.println("\n*** DELETE ***");
    optionalCustomer =
        customers
            .stream()
            .filter(s ->
                s.getFirstname().equals("Kevin") &&
                    s.getLastname().equals("Brandon")
            )
            .findFirst();
    if (optionalCustomer.isPresent()) {
      UUID customerID = optionalCustomer.get().getCustomerId();
      customerDao.delete(customerID);
      optionalCustomer = customerDao.getById(customerID);
      if (!optionalCustomer.isPresent()) {
        System.out.println(customerID + " deleted successfully");
      }
    }
    else {
      System.out.println("No Customer found with first_name=Kevin and lastname=Brandon");
    }
    
    //-------------------------------------------
    System.out.println("\n*** CREATE ***");
    customer = new Customer();
    customer.setFirstname("Kevin");
    customer.setLastname("Brandon");
    customer.setEmail("brnewton@orange.com");
    customer.setPhone("123456789");
    customer.setAddress("671 Riverside Coast, Miami, MI 11789");
    customer = customerDao.create(customer);
    System.out.println(customer);
    
    //-------------------------------------------
    System.out.println("\n*** UPDATE ***");
    customer.setPhone("99997654321");
    customer = customerDao.update(customer);
    System.out.println(customer);
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void testSqlVendors() {
    VendorDao vendorDao = new VendorDao();
    Optional<Vendor> optionalVendor;
    Vendor vendor;
    
    System.out.println("**** VENDORS ****");
    //-------------------------------------------
    System.out.println("\n*** GET_ALL ***");
    List<Vendor> vendors = vendorDao.getAll();
    vendors.forEach(System.out::println);
    
    //-------------------------------------------
    System.out.println("\n*** GET_BY_ID ***");
    UUID uuid = UUID.fromString("71fa27ec-5811-454a-8ff8-5b6fd8b4598b");
    optionalVendor = vendorDao.getById(uuid);
    if (optionalVendor.isPresent()) {
      System.out.println(optionalVendor.get());
    }
    else System.out.println("No Vendor found for vendor_id=" + uuid);
    
    //-------------------------------------------
    System.out.println("\n*** DELETE ***");
    optionalVendor =
        vendors
            .stream()
            .filter(s -> s.getName().equals("Robusta"))
            .findFirst();
    if (optionalVendor.isPresent()) {
      UUID vendorID = optionalVendor.get().getVendorId();
      vendorDao.delete(vendorID);
      optionalVendor = vendorDao.getById(vendorID);
      if (!optionalVendor.isPresent()) {
        System.out.println(vendorID + " deleted successfully");
      }
    }
    else {
      System.out.println("No Vendor found with name=Robusta");
    }
    
    //-------------------------------------------
    System.out.println("\n*** CREATE ***");
    vendor = new Vendor();
    vendor.setName("Robusta");
    vendor.setContact("Habit");
    vendor.setPhone("123456789");
    vendor.setEmail("infor@robusta.com");
    vendor.setAddress("671 Riverside Coast, Miami, MI 11789");
    vendor = vendorDao.create(vendor);
    System.out.println(vendor);
    
    //-------------------------------------------
    System.out.println("\n*** UPDATE ***");
    vendor.setPhone("99997654321");
    vendor = vendorDao.update(vendor);
    System.out.println(vendor);
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void testSqlProducts() {
    ProductDao productDao = new ProductDao();
    Optional<Product> optionalProduct;
    Product product;
    
    System.out.println("**** PRODUCTS ****");
    //-------------------------------------------
    System.out.println("\n*** GET_ALL ***");
    List<Product> products = productDao.getAll();
    products.forEach(System.out::println);
    
    //-------------------------------------------
    System.out.println("\n*** GET_BY_ID ***");
    UUID uuid = UUID.fromString("ed2ca4db-c78a-468f-a1f2-9c90b3cdae8d");
    optionalProduct = productDao.getById(uuid);
    if (optionalProduct.isPresent()) {
      System.out.println(optionalProduct.get());
    }
    else System.out.println("No Product found for product_id=" + uuid);
    
    //-------------------------------------------
    System.out.println("\n*** DELETE ***");
    optionalProduct = products
        .stream()
        .filter(p -> p.getName().equals("Prevendog"))
        .findFirst();
    if (optionalProduct.isPresent()) {
      UUID productID = optionalProduct.get().getProductId();
      productDao.delete(productID);
      optionalProduct = productDao.getById(productID);
      if (!optionalProduct.isPresent()) {
        System.out.println(productID + " deleted successfully");
      }
    }
    else {
      System.out.println("No Product found with product_id=9bb27067-5359-456a-88de-03a3a90a7253");
    }

    //-------------------------------------------
    System.out.println("\n*** CREATE ***");
    product = new Product();
    product.setName("Prevendog");
    product.setPrice(new  BigDecimal("23.40"));
    product.setVendorId(UUID.fromString("9bb27067-5359-456a-88de-03a3a90a7253"));
    product = productDao.create(product);
    System.out.println(product);

    //-------------------------------------------
    System.out.println("\n*** UPDATE ***");
    product.setPrice(new  BigDecimal("23.90"));
    product = productDao.update(product);
    System.out.println(product);
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
