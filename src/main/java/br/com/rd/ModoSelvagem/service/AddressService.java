package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.AddressDTO;
import br.com.rd.ModoSelvagem.model.dto.CustomerAddressesDTO;
import br.com.rd.ModoSelvagem.model.embeddable.CustomerAddressId;
import br.com.rd.ModoSelvagem.model.entity.Address;
import br.com.rd.ModoSelvagem.model.entity.Customer;
import br.com.rd.ModoSelvagem.model.entity.CustomerAddress;
import br.com.rd.ModoSelvagem.repository.AddressRepository;
import br.com.rd.ModoSelvagem.repository.CustomerAddressRepository;
import br.com.rd.ModoSelvagem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    public AddressDTO createAddress(AddressDTO address, Long customerId){
        Address newAddress = this.dtoToBusiness(address);
        Optional<Customer> opC = customerRepository.findById(customerId);
        if (opC.isPresent()){
            Customer customer = opC.get();
            List<Address> addresses = opC.get().getAddresses();
            addressRepository.save(newAddress);
            addresses.add(newAddress);
            customer.setAddresses(addresses);
            customerRepository.save(customer);
            return this.businessToDto(newAddress);
        }
        return null;
    }

//    public List<AddressDTO> findAllAddresses(){
//        List<Address> addressesList = addressRepository.findAll();
//        return this.listToDTO(addressesList);
//    }

    public List<AddressDTO> listToDTO(List<Address> list){
        List<AddressDTO> listDto = new ArrayList<>();
        for (Address address : list) {
            AddressDTO addressDto = this.businessToDto(address);
            listDto.add(addressDto);
        }
        return listDto;
    }

    public List<Address> listToBusiness(List<AddressDTO> listDto){
        List<Address> list = new ArrayList<>();
        for (AddressDTO addressDto : listDto) {
            Address address = this.dtoToBusiness(addressDto);
            list.add(address);
        }
        return list;
    }

    public AddressDTO searchAddressById(Long id) {
        Optional<Address> op = addressRepository.findById(id);

        if (op.isPresent()){
            return businessToDto(op.get());
        }
        return null;
    }

    public AddressDTO updateById(AddressDTO dto, Long customerId, Long addressId){
        CustomerAddressId id = new CustomerAddressId(customerId, addressId);
        Optional<CustomerAddress> opCA = customerAddressRepository.findById(id);
        Optional<Address> opAd = addressRepository.findById(addressId);

        if (opCA.isPresent()){
            Address obj = opAd.get();

            if(dto.getCep() != null){
                obj.setCep(dto.getCep());
            }
            if(dto.getCity() != null){
                obj.setCity(dto.getCity());
            }
            if(dto.getComplement() != null){
                obj.setComplement(dto.getComplement());
            }
            if(dto.getDistrict() != null){
                obj.setDistrict(dto.getDistrict());
            }
            if(dto.getNumber() != null){
                obj.setNumber(dto.getNumber());
            }

            if(dto.getStreet() != null){
                obj.setStreet(dto.getStreet());
            }

            if(dto.getUf() != null){
                obj.setUf(dto.getUf());
            }

            addressRepository.save(obj);
            return  businessToDto(obj);

        }
        return null;
    }

    public void deleteById(Long customerId, Long addressId) {
        CustomerAddressId id = new CustomerAddressId(customerId, addressId);
        Optional<CustomerAddress> opCA = customerAddressRepository.findById(id);
        if (opCA.isPresent()){
                customerAddressRepository.deleteById(id);
                addressRepository.deleteById(addressId);
        }
    }

    public Address dtoToBusiness(AddressDTO dto) {
        Address business = new Address();
        business.setId(dto.getId());
        business.setCity(dto.getCity());
        business.setCep(dto.getCep());
        business.setComplement(dto.getComplement());
        business.setDistrict(dto.getDistrict());
        business.setNumber(dto.getNumber());
        business.setStreet(dto.getStreet());
        business.setUf(dto.getUf());
        return business;
    }

    public AddressDTO businessToDto(Address business) {
        AddressDTO dto = new AddressDTO();
        dto.setId(business.getId());
        dto.setCity(business.getCity());
        dto.setCep(business.getCep());
        dto.setComplement(business.getComplement());
        dto.setDistrict(business.getDistrict());
        dto.setNumber(business.getNumber());
        dto.setStreet(business.getStreet());
        dto.setUf(business.getUf());
        return dto;
    }

    public CustomerAddressesDTO findAllCustomerAddresses(Long customerId){
        Optional<Customer> op = customerRepository.findById(customerId);

        if(op.isPresent()){
            Customer customer = op.get();
            CustomerAddressesDTO dto = new CustomerAddressesDTO();
            dto.setCustomerId(customer.getId());
            dto.setCustomerName(customer.getName());

            if(!customer.getAddresses().isEmpty()){
                dto.setAddresses(this.listToDTO(customer.getAddresses()));
            }
            return dto;
        }
        return null;
    }

    public AddressDTO findCustomerAddressById(Long customerId, Long addressId){
        CustomerAddressId id = new CustomerAddressId(customerId, addressId);
        Optional<CustomerAddress> opCA = customerAddressRepository.findById(id);
        Optional<Address> opAd = addressRepository.findById(addressId);

        if(opCA.isPresent()){
            Address address = opAd.get();
            AddressDTO dto = new AddressDTO();
            dto = businessToDto(address);
            return dto;
            }
        return null;
    }
}
