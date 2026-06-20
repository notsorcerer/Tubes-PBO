package com.liquid.liquidpedia.dto;

import java.util.List;

public class DashboardStats {

    private long totalProduk;
    private long totalKategori;
    private long totalBestSeller;
    private long totalNewArrival;
    private List<ProdukPerCategory> produkPerCategory;

    public long getTotalProduk() { return totalProduk; }
    public void setTotalProduk(long totalProduk) { this.totalProduk = totalProduk; }
    public long getTotalKategori() { return totalKategori; }
    public void setTotalKategori(long totalKategori) { this.totalKategori = totalKategori; }
    public long getTotalBestSeller() { return totalBestSeller; }
    public void setTotalBestSeller(long totalBestSeller) { this.totalBestSeller = totalBestSeller; }
    public long getTotalNewArrival() { return totalNewArrival; }
    public void setTotalNewArrival(long totalNewArrival) { this.totalNewArrival = totalNewArrival; }
    public List<ProdukPerCategory> getProdukPerCategory() { return produkPerCategory; }
    public void setProdukPerCategory(List<ProdukPerCategory> produkPerCategory) { this.produkPerCategory = produkPerCategory; }

    public static class ProdukPerCategory {
        private String namaKategori;
        private long jumlahProduk;

        public ProdukPerCategory() {}
        public ProdukPerCategory(String namaKategori, long jumlahProduk) {
            this.namaKategori = namaKategori;
            this.jumlahProduk = jumlahProduk;
        }

        public String getNamaKategori() { return namaKategori; }
        public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }
        public long getJumlahProduk() { return jumlahProduk; }
        public void setJumlahProduk(long jumlahProduk) { this.jumlahProduk = jumlahProduk; }
    }
}
