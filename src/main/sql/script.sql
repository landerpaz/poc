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
      
CREATE TABLE inventory.tally_summary (
	 tally_summary_id 		INT UNSIGNED  	NOT NULL AUTO_INCREMENT,
     report_id INT UNSIGNED   NOT NULL,
     report_name  			VARCHAR(100)  	NOT NULL,
     report_key 			VARCHAR(100) 	NOT NULL,
     report_value1			VARCHAR(100),
     report_value2			VARCHAR(100),
     created_date   		DATE NOT NULL,
     check_flag				BOOLEAN,
     PRIMARY KEY  (tally_summary_id)
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

--day book
CREATE TABLE inventory.DAYBOOK_MASTER(
VOUCHER_KEY	VARCHAR(25) NOT NULL,
VCH_TYPE VARCHAR(100),
VOUCHER_ACTION VARCHAR(100),
VOUCHER_DATE DATE,
VOUCHER_TYPE_NAME VARCHAR(100),
VOUCHER_NUMBER VARCHAR(25),
PARTY_LEDGER_NAME VARCHAR(100),
EFFECTIVE_DATE	DATE,
PERSISTED_VIEW	VARCHAR(100),
ALTER_ID VARCHAR(25),
MASTER_ID	VARCHAR(25),
LEDGER_NAME	VARCHAR(100),
FLAG boolean, 
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (VOUCHER_KEY)
);

CREATE TABLE inventory.DAYBOOK_INVENTORY(
ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
STOCK_ITEM_NAME VARCHAR(100),
AMOUNT decimal(10,2),
RATE VARCHAR(100),
BILLED_QTY VARCHAR(100),
VOUCHER_KEY	VARCHAR(25) NOT NULL, 
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (ID),
FOREIGN KEY (VOUCHER_KEY) REFERENCES DAYBOOK_MASTER(VOUCHER_KEY)
);


CREATE TABLE inventory.DAYBOOK_LEDGER(
ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
LEDGER_NAME VARCHAR(100),
AMOUNT decimal(10,2),
VOUCHER_KEY	VARCHAR(25) NOT NULL,
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (ID),
FOREIGN KEY (VOUCHER_KEY) REFERENCES DAYBOOK_MASTER(VOUCHER_KEY)
);

--Stock detail
CREATE TABLE inventory.stock_master(
VOUCHER_TYPE VARCHAR(100),
VOUCHER_ACTION	VARCHAR(100),
DATE_ALT	DATE,
DATE_ENT	DATE,
VOUCHER_TYPE_NAME	VARCHAR(100),
VOUCHER_NUMBER	VARCHAR(25),
VOUCHER_KEY		VARCHAR(25) NOT NULL,
EFFECTIVE_DATE	DATE,
PERSISTED_VIEW	VARCHAR(100),
ALTER_ID			VARCHAR(25),
MASTER_ID		VARCHAR(25),
OPR_DATE	DATE,
REEL_WEIGHT	VARCHAR(15),
START_TIME	VARCHAR(15),
REWIND_START	VARCHAR(15),
REWIND_END	VARCHAR(15),
OPERATED_BY 	VARCHAR(100),
FOREMAN1	VARCHAR(100),
FOREMAN2	VARCHAR(100),
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (VOUCHER_KEY) 
);

CREATE TABLE inventory.stock_details(
stock_details_id VARCHAR(50) NOT NULL,
STOCK_ITEM_NAME 	VARCHAR(100),
RATE	VARCHAR(25),
AMOUNT	decimal(10,2),
BILLED_QTY	VARCHAR(100),
ACTUAL_QTY	VARCHAR(100),
STATUS	VARCHAR(10),
VOUCHER_KEY	VARCHAR(25),
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (stock_details_id),
FOREIGN KEY (VOUCHER_KEY) REFERENCES stock_master(VOUCHER_KEY)
);

CREATE TABLE inventory.stock_item_details(
stock_item_details_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
GSM_TGT	decimal(10,2),
GSM_ACT	decimal(10,2),
BF_TGT	decimal(10,2),
BF_ACT	decimal(10,2),
SIZE_ACT	decimal(10,2),
REEL_LEN	decimal(10,2),
JOINTS	decimal(10,2),
REEL_DIA	decimal(10,2),
MOIST	decimal(10,2),
SIZE_TGT1	decimal(10,2),
SIZE_ACT1	decimal(10,2),
LENGTH1	decimal(10,2),
TEMP	decimal(10,2),
UNITS	VARCHAR(25),
VOUCHER_KEY	VARCHAR(25),
stock_details_id VARCHAR(50),
CREATED_DATE	DATE,
MODIFIED_DATE	DATE,
PRIMARY KEY (stock_item_details_id),
FOREIGN KEY (stock_details_id) REFERENCES stock_details(stock_details_id)
);

drop table inventory.stock_item_details;
drop table inventory.stock_details;
drop table inventory.stock_master;

delete from inventory.stock_item_details where voucherkey > "0";
delete from inventory.stock_details where ;
delete from inventory.stock_master;