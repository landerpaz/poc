package com.india.tamilnadu.jaxrs;

import java.util.List;
import com.india.tamilnadu.dao.ProductsDAO;
import com.india.tamilnadu.dao.SuppliersDAO;
import com.india.tamilnadu.dto.Response;

public class SupplierServiceImpl implements SupplierService {
	
	public List getSuppliers() {
		System.out.println("...invoking getSuppliers");
		
		SuppliersDAO suppliersDAO = new SuppliersDAO();
		List suppliers = suppliersDAO.getSuppliers();
				
		System.out.println("Number of suppliers : " + (null == suppliers? "0" : suppliers.size()));
		
		return suppliers;
	}
	
	/*public Response addProducts(List<Product> products) {
		System.out.println("...invoking addProducts");
		
		ProductsDAO productsDAO = new ProductsDAO();
		Response response = productsDAO.addProducts(products);
				
		System.out.println("Status : " + response.getStatus());
		
		return response;
	}*/
	
}
