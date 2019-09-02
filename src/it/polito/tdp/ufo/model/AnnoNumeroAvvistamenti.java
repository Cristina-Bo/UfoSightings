package it.polito.tdp.ufo.model;

public class AnnoNumeroAvvistamenti {

	private int anno;
	private int numeroAvvistamenti;
	
	public AnnoNumeroAvvistamenti(int anno, int numeroAvvistamenti) {
		this.anno = anno;
		this.numeroAvvistamenti = numeroAvvistamenti;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getNumeroAvvistamenti() {
		return numeroAvvistamenti;
	}

	public void setNumeroAvvistamenti(int numeroAvvistamenti) {
		this.numeroAvvistamenti = numeroAvvistamenti;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", anno, numeroAvvistamenti);
	}
	
	
	
	
	
}
