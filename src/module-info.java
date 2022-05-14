module zBibloteka {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.sql;
	
	    requires java.desktop;
	    requires javafx.web;
		requires javafx.base;

	
	opens application to javafx.graphics, javafx.fxml, javafx.base ;
	opens controllers to javafx.fxml, javafx.graphics, databaseHandler, javafx.base,   java.sql;
	opens repositories to javafx.graphics, javafx, java.sql;
	//opens processors to javafx.fxml, javafx.graphics, databaseHandler, javafx.base,   java.sql;
	opens models to javafx.fxml, javafx.graphics, databaseHandler, javafx.base,   java.sql;
	opens images to  javafx.fxml, javafx.graphics, databaseHandler, javafx.base,   java.sql;
	

}
