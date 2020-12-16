package poc.poscoTR.part;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import poc.poscoTR.model.MoteStatus;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class NewMoteDlg extends Dialog {

	private MoteStatus mote = new MoteStatus();
	private boolean modflag = true;
	private Text txtSeq ;
	private Text txtSensorNo ;
	private Text txtDesc , txtLoc;
	private Text txtMax , txtMin, txtSval ;
	private DateText txtBattdt ;
	private Button buttonS , buttonR , btnNo, btnYes;
	private Spinner spTval;
	private Combo cbloc ;
	private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");	
	final Font font = SWTResourceManager.getFont("Calibri", 14, SWT.NORMAL);
	final Font fonth = SWTResourceManager.getFont("맑은 고딕", 13, SWT.NORMAL);

	protected NewMoteDlg(Shell parentShell) {
		super(parentShell);
		
		// TODO Auto-generated constructor stub
	}
	protected NewMoteDlg(Shell parentShell, boolean flag) {
		super(parentShell);
		this.modflag = flag;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setFont(SWTResourceManager.getFont("나눔고딕코딩", 12, SWT.NORMAL));
		// TODO Auto-generated method stub
		Composite container = (Composite)super.createDialogArea(parent);

        GridLayout layout = new GridLayout(2, false);
        layout.marginRight = 5;
        layout.marginLeft = 10;
        
        container.setLayout(layout);

        Label lblSeq = new Label(container, SWT.NONE  );
        lblSeq.setFont(font);
        lblSeq.setText("Seq:");
        lblSeq.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        txtSeq = new Text(container, SWT.BORDER);
        txtSeq.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        txtSeq.setFont(font);

        txtSeq.addModifyListener(e -> {
            Text textWidget = (Text) e.getSource();
            mote.setSeq(Integer.parseInt(textWidget.getText()));
        });
        txtSeq.setEnabled(modflag);

        Label lblSno = new Label(container, SWT.NONE  );
        lblSno.setFont(font);
        lblSno.setText("Sensor No:");
        lblSno.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        txtSensorNo = new Text(container, SWT.BORDER);
        txtSensorNo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        txtSensorNo.setFont(font);

        Label lblDesc = new Label(container, SWT.NONE);
        lblDesc.setText("Description:");
        lblDesc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDesc.setFont(font);

        txtDesc = new Text(container, SWT.BORDER);
        txtDesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtDesc.setFont(font);
        txtDesc.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	mote.setDesc(textWidget.getText());
        });

        Label label = new Label(container, SWT.NONE);
        label.setText("Mote 구분:");
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setFont(fonth);
        // Group
        Group  genderGroup = new Group (container, SWT.NONE);
        genderGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
        genderGroup.setFont(new Font(null, "", 1, SWT.NORMAL));
 
        // Radio - mss
        buttonS = new Button(genderGroup, SWT.RADIO);
        buttonS.setText("Sensor");        
        buttonS.setFont(font);
        // Radio - mrs
        buttonR = new Button(genderGroup, SWT.RADIO);
        buttonR.setText("Repeater");
        buttonR.setFont(font);
        
        Label label_1 = new Label(container, SWT.NONE);
        label_1.setFont(fonth);
        label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_1.setText("예비품여부:");
        
        Group group = new Group(container, SWT.NONE);
        group.setLayout(new RowLayout(SWT.HORIZONTAL));
        group.setFont(SWTResourceManager.getFont( "", 1, SWT.NORMAL));
        
        btnNo = new Button(group, SWT.RADIO);
        btnNo.setFont(font);
        btnNo.setText("No");
        
        btnYes = new Button(group, SWT.RADIO);
        btnYes.setFont(font);
        btnYes.setText("Yes");
        
        Label label_2 = new Label(container, SWT.NONE);
        label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_2.setText("배터리교체일:");
        label_2.setFont(fonth);
        
        txtBattdt = new DateText(container, SWT.BORDER);
        txtBattdt.setFont(font);
        txtBattdt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label_3 = new Label(container, SWT.NONE);
        label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_3.setText("설치위치:");
        label_3.setFont(fonth);
        
//        txtLoc = new Text(container, SWT.BORDER);
//        txtLoc.setTextLimit(3);
//        txtLoc.setFont(font);
//        txtLoc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        cbloc = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
        cbloc.setFont(font);
        cbloc.setItems( PocMain.locname   );

        cbloc.select(0);
        cbloc.setTextLimit(3);
        cbloc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label_4 = new Label(container, SWT.NONE);
        label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_4.setText("Strain 기준값:");
        label_4.setFont(fonth);
        
        txtSval = new Text(container, SWT.BORDER);
        txtSval.setFont(font);
        txtSval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label_5 = new Label(container, SWT.NONE);
        label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_5.setText("1 Charge 초:");
        label_5.setFont(fonth);
        
	    spTval = new Spinner(container, SWT.BORDER | SWT.CENTER);
	    spTval.setMaximum(3600);
	    spTval.setIncrement(60);
	    spTval.setFont(font);
	    spTval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label_6 = new Label(container, SWT.NONE);
        label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_6.setText("Strain 최대값:");
        label_6.setFont(fonth);
        
        txtMax = new Text(container, SWT.BORDER);
        txtMax.setFont(font);
        txtMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label_7 = new Label(container, SWT.NONE);
        label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_7.setText("Strain 최소값:");
        label_7.setFont(fonth);
        
        txtMin = new Text(container, SWT.BORDER);
        txtMin.setFont(font);
        txtMin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        setValue();

		return container ;
	}
	
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "저장", true).setFont(fonth);
        createButton(parent, IDialogConstants.CANCEL_ID, "취소", false).setFont(fonth);

//        parent.getShell().pack();
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 500);
    }

    @Override
    protected void okPressed() {
    	if (modflag) mote.setSeq(Integer.parseInt(txtSeq.getText()));
    	mote.setSensorNo(Integer.parseInt(txtSensorNo.getText()));
    	mote.setDesc(txtDesc.getText());
    	mote.setGubun(buttonS.getSelection() ? "S" : "R");
    	mote.setSpare(btnYes.getSelection() ? "Y" : "N");
    	if ( txtBattdt.getText().replaceAll("-", "").matches("\\d{8}") )
    		mote.setBattDt(txtBattdt.getText().replaceAll("-", ""));
    	mote.setLoc(cbloc.getText());
    	mote.setSval(Integer.parseInt(txtSval.getText()));
    	mote.setTval(Integer.parseInt(spTval.getText()));
    	mote.setMaxval(Integer.parseInt(txtMax.getText()));
    	mote.setMinval(Integer.parseInt(txtMin.getText()));
        super.okPressed();
    }
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Mote 등록");
    }
    
	public MoteStatus getMote() {
		return mote;
	}

	public void setMote(MoteStatus mote) {
		this.mote = mote;
	}
	
	private void setValue() {
        txtSeq.setText(String.valueOf(mote.getSeq()));
        txtSensorNo.setText(String.valueOf(mote.getSensorNo()));

        txtDesc.setText(mote.getDesc());
        buttonS.setSelection(mote.getGubun().equals("S"));
        buttonR.setSelection(mote.getGubun().equals("R"));
        btnNo.setSelection(mote.getSpare().equals("N"));
        btnYes.setSelection(mote.getSpare().equals("Y"));

        if (mote.getBattDt().length() == 8) txtBattdt.setText( mote.getBattDt() );
	    spTval.setSelection(mote.getTval());
//	    txtLoc.setText( (mote.getLoc() == null ? "" : mote.getLoc()) );
	    cbloc.setText( (mote.getLoc() == null ? "" : mote.getLoc()) );
	    txtSval.setText("" + mote.getSval());
	    txtMax.setText("" + mote.getMaxval());
	    txtMin.setText("" + mote.getMinval());
        
	}
}
