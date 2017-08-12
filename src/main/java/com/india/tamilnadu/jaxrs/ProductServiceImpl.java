package com.india.tamilnadu.jaxrs;


import java.util.List;
import com.india.tamilnadu.dao.ProductsDAO;

public class ProductServiceImpl implements ProductService {
	
	public List getProducts() {
		System.out.println("...invoking getProducts");
		
		ProductsDAO productsDAO = new ProductsDAO();
		List products = productsDAO.getProducts();
				
		System.out.println("Number of products : " + (null == products? "0" : products.size()));
		
		return products;
	}
	
}
