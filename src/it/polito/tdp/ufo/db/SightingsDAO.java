package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.ufo.model.AnnoNumeroAvvistamenti;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.Stato;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<AnnoNumeroAvvistamenti> getAnniAvvistamenti(){
		LinkedList<AnnoNumeroAvvistamenti> risultato = new LinkedList<AnnoNumeroAvvistamenti>();
		String sql = "SELECT EXTRACT(YEAR FROM DATETIME) AS anno, COUNT(*) AS cnt " + 
				"FROM sighting " + 
				"WHERE country='us' " + 
				"GROUP BY EXTRACT(YEAR FROM DATETIME)" ;
		AnnoNumeroAvvistamenti annoNumAvv = null;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					annoNumAvv = new AnnoNumeroAvvistamenti(res.getInt("anno"), res.getInt("cnt"));
					risultato.add(annoNumAvv);
				
			}
			
			conn.close();
			//System.out.println(risultato);
			return risultato ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	
	public List<Stato> getStatiDaAnno(int anno){
		List<Stato> risultato = new LinkedList<Stato>();
		String sql = "SELECT DISTINCT state "+
				"FROM sighting "+
				"WHERE EXTRACT(YEAR FROM DATETIME)=? AND country='us'";
		Stato stato = null;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				stato = new Stato(res.getString("state"));
				if(!risultato.contains(stato)) {
					risultato.add(stato);
				}
				
			}
			
			conn.close();
			return risultato ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Sighting> getAvvistamentiDaAnno(int anno){
		List<Sighting> risultato = new LinkedList<Sighting>();
		String sql = "SELECT * "+
				"FROM sighting "+
				"WHERE EXTRACT(YEAR FROM DATETIME)=? AND country='us'";
		Sighting avv = null;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				avv = new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"));
				risultato.add(avv);				
			}
			
			conn.close();
			return risultato ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public boolean esisteArco(String s1, String s2, Year anno) {
		String sql = "select count(*) as cnt " + 
				"from Sighting s1,Sighting s2 " + 
				"where Year(s1.datetime) = Year(s2.datetime) " + 
				"	and Year(s1.datetime) = ? and " + 
				"	s1.state = ? and s2.state = ? and " + 
				"	s1.country = \"us\" and s2.country = \"us\" " + 
				"	and s2.datetime > s1.datetime";
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);
			ResultSet res = st.executeQuery() ;

			if(res.next()) {
				if(res.getInt("cnt") > 0) {
					conn.close();
					return true;
				}
				else {
					conn.close();
					return false;
				}
			} else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
