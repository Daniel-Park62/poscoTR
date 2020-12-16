package poc.poscoTR.part;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.WINDOWPLACEMENT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import poc.poscoTR.model.MoteConfig;
import poc.poscoTR.model.MoteStatus;

public class RegMote {

	private static class ContentProvider implements IStructuredContentProvider {
		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object input) {
			//return new Object[0];
			ArrayList<MoteStatus> arrayList = (ArrayList<MoteStatus>)input;
			return arrayList.toArray();
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
	
	EntityManager em = PocMain.emf.createEntityManager();
	
//	Bundle bundle = FrameworkUtil.getBundle(this.getClass());
    
//    URL url8 = FileLocator.find(bundle, new Path("images/slice_page5.png"), null);
    Image slice_page = new Image(Display.getCurrent(), "images/slice_page5.png");
    
    Label lblApActive;
    Label lblApInactive;
    Label lblTagActive;
    Label lblTagInactive;
    Label lblAlertActive;
    Label lblAlertInactive;
    Label lblDate, lblTime;
    
    private Table table;
    private TableViewer tableViewer;
    ArrayList<MoteStatus> tempList ;
    public static final String[] PROPS = { "", "", "", "","", "DESC","TYPE","SPARE","BATTDT" };
    
	public RegMote(Composite parent, int style) {

		ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginTop = 20;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginBottom = 5;
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Composite composite_15 = new Composite(composite, SWT.NONE);
		GridData gd_composite_15 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_15.heightHint = 120;
		composite_15.setLayoutData(gd_composite_15);
		composite_15.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Label lblNewLabel_4 = new Label(composite_15, SWT.NONE);
		lblNewLabel_4.setBounds(0, 0, 1647, 100);
		lblNewLabel_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_4.setImage(slice_page);
/*
		RegMote.addKeyListener(new keyAdapter() { 
			public void KeyPressed(KeyEvent e) {
				if ( e.keyCode == SWT.F15 ) refreshSensorList();  
			}
		});
*/		
		Composite modbutton = new Composite(composite, SWT.NORMAL);
		GridLayout gl_layout = new GridLayout(4, false);
		gl_layout.marginLeft = 15;
		modbutton.setLayout(gl_layout);
		modbutton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		modbutton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		{
			Button b = new Button(modbutton, SWT.ON_TOP);
			b.setFont(SWTResourceManager.getFont( "Consolas", 16, SWT.NORMAL));
			b.setText(" Add ");
			b.setSize(140, 50);
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					NewMoteDlg newmote = new NewMoteDlg(parent.getShell() ) ;
					if (newmote.open() == Window.OK) {
						if (newmote.getMote().getSeq() > 0 ) {
							em.getTransaction().begin();
							em.merge(newmote.getMote());
							em.getTransaction().commit();
							refreshSensorList();
						} else {
							MessageDialog.openError(parent.getShell(), "Mote 등록", "Seq 0 불가합니다.") ;
						}
						
					}
				}
			}); 
		}
		{
			Button b = new Button(modbutton, SWT.ON_TOP);
			b.setFont(SWTResourceManager.getFont("Consolas", 16, SWT.NORMAL));
			b.setText(" Modify ");
			b.setSize(140, 50);
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = tableViewer.getTable().getSelectionIndex() ;
					
					if (index != -1 ) {
						MoteStatus mote = tempList.get(index) ;
						NewMoteDlg newmote = new NewMoteDlg(parent.getShell() , false) ;
						newmote.setMote(mote);
						if (newmote.open() == Window.OK) {
							em.getTransaction().begin();
							if (!em.contains(mote)) {
							    mote = em.merge(mote);
							}
							em.getTransaction().commit();
							MessageDialog.openInformation(parent.getShell(), "Mote 수정", "수정되었습니다.!") ;
							refreshSensorList();
						}

					}
				}
			}); 
		}
		{
			Button b = new Button(modbutton, SWT.ON_TOP);
			b.setFont(SWTResourceManager.getFont( "Consolas", 16, SWT.NORMAL));
			b.setText(" Delete ");
			b.setSize(140, 50);
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = tableViewer.getTable().getSelectionIndex() ;
					
					if (index != -1 ) {
						em.getTransaction().begin();
						MoteStatus mote = tempList.get(index) ;
						if ( MessageDialog.openConfirm(parent.getShell(),"확인", mote.getDesc() + " : 삭제하시겠습니까?"  )) {
							if (!em.contains(mote)) {
							    mote = em.merge(mote);
							}
							em.remove(mote);
							em.getTransaction().commit();
							MessageDialog.openInformation(parent.getShell(), "Mote 삭제", "삭제되었습니다.!") ;
							refreshSensorList();
						}

					}
				}
			}); 
		}

		{
			Button b = new Button(modbutton, SWT.ON_TOP);
			b.setFont(SWTResourceManager.getFont( "Consolas", 16, SWT.NORMAL));
			b.setText(" Out Limit clear ");
			b.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true,true)) ;
			b.setSize(-1, 50);
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = tableViewer.getTable().getSelectionIndex() ;
					
					if (index != -1 ) {
						em.getTransaction().begin();
						MoteStatus mote = tempList.get(index) ;
						if ( MessageDialog.openConfirm(parent.getShell(),"확인", mote.getDesc() + " : Out limit체크를 해제하시겠습니까?"  )) {
							if (!em.contains(mote)) {
							    mote = em.merge(mote);
							}
							mote.setObcnt(0);
							em.getTransaction().commit();
							MessageDialog.openInformation(parent.getShell(), "Out Limit", "해제되었습니다.!") ;
							refreshSensorList();
						}

					}
				}
			}); 
		}
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginRight = 15;
		gl_composite_3.marginLeft = 15;
		gl_composite_3.marginBottom = 5 ;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION );
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(SWTResourceManager.getFont("Calibri", 14, SWT.NORMAL));
		table.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

		tableViewer.setUseHashlookup(true); 

		TableViewerColumn tvcol = new TableViewerColumn(tableViewer, SWT.NONE | SWT.CENTER);
		tvcol.getColumn().setWidth(0);

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE | SWT.CENTER);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(60);
		tvcol.getColumn().setText("SEQ");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(120);
		tvcol.getColumn().setText("Status");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(230);
		tvcol.getColumn().setText("Mac Address");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(100);
		tvcol.getColumn().setText("Sensor ID");


		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.NONE);
		tvcol.getColumn().setWidth(250);
		tvcol.getColumn().setText("Description");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(50);
		tvcol.getColumn().setText("Type");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(60);
		tvcol.getColumn().setText("Spare");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(150);
		tvcol.getColumn().setText("배터리설치일");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(100);
		tvcol.getColumn().setText("설치위치");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER | SWT.WRAP);
		tvcol.getColumn().setWidth(120);
		tvcol.getColumn().setText( "Charge sec");

		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER | SWT.WRAP);
		tvcol.getColumn().setWidth(80);
		tvcol.getColumn().setText( "기준값");
	
		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(60);
		tvcol.getColumn().setText("MAX");
		tvcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcol.getColumn().setAlignment(SWT.CENTER);
		tvcol.getColumn().setWidth(60);
		tvcol.getColumn().setText("MIN");

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new MoteLabelProvider());
		tableViewer.setInput(tempList);
		tableViewer.setColumnProperties(PROPS);
		
	    table.addListener(SWT.MeasureItem,  new Listener() {
	    	@Override
	    	public void handleEvent(Event event) {
	    	event.height = (int)(event.gc.getFontMetrics().getHeight() * 1.5) ;
	    	}

	    });		
	    
		refreshSensorList();
//		table.layout();
		composite_3.layout();
	
		MoteConfig moteConfig  = PocMain.MOTECNF ;

		Composite comp_b = new Composite(composite, SWT.NONE );
		comp_b.setBackground(SWTResourceManager.getColor(250,250,250));
		GridData gd_Composite_2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_Composite_2.heightHint = 120 ;
		gd_Composite_2.minimumHeight = 120 ;
		comp_b.setLayoutData(gd_Composite_2);
//		group_t.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridLayout gl_composite_2 = new GridLayout(1, true);
		gl_composite_2.marginTop = 15;
		gl_composite_2.marginLeft = 15;
		gl_composite_2.marginRight = 15;

		comp_b.setLayout(gl_composite_2);

		Group group_t = new Group(comp_b, SWT.NONE );
		group_t.setBackground(SWTResourceManager.getColor(250,250,250));

		group_t.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		group_t.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridLayout gl_g = new GridLayout(10, false);
		gl_g.horizontalSpacing = 10;
		gl_g.marginTop = 15;
//		gl_composite_2.marginBottom = 5;

		group_t.setLayout(gl_g);
		group_t.setText("Time Interval 변경");


		Label lblsyscode = new Label(group_t, SWT.NONE);
		lblsyscode.setText(" SYSTEM CODE ");
		lblsyscode.setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.BOLD));
		lblsyscode.setAlignment(SWT.RIGHT);
		lblsyscode.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblsyscode.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		Label systext = new Label(group_t, SWT.BORDER );
		systext.setText(" " + moteConfig.getSysCode() + " ");
		systext.setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.BOLD));

		Label lblmeasure = new Label(group_t, SWT.NONE);
		lblmeasure.setText("   Time Interval ");
		lblmeasure.setFont(SWTResourceManager.getFont("Calibri", 14, SWT.BOLD));
		lblmeasure.setAlignment(SWT.RIGHT);
		lblmeasure.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblmeasure.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
	    Spinner spinner = new Spinner(group_t, SWT.BORDER | SWT.CENTER);
	    spinner.setMinimum(1);
	    spinner.setMaximum(60);
	    spinner.setSelection(moteConfig.getMeasure());
	    spinner.setIncrement(1);
	    spinner.setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.NORMAL));
	    spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false , true));
	    spinner.setSize(120, -1);

/*		
		Label lblmeasure = new Label(group_t, SWT.NONE);
		lblmeasure.setText("Time Interval");
		lblmeasure.setAlignment(SWT.RIGHT);
		lblmeasure.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		
		Text measuretext = new Text(group_t, SWT.BORDER | SWT.CENTER	);
		measuretext.setText(String.valueOf(moteConfig.getMeasure()) );
		measuretext.setLayoutData(new GridData(50,10));
*/
		Label lblpass = new Label(group_t, SWT.NONE);
		lblpass.setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.BOLD));
		lblpass.setText("   PASSWORD ");
		lblpass.setAlignment(SWT.RIGHT);
		lblpass.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblpass.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Text passwordField = new Text(group_t, SWT.SINGLE | SWT.BORDER  | SWT.PASSWORD);
	    passwordField.setEchoChar('*');
	    passwordField.setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.BOLD));
	    GridData gd_pss = new GridData(SWT.FILL, GridData.CENTER, false, false, 1, 1);
	    gd_pss.widthHint = 150 ;
	    passwordField.setLayoutData(gd_pss);

		Composite btncontainer = new Composite(group_t, SWT.NONE);
		
		btncontainer.setLayout(new GridLayout(2, false));
		btncontainer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1));
		btncontainer.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		{
			Button b = new Button(btncontainer, SWT.PUSH);
			
			b.setFont(SWTResourceManager.getFont( "Calibri", 16, SWT.BOLD));
			b.setEnabled(false);
			b.setText(" SAVE ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					int lmeas = spinner.getSelection()  ;

					if ( PocMain.sendMeasur( lmeas + "" ) == 1 ) {
						moteConfig.setSysCode(systext.getText());
						moteConfig.setMeasure( (short) lmeas  ); 
						em.getTransaction().begin();
						em.merge(moteConfig);
						em.getTransaction().commit();
						MessageDialog.openInformation(parent.getShell(), "Save Infomation", "수정되었습니다.") ;
					} else {
						spinner.setSelection(moteConfig.getMeasure());
						MessageDialog.openError(parent.getShell(), "Save Infomation", "처리중 오류가 발생하였습니다!") ;

					}
					passwordField.setText("");
				}
			}); 
			
			passwordField.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent evt) {
					b.setEnabled( (passwordField.getText().equals("Passw0rd!")) ) ; 
				}
			});
		}		
		{
			Button b = new Button(btncontainer, SWT.PUSH);
			b.setFont(SWTResourceManager.getFont("Calibri", 16, SWT.BOLD));
			b.setText(" CANCEL ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					refreshSensorList();
					moteConfig.setSysCode(systext.getText());
					spinner.setSelection(moteConfig.getMeasure());

				}
			}); 
		}
		group_t.layout();
		

	}


//	@SuppressWarnings("unchecked")
	public void refreshSensorList() {
	    EntityManager em = PocMain.emf.createEntityManager();
		tempList = new ArrayList<MoteStatus>();

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
        
        TypedQuery<MoteStatus> qMotes = em.createQuery("select t from MoteStatus t order by t.seq ", MoteStatus.class);

        qMotes.getResultList().stream().forEach( t -> tempList.add(t));
        
		em.close();
		// 화면 업데이트

		tableViewer.setInput(tempList);
		tableViewer.refresh();

//		}

	}

	private static class MoteLabelProvider implements ITableLabelProvider {
	  /**
	   * Returns the image
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return Image
	   */
	
	String[] statusNm = {"Inactive","Sleep","Active"} ;
	  public Image getColumnImage(Object element, int columnIndex) {
	    return null;
	  }

	  /**
	   * Returns the column text
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return String
	   */
	  public String getColumnText(Object element, int columnIndex) {
		  MoteStatus mote = (MoteStatus) element;
	    switch (columnIndex) {
	    case 1:
	    	return mote.getSeq()+"";
	    case 2:
	    	return statusNm[mote.getAct()];
	    case 3:
	    	return mote.getMac();
	    case 4:
	    	return mote.getDispNm();
	    case 5:
	    	return mote.getDesc();
	    case 6:
	    	return mote.getGubun() ;
	    case 7:
	    	return mote.getSpare() ;
	    case 8:
	    	return mote.getBattDt().length() < 8 ? mote.getBattDt() + "" : mote.getBattDt().substring(0, 4) + "-" + mote.getBattDt().substring(4, 6) + "-" + mote.getBattDt().substring(6) ;
	    case 9:
	    	return mote.getLoc();
	    case 10:
	    	return mote.getTval()+"";
	    case 11:
	    	return mote.getSval()+"";
	    case 12:
	    	return mote.getMaxval()+"";
	    case 13:
	    	return mote.getMinval()+"";
	    }
	    return null;
	  }

	  /**
	   * Adds a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void addListener(ILabelProviderListener listener) {
	    // Ignore it
	  }

	  /**
	   * Disposes any created resources
	   */
	  public void dispose() {
	    // Nothing to dispose
	  }

	  /**
	   * Returns whether altering this property on this element will affect the
	   * label
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return boolean
	   */
	  public boolean isLabelProperty(Object element, String property) {
	    return false;
	  }

	  /**
	   * Removes a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void removeListener(ILabelProviderListener listener) {
	    // Ignore
	  }
	}

}