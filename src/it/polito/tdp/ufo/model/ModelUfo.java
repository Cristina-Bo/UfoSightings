package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class ModelUfo {
	private DirectedMultigraph<Stato, DefaultEdge>grafo;
	private List<Stato> stati;
	private List<Sighting> avvistamenti;
	private Map<String, Stato> mapStati;
	private LinkedList<Stato> ottima;
	

	public ModelUfo() {
		
	}

	public List<AnnoNumeroAvvistamenti> getAnnoNumeroAvvistamenti(){
		SightingsDAO dao = new SightingsDAO();
		return dao.getAnniAvvistamenti();
	}
	
	public List<Stato> getStatiDaAnno(int anno){
		SightingsDAO dao = new SightingsDAO();
		return dao.getStatiDaAnno(anno);
	}

	public void creaGrafo(int anno) {

		this.grafo = new DirectedMultigraph<Stato, DefaultEdge>(DefaultEdge.class);
		this.stati = new LinkedList<Stato>();
		SightingsDAO dao = new SightingsDAO();
		stati = dao.getStatiDaAnno(anno);
		Graphs.addAllVertices(this.grafo, this.stati);
		this.mapStati = new HashMap<String, Stato>();
		creaMappa(stati);
		//Sighting avv1Primo = dao.getAvvistamentiDaAnno(anno).get(0);
		//	Sighting avv2Primo = dao.getAvvistamentiDaAnno(anno).get(1);
		Stato source = null;
		Stato dest = null;


		// crea archi
		for(Sighting avv1: dao.getAvvistamentiDaAnno(anno)) {
			for(Sighting avv2: dao.getAvvistamentiDaAnno(anno)) {
				if(!avv1.getState().equalsIgnoreCase(avv2.getState())) {
					if(avv1.getDatetime().isAfter(avv2.getDatetime())) {
						if(!grafo.containsEdge(avv2.getStato(), avv1.getStato())) {
							source = avv2.getStato();
							dest = avv1.getStato();	
							if(source != null && dest != null ) {
								grafo.addEdge(source, dest);
								System.out.println("Sto creando l'arco tra "+ source+" e "+ dest);
							}
						}		

					}
				}
			}

		}


		System.out.println("Grafo creato con "+grafo.vertexSet().size() +" vertici e "+grafo.edgeSet().size()+" archi");
	}
	
	

	
	private void creaMappa(List<Stato> stati) {
		System.out.println("Sono dentro al creaMappa");
		for(Stato s: stati) {
			System.out.println("Sono dentro al for in creaMappa");
			System.out.println("Stampo s "+ s+ " Stampo anche il nome di s "+s.getNome());
			if(!mapStati.containsKey(s.getNome())) {
				System.out.println("Sono dentro al creaMappa nell'if");
				mapStati.put(s.getNome(), s);
			}
		}
		
	}

	public DirectedMultigraph<Stato, DefaultEdge> getGrafo(){
		return this.grafo;
	}
	
	public List<Stato> getStatiPrecedenti(String statoString){
		// restituisce gli stati con archi entranti in quello passato
		Stato stato = mapStati.get(statoString);
		/*List<Stato> result = new LinkedList<Stato>();
		Set<DefaultEdge> archi = new HashSet<DefaultEdge>();
		archi = this.grafo.incomingEdgesOf(stato);
		for(DefaultEdge a : archi) {
			if(!result.contains(grafo.getEdgeSource(a))) {
				result.add(grafo.getEdgeSource(a));
			}
		}*/
		
		return Graphs.predecessorListOf(this.grafo, stato) ;
	}

	public List<Stato> getStatiSuccessivi(String statoString) {
		
				Stato stato = mapStati.get(statoString);
				/*List<Stato> result = new LinkedList<Stato>();
				Set<DefaultEdge> archi = new HashSet<DefaultEdge>();
				archi = this.grafo.outgoingEdgesOf(stato);
				for(DefaultEdge a : archi) {
					if(!result.contains(grafo.getEdgeSource(a))) {
						result.add(grafo.getEdgeSource(a));
					}
				}*/
				
				return Graphs.successorListOf(this.grafo, stato) ;
	}
	
	public List<Stato> statiRaggiungibili(String statoString){
		Stato stato = mapStati.get(statoString);
		GraphIterator<Stato, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, stato);
		List<Stato> result = new ArrayList<Stato>();
		
		while(it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}
	
	
	public List<Stato> PercorsoUfo(String statoString) {
		Stato partenza = mapStati.get(statoString);
		this.ottima = new LinkedList<Stato>();
		LinkedList<Stato> parziale = new LinkedList<Stato>();
		parziale.add(partenza);
		
		cercaPercorso(parziale);
		return ottima;
	}

	private void cercaPercorso(LinkedList<Stato> parziale) {
		List<Stato> candidati = this.getStatiSuccessivi((parziale.get(parziale.size()-1)).getNome());
		//Stato ultimoStato = parziale.get(parziale.size()-1);
		//System.out.println(ultimoStato);
	/*	if(getStatiSuccessivi(ultimoStato.getNome()).size()<1) {
			// non ci sono stati successivi, termino
			return;
		}*/
		for(Stato s : candidati) {
			parziale.add(s);
			if(!parziale.contains(s)) {
				// e' un candidato non preso
				parziale.add(s);
				this.cercaPercorso(parziale);
				parziale.remove(parziale.size()-1);
			}	
			
		}
		// vedere se la soluzione corrente e' migliore della ottima
		if(parziale.size()> ottima.size()) {
			this.ottima = new LinkedList(parziale);
		}
		
	}
}
