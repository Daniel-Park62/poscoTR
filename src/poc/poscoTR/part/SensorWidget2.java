package poc.poscoTR.part;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import poc.poscoTR.model.MoteStatus;

public class SensorWidget2 extends Composite {

	Bundle bundle = FrameworkUtil.getBundle(this.getClass());
//	ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
//	URL url2 = FileLocator.find(bundle, new Path("images/icon_active.png"), null);
//	ImageDescriptor icon_active = ImageDescriptor.createFromURL(url2);
	Image img_active = new Image(Display.getCurrent(), "images/icon_active.png");
//	URL urlin = FileLocator.find(bundle, new Path("images/icon_inactive.png"), null);
//	ImageDescriptor icon_inactive = ImageDescriptor.createFromURL(urlin);
//	Image img_inactive = resourceManager.createImage(icon_inactive);
	Image img_inactive = new Image(Display.getCurrent(), "images/icon_inactive.png");
	String sloc ;
	String temp ;
	String id ;
	int idx ;
	Label lbl_humi ;
	Label lbl_temp ;
	Label lbl ;
	MoteStatus sensor ;
	
	public MoteStatus getSensor() {
		return sensor;
	}

	public void setSensor(MoteStatus sensor) {
		this.sensor = sensor;
	}

	/**
	 * @wbp.parser.constructor
	 */

	public SensorWidget2(Composite parent, MoteStatus sensor ) {
		super(parent, SWT.NONE);
//		this.setSize(87, 82);
//		this.setBackgroundImage(img_active);
		this.sensor = sensor ;
		this.id =  sensor.getSensorNm();
		this.temp = "" ;
//		this.pack();
		this.setLayout(new GridLayout(1,true));
		this.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true,true));
		lbl_temp = new Label(this, SWT.CENTER);
		lbl_humi = new Label(this, SWT.CENTER);
		lbl = new Label(this, SWT.CENTER);

		lbl_temp.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 14, SWT.BOLD));
		lbl_temp.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lbl_temp.setText(String.format("%.2f  กษ", 11.0));
		lbl_temp.setBounds(10, 15, 60, 30);
		lbl_humi.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 14, SWT.BOLD));
		lbl_humi.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lbl_humi.setText(String.format("%.2f %", 11.0));
		lbl_humi.setBounds(10, 15, 60, 30);

		lbl_temp.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl_temp.setLayoutData(new GridData(SWT.CENTER , SWT.FILL, true,true));
		lbl_humi.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl_humi.setLayoutData(new GridData(SWT.CENTER , SWT.FILL, true,true));


		lbl.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 14, SWT.BOLD));
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		lbl.setText(id);
		lbl.setLayoutData(new GridData(SWT.CENTER , SWT.FILL, true,true));

		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

	}
	
	public void setImage(int x) {
		if (x == 2) {
			this.setBackgroundImage(img_active);
		} else {
			this.setBackgroundImage(img_inactive);
		}
	}
	public void setValues() {
		
	}
	public void setTemp(String temp) {
		lbl_temp.setText(temp);

	}


	public void setId(String id) {
		lbl.setText(id);
	}


	public int getIdx() {
		return idx;
	}

}