CREATE TABLE `products` (
  `productID`   int(10) unsigned  NOT NULL AUTO_INCREMENT,
  `productCode` char(3)           NOT NULL DEFAULT '',
  `name`        varchar(30)       NOT NULL DEFAULT '',
  `quantity`    int(10) unsigned  NOT NULL DEFAULT '0',
  `price`       decimal(7,2)      NOT NULL DEFAULT '99999.99',
  `supplierID`  int(10) unsigned   NOT NULL DEFAULT '501',
  PRIMARY KEY (`productID`),
  KEY `supplierID` (`supplierID`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`supplierID`) 
     REFERENCES `suppliers` (`supplierID`)
);

CREATE TABLE suppliers (
         supplierID  INT UNSIGNED  NOT NULL AUTO_INCREMENT, 
         name        VARCHAR(30)   NOT NULL DEFAULT '', 
         phone       CHAR(8)       NOT NULL DEFAULT '',
         PRIMARY KEY (supplierID)
       );
	   
	   
	   CREATE TABLE employees (
          emp_no      INT UNSIGNED   NOT NULL AUTO_INCREMENT,
          name        VARCHAR(50)    NOT NULL,
          gender      ENUM ('M','F') NOT NULL,    
          birth_date  DATE           NOT NULL,
          hire_date   DATE           NOT NULL,
          PRIMARY KEY (emp_no) 
       );
	   
	   CREATE TABLE departments (
         dept_no    CHAR(4)      NOT NULL,
         dept_name  VARCHAR(40)  NOT NULL,
         PRIMARY KEY  (dept_no),  
         UNIQUE INDEX (dept_name) 
       );
       
       
insert into suppliers(name, phone) values ("ashok", "60972736");
insert into suppliers(name, phone) values ("selva", "61458565");
commit;

select * from suppliers;

insert into inventory.products (productCode, name, quantity, price, supplierID) values ('p1', 'p1_name', 10,10.0, 1);
insert into inventory.products (productCode, name, quantity, price, supplierID) values ('p2', 'p2_name', 10,10.0, 1);
insert into inventory.products (productCode, name, quantity, price, supplierID) values ('p3', 'p3_name', 10,10.0, 2);
insert into inventory.products (productCode, name, quantity, price, supplierID) values ('p4', 'p4_name', 10,10.0, 2);

commit;


select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , suppliers.name , suppliers.phone from 
products product, suppliers supplier where suppliers.supplierID = product.supplierID;

select product.productid, product.productCode , product.name , product.quantity , product.price,  supplier.supplierID , suppliers.name , suppliers.phone from 
products product, suppliers supplier where suppliers.supplierID = product.supplierID;

