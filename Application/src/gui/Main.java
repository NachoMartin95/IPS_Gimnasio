package gui;

import java.awt.EventQueue;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;

import reservation.Agenda;
import reservation.Instalacion;
import reservation.Reservation;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
	
	//Constantes para rellenar el calendario con los colores
	final static int BLANK = 0;
	final static int ADMIN = 1;
	final static int SOCIO = 2;

	private JFrame frame;
	private JTable table;
	Agenda agenda;
	private JLabel lblFecha;
	JComboBox<Instalacion> cbInstalaciones ;
	
	private SelectedDate sD = new SelectedDate();
	private VentanaReserva vR;
	private Main m = this;

	/**
	*
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * inicializa los componentes ademas de añadir algunos gatillos
	 */
	protected void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1600, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setLocationRelativeTo(null);
		cbInstalaciones = new JComboBox<Instalacion>();
		cbInstalaciones.setModel(new DefaultComboBoxModel(Instalacion.values()));		
		frame.getContentPane().add(cbInstalaciones, BorderLayout.SOUTH);
		cbInstalaciones.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				fillCalendar();				
			}		
		});		
		cbInstalaciones.setSelectedIndex(0);
		
		
		initializeCalendar();
		
		lblFecha = new JLabel();
		updateDate();
		frame.getContentPane().add(lblFecha, BorderLayout.NORTH);
		
		JButton btnLessWeek = new JButton("<");
		btnLessWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agenda.oneWeekLess();
				fillCalendar();
				updateDate();
			}
		});
		frame.getContentPane().add(btnLessWeek, BorderLayout.WEST);
		
		JButton btnPlusWeek = new JButton(">");
		btnPlusWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agenda.oneWeekMore();
				fillCalendar();
				updateDate();
			}
		});
		frame.getContentPane().add(btnPlusWeek, BorderLayout.EAST);
		
		
	}
	
	/**
	*Actualiza la fecha mostrada encima del calendario con la ayuda de la Agenda
	*/
	private void updateDate(){	
		Calendar auxCalendar = (Calendar) agenda.getCalendar().clone();
		auxCalendar.add(Calendar.DAY_OF_MONTH, (auxCalendar.get(Calendar.DAY_OF_WEEK)-2)*-1);
		String prev = (auxCalendar.get(Calendar.DAY_OF_MONTH)+"-"+auxCalendar.get(Calendar.MONTH)+"-"+auxCalendar.get(Calendar.YEAR));
		auxCalendar.add(Calendar.DAY_OF_MONTH, 6);
		lblFecha.setText("Desde: "+prev + " hasta: "+auxCalendar.get(Calendar.DAY_OF_MONTH)+"-"+auxCalendar.get(Calendar.MONTH)+"-"+auxCalendar.get(Calendar.YEAR)+"  -Administracion: Azul, Socio: Verde");
	}
	
	private class SelectedDate extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			
			if(!table.getModel().getValueAt(row, col).equals(BLANK))
				System.out.println("Instalacion ocupada");
			else{			
				Calendar c = (Calendar) agenda.getCalendar().clone();
				c.add(Calendar.DAY_OF_MONTH, col-1);
				String dia = (c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
				
				vR = new VentanaReserva(m, dia, row);
				vR.setVisible(true);
			}
		}
	}
	
	/*
	*Iniicializa el calendario y lo adapta a la pantalla
	*/
	private void initializeCalendar() {
		agenda = new Agenda();
		table = new JTable(24,8);
		table.addMouseListener(sD);	
		table.setRowSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		final JScrollPane scrollPane = new JScrollPane(table);
        table.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e)
            {
                table.setRowHeight(16);
                Dimension p = table.getPreferredSize();
                Dimension v = scrollPane.getViewportBorderBounds().getSize();
                if (v.height > p.height)
                {
                    int available = v.height - 
                        table.getRowCount() * table.getRowMargin();
                    int perRow = available / table.getRowCount();
                    table.setRowHeight(perRow);
                }
            }
        });
		
		for(int i = 0;i < table.getColumnCount();i++)table.getColumnModel().getColumn(i).setCellRenderer(new ColorRenderer());
		
		fillCalendar();		
		
		frame.getContentPane().add(scrollPane , BorderLayout.CENTER);
	}
	
	/**
	*Rellena el calendario con los datos proporcionados por la agenda y el combobox
	*
	*/
	void fillCalendar() {
		Instalacion currentInstalacion = (Instalacion) cbInstalaciones.getSelectedItem();
		
		
		for(int i = 0;i < table.getColumnCount();i++)
			for(int j = 0;j < table.getRowCount();j++)
				table.setValueAt(new Integer(BLANK), j, i);	
		agenda.loadCurrentWeek();
		List<Reservation> reservations = agenda.getReservations();
		
		for(Reservation res: reservations){
			if(currentInstalacion.equals(res.getInstalacion())){
				for(int i = res.getStartHour();i < res.getEndHour();i++)
				table.setValueAt((res.isAdministracion())?new Integer(ADMIN):new Integer(SOCIO), i, res.getWeekDay()+1);
				}			
		}
		
		for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt("From "+i +":00 to "+i+":59", i, 0);
        }
		
	}
	
	public void close(){
		vR.dispose();
		fillCalendar();
	}

}

@SuppressWarnings("serial")
class ColorRenderer extends DefaultTableCellRenderer{
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	
    	Component cell = super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
        if( value instanceof Integer ) {
            Integer amount = (Integer) value;
            if(amount == Main.BLANK){//Vacia
                cell.setBackground( Color.white);
                cell.setForeground(Color.white);
            }
            else if(amount == Main.ADMIN){//admin
                cell.setBackground( Color.blue);
                cell.setForeground(Color.blue);   
            }
            else if(amount == Main.SOCIO){//socio
            	cell.setBackground( Color.green );
                cell.setForeground(Color.green);           	
            }
        }
        return cell;
    }
}


