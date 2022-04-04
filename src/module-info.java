module KNKLibraria {
	requires javafx.controls;
	requires java.sql;
	requires mysql.connector.java;
	
	opens application to javafx.graphics, javafx.fxml;
}
