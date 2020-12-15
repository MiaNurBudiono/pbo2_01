/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toko.komputer.barang;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import toko.komputer.pengaturan.Koneksi;

/**
 *
 * @author User
 */
public class BarangView extends javax.swing.JInternalFrame {

    /**
     * Creates new form BarangView
     */
    public BarangView() {
        initComponents();
    }
    
    PreparedStatement pst;
    ResultSet rs;
    String sql, status;
    Connection conn = new Koneksi().getKoneksi();

    private void tampil_data() {
        try {
            String[] judul = {"Kode Barang", "Nama Barang", "Kategori", "Harga", "Stok"};
            DefaultTableModel dtm = new DefaultTableModel(null, judul);
            tabelBarang.setModel(dtm);

            if (textCari.getText().isEmpty()) {
                sql = "select * from tb_barang";
                pst = conn.prepareStatement(sql);
            } else {
                sql = "select * from tb_barang where nama_barang like '%" + textCari.getText() + "%'";
                pst = conn.prepareStatement(sql);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
                dtm.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    
    private void ulang() {
        textKodeBarang.setEnabled(false);
        textNamaBarang.setEnabled(false);
        textKategori.setEnabled(false);
        textHarga.setEnabled(false);
        textStok.setEnabled(false);
        buttonSimpan.setEnabled(false);
        buttonHapus.setEnabled(false);
        buttonUbah.setEnabled(false);
        buttonTambah.setEnabled(true);
        textKodeBarang.setText("");
        textNamaBarang.setText("");
        textKategori.setText("");
        textHarga.setText("");
        textStok.setText("");
        tampil_data();
    }
    
     private void id_otomatis() {
        try {
            sql = "select id_barang from tb_barang order by id_barang desc limit 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                int kode = Integer.parseInt(rs.getString(1).substring(4)) + 1;
                textKodeBarang.setText("BRG-" + kode);
            } else {
                textKodeBarang.setText("BRG-1000");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textNamaBarang = new javax.swing.JTextField();
        textKategori = new javax.swing.JTextField();
        textHarga = new javax.swing.JTextField();
        buttonTambah = new javax.swing.JButton();
        buttonSimpan = new javax.swing.JButton();
        buttonHapus = new javax.swing.JButton();
        buttonUbah = new javax.swing.JButton();
        buttonUlang = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        buttonKeluar = new javax.swing.JButton();
        textKodeBarang = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textStok = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        textCari = new javax.swing.JTextField();

        setBackground(new java.awt.Color(0, 204, 204));
        setClosable(true);
        setTitle("Form Barang");

        textHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textHargaActionPerformed(evt);
            }
        });

        buttonTambah.setText("TAMBAH");
        buttonTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahActionPerformed(evt);
            }
        });

        buttonSimpan.setText("SIMPAN");
        buttonSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimpanActionPerformed(evt);
            }
        });

        buttonHapus.setText("HAPUS");
        buttonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHapusActionPerformed(evt);
            }
        });

        buttonUbah.setText("UBAH");
        buttonUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUbahActionPerformed(evt);
            }
        });

        buttonUlang.setText("ULANG");
        buttonUlang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUlangActionPerformed(evt);
            }
        });

        jLabel1.setText("FORM BARANG");

        jLabel2.setText("KODE BARANG ");

        jLabel3.setText("NAMA BARANG");

        jLabel4.setText("KATEGORI");

        jLabel5.setText("HARGA");

        buttonKeluar.setText("KELUAR");
        buttonKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKeluarActionPerformed(evt);
            }
        });

        jLabel7.setText("STOK");

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelBarang);

        jLabel8.setText("Cari");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(buttonTambah)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonSimpan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonHapus)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonUbah)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonUlang)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonKeluar))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(textHarga, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textNamaBarang, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textKodeBarang, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textKategori, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                                    .addComponent(textCari, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addGap(107, 107, 107)
                        .addComponent(textStok, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(textKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(textHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(textStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonTambah)
                    .addComponent(buttonSimpan)
                    .addComponent(buttonHapus)
                    .addComponent(buttonUbah)
                    .addComponent(buttonKeluar)
                    .addComponent(buttonUlang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textHargaActionPerformed

    private void buttonTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahActionPerformed
        textKodeBarang.setEnabled(false);
        textNamaBarang.setEnabled(true);
        textKategori.setEnabled(true);
        textHarga.setEnabled(true);
        textStok.setEnabled(true);
        buttonTambah.setEnabled(false);
        buttonSimpan.setEnabled(true);
        status = "tambah";
        id_otomatis();

    }//GEN-LAST:event_buttonTambahActionPerformed

    private void buttonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimpanActionPerformed
        if (textKodeBarang.getText().isEmpty() || textNamaBarang.getText().isEmpty() || textHarga.getText().isEmpty()) {
        } else {
            try {
                if (status.equals("tambah")) {
                    sql = "insert into tb_Barang values(?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, textKodeBarang.getText());
                    pst.setString(2, textNamaBarang.getText());
                    pst.setString(3, textKategori.getText());
                    pst.setString(4, textHarga.getText());
                    pst.setString(5, textStok.getText());
                } else if (status.equals("ubah")) {
                    sql = "update tb_barang set nama_barang=?,harga=?,stok=?, where kode_barang=?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, textNamaBarang.getText());
                    pst.setString(2, textHarga.getText());
                    pst.setString(3, textStok.getText());
                    pst.setString(4, textKodeBarang.getText());
                }
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Barang berhasil ditambahkan");
                ulang();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }
    }//GEN-LAST:event_buttonSimpanActionPerformed

    private void buttonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHapusActionPerformed
        int konf = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data pelanggan ini?", "Hapus Data?", JOptionPane.YES_NO_OPTION);
        if (konf == 0) {
            try {
                pst = conn.prepareStatement("delete from tb_Barang where kd_Barang=?");
                pst.setString(1, textKodeBarang.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data barang berhasil dihapus!");
                ulang();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
    }//GEN-LAST:event_buttonHapusActionPerformed

    private void buttonUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUbahActionPerformed
        textKodeBarang.setEnabled(false);
        textNamaBarang.setEnabled(true);
        textKategori.setEnabled(true);
        textHarga.setEnabled(true);
        textStok.setEnabled(true);
        buttonUbah.setEnabled(true);
        buttonSimpan.setEnabled(true);
        buttonHapus.setEnabled(false);
        status = "ubah";
    }//GEN-LAST:event_buttonUbahActionPerformed

    private void buttonUlangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUlangActionPerformed
        ulang();        // TODO add your handling code here:
    }//GEN-LAST:event_buttonUlangActionPerformed

    private void buttonKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKeluarActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_buttonKeluarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonHapus;
    private javax.swing.JButton buttonKeluar;
    private javax.swing.JButton buttonSimpan;
    private javax.swing.JButton buttonTambah;
    private javax.swing.JButton buttonUbah;
    private javax.swing.JButton buttonUlang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTextField textCari;
    private javax.swing.JTextField textHarga;
    private javax.swing.JTextField textKategori;
    private javax.swing.JTextField textKodeBarang;
    private javax.swing.JTextField textNamaBarang;
    private javax.swing.JTextField textStok;
    // End of variables declaration//GEN-END:variables
}
