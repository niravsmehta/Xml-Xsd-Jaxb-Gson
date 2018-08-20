package com.xmlSchema.XMLSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {
	
    public static void main( String[] args ) throws JsonGenerationException, JsonMappingException, FileNotFoundException, ParseException, DatatypeConfigurationException, IOException, JAXBException {
    	
    	marshalling();
    	unmarshalling();
    	
    	serialize();
    	deserialize();
    }
    
    private static Customer createCustomer() throws ParseException, DatatypeConfigurationException {
    	
    	Address address = new Address();
    	address.setStreet("8059 N Macarthur Blvd");
    	address.setCity("Irving");
    	address.setZipCode(75063);
    	address.setState("Texas");
    	
    	PaymentMethod paymentMethod = new PaymentMethod();
    	paymentMethod.setCardNumber(34005400);
    	paymentMethod.setCardName("MasterCard");
    	paymentMethod.setCardType("CREDITCARD");
    	
    	XMLGregorianCalendar dateFrom = stringToXMLGregorianCalendar("2014-04-10T00:00:00");
    	paymentMethod.setDateFrom(dateFrom);
    	
    	XMLGregorianCalendar dateTill = stringToXMLGregorianCalendar("2019-04-09T00:00:00");
    	paymentMethod.setDateTill(dateTill);

    	Customer customer = new Customer();
    	customer.setCustomerId(101);
    	customer.setName("Nirav");
    	customer.setAnnualSalary(50000.00);
    	customer.setAddress(address);
        customer.setPayment(paymentMethod);
    	
    	XMLGregorianCalendar dob = stringToXMLGregorianCalendar("1996-04-23T00:00:00");
    	customer.setDateOfBirth(dob);
    	
    	return customer;
    }
    
    private static XMLGregorianCalendar stringToXMLGregorianCalendar(String string) throws ParseException, DatatypeConfigurationException {
    	
    	XMLGregorianCalendar xgc = null;
		Date date;
		SimpleDateFormat sdm;
		GregorianCalendar gc;
		 
		sdm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		date = sdm.parse(string);        
		gc = (GregorianCalendar)GregorianCalendar.getInstance();
	    gc.setTime(date);
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
	    return xgc;
	}

	private static void serialize() throws ParseException, DatatypeConfigurationException, JsonGenerationException, JsonMappingException, FileNotFoundException, IOException {
    	try {
	    	ObjectMapper object = new ObjectMapper();
	    	Customer customer = createCustomer();
	    	object.configure(SerializationFeature.INDENT_OUTPUT, true);
			object.writeValue(System.out, customer);
	        object.writeValue(new PrintWriter("src/resources/customer.json"), customer);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private static void deserialize() throws IOException {
    	try {
    		byte[] data = Files.readAllBytes(Paths.get("src/resources/customer.json"));
        	ObjectMapper obj = new ObjectMapper();
        	Customer customer = obj.readValue(data, Customer.class);
        	System.out.println(customer.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private static void unmarshalling() throws JAXBException {
    	
    	try {
    	File file = new File("src/resources/output.xml");
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		Unmarshaller um = jc.createUnmarshaller();
		Customer customer = (Customer) um.unmarshal(file);
		System.out.println("" + customer.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private static void marshalling() throws JAXBException, ParseException, DatatypeConfigurationException {
    	try {
	    	File file = new File("src/resources/output.xml");
			JAXBContext jc = JAXBContext.newInstance(Customer.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			Customer customer = createCustomer();
			m.marshal(customer, file);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
