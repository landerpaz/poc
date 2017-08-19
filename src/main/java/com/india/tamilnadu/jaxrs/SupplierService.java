package com.india.tamilnadu.jaxrs;

import java.util.List;


import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.india.tamilnadu.dto.Response;

@Produces({ "application/xml", "application/json" })
public interface SupplierService {

	@GET
	@Path("/suppliers/")
	List<Supplier> getSuppliers();
	
	
}