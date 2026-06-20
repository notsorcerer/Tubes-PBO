package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.AddressDto;
import com.liquid.liquidpedia.entity.Address;
import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAddressesByCustomer(Customer customer) {
        return addressRepository.findByCustomerId(customer.getId());
    }

    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Alamat tidak ditemukan"));
    }

    @Transactional
    public Address save(Customer customer, AddressDto dto) {
        if (dto.isDefault()) {
            addressRepository.resetDefaultByCustomerId(customer.getId());
        }
        Address address = new Address();
        address.setCustomer(customer);
        address.setLabel(dto.getLabel());
        address.setReceiverName(dto.getReceiverName());
        address.setPhoneNum(dto.getPhoneNum());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setPosCode(dto.getPosCode());
        address.setIsDefault(dto.isDefault());
        return addressRepository.save(address);
    }

    @Transactional
    public Address update(Customer customer, AddressDto dto) {
        Address address = findById(dto.getIdAddress());
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Alamat tidak ditemukan");
        }
        if (dto.isDefault()) {
            addressRepository.resetDefaultByCustomerId(customer.getId());
        }
        address.setLabel(dto.getLabel());
        address.setReceiverName(dto.getReceiverName());
        address.setPhoneNum(dto.getPhoneNum());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setPosCode(dto.getPosCode());
        address.setIsDefault(dto.isDefault());
        return addressRepository.save(address);
    }

    @Transactional
    public void delete(Customer customer, Long addressId) {
        Address address = findById(addressId);
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Alamat tidak ditemukan");
        }
        addressRepository.delete(address);
    }

    @Transactional
    public void setDefault(Customer customer, Long addressId) {
        addressRepository.resetDefaultByCustomerId(customer.getId());
        Address address = findById(addressId);
        address.setIsDefault(true);
        addressRepository.save(address);
    }
}
