package com.ups.uearv.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class ImageStoreBean {

	private UploadedFile file;
	
	// Store file in the database
	public void storeImage() {
		// Create connection
		try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to the database
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/itcuties?user=uploader&password=password");
			// Set autocommit to false to manage it by hand
			connection.setAutoCommit(false);
			
			// Create the statement object
			PreparedStatement statement = connection.prepareStatement("INSERT INTO files (file) VALUES (?)");
			// Set file data
			statement.setBinaryStream(1, file.getInputstream());
			
			// Insert data to the database
			statement.executeUpdate();
			
			// Commit & close
			connection.commit();	// when autocommit=false
			connection.close();
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload success", file.getFileName() + " is uploaded.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// Add error message
			FacesMessage errorMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload error", e.getMessage());  
	        FacesContext.getCurrentInstance().addMessage(null, errorMsg);
		}
		
	}

	// Getter method
	public UploadedFile getFile() {
		return file;
	}

	// Setter method
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
}
