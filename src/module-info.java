module KNKLibraria {
	requires javafx.controls;
	requires java.sql;
	requires mysql.connector.java;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
