/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	int N=-1;
    	try {
    		N=Integer.parseInt(this.txtPassi.getText());
    	}catch(NumberFormatException ne) {
    		txtResult.appendText("Selezionare un numero di passi intero e positivo");
    		return;
    	
    	}
    	if(N<0) {
    		txtResult.appendText("Selezionare un numero di passi intero e positivo");
    		return;
    	}
    	
    	txtResult.appendText("Cerco cammino peso massimo...\n");
    	String tipo= this.boxPorzioni.getValue();
    	List<String>cammino=model.camminoMax(N,tipo);
    	int pesoMax=model.getPesoMax();
    	if(cammino==null) {
    		txtResult.appendText("Non c'è un cammino massimo con "+N+" vertici partendo da "+tipo+"\n");
    	}
    	else {
    		for(String v: cammino) {
    			txtResult.appendText(v+"\n");
    		}
    		txtResult.appendText("Con peso massimo: "+pesoMax);
    	}
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	String tipo= this.boxPorzioni.getValue();
    	if(tipo==null) {
    		txtResult.appendText("Selezionare un tipo di porzione");
    		return;
    	}
    	txtResult.appendText("Cerco porzioni correlate...\n");
    	Set<DefaultWeightedEdge> result= model.getConnessi(tipo);
    	txtResult.appendText("Tipi di porzioni correlate a "+tipo+"\n");
    	if(result==null || result.isEmpty()) {
    		txtResult.appendText("Non ci sono tipi di porzioni correlate a "+tipo);
    		return;
    	}
    	for(DefaultWeightedEdge de: result) {
    		txtResult.appendText(Graphs.getOppositeVertex(model.grafo, de,tipo)+" con peso "+model.grafo.getEdgeWeight(de)+"\n");
    	}
    	
    	this.btnCammino.setDisable(false);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...\n");
    	
    	int calo=-1;
    	try {
    		calo=Integer.parseInt(this.txtCalorie.getText());
    	}catch(NumberFormatException ne) {
    		txtResult.appendText("Selezionare un numero di calorie intero e positivo");
    		return;
    	
    	}
    	if(calo<0) {
    		txtResult.appendText("Selezionare un numero di calorie intero e positivo");
    		return;
    	}
    	model.creaGrafo(calo);
    	txtResult.clear();
    	this.boxPorzioni.getItems().clear();
    	this.boxPorzioni.getItems().addAll(model.getPorzioni());
    	this.btnCorrelate.setDisable(false);
    	txtResult.appendText("#VERTICI: "+model.getVertici()+"\n#ARCHI: "+model.getArchi());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnCammino.setDisable(true);
    	this.btnCorrelate.setDisable(true);
    }
}
