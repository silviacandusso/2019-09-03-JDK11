package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	FoodDao dao;
	public Graph<String,DefaultWeightedEdge> grafo;
	List<String>vertici;
	//variabili per la ricorsione
	List<String> camminoMax;
	int pesoMax;
	
	public Model() {
		dao=new FoodDao();
	}

	public void creaGrafo( int calo) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//creo i vertici
		Graphs.addAllVertices(grafo, dao.getVertici(calo));
		vertici=new ArrayList<String>(dao.getVertici(calo));
		//creo gli archi
		for(Adiacenza a: dao.getArchi(calo, vertici)) {
			Graphs.addEdgeWithVertices(grafo,a.getT1(), a.getT2(),a.getPeso());
		}
		
	}
	
	public List<String> getPorzioni(){
		return vertici;
	}

	public Set<DefaultWeightedEdge> getConnessi(String tipo) {
		Set<DefaultWeightedEdge> result=grafo.edgesOf(tipo);
		return result;
	}

	public List<String> camminoMax(int n, String tipo) {
		pesoMax=0;
		camminoMax=null;
		//inizializzo
		List<String> parziale= new ArrayList<String>();
		parziale.add(tipo);
		cerca(parziale,n);
	    return camminoMax;
	}
	
	private void cerca(List<String> parziale, int n) {
	//CASO TERMINALE
		     //1 finisco i passi
		          //n=vertici-1
		if(parziale.size()-1==n) {
			//controllo se ha peso massimo
			if(getPeso(parziale)>pesoMax) {
				camminoMax= new ArrayList<String>(parziale);
				pesoMax=getPeso(parziale);
				return;
			}
		}
		    //2 finisco i vertici
		if(parziale.size()==grafo.vertexSet().size() && parziale.size()-1<n) {
			return;
		}
		
	//AGGIUNGO UN VERTICE
		for(String v: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))){
			if(!parziale.contains(v)) {
				parziale.add(v);
				cerca(parziale,n);
				//backtracking
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private int getPeso(List<String>parziale) {
		int peso=0;
		for(int i=1; i<parziale.size();i++) {
			peso+=grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1),parziale.get(i)));
		}
		
		return peso;
	}

	public List<String> getCamminoMax() {
		return camminoMax;
	}

	public int getPesoMax() {
		return pesoMax;
	}
	
	
	
	
	
}
