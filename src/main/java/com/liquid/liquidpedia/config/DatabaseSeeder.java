package com.liquid.liquidpedia.config;

import com.liquid.liquidpedia.entity.*;
import com.liquid.liquidpedia.entity.enums.Role;
import com.liquid.liquidpedia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProdukRepository produkRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;

        Category liquid = new Category();
        liquid.setCategoryName("Liquid");
        liquid.setDeskripsi("Berbagai macam liquid vape dengan rasa favorit");
        categoryRepository.save(liquid);

        Category vape = new Category();
        vape.setCategoryName("Vape Device");
        vape.setDeskripsi("Device vape original dan berkualitas");
        categoryRepository.save(vape);

        String[][] produkData = {
            {"Mango Ice", "150000", "50", "Liquid mango dengan sensasi ice yang menyegarkan", "true", "true", "Liquid"},
            {"Strawberry Lychee", "135000", "45", "Perpaduan stroberi dan lychee yang manis", "true", "false", "Liquid"},
            {"Blueberry Mint", "140000", "60", "Blueberry segar dengan sentuhan mint", "false", "true", "Liquid"},
            {"Grape Blast", "145000", "55", "Ledakan rasa anggur yang intense", "true", "false", "Liquid"},
            {"Pod System X1", "250000", "30", "Pod system portable dengan battery 1000mAh", "true", "true", "Vape Device"},
            {"Starter Kit Pro", "450000", "20", "Starter kit lengkap untuk pemula", "true", "false", "Vape Device"},
            {"Mechanical Mod V2", "350000", "15", "Mechanical mod dengan desain elegan", "false", "true", "Vape Device"},
            {"Disposable Vape Lite", "85000", "100", "Disposable vape praktis 6000 puff", "true", "true", "Vape Device"},
            {"Lychee Ice", "140000", "40", "Lychee ice segar untuk vape harian", "false", "false", "Liquid"},
            {"Cotton Candy", "130000", "35", "Rasa cotton candy manis dan lembut", "false", "false", "Liquid"},
            {"RDA Atomizer X", "200000", "25", "RDA atomizer dengan airflow besar", "false", "true", "Vape Device"},
            {"Coil Pack 5pcs", "75000", "80", "Coil replacement 0.2 ohm 5 pcs", "true", "false", "Vape Device"}
        };

        for (String[] data : produkData) {
            Produk produk = new Produk();
            produk.setNameProduk(data[0]);
            produk.setPrice(Double.parseDouble(data[1]));
            produk.setStock(Integer.parseInt(data[2]));
            produk.setDeskripsi(data[3]);
            produk.setIsBestSeller(Boolean.parseBoolean(data[4]));
            produk.setIsNewArrival(Boolean.parseBoolean(data[5]));
            if ("Liquid".equals(data[6])) {
                produk.setCategory(liquid);
            } else {
                produk.setCategory(vape);
            }
            produkRepository.save(produk);
        }

        Voucher voucher = new Voucher();
        voucher.setKodeVoucher("LIQUID10");
        voucher.setDiscount(0.10);
        voucher.setMinPurchase(100000);
        voucher.setMaxDiscount(50000.0);
        voucher.setStock(100);
        voucher.setExpiredAt(LocalDate.of(2027, 12, 31));
        voucherRepository.save(voucher);

        if (!userRepository.existsByEmail("admin@liquidpedia.com")) {
            Admin admin = new Admin();
            admin.setEmail("admin@liquidpedia.com");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRole(Role.ADMIN);
            admin.setNameAdmin("Admin LiquidPedia");
            userRepository.save(admin);
        }
    }
}
