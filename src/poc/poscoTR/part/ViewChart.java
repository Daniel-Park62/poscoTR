package poc.poscoTR.part;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Range;

import com.ibm.icu.util.Calendar;

import poc.poscoTR.model.ChartData;
import poc.poscoTR.model.FindMoteInfo;
import poc.poscoTR.model.MoteHist;
import poc.poscoTR.model.MoteInfo;

public class ViewChart {
	private Chart chart ;
	
	private static final double[] yS1 = { 30,31,32,33,34,35,36,37,38,37,35,34,33,32,31,30,29,27};
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
	final Color BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	final Color RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	final Color BLUE = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	final Color GREEN = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	final Color CYAN = Display.getDefault().getSystemColor(SWT.COLOR_DARK_CYAN);
    private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");
	
	FindMoteInfo findMoteinfo = new FindMoteInfo();
//	ArrayList<MoteHist> arrayinfo ;
	ArrayList<ChartData> arrayinfo ;

	/**
     * Create the composite.
     *
     * @param parent
     * @param style
     * @wbp.parser.entryPoint
     */

	int sel = 0;
	Label lblDate, lblTime,  lblfrom ,lblfromd, lblto, lbltod ;
	
	int px ;
	double py ;
    public ViewChart(Composite parent, int style) {

	    final Font font2 = new Font(Display.getCurrent(),"Calibri", 16, SWT.NORMAL);
	    final Font font21 = new Font(null,"Calibri", 14, SWT.NORMAL);

	    final Image chart_icon = new Image(Display.getCurrent(),"images/chart_icon.png");

	    final Cursor busyc = PocMain.busyc ;
	    final Cursor curc = parent.getCursor() ;
	    
	    Color COLOR_T = Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT) ;
    	
    	SashForm sash1 = new SashForm(parent, SWT.VERTICAL);

    	Composite comps1 = new Composite(sash1, SWT.NONE);
    	comps1.setLayout(new GridLayout(2, false));
    	comps1.setBackground(parent.getBackground());
//    	comps1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    	
    	Label lticon = new Label(comps1, SWT.NONE);
    	lticon.setImage(chart_icon);
    	lticon.setLayoutData(new GridData(SWT.FILL, GridData.VERTICAL_ALIGN_CENTER, false, true));
    	lticon.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//    	lticon.setSize(120, 120);
    	
		Label ltitle = new Label(comps1, SWT.NONE ) ;
    	ltitle.setText(" Chart View ���� ���� TR Strain ����͸�" ) ;
    	ltitle.setFont(new Font(null,"���� ���", 22, SWT.BOLD ) );
    	ltitle.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    	ltitle.setLayoutData(new GridData( SWT.FILL , SWT.CENTER, true, false));
//    	ltitle.setBounds(121, 0, -1,-1);
    	
		Composite composite_2 = new Composite(sash1, SWT.NONE);
		GridLayout gl_in = new GridLayout(12,false);
		gl_in.marginRight = 50;
		gl_in.marginLeft = 65;
		
		composite_2.setLayout(gl_in);
		GridData gd_in = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_in.heightHint = 50;

		composite_2.setLayoutData(gd_in);
		composite_2.setBackground(COLOR_T);
		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText(" * ID Select ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		}
		
		slist = findMoteinfo.getSensorList() ;
		slistA = new String[slist.length + 1] ;
		
		System.arraycopy(new String[] {" ALL "}, 0, slistA, 0, 1);
		System.arraycopy(slist,0,slistA, 1, slist.length ) ;
	    lineSeries = new ILineSeries[slist.length] ; 
		
		Combo cbddown = new Combo(composite_2, SWT.DROP_DOWN | SWT.BORDER);
		cbddown.setFont(font21);
		cbddown.setItems(slistA);
		cbddown.select(0);
		cbddown.pack();
		cbddown.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		cbddown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
				sel = cbddown.getSelectionIndex();
				chart.setFocus();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText("  * ��ȸ�Ⱓ Date/Time ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		}
		
		GridData gdinput = new GridData(100,20);
		DateText  fromDate = new DateText (composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER  );
		fromDate.setLayoutData(gdinput);
		fromDate.setFont(font21);
		
		TimeText fromTm = new TimeText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		fromTm.setLayoutData(gdinput);
		fromTm.setFont(font21);

		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText(" ~ ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		}
		DateText toDate = new DateText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		toDate.setLayoutData(gdinput);
		toDate.setFont(font21);
		
		TimeText toTm = new TimeText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		toTm.setLayoutData(gdinput);
		toTm.setFont(font21);

        Group  rGroup = new Group (composite_2, SWT.NONE);
        rGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
        rGroup.setFont(new Font(null, "", 1, SWT.NORMAL));
        rGroup.setBackground(COLOR_T);
        rGroup.setSize(400, -1);
        {
        	Button b2 = new Button(rGroup, SWT.RADIO);
        	b2.setText("2�ð�");
        	b2.setFont(font21);
        	Button b1 = new Button(rGroup, SWT.RADIO);
        	b1.setText("1�ð�");
        	b1.setFont(font21);
        	Button b30 = new Button(rGroup, SWT.RADIO);
        	b30.setText("30��");
        	b30.setFont(font21);
        	b1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        	b2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        	b30.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        	b2.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime();
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -7200); 

    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(dateFmt2.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(dateFmt2.format(todt ) );
    				
    			}
			});
        	b1.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime();
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -3600); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(dateFmt2.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(dateFmt2.format(todt ) );
    				
    			}
			});
        	b30.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime();
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -1800); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(dateFmt2.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(dateFmt2.format(todt ) );
    				
    			}

			});

        	b1.setSelection(true);
        	
        }

		Button searchb = new Button(composite_2, SWT.PUSH);
		searchb.setFont(font2);
		searchb.setText(" Search ");
		searchb.pack();
		searchb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String sfrom = fromDate.getText() + " " + fromTm.getText() ;
				String sto = toDate.getText() + " " + toTm.getText() ;
				
				try {
					Timestamp ts_dt = Timestamp.valueOf(sfrom) ;
					ts_dt = Timestamp.valueOf(sto) ;
				} catch (Exception e2) {
					MessageDialog.openError(parent.getShell(), "��¥����", "��¥ �Է��� �ٸ����ϼ���.") ;
					return ;
				}
				parent.getShell().setCursor( busyc);

				arrayinfo = findMoteinfo.getChartData_hist( Timestamp.valueOf(sfrom), 
						                               Timestamp.valueOf(sto), 
						                               cbddown.getSelectionIndex());
				/*
				 * catdate = null ; catdate = arrayinfo.stream().map(a ->
				 * datefmt.format(a.getTm()).substring(0, 21) )
				 * .distinct().toArray(String[]::new) ;
				 */
				setYdata();
				parent.getShell().setCursor( curc);
				chart.setFocus();
			}
		}); 

		Button initb = new Button(composite_2, SWT.PUSH);
		initb.setFont(font2);
		initb.setText(" zoom �ʱ�ȭ ");
		initb.pack();
//		initb.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true,2, 1));
		initb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chart.setRedraw(false);
				chart.getAxisSet().getXAxis(0).adjustRange();
				chart.getAxisSet().getYAxis(0).adjustRange();
				chart.setRedraw(true);
				chart.setFocus();
			}
		}); 

		Timestamp todt = findMoteinfo.getLasTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -3600); 
		
		Timestamp fmdt = new Timestamp(cal.getTime().getTime());
		fromDate.setText(dateFmt1.format(fmdt ) );
		fromTm.setText(dateFmt2.format(fmdt ) );
		toDate.setText(dateFmt1.format(todt ) );
		toTm.setText(dateFmt2.format(todt ) );
		
    	
		chart = new Chart(sash1, SWT.NONE);
		chart.setBackground(parent.getBackground());
		chart.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_CROSS));
		
//		chart.setLayoutData(new FillLayout());
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

		chart.getLegend().setFont(new Font(Display.getCurrent(),"Calibri", 14, SWT.NORMAL));
		// adjust the axis range
//		chart.getAxisSet().adjustRange();
		sash1.setWeights(new int[] {12,5,88});
		
		chart.setFocus();
		final IAxis xAxis = chart.getAxisSet().getXAxis(0);
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);
		xAxis.getTick().setForeground(BLACK);
		xAxis.getTick().setFont(new Font(Display.getCurrent(),"Calibri", 12, SWT.BOLD));
		xAxis.getTitle().setForeground(BLACK);
		xAxis.getTick().setFormat(timeFmt);
		
		yAxis.getTick().setForeground(BLACK);
		yAxis.getTitle().setForeground(BLACK);
		yAxis.getTitle().setText("Strain Value");
		yAxis.getTick().setFont(new Font(Display.getCurrent(),"Calibri", 12, SWT.BOLD));
		yAxis.getTick().setFormat(new DecimalFormat("##,###"));
		
		
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

				if (x != null )
				try {
					chart.getPlotArea().setToolTipText( datefmt.format(x) + "\n Strain:" + String.format("%,2.0f", py));
				} catch (Exception e2) {
					// TODO: handle exception
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
                  
			}
		});

    }
    
//	String[] catdate = {""};
    SimpleDateFormat timeFmt =  new SimpleDateFormat("HH:mm.ss") ;
    
    private void setYdata() {

    	IAxis xAxis = chart.getAxisSet().getXAxis(0);

    	try {
    		for (ILineSeries ls : lineSeries ) {
    			ls.setVisible(false);
    		}
    		for (int ix = 0; ix < lineSeries.length; ix++ )
    			lineSeries[ix].setVisible(false);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		double[][] ydv = new double[slist.length][] ;
		Date[][] xdv = new Date[slist.length][] ;
		
    	time_c = findMoteinfo.getLasTime() ;
    	chart.setRedraw(false);
    	for (int ix = 0; ix < slist.length; ix++) {
    		final int sno = Integer.parseInt(slist[ix].substring(2)) ;

        	if (sel == 0 || sel == sno ) {
        		int i2 = 0;

//        		ydv[ix] =  new double[(int)arrayinfo.stream()
//        		                      .filter(m -> m.getSensorNo() == sno).count()];
//        		for (ChartData m : arrayinfo.stream().filter(m -> m.getSensorNo() == sno).collect(Collectors.toList()) ) {
//        			ydv[ix][i2++]  = m.getTemp();
//        		}

        		ydv[ix] =  arrayinfo.stream().filter(m -> m.getSensorNo() == sno)
        				.mapToDouble(a -> a.getTemp()).toArray() ;
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

    	xAxis.enableCategory(true) ;
    	
		// adjust the axis range
		try {

			chart.getAxisSet().adjustRange();
			IAxis yAxis = chart.getAxisSet().getYAxis(0);
			yAxis.adjustRange();
			chart.getAxisSet().getXAxis(0).adjustRange();
		} catch (SWTException e) {
			// TODO: handle exception
		}
		chart.setRedraw(true);
		
		
    }

    private DateFormat datefmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss.S");

	Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
}
