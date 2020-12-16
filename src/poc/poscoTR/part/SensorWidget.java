package poc.poscoTR.part;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import poc.poscoTR.model.MoteStatus;

@SuppressWarnings("serial")
public class SensorWidget extends Composite {

	Image image2 = new Image(Display.getCurrent(),"images/icon_active.png" );
	String sloc ;
	int idx ;
	MoteStatus  mote;
	
	public MoteStatus getMote() {
		return mote;
	}

	public void setMote(MoteStatus mote) {
		this.mote = mote;
	}

	public SensorWidget getDrag() { return this; }
	
	public SensorWidget(Composite parent, int idx, String text) {
		super(parent, SWT.NONE);
		this.setSize(120, 120);
		this.setBackgroundImage(image2);
		this.sloc = text ;
		this.idx  = idx ;
		
		Label lbl = new Label(this, SWT.CENTER);
		lbl.setFont(new Font(null, "¸¼Àº °íµñ", 18, SWT.BOLD));
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		lbl.setText(text);
		lbl.setBounds(35, 40, 50, 30);
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		DragSource dragSource = new DragSource(lbl, DND.DROP_MOVE);
		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer()};
		dragSource.setTransfer(types);
		dragSource.addDragListener(new DragSourceListener() {
			@Override
            public void dragStart(DragSourceEvent event) {
				event.doit = true ;
            }

			@Override
			public void dragSetData(DragSourceEvent event) {
				SensorPos.dragWidget =  getDrag();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
			
			}
        });
	}

	public SensorWidget(Composite parent, MoteStatus sensor) {
		super(parent, SWT.NONE);
		this.setMote(sensor);
		this.setSize(87, 82);
		this.setBackgroundImage(image2);
		this.sloc = sensor.getSensorNm() ;
		
		Label lbl = new Label(this, SWT.CENTER);
		lbl.setFont(new Font(null, "¸¼Àº °íµñ", 18, SWT.BOLD));
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		lbl.setText(sloc);
		lbl.setBounds(10, 30, 60, 30);
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		DragSource dragSource = new DragSource(lbl, DND.DROP_MOVE);
		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer()};
		dragSource.setTransfer(types);
		dragSource.addDragListener(new DragSourceListener() {
			@Override
            public void dragStart(DragSourceEvent event) {
				event.doit = true ;
            }

			@Override
			public void dragSetData(DragSourceEvent event) {
				SensorPos.dragWidget =  getDrag();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
			
			}
        });
	}

	public String getSloc() {
		return this.sloc ;
	}

	public int getIdx() {
		return idx;
	}

}
