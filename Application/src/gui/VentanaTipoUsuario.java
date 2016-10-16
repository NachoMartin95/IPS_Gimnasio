package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaTipoUsuario extends JFrame {

	private JPanel contentPane;
	private JTextField textField;



	/**
	 * Create the frame.
	 */
	public VentanaTipoUsuario(Main m) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 202);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblIdUsuario = new JLabel("ID Usuario");
		lblIdUsuario.setBounds(56, 48, 82, 14);
		panel.add(lblIdUsuario);
		
		textField = new JTextField();
		textField.setBounds(170, 45, 209, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Aceptar");
		JFrame vtu = this;
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().toString().equals("")){
					m.setUser(textField.getText().toString());
					vtu.dispose();
					m.frame.setVisible(true);				
				}
					
			}
		});
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
	}
}
