package poc.poscoTR.part;


import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import poc.poscoTR.model.MoteConfig;
import poc.poscoTR.model.MoteInfo;
import poc.poscoTR.model.MoteStatus;

public class SensorMap  {

	Thread uiUpdateThread;
	public SensorMap(Composite parent, int style) {
		// TODO Auto-generated constructor stub
		postConstruct(parent);
	}
	//	EntityManager em = e4Application.emf.createEntityManager();

	MoteConfig moteConfig = PocMain.MOTECNF ;

	List<MoteInfo> moteInfoList ;

	final int RCOUNT = 10 ;

	final int[][] rDevXy = { 
			{100,550},	{180,550},	{260,550},	{340,550},{420,550} ,
			{500,550},	{580,550},	{660,550},	{740,550},{820,550} 
	} ;

	SensorWidget2[] sWidget ;
	RepeatWidget[] rWidget = new RepeatWidget[RCOUNT] ;
	float levelScale = 1 ;
	Canvas canvas;

	Image image_active ;
	Image image_inactive ;
	Image image_lowbattery ;
	Image image_sos ;
	Image image_popup_inactive_1;

	Label lblApActive;
	Label lblApInactive;
	Label lblApLow;

	Label lblTagActive;
	Label lblTagInactive;
	Label lblTagLow;
	Label lblDate , lblTime ;
	Composite child ;
	ScrolledComposite scrolledComposite_1;
	
	DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S");
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

	@PostConstruct
	public void postConstruct(Composite parent) {

		//		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		parent.setLayout(new GridLayout(1, false));
		Image image = new Image(parent.getDisplay(),"images/map.png") ;
		image_active = new Image(parent.getDisplay(),"images/icon_active.png") ;
		image_inactive = new Image(parent.getDisplay(),"images/icon_inactive.png") ;
		image_lowbattery = new Image(parent.getDisplay(),"images/icon_lowbattery.png") ;
		image_sos = new Image(parent.getDisplay(),"images/moteicon_sos.png") ;
		image_popup_inactive_1 = new Image(parent.getDisplay(),"images/popup_inactive_1.png");
		Image slice_page2_1 = new Image(parent.getDisplay(), "images/slice_page2_1.png") ;
		Image slice_page2_2 = new Image(parent.getDisplay(), "images/slice_page2_2.png") ;
		Image category = new Image(parent.getDisplay(), "images/category.png") ;

		//		Composite composite = new Composite(parent, SWT.NONE);
		//		composite.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				uiUpdateThread.stop();
			}
		});
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		//		gd_composite_1.minimumHeight = 240;
		gd_composite_1.heightHint = 200;
		//		gd_composite_1.widthHint = 2500;
		//		gd_composite_1.minimumWidth = 1920;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		//		composite_1.setBackgroundImage(resourceManager.createImage(slice_page2_1));
		composite_1.setBackgroundMode(SWT.INHERIT_FORCE);

		lblDate = new Label(composite_1, SWT.NONE);

		lblDate.setBounds(340, 60, 60, 30);
		lblDate.setFont(new Font(null, "Microsoft Sans Serif", 16, SWT.NORMAL));
		lblDate.setText("New Label");
		lblDate.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblDate.setBackground(new Color (Display.getCurrent(), 141,153,208));

		lblTime = new Label(composite_1, SWT.NONE);
		lblTime.setBounds(340, 90 , 60, 30);
		lblTime.setFont(new Font(null, "Microsoft Sans Serif", 22, SWT.NORMAL));
		lblTime.setText("New Label");
		lblTime.setBackground(new Color (Display.getCurrent(), 141,153,208));
		lblTime.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		Label lblt = new Label(composite_1, SWT.NONE);
		lblt.setBounds(0, 0, 1900, 200);
		lblt.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblt.setImage(slice_page2_1);

		//MAP
		scrolledComposite_1 = new ScrolledComposite(parent, SWT.NONE);

		GridData gd_scrolledComposite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_scrolledComposite_1.widthHint = 1620;
		scrolledComposite_1.setLayoutData(gd_scrolledComposite_1);
		scrolledComposite_1.setExpandVertical(true);
		scrolledComposite_1.setExpandHorizontal(true);

		child = new Composite(scrolledComposite_1, SWT.NONE);
		child.setBackgroundMode(SWT.INHERIT_FORCE);
		child.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		child.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
			}
		});

		scrolledComposite_1.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				//int width = scrolledComposite_1.getClientArea().width;
				scrolledComposite_1.setMinSize( child.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
				//scrolledComposite_1.setMinSize(scrolledComposite_1.getClientArea().width,scrolledComposite_1.getClientArea().height);
			}
		});

		scrolledComposite_1.setContent(child);
		EntityManager em = PocMain.emf.createEntityManager();

		sList = em.createNamedQuery("MoteStatus.sensorList", MoteStatus.class).getResultList() ;
		sWidget = new SensorWidget2[sList.size() ];
		
		for (int i = 0 ;i < sList.size() ; i++) {
			sWidget[i] = new SensorWidget2(child, sList.get(i));
			Point point = sList.get(i).getXy();
			sWidget[i].setLocation( (int)( point.x * levelScale ) -65 , (int)( point.y * levelScale - 65) );
		}
		
		for (int i = 0 ;i < RCOUNT ; i++) {
			rWidget[i] = new RepeatWidget(child, "R", rDevXy[i][0], rDevXy[i][1]);
		}

		{
			ImageData imageData = image.getImageData();
			Label lbl = new Label(child, SWT.NONE);
			lbl.setBounds(50, 0, imageData.width, imageData.height);
			lbl.setSize(imageData.width, imageData.height);
			lbl.setBackgroundImage(image);
		}

		

		//		reLocate();

		// 	map end

		Composite composite_2 = new Composite(parent, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_2.widthHint = 1200;
		gd_composite_2.heightHint = 100;
		composite_2.setLayoutData(gd_composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_composite_5.minimumWidth = 900;
		gd_composite_5.heightHint = 100;
		gd_composite_5.widthHint = 850;
		composite_5.setLayoutData(gd_composite_5);
		composite_5.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite_5.setBackgroundImage(slice_page2_2);
//
//		Label lblBack = new Label(composite_5, SWT.NONE);
//		lblBack.setCursor(new Cursor(Display.getCurrent(),SWT.CURSOR_HAND));
//		lblBack.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDown(MouseEvent e) {
//			}
//		});
//		lblBack.setBounds(41, 20, 69, 60);
//		lblBack.setText("");
//		lblBack.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblApActive = new Label(composite_5, SWT.NONE);
		lblApActive.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblApActive.setAlignment(SWT.CENTER);
		lblApActive.setBounds(200, 15, 41, 61);
		lblApActive.setText(" 8");
		lblApActive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblApInactive = new Label(composite_5, SWT.NONE);
		lblApInactive.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblApInactive.setAlignment(SWT.CENTER);
		lblApInactive.setBounds(290, 15, 41, 61);
		lblApInactive.setText(" 8");
		lblApInactive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblApLow = new Label(composite_5, SWT.NONE);
		lblApLow.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblApLow.setAlignment(SWT.CENTER);
		lblApLow.setBounds(385, 15, 41, 61);
		lblApLow.setText(" 8");
		lblApLow.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblTagActive = new Label(composite_5, SWT.NONE);
		lblTagActive.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblTagActive.setAlignment(SWT.CENTER);
		lblTagActive.setBounds(580, 15, 41, 61);
		lblTagActive.setText(" 8");
		lblTagActive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblTagInactive = new Label(composite_5, SWT.NONE);
		lblTagInactive.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblTagInactive.setAlignment(SWT.CENTER);
		lblTagInactive.setBounds(660, 15, 41, 61);
		lblTagInactive.setText(" 8");
		lblTagInactive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		lblTagLow = new Label(composite_5, SWT.NONE);
		lblTagLow.setFont(new Font(null, "¸¼Àº °íµñ", 24, SWT.BOLD));
		lblTagLow.setAlignment(SWT.CENTER);
		lblTagLow.setBounds(765, 15, 41, 61);
		lblTagLow.setText(" 8");
		lblTagLow.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		{
			Label lbltc = new Label(composite_2, SWT.NONE );
			GridData gdc = new GridData( GridData.CENTER,GridData.FILL_VERTICAL,true,true );
			gdc.heightHint = 100 ;
			//			gdc.widthHint  = 700 ;
			lbltc.setLayoutData(gdc);
			lbltc.setImage(category);
			lbltc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		}

		uiUpdateThread = new MyThread(Display.getCurrent(), 10000) ;
		uiUpdateThread.start();

	} // postconstruct


	private class MyThread extends Thread {
		private Display display = null;
		private int interval ;
		MyThread(Display display, int interval){
			this.display = display ;
			this.interval = interval ;
		}
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted() && !lblDate.isDisposed() ) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						refreshConfig();
						reLocate();
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

	private Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
	int tagActcnt = 0, tagInActcnt = 0, tagLowcnt = 0;
	int moteActcnt = 0, moteInActcnt = 0, moteLowcnt = 0;

	private  void refreshConfig( ) {
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		moteConfig = em.createQuery("select m from MoteConfig m ", MoteConfig.class).getSingleResult() ;

		//		time_c = em.createQuery("select max(t.tm) from MoteInfo t", Timestamp.class).getSingleResult() ;
		time_c = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;

		TypedQuery<MoteInfo> qMotes = em.createQuery("select t from MoteInfo t "
				+ " where t.tm = :tm  order by t.act desc ", MoteInfo.class);
		qMotes.setParameter("tm", time_c );
		moteInfoList = qMotes.getResultList();
		em.close();
	}

	List<MoteStatus> sList ;
	private   void reLocate() {
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		lblDate.setText(dateFormat.format(new Date( time_c.getTime() ) ));
		lblTime.setText((timeFormat.format(new Date( time_c.getTime() ) )).substring(0, 10)  );
		lblDate.pack();
		lblTime.pack();
		sList = null;
//		sWidget = null;
		sList = em.createNamedQuery("MoteStatus.sensorList", MoteStatus.class).getResultList() ;
//		sWidget = new SensorWidget2[sList.size() ];
		
		for ( int i = 0 ;i < sList.size() ; i++) {
			
			final MoteStatus ss = sList.get(i); 
			
//			sWidget[i] = new SensorWidget2(child, ss );

			Point point = ss.getXy() ;
			
			if (point.x <= 0) point.x = 100 ;
			if (point.y <= 0) point.y = 100 ;
			
			sWidget[i].setLocation( (int)( point.x * levelScale ) -65 , (int)( point.y * levelScale - 65) );
			sWidget[i].setId(ss.getSensorNm());
			sWidget[i].setImage(1);
			for (MoteInfo t : moteInfoList ) {
				if (ss.getSensorNo() == t.getSensorNo() ) {
					sWidget[i].setTemp(String.format("%,2.0f", t.getTemp() ) );
					
					if (t.getTemp() != 0.0) sWidget[i].setImage(2);
				}
			};
			
		}
		scrolledComposite_1.setContent(child);
		
		tagActcnt =  tagInActcnt =  tagLowcnt = 0;
		moteActcnt =  moteInActcnt =  moteLowcnt = 0;

		moteActcnt = (int)moteInfoList.stream().filter(t -> t.getTemp() > 0).count() ;
		moteInActcnt = (int)moteInfoList.stream().filter(t -> t.getTemp() == 0).count() ;

		TypedQuery<MoteStatus> q2 = em.createQuery("select t from MoteStatus t where t.gubun = 'R' and t.spare = 'N' order by t.seq", MoteStatus.class);
		List<MoteStatus> moteList = q2.getResultList(); 
		if (moteList != null)
			for (int ri = 0; ri < RCOUNT; ri++ )  {
				MoteStatus mote = moteList.get(ri) ;

				rWidget[ ri ].setId(String.format("R%02d",ri+1 ));
				if (mote.getAct() == 2) {
					tagActcnt++ ;
					rWidget[ ri ].setImage(2);
				} else {
					tagInActcnt++ ;
					rWidget[ ri ].setImage(1);
				}

				if (mote.getBatt() < 3.5 && mote.getAct() > 0)  {
					tagLowcnt++ ;
					rWidget[ ri ].setImage(0);
				}
			}

		lblApActive.setText(moteActcnt+"");
		lblApInactive.setText(moteInActcnt+"");
		lblApLow.setText(moteLowcnt+"");
		lblTagActive.setText(tagActcnt+"");
		lblTagInactive.setText(tagInActcnt+"");
		lblTagLow.setText(tagLowcnt+"");
	}
}
