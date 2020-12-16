package poc.poscoTR.part;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import poc.poscoTR.model.FindMoteInfo;
import poc.poscoTR.model.MoteStatus;
import poc.poscoTR.model.PeakTrend;
import poc.poscoTR.model.Vstatus;

import org.eclipse.wb.swt.SWTResourceManager;

public class DashBoard {

//	List<MoteInfo> selectedTagList = new ArrayList<MoteInfo>();
	Point selectedPoint = new Point(0, 0);

	HashMap<Integer, Integer> tagCount = new HashMap<Integer, Integer>();
    // use the org.eclipse.core.runtime.Path as import
    
//    URL url8 = FileLocator.find(bundle, new Path("images/slice_page1.png"), null);
//    ImageDescriptor slice_page1 = ImageDescriptor.createFromURL(url8);
	Image slice_page1 = SWTResourceManager.getImage("images/slice_page1c.png");
	
    Label lblApActive;
    Label lblApInactive;
    Label lblTagActive;
    Label lblTagInactive;
    Label lblAlertActive;
    Label lblAlertInactive;
    Label lblDate, lblTime;
    Label lblinterval;
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//    DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");
    Font font1 = SWTResourceManager.getFont("Microsoft Sans Serif", 22 , SWT.NORMAL ) ;
    Font font2 = SWTResourceManager.getFont("Calibri", 14, SWT.NORMAL);
    Font font3 = SWTResourceManager.getFont("Calibri", 13, SWT.NORMAL ) ;
    Thread uiUpdateThread ;
    
    FindMoteInfo findMoteinfo = new FindMoteInfo() ;
	List<MoteStatus > motelist ;

    MiniChart[] minic = new MiniChart[6];
    
	public DashBoard(Composite parent, int style) {

//		super(parent, style) ;
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginTop = 10;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
	
		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				uiUpdateThread.stop();
				uiUpdateThread = null;
			}
		});
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Composite composite_15 = new Composite(composite, SWT.NONE);
		GridData gd_composite_15 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_15.heightHint = 370;
		composite_15.setLayoutData(gd_composite_15);
		composite_15.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		composite_15.setBackgroundMode(SWT.INHERIT_DEFAULT);

		
		lblApActive = new Label(composite_15, SWT.NONE);
		lblApActive.setAlignment(SWT.RIGHT);
		lblApActive.setFont(font1);
		lblApActive.setBounds(295, 170, 40, 30);
		lblApActive.setText("99");
		
		lblApInactive = new Label(composite_15, SWT.NONE);
		lblApInactive.setAlignment(SWT.RIGHT);
		lblApInactive.setFont(font1);
		lblApInactive.setBounds(295, 205, 40, 30);
		lblApInactive.setText("99");
		
		lblTagActive = new Label(composite_15, SWT.NONE);
		lblTagActive.setAlignment(SWT.RIGHT);
		lblTagActive.setFont(font1);
		lblTagActive.setBounds(665, 170, 40, 30);
		lblTagActive.setText("99");
		
		lblTagInactive = new Label(composite_15, SWT.NONE);
		lblTagInactive.setAlignment(SWT.RIGHT);
		lblTagInactive.setFont(font1);
		lblTagInactive.setBounds(665, 205, 40, 30);
		lblTagInactive.setText(" 0");
		
		lblAlertActive = new Label(composite_15, SWT.NONE);
		lblAlertActive.setAlignment(SWT.RIGHT);
		lblAlertActive.setFont(font1);
		lblAlertActive.setBounds(1045, 170, 40, 30);
		lblAlertActive.setText(" 0");
		lblAlertActive.setBackground(SWTResourceManager.getColor( 255,240,240));
		
		lblAlertInactive = new Label(composite_15, SWT.NONE);
		lblAlertInactive.setAlignment(SWT.RIGHT);
		lblAlertInactive.setFont(font1);
		lblAlertInactive.setBounds(1045, 205, 40, 30);
		lblAlertInactive.setText(" 0");
		lblAlertInactive.setBackground(SWTResourceManager.getColor( 255,240,240));
		
		Label lblNewLabel_4 = new Label(composite_15, SWT.NONE);
		lblNewLabel_4.setBounds(0, 0, 1450, 400);
		lblNewLabel_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT)); 
		lblNewLabel_4.setBackgroundImage(slice_page1);
		
		lblApActive.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblApInactive.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTagActive.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblTagInactive.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Label lbldate_1 = new Label(composite_15, SWT.SHADOW_IN);
		lbldate_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbldate_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lbldate_1.setFont(font2);
		lbldate_1.setBounds(1020, 55, 50, 30);
		lbldate_1.setText("Date ");
		lbldate_1.pack();


		lblDate = new Label(composite_15, SWT.NONE);
		lblDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblDate.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lblDate.setFont(font2);
		lblDate.setBounds(1070, 55, 250, 30);
		lblDate.setText("2019-02-02");

		lblinterval = new Label(composite_15, SWT.NONE);
		lblinterval.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblinterval.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lblinterval.setFont(font2);
		lblinterval.setBounds(1020, 80, 220, 30);
		lblinterval.setText("Time Interval 10 sec ");

		lbldate_1.moveAbove(lblNewLabel_4);
		lblinterval.moveAbove(lblNewLabel_4);
		lblDate.moveAbove(lblNewLabel_4);

// motestatus list 
		motelist = em.createNamedQuery("MoteStatus.sensorList", MoteStatus.class).getResultList()  ;
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginRight = 10;
		gl_composite_3.marginLeft = 10;
		gl_composite_3.marginBottom = 5;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		SashForm  sashv = new SashForm(composite_3, SWT.HORIZONTAL);
		SashForm  sash1 = new SashForm(sashv, SWT.VERTICAL);
		SashForm  sash2 = new SashForm(sashv, SWT.VERTICAL);
//		comphart.setLayout(new GridLayout(3, false));
		sashv.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true));
		sashv.setBackground(composite.getBackground());
		sash2.setBackground(composite.getBackground());
		sash1.setBackground(composite.getBackground());
		sash1.setSashWidth(5);
		sash2.setSashWidth(5);
		sashv.setSashWidth(8);
		try {
			for (int i = 0 ; i < 3 ; i++ ) {
				final String locnm = PocMain.locname[i] ;
				minic[i] = new MiniChart(sash1, SWT.NONE, motelist.stream().filter(m -> locnm.equals(m.getLoc())).findAny().orElse(null) );
			}
				
			for (int i = 3 ; i < 6 ; i++ ) {
				final String locnm = PocMain.locname[i] ;
				minic[i] = new MiniChart(sash2, SWT.NONE, motelist.stream().filter(m -> locnm.equals(m.getLoc())).findAny().orElse(null) );
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		uiUpdateThread = new MyThread(Display.getCurrent(), PocMain.MOTECNF.getMeasure() * 1000) ;
		uiUpdateThread.start();
	}

	int activeCnt = 0;
	int inactiveCnt = 0;

	int activeSsCnt = 0;
	int failCnt = 0;
	int moteLBCnt = 0;
	int oBCnt = 0;

	private Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
    EntityManager em = PocMain.emf.createEntityManager();
	
	private class MyThread extends Thread {
		private Display display = null;
		private int interval ;
		MyThread(Display display, int interval){
			this.display = display ;
			this.interval = interval ;
		}
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted() && !lblinterval.isDisposed()) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						refreshSensorList();
					}
				});
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
//					e.printStackTrace();
				}

			}
		}

	}


//	@SuppressWarnings("unchecked")
	public void refreshSensorList() {
		Cursor cursor = PocMain.cur_comp.getCursor() ;
		PocMain.cur_comp.setCursor(PocMain.busyc);
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();

		activeCnt = 0;
		inactiveCnt = 0;
		activeSsCnt = 0;
		failCnt = 0;
		moteLBCnt = 0;
		oBCnt = 0;

		PocMain.VSTATUS = em.createNamedQuery("Vstatus.find", Vstatus.class).getSingleResult() ;
		time_c = findMoteinfo.getLasTime();
		
		activeCnt = PocMain.VSTATUS.getActcnt() ;
		inactiveCnt = PocMain.VSTATUS.getInactcnt() ;
		moteLBCnt = PocMain.VSTATUS.getLbcnt() ;

        activeSsCnt = PocMain.VSTATUS.getSactcnt();
        failCnt = PocMain.VSTATUS.getSinactcnt() ;
        oBCnt  = PocMain.VSTATUS.getObcnt() ;
        
		lblinterval.setText("Time Interval  "+ PocMain.MOTECNF.getMeasure() + " sec ");
		lblDate.setText( ( dateFormat.format(time_c )).substring(0, 21) );
		lblDate.pack();
		lblApActive.setText(activeCnt+"");
		lblApInactive.setText(inactiveCnt+"");

		lblTagActive.setText(activeSsCnt+"");
		lblTagInactive.setText(failCnt+"");
		lblAlertActive.setText(moteLBCnt+"");
		lblAlertInactive.setText(oBCnt+"");

		
		List<PeakTrend> arrayinfo ;

		for (int i = 0; i < 6; i++) {
 
			if (minic[i] != null) minic[i].setChartData();
		}
		PocMain.cur_comp.setCursor(cursor);

	}
	

}