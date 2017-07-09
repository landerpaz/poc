package com.india.tamilnadu.jaxrs;

import java.util.List;


import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Produces({ "application/xml", "application/json" })
public interface PatientService {

	@GET
	@Path("/patients/{id}/")
	Patient getPatient(@PathParam("id") String id);
	
	@GET
	@Path("/patients/")
	List<Patient> getPatients();

	@PUT
	@Path("/patients/")
	Response updatePatient(Patient patient);

	@POST
	@Path("/patients/")
	Response addPatient(Patient patient);

	@DELETE
	@Path("/patients/{id}/")
	Response deletePatients(@PathParam("id") String id);

	@Path("/prescriptions/{id}")
	Prescription getPrescription(@PathParam("id") String prescriptionId);

}