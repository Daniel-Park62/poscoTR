package poc.poscoTR.model;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import com.ibm.icu.util.Calendar;

import poc.poscoTR.part.PocMain;

public class FindMoteInfo {

	
	private ArrayList<MoteInfo> tempList ;
	private ArrayList<MoteHist> tempList2 ;
	private ArrayList<ChartData> chList ;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");

    public ArrayList<MoteInfo> getMoteInfos(String fmdt, String todt, int seq) {
    	Timestamp ts_fmdt = Timestamp.valueOf(fmdt) ;
    	Timestamp ts_todt = Timestamp.valueOf(todt) ;

    	return getMoteInfos(ts_fmdt, ts_todt, seq)  ;
    }

    public ArrayList<MoteHist> getMoteHists(String fmdt, String todt, int seq) {
    	Timestamp ts_fmdt = Timestamp.valueOf(fmdt) ;
    	Timestamp ts_todt = Timestamp.valueOf(todt) ;

    	return getMoteHists(ts_fmdt, ts_todt, seq)  ;
    }
    
    public String[] getSensorList() {
    	List<String> slist = new ArrayList() ;
    	EntityManager em = PocMain.emf.createEntityManager();
    	em.createQuery("select m.sensorNo  "
    			+ "from MoteStatus m where m.gubun = 'S' and m.spare = 'N' order by m.sensorNo ")
    		.getResultList().stream().forEach(m -> slist.add(m.toString()));
    	String[] sresult = new String[slist.size()] ;
    	int ix = 0;
    	for (String ss : slist) {
			sresult[ix++] = " S" + ss ;
		}
    	return sresult ;
    }

    public ArrayList<MoteInfo> getMoteInfos(Timestamp fmdt, Timestamp todt, int seq) {
		tempList = new ArrayList<MoteInfo>();
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String sseqif ;
		if (seq == 0) {
			sseqif = "";
		} else {
			sseqif = "and t.seq = " + seq ;
		}
		
        TypedQuery<MoteInfo> qMotes = em.createQuery("select t from MoteInfo t " 
        		+ "where t.tm between :fmdt and :todt " + sseqif + " order by t.tm ,t.seq ", MoteInfo.class);

        qMotes.setParameter("fmdt", fmdt);
        qMotes.setParameter("todt", todt);
        qMotes.getResultList().stream().forEach( t -> tempList.add(t));
        
		em.close();

		return tempList ;

    }

    public ArrayList<MoteHist> getMoteHists(Timestamp fmdt, Timestamp todt, int seq) {
		tempList2 = new ArrayList<MoteHist>();
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String sseqif ;
		if (seq == 0) {
			sseqif = "";
		} else {
			sseqif = "and t.sensorNo = " + seq ;
		}
		
        TypedQuery<MoteHist> qMotes = em.createQuery("select t from MoteHist t " 
        		+ "where t.tm between :fmdt and :todt " + sseqif + " order by t.sensorNo ,t.tm desc", MoteHist.class);

        qMotes.setParameter("fmdt", fmdt);
        qMotes.setParameter("todt", todt);
        qMotes.setHint(QueryHints.READ_ONLY, HintValues.TRUE);
        qMotes.getResultList().stream().forEach( t -> tempList2.add(t));
        
		em.close();

		return tempList2 ;

    }

    public List<PeakTrend> getPeakTrend(int seq) {

		EntityManager em = PocMain.emf.createEntityManager();
    	Timestamp todt, fmdt ;
		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.HOUR, -24); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		
		String sql = " select tm, maxval - minval as val from peaktrend where tm >= '" 
		            + fmdt + "' and sensorno = "  + seq  ; 
 
		Query nativeQuery = em.createNativeQuery(sql);
	    List<Object[]> resultList = nativeQuery.getResultList();
	    List<PeakTrend> chList = resultList.stream().map(r -> new PeakTrend( 
	    		Timestamp.valueOf(r[0].toString()) , ((Long)r[1]).intValue()  ))
	    		.collect(Collectors.toCollection(ArrayList::new)) ;

        
		em.close();

		return chList ;

    }

    public ArrayList<ChartData> getChartData(Timestamp fmdt, Timestamp todt, int seq) {
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
//		em.getEntityManagerFactory().getCache().evictAll();
		
		String sql = " CALL SP_CHARTDATA('" + fmdt + "', '" + todt + "'," + seq + ")" ; 
 
		Query nativeQuery = em.createNativeQuery(sql);
	    List<Object[]> resultList = nativeQuery.getResultList();
	    chList = resultList.stream().map(r -> new ChartData( 
	    		Timestamp.valueOf(r[0].toString()) , ((Long)r[1]).intValue(),  (Double)r[2] ))
	    		.collect(Collectors.toCollection(ArrayList::new)) ;
        
		em.close();

		return chList ;

    }

    public ArrayList<ChartData> getChartData(int seq , int sec) {
    	Timestamp todt, fmdt ;
		EntityManager em = PocMain.emf.createEntityManager();
		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;
		em.close();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -sec); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		
    	return getChartData(fmdt, todt, seq)  ;
		
    }

    public ArrayList<ChartData> getChartData_hist(Timestamp fmdt, Timestamp todt, int seq) {
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
//		em.getEntityManagerFactory().getCache().evictAll();
		
		String sql = " CALL SP_CHARTDATA_HIST('" + fmdt + "', '" + todt + "'," + seq + ")" ; 
 
		Query nativeQuery = em.createNativeQuery(sql);
	    List<Object[]> resultList = nativeQuery.getResultList();
	    chList = resultList.stream().map(r -> new ChartData( 
	    		Timestamp.valueOf(r[0].toString()) , ((Long)r[1]).intValue(),  (Double)r[2] ))
	    		.collect(Collectors.toCollection(ArrayList::new)) ;
        
		em.close();

		return chList ;

    }

    public ArrayList<ChartData> getChartData_hist(int seq ) {
    	Timestamp todt, fmdt ;
		EntityManager em = PocMain.emf.createEntityManager();
		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;
		em.close();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -3600); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		
    	return getChartData_hist(fmdt, todt, seq)  ;
		
    }

    public ArrayList<MoteInfo> getMoteInfos(int seq, int cnt) {
    	Timestamp todt, fmdt ;
		EntityManager em = PocMain.emf.createEntityManager();
		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;
		em.close();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -cnt); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		
    	return getMoteInfos(fmdt, todt, seq)  ;
		
    }
    
    public Timestamp getLasTime() {
    	Timestamp lstime ;
		EntityManager em = PocMain.emf.createEntityManager();
		lstime = em.createQuery("select t.lastm from LasTime t  ", Timestamp.class).getSingleResult() ;
		em.close();
    	return lstime  ;
    }
}