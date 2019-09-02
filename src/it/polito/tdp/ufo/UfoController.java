/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoNumeroAvvistamenti;
import it.polito.tdp.ufo.model.ModelUfo;
import it.polito.tdp.ufo.model.Stato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	ModelUfo model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<AnnoNumeroAvvistamenti> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<Stato> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleAnalizza(ActionEvent event) {
    	if(boxStato.getSelectionModel().isEmpty()) {
    		txtResult.setText("Selezionare uno stato!");
    		return;
    	}else {
    		String statoString = boxStato.getSelectionModel().getSelectedItem().getNome(); 
    		txtResult.setText("Stati precedenti: "+model.getStatiPrecedenti(statoString)+
    				"\n"+"Stati successivi: "+model.getStatiSuccessivi(statoString)+
    				"\n"+ "Gli stati raggiungibili sono "+model.statiRaggiungibili(statoString).size()+" : "
    				+model.statiRaggiungibili(statoString));
    	}
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	int anno;
    	if(boxAnno.getSelectionModel().isEmpty()) {
    		txtResult.setText("Selezionare un anno");
    		return;
    	}else {
    		anno = boxAnno.getSelectionModel().getSelectedItem().getAnno();
    		model.creaGrafo(anno);
    		boxStato.getItems().addAll(model.getGrafo().vertexSet());
    		txtResult.setText("Grafo creato con "+model.getGrafo().vertexSet().size() +" vertici e "+model.getGrafo().edgeSet().size());
    	}
    	
    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	if(boxAnno.getSelectionModel().isEmpty()) {
    		txtResult.setText("Selezionare uno stato!");
    		return;
    	}else {
    		String statoString = boxStato.getSelectionModel().getSelectedItem().getNome(); 
    		txtResult.setText("Percorso ufo: "+model.PercorsoUfo(statoString));
    	}
    
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";
        this.model= new ModelUfo();
        txtResult.clear();
        boxAnno.getItems().addAll(model.getAnnoNumeroAvvistamenti());

    }

	public void setModel(ModelUfo model) {
		this.model = model;
	}
    
    
}
