package poc.poscoTR.part;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import poc.poscoTR.model.FindMoteInfo;
import poc.poscoTR.model.MoteConfig;
import poc.poscoTR.model.MoteStatus;

import org.json.simple.JSONObject;

public class SensorPos  {

	public SensorPos(Composite parent, int style) {
		// TODO Auto-generated constructor stub
		postConstruct(parent);
	}
	EntityManager em = PocMain.emf.createEntityManager();

	float levelScale = 1.0f;

	SensorWidget[] sWidget = new SensorWidget[8] ;
	public static SensorWidget dragWidget ;
	
	MoteConfig moteConfig  ;
	FindMoteInfo findMoteinfo = new FindMoteInfo();
	java.util.List<MoteStatus> sList ;
	
	Cursor handc = new Cursor(Display.getCurrent(),SWT.CURSOR_HAND);

	@PostConstruct
	public void postConstruct(Composite parent) {
		refreshConfig();
		moteConfig = PocMain.MOTECNF ;
//		ImageDescriptor map = ImageDescriptor.createFromFile(this.getClass(),"images/map.png");
		Image slice_page = new Image(parent.getDisplay(),"images/slice_page4.png");	
		Image image = new Image(parent.getDisplay(),"images/map.png"); 
		
		Image image2 = new Image(Display.getCurrent(),"images/icon_active.png");

//		GridLayout gl_parent = new GridLayout(1, false);
//		gl_parent.marginTop = 20;
//		gl_parent.horizontalSpacing = 10;
//		gl_parent.marginWidth = 0;
//		gl_parent.marginHeight = 0;
//		gl_parent.verticalSpacing = 0;
//		parent.setLayout(gl_parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 10;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
// MAP TITLE
		Composite composite_t = new Composite(composite, SWT.NONE );
		composite_t.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		GridData gd_Composite_t = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_Composite_t.heightHint = 120;
		composite_t.setLayoutData(gd_Composite_t);

		composite_t.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		{
			Label lbl = new Label(composite_t, SWT.FILL);
			lbl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
			lbl.setImage(slice_page);
			lbl.pack();
		}
/*		
		Label lbltitle = new Label(composite_t, SWT.CENTER);
		lbltitle.setFont(new Font(null, "���� ���", 24, SWT.BOLD));
		lbltitle.setText("Setup Stand Position");
		lbltitle.setBounds(30, 50, 300, 50);
		lbltitle.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
*/		
		
// MAP ����
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);

		GridData gd_scrolledComposite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_scrolledComposite_1.widthHint = 1620;
		scrolledComposite_1.setLayoutData(gd_scrolledComposite_1);
		scrolledComposite_1.setExpandVertical(true);
		scrolledComposite_1.setExpandHorizontal(true);

		Composite child = new Composite(scrolledComposite_1, SWT.NONE);
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
		
		Composite composite_2 = new Composite(composite, SWT.BORDER );
		composite_2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		GridData gd_Composite_2 = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gd_Composite_2.widthHint = 1620;
		gd_Composite_2.heightHint = 100;
		composite_2.setLayoutData(gd_Composite_2);
//		Composite_2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		GridLayout gl_composite_2 = new GridLayout(4, false);
		gl_composite_2.marginTop = 15;
		gl_composite_2.marginLeft = 30;
		gl_composite_2.horizontalSpacing = 30;
		gl_composite_2.verticalSpacing = 10;
		gl_composite_2.marginBottom = 10;

		composite_2.setLayout(gl_composite_2);
		composite_2.setFont(new Font(null, "���� ���", 16, SWT.BOLD));

		Label lblsyscode = new Label(composite_2, SWT.BORDER);
		lblsyscode.setText(" SYSTEM Code ");
		lblsyscode.setFont(new Font(null, "���� ���", 14, SWT.BOLD));
		lblsyscode.setAlignment(SWT.RIGHT);
		lblsyscode.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

		Label systext = new Label(composite_2, SWT.BORDER );
		systext.setText(" " + moteConfig.getSysCode() + " ");
		systext.setFont(new Font(null, "���� ���", 14, SWT.BOLD));

		Label lblmeasure = new Label(composite_2, SWT.BORDER);
		lblmeasure.setText(" Time Interval ");
		lblmeasure.setFont(new Font(null, "���� ���", 14, SWT.BOLD));
		lblmeasure.setAlignment(SWT.RIGHT);
		lblmeasure.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		lblmeasure.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
	    Spinner spinner = new Spinner(composite_2, SWT.BORDER | SWT.CENTER);
	    spinner.setMinimum(1);
	    spinner.setMaximum(60);
	    spinner.setSelection(moteConfig.getMeasure());
	    spinner.setIncrement(1);
	    spinner.setSize(80, -1);
	    spinner.setFont(new Font(null, "���� ���", 14, SWT.NORMAL));

/*		
		Label lblmeasure = new Label(composite_2, SWT.NONE);
		lblmeasure.setText("Time Interval");
		lblmeasure.setAlignment(SWT.RIGHT);
		lblmeasure.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		
		Text measuretext = new Text(composite_2, SWT.BORDER | SWT.CENTER	);
		measuretext.setText(String.valueOf(moteConfig.getMeasure()) );
		measuretext.setLayoutData(new GridData(50,10));
*/
		Label lblpass = new Label(composite_2, SWT.BORDER);
		lblpass.setFont(new Font(null, "���� ���", 16, SWT.BOLD));
		lblpass.setText("  PASSWORD  ");
		lblpass.setAlignment(SWT.RIGHT);
		lblpass.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		lblpass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
		Text passwordField = new Text(composite_2, SWT.SINGLE | SWT.BORDER  | SWT.PASSWORD);
	    passwordField.setEchoChar('*');
	    passwordField.setFont(new Font(null, "���� ���", 14, SWT.BOLD));
	    GridData gd_pss = new GridData(SWT.FILL, GridData.CENTER, false, false, 1, 1);
	    gd_pss.widthHint = 200 ;
	    passwordField.setLayoutData(gd_pss);

		Composite buttonContainer = new Composite(composite_2, SWT.NONE);
		
		buttonContainer.setLayout(new GridLayout(3, false));
		buttonContainer.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));

		{
			Button b = new Button(buttonContainer, SWT.PUSH);
			System.out.println(passwordField.getText());
			
			b.setFont(new Font(null, "���� ���", 16, SWT.BOLD));
			b.setEnabled(false);
			b.setText(" SAVE ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					int lmeas = spinner.getSelection()  ;

					if ( sendMeasur( lmeas + "" ) == 1 ) {
						moteConfig.setSysCode(systext.getText());
						moteConfig.setMeasure( (short) lmeas  ); 
						em.getTransaction().begin();
						em.merge(moteConfig);
						sList.forEach(m -> em.merge(m));
						em.getTransaction().commit();
						MessageDialog.openInformation(parent.getShell(), "Save Infomation", "�����Ǿ����ϴ�.") ;
					} else {
						spinner.setSelection(moteConfig.getMeasure());
						MessageDialog.openError(parent.getShell(), "Save Infomation", "ó���� ������ �߻��Ͽ����ϴ�!") ;

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
			Button b = new Button(buttonContainer, SWT.PUSH);
			b.setFont(new Font(null, "���� ���", 16, SWT.BOLD));
			b.setText(" CANCEL ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					refreshConfig();
					moteConfig.setSysCode(systext.getText());
					spinner.setSelection(moteConfig.getMeasure());

					reLocate();
				}
			}); 
		}

		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer()};
//		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		sList = 
		 em.createNamedQuery("MoteStatus.sensorList", MoteStatus.class).getResultList();
		String sloc[] = sList.stream().map(a -> "S"+String.format("%02d",a.getSensorNo() )).toArray(String[]::new) ;

		for (int i = 0 ;i < sloc.length; i++) {
			sWidget[i] = new SensorWidget(child, sList.get(i));
		}
		
		ImageData imageData = image.getImageData();

		Label lblFloorImg = new Label(child, SWT.NONE);
		lblFloorImg.setBounds(50, 0, imageData.width, imageData.height);
		lblFloorImg.setSize(imageData.width, imageData.height);
		lblFloorImg.setBackgroundImage(image);
		
		DropTarget dropTarget_1 = new DropTarget(child, DND.DROP_MOVE);
		dropTarget_1.setTransfer(types);
		dropTarget_1.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				int x = (int) (child.toControl(event.x, event.y).x / levelScale);
				int y = (int) (child.toControl(event.x, event.y).y / levelScale);
				int ix =  dragWidget.getIdx();
				
				dragWidget.setLocation( (int) (x * levelScale) -65, (int) (y * levelScale) - 65);
				dragWidget.mote.setXy(x, y);
//				moteConfig.setXy(ix, x, y);
			}
		});
		dropTarget_1.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer()});

		ImageData xx = image2.getImageData();
		xx.transparentPixel = xx.getPixel(0, 0);
		image2 = new Image(null, xx);
		reLocate();
		
	}
	private void refreshConfig( ) {
		em.clear();
		sList =	 em.createNamedQuery("MoteStatus.sensorList", MoteStatus.class).getResultList();
//		moteConfig = em.createQuery("select m from MoteConfig m ", MoteConfig.class).getSingleResult() ;

	}

	private void reLocate() {
		for (int i = 0 ;i < sList.size() ; i++) {
			Point point = sList.get(i).getXy() ;
			if (point.x <= 0) point.x = 100 ;
			if (point.y <= 0) point.y = 100 ;
			sWidget[i].setLocation( (int)( point.x * levelScale ) -65 , (int)( point.y * levelScale - 65) );
		}
		
	}
	private int sendMeasur(String meas ) {
        String s = System.getenv("MONIP") ;
        if (s == null) s = "localhost" ;
        System.out.println("MONIP:"+s);

		String url="http://"+ s + ":9977?meas=" + meas ;

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

	private int sendZero( ) {
        String s = System.getenv("MONIP") ;
        if (s == null) s = "localhost" ;
        System.out.println("MONIP:"+s);

		String url="http://"+ s + ":9977/zero";

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