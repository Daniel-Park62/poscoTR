package poc.poscoTR.part;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.wb.swt.SWTResourceManager;

import poc.poscoTR.model.ChartData;
import poc.poscoTR.model.FindMoteInfo;

public class RealChart {
	private Chart chart ;
	FindMoteInfo findMoteinfo = new FindMoteInfo();
	
	private static final double[] yS1 = { 30,31,32,33,34,35,36,37,38,37,35,34,33,32,31,30,29,27};
	Cursor handc = PocMain.handc ;
	Cursor busyc = PocMain.busyc ;
	Cursor curc ;
	String[] slist,  slistA;
	private ILineSeries[] lineSeries  ; 
	private Color[] colors = {
			SWTResourceManager.getColor(SWT.COLOR_BLUE),
			SWTResourceManager.getColor(SWT.COLOR_RED),
			SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN),
			SWTResourceManager.getColor(SWT.COLOR_GREEN),
			SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA),
			SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW),
			SWTResourceManager.getColor(SWT.COLOR_GREEN),
	};
	private int axisId ;
	final Color BLACK = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	final Color BLUE = SWTResourceManager.getColor(SWT.COLOR_BLUE);
	final Color GREEN = SWTResourceManager.getColor(SWT.COLOR_GREEN);
	final Color CYAN = SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN);
	/**
     * Create the composite.
     *
     * @param parent
     * @param style
     * @wbp.parser.entryPoint
     */

	int sel = 0;
	Label lblDate, lblTime,  lblfrom ,lblfromd, lblto, lbltod ;
	DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S");
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private DateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
	
    Thread uiUpdateThread ;
	int px ;
	double py ;
	Spinner spinner ;
	
    public RealChart(Composite parent, int style) {

	    final Font font2 = SWTResourceManager.getFont("Calibri", 16, SWT.NORMAL);
	    final Image slice_page = SWTResourceManager.getImage("images/slice_page6.png");
	    
	    Color COLOR_T = SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT) ;
    	
    	SashForm sash1 = new SashForm(parent, SWT.VERTICAL);
    	Composite comps1 = new Composite(sash1, SWT.NONE);
    	sash1.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				uiUpdateThread.stop();
			}
		});

    	comps1.setBackground(parent.getBackground());
		GridLayout gl_in = new GridLayout(2,true);
		gl_in.marginRight = 50;
		gl_in.marginLeft = 65;
		
//		comps1.setLayout(gl_in);
		GridData gd_in = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_in.heightHint = 30;
		gd_in.minimumHeight = 30;

		comps1.setLayoutData(gd_in);
		comps1.setBackgroundImage(slice_page);
		
		lblDate = new Label(comps1, SWT.NONE);
		
		lblDate.setBounds(340, 60, 160, 30);
		lblDate.setFont(new Font(null, "Microsoft Sans Serif", 16, SWT.NORMAL));
		lblDate.setText("");
		lblDate.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblDate.setBackground(new Color (Display.getCurrent(), 141,153,208));

		lblTime = new Label(comps1, SWT.NONE);
		lblTime.setBounds(340, 90 , 160, 30);
		lblTime.setFont(new Font(null, "Microsoft Sans Serif", 22, SWT.NORMAL));
		lblTime.setText("");
		lblTime.setBackground(new Color (Display.getCurrent(), 141,153,208));
		lblTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		{
			Label lbl = new Label(comps1,SWT.NONE) ;
			lbl.setText(" * ID Select ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.setBounds(650, 120, 150, 30);
			lbl.pack();
		}

		slist = findMoteinfo.getSensorList() ;
		slistA = new String[slist.length + 1] ;
		
		System.arraycopy(new String[] {" ALL "}, 0, slistA, 0, 1);
		System.arraycopy(slist,0,slistA, 1, slist.length ) ;
	    lineSeries = new ILineSeries[slist.length] ; 

		Combo cbddown = new Combo(comps1, SWT.DROP_DOWN | SWT.BORDER);
		cbddown.setFont(font2);
		cbddown.setItems(slistA);
		cbddown.select(0);
		cbddown.pack();
		cbddown.setBounds(760, 120, 80,25 );

		cbddown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				sel = cbddown.getSelectionIndex();
				try {
					setYdata();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		Label lblrg = new Label(comps1, SWT.NONE);
		lblrg.setText("범위(분)");
		lblrg.setFont(font2);
		lblrg.setBounds(870, 120, 80, 30);
		lblrg.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
	    spinner = new Spinner(comps1, SWT.BORDER | SWT.CENTER | GridData.CENTER);
	    spinner.setMinimum(10);
	    spinner.setMaximum(180);
	    spinner.setSelection(60);
	    spinner.setIncrement(5);
	    spinner.setSize(80, -1);
	    spinner.setFont(font2);
	    spinner.setBounds(960, 120, 60, 30);
		
		final Button bstart = new Button(comps1, SWT.ARROW | SWT.RIGHT);
		final Button bpause = new Button(comps1, SWT.PUSH | SWT.CENTER);
		final Button bzero = new Button(comps1, SWT.PUSH | SWT.CENTER);
		bpause.setCursor(handc);
		bpause.setFont(font2);
		bpause.setText(" PAUSE ");
		bpause.setBounds(1080, 120, 80, 30);
		bpause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uiUpdateThread.interrupt();
				uiUpdateThread = null;
				bstart.setEnabled(true);
				bpause.setEnabled(false);
			}
		}); 
		bpause.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false, 1, 1));
		
		bstart.setCursor(handc);
		bstart.setFont(font2);
		bstart.setToolTipText("Play");
		bstart.setBounds(1170, 120, 80, 30);
		bstart.setEnabled(false);
		bstart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( uiUpdateThread == null) {
					uiUpdateThread = new MyThread(Display.getCurrent(), PocMain.MOTECNF.getMeasure() * 1000 ) ;
					uiUpdateThread.start();
					bstart.setEnabled(false);
					bpause.setEnabled(true);
				}
			}
		}); 
		bstart.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false, 1, 1));

		bzero.setCursor(handc);
		bzero.setFont(font2);
		bzero.setToolTipText("Zeroing");
		bzero.setText("Zeroing");
		bzero.setBounds(1260, 120, 90, 30);
		bzero.setEnabled(true);
		bzero.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sendZero(cbddown.getText()) ;
			}
		}); 
		bzero.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false, 1, 1));
		
		
		chart = new Chart(sash1, SWT.NONE);
		chart.setBackground(parent.getBackground());
		chart.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_CROSS));
		Composite compb = new Composite(sash1, SWT.NONE);
		compb.setSize(600, -1);
		compb.setBackground(parent.getBackground());
		compb.setLayout(new GridLayout(5, false)) ;
		lblfrom = new Label(compb,SWT.FILL );
		lblfromd = new Label(compb,SWT.BORDER);
		lblto = new Label(compb,SWT.NONE);
		lbltod = new Label(compb,SWT.BORDER);
		lblfrom.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false, true, 1, 1));
		lblfromd.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false, true, 1, 1));
		lblto.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false, true, 1, 1));
		lbltod.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false, true, 1, 1));
		lblfrom.setFont(font2) ;
		lblto.setFont(font2);
		lblfromd.setFont(new Font(null, "MS Gothic", 14, SWT.BOLD )) ;
		lbltod.setFont(new Font(null, "MS Gothic", 14, SWT.BOLD )) ;
		lblfrom.setText(" 조회기간  Date/Time ");
		lblto.setText(" ~ ");
    	lblfromd.setText(" " + datefmt.format(time_c).substring(0, 21)) ; 
    	lbltod.setText(" " + datefmt.format(time_c).substring(0, 21)) ;
		compb.pack();
		lblfrom.setBackground(parent.getBackground());
		lblto.setBackground(parent.getBackground());
		lblfrom.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		lblto.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		
		chart.setLayoutData(new FillLayout());
		// set titles
		chart.getTitle().setText("Strain Chart");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Time(Sec)");
		chart.getAxisSet().getYAxis(0).getTitle().setText("");
		// create second Y axis
//		axisId = chart.getAxisSet().createYAxis();
		axisId = 0 ;
		
		// set the properties of second Y axis
//		IAxis yAxis2 = chart.getAxisSet().getYAxis(axisId);
//		yAxis2.setPosition(Position.Secondary);

		// create line series
		for (int ix = 0; ix < slist.length ; ix++) {
			lineSeries[ix] = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, slist[ix]);
			lineSeries[ix].setYSeries(yS1);
		}

		chart.getLegend().setFont(new Font(Display.getCurrent(),"Calibri", 14, SWT.BOLD));
		// adjust the axis range
//		chart.getAxisSet().adjustRange();
		sash1.setWeights(new int[] {23,90,5});
		
		final IAxis xAxis = chart.getAxisSet().getXAxis(0);
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);

		IAxisTick xTick = xAxis.getTick();
    	xTick.setTickMarkStepHint(10);
		
    	xTick.setForeground(BLACK);
    	xTick.setFont(new Font(Display.getCurrent(),"굴림", 10, SWT.BOLD));
		xAxis.getTitle().setForeground(BLACK);
		xAxis.getTick().setFormat(new SimpleDateFormat("HH:mm:ss"));
		
		yAxis.getTick().setForeground(BLACK);
		yAxis.getTitle().setForeground(BLACK);
		yAxis.getTitle().setText("Strain Value");
		yAxis.getTick().setFont(new Font(Display.getCurrent(),"Calibri", 12, SWT.BOLD));
		yAxis.getTick().setFormat(new DecimalFormat("#,###"));
		
		Composite plot = (Composite)chart.getPlotArea() ;
		plot.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				chart.setRedraw(false);
				if (arg0.button == 1) { 
					chart.getAxisSet().getXAxis(0).zoomIn(px);
					chart.getAxisSet().getYAxis(0).zoomIn(py);
				};
				if (arg0.button == 3) { 
					chart.getAxisSet().getXAxis(0).zoomOut(px);
					chart.getAxisSet().getYAxis(0).zoomOut(py);
					if (chart.getAxisSet().getXAxis(0).getRange().lower < 0) {
						chart.getAxisSet().getXAxis(0).scrollUp();
					}
				};
				chart.setRedraw(true);
				chart.setFocus();
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {

				Date x = new Date((long)chart.getAxisSet().getXAxis(0).getDataCoordinate(e.x));
				py = yAxis.getDataCoordinate(e.y);
				if (x != null)
				try {
					chart.getPlotArea().setToolTipText(datefmt.format(x) + "\n Strain: " + String.format("%,2.0f", py));	
				} catch (Exception e2) {
					System.out.println(e2 + ": " + px + ":" + py);
				}
				
			}
		});
		
		chart.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent arg0) {
				int wheelCount = (int) Math.ceil(arg0.count / 3.0f);
				chart.setRedraw(false);
				while (wheelCount != 0) {
						
						if (wheelCount > 0) {
							chart.getAxisSet().getYAxis(0).scrollUp();
							wheelCount-- ;
						} else {
							chart.getAxisSet().getYAxis(0).scrollDown();
							wheelCount++ ;
						}
				}
				
				/*
				wheelCount = (int) Math.ceil(wheelCount / 3.0f);
				Range yrange = yAxis.getRange();

				yrange.lower +=  50 * wheelCount;
				yrange.upper -=  50 * wheelCount;

				try {
					yAxis.setRange(yrange);
				} catch (Exception e) {

				}
				*/
				chart.setRedraw(true);
//				chart.redraw();
                  
			}
		});
//		setYdata();
		uiUpdateThread = new MyThread(Display.getCurrent(), PocMain.MOTECNF.getMeasure() * 1000 ) ;
		uiUpdateThread.start();

    }
    

	private class MyThread extends Thread {
		private Display display = null;
		private int interval ;
		MyThread(Display display, int interval){
			this.display = display ;
			this.interval = interval ;
		}
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted() && !chart.isDisposed() ) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							curc = chart.getCursor();
							chart.setCursor(busyc);
							setYdata();
							chart.setCursor(curc);
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				});
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
//					e.printStackTrace();
					break;
				}

			}
		}

	}

    private synchronized void setYdata() {
    	ArrayList<ChartData> arrayinfo ;
    	
    	IAxis xAxis = chart.getAxisSet().getXAxis(0);

    	try {
    		for (int ix = 0; ix < lineSeries.length; ix++ )
    			lineSeries[ix].setVisible(false);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		double[][] ydv = new double[slist.length][] ;
		Date[][] xdv = new Date[slist.length][] ;

    	time_c = findMoteinfo.getLasTime() ;

    	arrayinfo = findMoteinfo .getChartData(0, spinner.getSelection() * 60); 

    	chart.setRedraw(false);

    	for (int ix = 0; ix < slist.length; ix++) {
    		final int sno = Integer.parseInt(slist[ix].substring(2)) ;
        	if (sel == 0 || sel == sno ) {

        		ydv[ix] =  arrayinfo.stream().filter(m -> m.getSensorNo() == sno)
        				.map(a -> a.getTemp()).mapToDouble(d -> d)
        				.toArray() ;
        		xdv[ix] =  arrayinfo.stream().filter(m -> m.getSensorNo() == sno)
       				 .map(a -> a.getTm()).toArray(Date[]::new) ;
        		
        		lineSeries[ix].setYSeries(ydv[ix]);
        		lineSeries[ix].setXDateSeries(xdv[ix]);

//        		lineSeries[ix].setSymbolColor(colors[ix]);
        		lineSeries[ix].setLineColor(colors[ix]);
        		lineSeries[ix].setYAxisId(axisId);
        		lineSeries[ix].setSymbolType(PlotSymbolType.NONE);
        		lineSeries[ix].setSymbolSize(2);
        		lineSeries[ix].setLineStyle(LineStyle.SOLID);
        		lineSeries[ix].setAntialias(SWT.ON);
        		lineSeries[ix].setVisible(true);
        	}
    	}
    	
    	if (arrayinfo.size() >0) lblfromd.setText(" " + datefmt.format(arrayinfo.get(0).getTm()).substring(0, 21)) ; 
    	lbltod.setText(" " + datefmt.format(time_c).substring(0, 21)) ;
		lblDate.setText(dateFormat.format(time_c));
		lblTime.setText( ( timeFormat.format(time_c)).substring(0, 10));
		lblDate.pack();
		lblTime.pack();
		chart.setFocus();

    	xAxis.enableCategory(true) ;
//    	xAxis.setCategorySeries(catdate) ;

		// adjust the axis range
		try {
			chart.getAxisSet().adjustRange();
			IAxis yAxis = chart.getAxisSet().getYAxis(0);
//			IAxisTick xTick = xAxis.getTick();

//	    	xTick.setTickMarkStepHint(150);
			yAxis.adjustRange();
			chart.getAxisSet().getXAxis(0).adjustRange();
//			chart.redraw();
		} catch (SWTException e) {
			// TODO: handle exception
		}
		chart.setRedraw(true);
		chart.setFocus();
		
    }
	private int sendZero(String sno ) {
		int no ;
		String mac = "";
		if (!" ALL ".equals(sno)) { 
			no = Integer.parseInt(sno.substring(2)) ;
			EntityManager em = PocMain.emf.createEntityManager();
			mac = em.createQuery("select t.mac from MoteStatus t where t.sensorNo = :sno ", 
					String.class).setParameter("sno", no).getSingleResult() ;
			em.close();
		};
        String s = System.getenv("MONIP") ;
        if (s == null) s = "localhost" ;
        System.out.println("MONIP:"+s);

		String url="http://"+ s + ":9977/zero" + (mac.indexOf("00:") != -1 ? "?mac=" + mac : ""); 

		try {
			System.out.println(url) ;
			URL obj = new URL(url);
			URLConnection conn = obj.openConnection();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			char[] buff = new char[512];
			int len = -1;
			while( (len = br.read(buff)) != -1) {
				//	           System.out.print(new String(buff, 0, len));
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println(url + " http send error !!") ;
		}
		return 1;
	}

}
