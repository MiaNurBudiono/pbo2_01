/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toko.komputer.transaksi;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import toko.komputer.barang.CariBarangView;
import toko.komputer.pengaturan.Koneksi;

/**
 *
 * @author Mia
 */
public class TransaksiView extends javax.swing.JInternalFrame {

    /**
     * Creates new form TransaksiView
     */
    public TransaksiView() {
        initComponents();
        data_pelanggan();
        ulang();

    }
    PreparedStatement pst;
    ResultSet rs;
    Connection conn = new Koneksi().getKoneksi();
    String sql;
    DefaultTableModel dtm;

    private void nota_otomatis() {
        try {
            sql = "select no_nota from tb_penjualan order by no_nota desc limit 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                int kode = Integer.parseInt(rs.getString(1).substring(4)) + 1;
                textNota.setText("NTA-" + kode);
            } else {
                textNota.setText("NTA-1000");
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    private void data_pelanggan() {
        try {
            comboPelanggan.removeAllItems();
            comboPelanggan.addItem("Pilih Pelanggan");
            pst = conn.prepareStatement("select nama_pelanggan from tb_pelanggan");
            rs = pst.executeQuery();
            while (rs.next()) {
                comboPelanggan.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    private void ulang() {
        nota_otomatis();
        comboPelanggan.setSelectedIndex(0);
        textNota.setEnabled(false);
        textIDPelanggan.setEnabled(false);
        textKodeBarang.setEnabled(false);
        textNamaBarang.setEnabled(false);
        textKategori.setEnabled(false);
        textHarga.setEnabled(false);
        textStok.setEnabled(false);
        textTotal.setEnabled(false);
        textIDPelanggan.setText("");
        textKodeBarang.setText("");
        textKategori.setText("");
        textHarga.setText("");
        textStok.setText("");
        textQty.setText("");
        textTotal.setText("");
        textBayar.setText("");
        textKembali.setEnabled(false);
        dtm = (DefaultTableModel) tabelItemBelanja.getModel();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
    }

    private void hitung_total() {
        BigDecimal total = new BigDecimal(0);
        for (int a = 0; a < tabelItemBelanja.getRowCount(); a++) {
            total = total.add(new BigDecimal(tabelItemBelanja.getValueAt(a, 5).toString()));
        }
        textTotal.setText(total.toString());
    }

    private boolean validasi() {
        boolean cek = false;
        java.util.Date tgl = textTanggal.getDate();
        if (tgl == null) {
            JOptionPane.showMessageDialog(null, "Tanggal Transaksi Belum Diisi!", null, JOptionPane.ERROR_MESSAGE);
            textTanggal.requestFocus();
        } else if (textIDPelanggan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Data Pelanggan Belum Dipilih", null, JOptionPane.ERROR_MESSAGE);
            comboPelanggan.requestFocus();
        } else if (tabelItemBelanja.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Data barang Belanja Masih Kosong!", null, JOptionPane.ERROR_MESSAGE);
            buttonCariBarang.requestFocus();
        } else if (textBayar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Textbox Bayar Belum Diisi!", null, JOptionPane.ERROR_MESSAGE);
            textBayar.requestFocus();
        } else {
            cek = true;
        }
        return cek;
    }

    private void simpan_transaksi() {
        if (validasi()) {
            try {
                java.util.Date d = textTanggal.getDate();
                java.sql.Date tgl = new java.sql.Date(d.getTime());
                pst = conn.prepareStatement("insert into tb_penjualan values (?,?,?,?,?,?)");
                pst.setString(1, textNota.getText());
                pst.setString(2, tgl.toString());
                pst.setString(3, textIDPelanggan.getText());
                pst.setBigDecimal(4, new BigDecimal(textTotal.getText()));
                pst.setBigDecimal(5, new BigDecimal(textBayar.getText()));
                pst.setBigDecimal(6, new BigDecimal(textKembali.getText()));
                int isSuccess = pst.executeUpdate();
                if (isSuccess == 1){
                simpan_item_belanja();
                }
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
                ulang();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada Simpan Transaksi Details \n" + ex.toString());
            }
        }
    }

    private void simpan_item_belanja() {
        for (int a = 0; a <= tabelItemBelanja.getRowCount() - 1; a++) {
            try {
                pst = conn.prepareStatement("insert into tb_detail_pejualan(no_nota,kode_barang,qty)values(?,?,?)");
                String kode;
                int jumlah;
                kode = tabelItemBelanja.getValueAt(a, 0).toString();
                jumlah = Integer.parseInt(tabelItemBelanja.getValueAt(a, 4).toString());
                pst.setString(1, textNota.getText());
                pst.setString(2, kode);
                pst.setInt(3, jumlah);
                pst.executeUpdate();
                update_stok(kode, jumlah);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi Kesalahan pada Simpan Item Belanja:Details\n" + ex.toString());
            }
        }
    }

    private void update_stok(String kode, int jumlah) {
        try {
            sql = "update tb_barang set stok=stok-?where kode_barang=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, jumlah);
            pst.setString(2, kode);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan pada Update Stok:" + ex.toString());
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textNota = new javax.swing.JTextField();
        textKodeBarang = new javax.swing.JTextField();
        textNamaBarang = new javax.swing.JTextField();
        textKategori = new javax.swing.JTextField();
        comboPelanggan = new javax.swing.JComboBox<>();
        textTanggal = new com.toedter.calendar.JDateChooser();
        textIDPelanggan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        textHarga = new javax.swing.JTextField();
        textStok = new javax.swing.JTextField();
        textQty = new javax.swing.JTextField();
        buttonTambah = new javax.swing.JButton();
        buttonCariBarang = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelItemBelanja = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        buttonBatal = new javax.swing.JButton();
        buttonSimpan = new javax.swing.JButton();
        textTotal = new javax.swing.JTextField();
        textBayar = new javax.swing.JTextField();
        textKembali = new javax.swing.JTextField();
        buttonHapus = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("FORM TRANSAKSI");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("NO.NOTA");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("NAMA PELANGGAN");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("KODE BARANG");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("NAMA BARANG");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("KATEGORI");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("TANGGAL TRANSAKSI");

        comboPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPelangganActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("HARGA");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("STOK");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("JUMLAH BELI");

        buttonTambah.setText("TAMBAH");
        buttonTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahActionPerformed(evt);
            }
        });

        buttonCariBarang.setText("CARI");
        buttonCariBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCariBarangActionPerformed(evt);
            }
        });

        tabelItemBelanja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Barang", "Nama Barang", "Kategori", "Harga", "Qty", "Sub Total"
            }
        ));
        jScrollPane1.setViewportView(tabelItemBelanja);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("TOTAL");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("BAYAR");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("KEMBALI");

        buttonBatal.setText("BATAL");

        buttonSimpan.setText("SIMPAN");

        buttonHapus.setText("Hapus");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textNota, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textNamaBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addComponent(textKodeBarang, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textKategori))
                                .addGap(18, 18, 18)
                                .addComponent(buttonCariBarang)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)))
                            .addComponent(comboPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textHarga)
                            .addComponent(textIDPelanggan)
                            .addComponent(textTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textStok)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(textQty, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(buttonTambah)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addComponent(textBayar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(buttonBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(buttonSimpan))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(164, 164, 164)
                                    .addComponent(buttonHapus)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(textKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(textNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textIDPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(textKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonCariBarang)
                                    .addComponent(jLabel8)
                                    .addComponent(textHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addComponent(buttonTambah))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(textNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(textKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10)
                                        .addComponent(textQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(1, 1, 1))
                    .addComponent(textTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(textTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonHapus))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(textBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(textKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonBatal)
                    .addComponent(buttonSimpan))
                .addGap(243, 243, 243))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPelangganActionPerformed
        try {
            pst = conn.prepareStatement("select id_pelanggan from tb_pelanggan where nama_pelanggan=?");
            pst.setString(1, comboPelanggan.getSelectedItem().toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                textIDPelanggan.setText(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiView.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_comboPelangganActionPerformed

    private void buttonCariBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCariBarangActionPerformed
        CariBarangView cbv = new CariBarangView(null, true);
        cbv.setVisible(true);
    }//GEN-LAST:event_buttonCariBarangActionPerformed

    private void buttonTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahActionPerformed
        if (textKodeBarang.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Data barang belum dipilih");
        } else if (textQty.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Jumlah beli belum diisi");
        } else if (Integer.parseInt(textQty.getText()) > Integer.parseInt(textStok.getText())) {
            JOptionPane.showMessageDialog(null, "Stok barang tidak cukup!");
            textQty.setText("0");
            textQty.requestFocus();
        } else {
            dtm = (DefaultTableModel) tabelItemBelanja.getModel();
            ArrayList list = new ArrayList();
            list.add(textKodeBarang.getText());
            list.add(textNamaBarang.getText());
            list.add(textKategori.getText());
            list.add(textHarga.getText());
            list.add(textQty.getText());
            list.add(Integer.parseInt(textHarga.getText()) * Integer.parseInt(textQty.getText()));
            dtm.addRow(list.toArray());
            textKodeBarang.setText("");
            textNamaBarang.setText("");
            textKategori.setText("");
            textHarga.setText("");
            textStok.setText("");
            textQty.setText("");
            hitung_total();
        }

// TODO add your handling code here:
    }//GEN-LAST:event_buttonTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBatal;
    private javax.swing.JButton buttonCariBarang;
    private javax.swing.JButton buttonHapus;
    private javax.swing.JButton buttonSimpan;
    private javax.swing.JButton buttonTambah;
    private javax.swing.JComboBox<String> comboPelanggan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelItemBelanja;
    private javax.swing.JTextField textBayar;
    public static javax.swing.JTextField textHarga;
    private javax.swing.JTextField textIDPelanggan;
    public static javax.swing.JTextField textKategori;
    private javax.swing.JTextField textKembali;
    public static javax.swing.JTextField textKodeBarang;
    public static javax.swing.JTextField textNamaBarang;
    private javax.swing.JTextField textNota;
    private javax.swing.JTextField textQty;
    public static javax.swing.JTextField textStok;
    private com.toedter.calendar.JDateChooser textTanggal;
    private javax.swing.JTextField textTotal;
    // End of variables declaration//GEN-END:variables
}
