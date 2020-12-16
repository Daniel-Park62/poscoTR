package poc.poscoTR.part;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.icu.util.Calendar;

import poc.poscoTR.model.FindMoteInfo;
import poc.poscoTR.model.MoteHist;
import poc.poscoTR.model.MoteStatus;

public class RealTime  {

    EntityManager em = PocMain.emf.createEntityManager();
    FindMoteInfo findMoteinfo = new FindMoteInfo() ;
    Composite parent ;
    public RealTime(Composite parent, int style) {
    	this.parent = parent ;
		postConstruct(parent);
	}

    private static class ContentProvider_1 implements IStructuredContentProvider {
    	@Override
    	public Object[] getElements(Object input) {
    		return ((ArrayList<MoteStatus>)input).toArray();
    	}
    	@Override
    	public void dispose() {
    	}
    	@Override
    	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    	}
	}
	private static class ContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object input) {
			//return new Object[0];
			return ((ArrayList<MoteHist>)input).toArray();
		}
		@Override
		public void dispose() {
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
//	List<MoteInfo> selectedTagList = new ArrayList<MoteInfo>();
	Point selectedPoint = new Point(0, 0);
	
	HashMap<Integer, Integer> tagCount = new HashMap<Integer, Integer>();

    Image slice_page3 = new Image(Display.getCurrent(),"images/slice_page3.png");
    
//    URL url9 = FileLocator.find(bundle, new Path("images/categoryicon_1.png"), null);
//    ImageDescriptor categoryicon_1 = ImageDescriptor.createFromURL(url9);

//    URL url10 = FileLocator.find(bundle, new Path("images/categoryicon_2.png"), null);
//    ImageDescriptor categoryicon_2 = ImageDescriptor.createFromURL(url10);

//    URL url11 = FileLocator.find(bundle, new Path("images/categoryicon_3.png"), null);
//    ImageDescriptor categoryicon_3 = ImageDescriptor.createFromURL(url11);

    Label lblApActive;
    Label lblApInactive;
    Label lblTagActive;
    Label lblTagInactive;
    Label lblAlertActive;
    Label lblAlertInactive;
    Label lblDate, lblTime;
    Label lblinterval ;
    private Label bottoml ;
    private Table table;
    private TableViewer tableViewer;
    private DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss.S");
    private DateFormat timeFmt = new SimpleDateFormat("HH:mm:ss");
    private Combo cbddown ;
    private Cursor busyc = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
    private Cursor curc ;
    private String[] slist, slistA ;
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		curc = parent.getCursor() ;
	    Font font2 = new Font(null,"맑은 고딕", 16, SWT.NORMAL);
	    Font font21 = new Font(null,"Calibri", 14, SWT.NORMAL);
	    Font font3 = new Font(null,"Calibri", 13, SWT.NORMAL ) ;
	    Color COLOR_T = Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT) ;
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginTop = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.verticalSpacing = 0;
		gl_parent.marginBottom = 10;
		parent.setLayout(gl_parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Composite composite_15 = new Composite(composite, SWT.NONE);
		composite_15.setLayout(new GridLayout(1,true));
		GridData gd_composite_15 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_15.heightHint = 150;
		composite_15.setLayoutData(gd_composite_15);
		composite_15.setBackground(COLOR_T);
//		{
//			Label lbl = new Label(composite_15, SWT.NONE) ;
//			lbl.setText("제강 KR IMPELLER 내부 온도 모니터링");
//			lbl.setBackground(COLOR_T);
//			lbl.setFont(font2);
//		}
//		composite_15.setBackgroundImage(slice_page3);
		
		
		Label lblNewLabel_4 = new Label(composite_15, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		lblNewLabel_4.setBounds(0, 0, 1647, 600);
		lblNewLabel_4.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_4.setImage(slice_page3);
		
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_sep = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_sep.heightHint = 20;
		label.setBackground(COLOR_T);
		label.setLayoutData(gd_sep);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridLayout gl_in = new GridLayout(12,false);
		gl_in.marginRight = 50;
		gl_in.marginLeft = 40;
		
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
		}
		slist = findMoteinfo.getSensorList() ;
		slistA = new String[slist.length + 1] ;
		
		System.arraycopy(new String[] {" ALL "}, 0, slistA, 0, 1);
		System.arraycopy(slist,0,slistA, 1, slist.length ) ;

		cbddown = new Combo(composite_2, SWT.DROP_DOWN | SWT.BORDER);
		cbddown.setFont(font21);
		cbddown.setItems(slistA);
		cbddown.select(0);
		cbddown.pack();
		
		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText("  * 조회기간 Date/Time ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
		}
		GridData gdinput = new GridData(100,20);
		DateText fromDate = new DateText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER  );
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
        	b2.setText("2시간");
        	b2.setFont(font21);
        	Button b1 = new Button(rGroup, SWT.RADIO);
        	b1.setText("1시간");
        	b1.setFont(font21);
        	Button b30 = new Button(rGroup, SWT.RADIO);
        	b30.setText("30분");
        	b30.setFont(font21);
        	Button b10 = new Button(rGroup, SWT.RADIO);
        	b10.setText("10분");
        	b10.setFont(font21);
        	b1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        	b2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        	b30.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        	b10.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        	b2.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime() ;
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -7200); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(timeFmt.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(timeFmt.format(todt ) );
    				
    			}
			});
        	b1.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime() ;
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -3600); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(timeFmt.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(timeFmt.format(todt ) );
    				
    			}
			});
        	b30.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime() ;
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -1800); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(timeFmt.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(timeFmt.format(todt ) );
    				
    			}

			});
        	b10.addSelectionListener(new SelectionAdapter() {
    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				Timestamp todt = findMoteinfo.getLasTime() ;
    				
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(todt.getTime()); 
    				cal.add(Calendar.SECOND, -600); 
    				
    				Timestamp fmdt = new Timestamp(cal.getTime().getTime());
    				fromDate.setText(dateFmt1.format(fmdt ) );
    				fromTm.setText(timeFmt.format(fmdt ) );
    				toDate.setText(dateFmt1.format(todt ) );
    				toTm.setText(timeFmt.format(todt ) );
    				
    			}

			});

        	b10.setSelection(true);
        	
        }
		
		{
			Button b = new Button(composite_2, SWT.PUSH);
			b.setFont(font2);
			b.setText(" Search ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String sfrom = fromDate.getText() + " " + fromTm.getText() ;
					String sto = toDate.getText() + " " + toTm.getText() ;
					try {
						Timestamp.valueOf(sfrom) ;
						Timestamp.valueOf(sto) ;
					} catch (Exception e2) {
						MessageDialog.openError(parent.getShell(), "날짜오류", "날짜 입력을 바르게하세요.") ;
						return ;
					}

					retriveData(sfrom, sto);
				}
			}); 
		}
		{
			Button b = new Button(composite_2, SWT.PUSH);
			b.setFont(font2);
			b.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
			b.setText(" 파일저장 ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PocMain.exportTable(tableViewer, 1);
				}
			}); 
 
		}

		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginRight = 20;
		gl_composite_3.marginLeft = 20;
//		gl_composite_3.marginBottom = 5;
		
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION |SWT.VIRTUAL );
		
		tableViewer.setUseHashlookup(true);
		

		bottoml = new Label(composite, SWT.NONE) ;
		GridData gd_blabel = new GridData(SWT.CENTER, SWT.CENTER, true, false );
		gd_blabel.heightHint = 50 ;
		bottoml.setLayoutData(gd_blabel);
		bottoml.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		bottoml.setFont(font3);
//		bottoml.setSize(500, 30);

		
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font3);
		table.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		
		TableViewerColumn tv_dummy = new TableViewerColumn(tableViewer, SWT.NONE);
		tv_dummy.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return null;
			}
		});

		TableColumn tc_dummy = tv_dummy.getColumn();
		tc_dummy.setAlignment(SWT.CENTER);
		tc_dummy.setWidth(0);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				
				return element == null ? "" :((MoteHist)element).getDispNm() ;
			}
		});

		TableColumn tblclmnNewColumn1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn1.setAlignment(SWT.CENTER);
		tblclmnNewColumn1.setWidth(80);
		tblclmnNewColumn1.setText("ID");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				
				return element == null ? "" : dateFmt1.format( ((MoteHist)element).getTm()) ;
			}
		});
		
		TableColumn tblclmnNewColumn2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn2.setAlignment(SWT.CENTER);
		tblclmnNewColumn2.setWidth(150);
		tblclmnNewColumn2.setText("Date");

		TableViewerColumn tvc_time = new TableViewerColumn(tableViewer, SWT.NONE);
		tvc_time.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				
				return element == null ? "" :  dateFmt2.format( ((MoteHist)element).getTm()).substring(0, 10) ;
			}
		});
		TableColumn tcl_time = tvc_time.getColumn();
		tcl_time.setAlignment(SWT.CENTER);
		tcl_time.setWidth(150);
		tcl_time.setText("Time");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				String tmp = "";
				tmp = (((MoteHist)element).getTemp() != 0) ? "Active" : "Inactive";

//				switch (((MoteInfo)element).getAct()) {
//				case 2:
//					tmp = "Active";
//					break;
//
//				case 1:
//					tmp = "Sleep";
//					break;
//
//				default:
//					tmp = "Inactive";
//					break;
//				}
				return element == null ? "" :tmp  ;
			}
		});
		TableColumn tblclmnNewColumn3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn3.setAlignment(SWT.CENTER);
		tblclmnNewColumn3.setWidth(150);
		tblclmnNewColumn3.setText("Status");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" :  String.format("%,2.0f", ((MoteHist)element).getTemp() )   ;
			}
		});
		TableColumn tblclmnNewColumn4 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn4.setAlignment(SWT.CENTER);
		tblclmnNewColumn4.setWidth(150);
		tblclmnNewColumn4.setText("Strain");


		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return "";
			}
		});
		TableColumn tblclmnNewColumn7 = tableViewerColumn_7.getColumn();
		tblclmnNewColumn7.setAlignment(SWT.CENTER);
		tblclmnNewColumn7.setWidth(150);
		tblclmnNewColumn7.setText("Location");

		tableViewer.setContentProvider(new ContentProvider());

//		Timestamp time_c = em.createQuery("select max(t.tm) from MoteInfo t", Timestamp.class).getSingleResult() ;
//		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;
		todt = findMoteinfo.getLasTime() ;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -600); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		fromDate.setText(dateFmt1.format(fmdt ) );
		fromTm.setText(timeFmt.format(fmdt ) );
		toDate.setText(dateFmt1.format(todt ) );
		toTm.setText(timeFmt.format(todt ) );

		String sfrom = dateFmt.format(fmdt ) ;
		String sto = dateFmt.format(todt ) ;
		
		int sno = 0 ;
		if  (cbddown.getSelectionIndex() > 0) sno = Integer.parseInt(cbddown.getText().substring(2)) ;
		retriveData(sfrom, sto);
//		tableViewer.setInput(findMoteinfo.getMoteHists( fmdt, todt, sno));
//		tableViewer.refresh();

	}

	private void retriveData(String sfrom, String sto ) {
		
		try {
			Timestamp ts_dt = Timestamp.valueOf(sfrom) ;
			ts_dt = Timestamp.valueOf(sto) ;
		} catch (Exception e2) {
			MessageDialog.openError(parent.getShell(), "날짜오류", "날짜 입력을 바르게하세요.") ;
			return ;
		}
		int sno = 0 ;
		if  (cbddown.getSelectionIndex() > 0) sno = Integer.parseInt(cbddown.getText().substring(2)) ;
		parent.getShell().setCursor( busyc);
		tableViewer.setInput(findMoteinfo.getMoteHists( sfrom, sto, sno ));
		tableViewer.refresh();
		bottoml.setText(String.format("%,d 건",tableViewer.getTable().getItemCount()) );
		bottoml.pack();	
		parent.getShell().setCursor( curc);

	}
	private Timestamp time_s = Timestamp.valueOf("1900-01-01 00:00:00") ;
	private Timestamp fmdt, todt, time_c = time_s ;

}
