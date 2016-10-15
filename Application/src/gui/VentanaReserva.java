package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

import database.QueryExecutor;
import reservation.Instalacion;

public class VentanaReserva extends JFrame {

	private JPanel contentPane;
	private JPanel panelBotones;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JPanel panelDatos;
	private JLabel lblSocio;
	private JTextField txSocio;
	private JCheckBox chckbxDiaCompleto;
	private JTextField txHoraInicial;
	private JTextField txHoraFinal;
	
	private Main m;
	private JTextField txDia;
	private String dia;

	/**
	 * Create the frame.
	 */
	public VentanaReserva(Main m, String dia, int hora_inicial) {
		this.m = m;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelBotones(), BorderLayout.SOUTH);
		contentPane.add(getPanelDatos(), BorderLayout.CENTER);
		
		if(hora_inicial == 9){
			txHoraInicial.setText("0" + hora_inicial + ":00:00");
			txHoraFinal.setText(10 + ":00:00");
		}
		else if(hora_inicial < 9){
			txHoraInicial.setText("0" + hora_inicial + ":00:00");
			txHoraFinal.setText("0" + (hora_inicial + 1) + ":00:00");
		}
		else{
			txHoraInicial.setText(hora_inicial + ":00:00");
			txHoraFinal.setText((hora_inicial + 1) + ":00:00");
		}
		this.dia = dia;
		txDia.setText(dia);
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			FlowLayout fl_panelBotones = (FlowLayout) panelBotones.getLayout();
			fl_panelBotones.setAlignment(FlowLayout.RIGHT);
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m.close();			
				}
			});
		}
		return btnCancelar;
	}
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(check()){	
						if(txSocio.getText().equals("")) return;
						guardaReserva();
						System.out.println("Su reserva se ha efectuado con exito");
						m.agenda.updated = false;
						m.fillCalendar();
						m.close();
					}
				}
			});
		}
		return btnAceptar;
	}
	
	public void guardaReserva(){
		int codigo = 0;
		String instalacion = String.valueOf(m.cbInstalaciones.getSelectedItem()).toLowerCase();
		switch(instalacion){
		case "gimnasio":
			codigo = 1;
			break;
		case "p futbol":
			codigo = 2;
			break;
		case "p baloncesto":
			codigo = 3;
			break;
		}
		String query = "INSERT INTO reserva VALUES(" + codigo + ", " + txSocio.getText() + ", '" + dia + "', '" + txHoraInicial.getText() + "', '" + txHoraFinal.getText() + "')";
		QueryExecutor qe = new QueryExecutor();
		try {
			qe.executeInsert(query);
		} catch (Exception e){
		}
	}
	
	/**
	 * Metodo que rellenaremos en el futuro para evitar que se solapen reservas
	 */
	public boolean check(){
		return true;
	}
	
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setLayout(null);
			panelDatos.add(getLblSocio());
			panelDatos.add(getTxSocio());
			panelDatos.add(getChckbxDiaCompleto());
			panelDatos.add(getTxHoraInicial());
			panelDatos.add(getTxHoraFinal());
			panelDatos.add(getTxDia());
		}
		return panelDatos;
	}
	private JLabel getLblSocio() {
		if (lblSocio == null) {
			lblSocio = new JLabel("ID Socio: ");
			lblSocio.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblSocio.setBounds(84, 38, 78, 25);
		}
		return lblSocio;
	}
	private JTextField getTxSocio() {
		if (txSocio == null) {
			txSocio = new JTextField();
			txSocio.setBounds(172, 41, 197, 20);
			txSocio.setColumns(10);
		}
		return txSocio;
	}
	private JCheckBox getChckbxDiaCompleto() {
		if (chckbxDiaCompleto == null) {
			chckbxDiaCompleto = new JCheckBox("Dia completo");
			chckbxDiaCompleto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(chckbxDiaCompleto.isSelected()){
						txHoraInicial.setText("00:00:00");
						txHoraFinal.setText("23:59:59");
						txHoraFinal.setEnabled(false);
						txHoraInicial.setEnabled(false);
					}
					else{
						txHoraFinal.setEnabled(true);
						txHoraInicial.setEnabled(true);
					}
				}
			});
			chckbxDiaCompleto.setBounds(108, 143, 97, 40);
		}
		return chckbxDiaCompleto;
	}
	private JTextField getTxHoraInicial() {
		if (txHoraInicial == null) {
			txHoraInicial = new JTextField();
			txHoraInicial.setBorder(new TitledBorder(null, "Hora inicial", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			txHoraInicial.setBounds(239, 85, 86, 39);
			txHoraInicial.setColumns(10);
		}
		return txHoraInicial;
	}
	private JTextField getTxHoraFinal() {
		if (txHoraFinal == null) {
			txHoraFinal = new JTextField();
			txHoraFinal.setBorder(new TitledBorder(null, "Hora final", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			txHoraFinal.setBounds(239, 143, 86, 39);
			txHoraFinal.setColumns(10);
		}
		return txHoraFinal;
	}
	private JTextField getTxDia() {
		if (txDia == null) {
			txDia = new JTextField();
			txDia.setBorder(new TitledBorder(null, "Dia", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			txDia.setBounds(108, 85, 86, 39);
			txDia.setColumns(10);
		}
		return txDia;
	}
}
