package com.india.tamilnadu.jaxrs;


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class PatientServiceImpl implements PatientService {
	private long currentId = 123;
	Map<Long, Patient> patients = new HashMap<Long, Patient>();
	Map<Long, Prescription> prescriptions = new HashMap<Long, Prescription>();

	public PatientServiceImpl() {
		init();
	}

	final void init() {
		Patient patient = new Patient();
		patient.setName("John");
		patient.setId(currentId);
		patients.put(patient.getId(), patient);

		Prescription prescription = new Prescription();
		prescription.setDescription("prescription 223");
		prescription.setId(223);
		prescriptions.put(prescription.getId(), prescription);
	}

	public Response addPatient(Patient patient) {
		System.out.println("...invoking addPatient, Patient Name is... " + patient.getName());
		patient.setId(++currentId);
		patients.put(patient.getId(), patient);
		return Response.ok(patient).build();
	}

	public Patient getPatient(String id) {
		System.out.println("...invoking getPatient, Patient Id is... " + id);
		Long patientId = Long.parseLong(id);
		Patient patient = patients.get(patientId);
		return patient;
	}

	public java.util.List getPatients() {
		System.out.println("...invoking getPatients");
		
		List patientList = new ArrayList();
				
		//Map<Long, Patient> patients 
		
		for(Patient patient : patients.values()) {
			patientList.add(patient);
		}
				
		System.out.println("Number of patients : " + (null != patientList? "0" : patientList.size()));
		
		return patientList;
	}
	
	public Response updatePatient(Patient updatedPatient) {
		System.out.println("...invoking updatePatient, Patient Name is... " + updatedPatient.getName());

		Patient currentPatient = patients.get(updatedPatient.getId());

		Response response = null;
		if (currentPatient != null) {
			patients.put(updatedPatient.getId(), updatedPatient);
			response = Response.ok().build();
		} else {
			response = Response.notModified().build();
		}

		return response;
	}

	public Response deletePatients(String id) {
		System.out.println("...invoking deletePatients, Patient Id is... " + id);
		Long patientId = Long.parseLong(id);
		Patient patient = patients.get(patientId);
		Response response = null;
		if (patient != null) {
			patients.remove(patientId);
			response = Response.ok().build();
		} else {
			response = Response.notModified().build();
		}

		return response;
	}

	public Prescription getPrescription(String prescriptionId) {
		long id = Long.parseLong(prescriptionId);
		Prescription prescription = prescriptions.get(id);
		return prescription;
	}

}
