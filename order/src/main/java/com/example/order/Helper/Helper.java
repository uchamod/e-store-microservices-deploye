package com.example.order.Helper;

import com.example.order.DTO.EmailDTO;
import com.example.order.Model.OrderProductModel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class Helper {

    public List<EmailDTO> createEmailDTO(List<OrderProductModel> orderProductModels){
        try{
            List<EmailDTO> emailDTOList=new ArrayList<>();
            for(int i=0; i < orderProductModels.size();i++){
                EmailDTO emailDTO=new EmailDTO();
                emailDTO.setTotalAmount(0.0);
                UUID sellerId= orderProductModels.getFirst().getSellerId();
                List<OrderProductModel> orderProductModelList=new ArrayList<>();
                emailDTO.setSellerId(sellerId);
                for(int j=0;j < orderProductModels.size();j++){
                    if(orderProductModels.get(j).getSellerId().equals(sellerId)){
                       orderProductModelList.add(orderProductModels.get(j));

                        emailDTO.setTotalAmount(emailDTO.getTotalAmount()+
                                orderProductModels.get(j).getProductCount()*
                                orderProductModels.get(j).getProductItemPrice());

                    }

                }
                orderProductModels.removeIf(orderProductModel -> orderProductModel.getSellerId().equals(sellerId));
                emailDTO.setOrderProductModelList(orderProductModelList);
                emailDTOList.add(emailDTO);
            }
                return emailDTOList;
        }catch (Exception e){
                System.out.println("error while selection products "+e.getMessage());
                return null;
        }
    }
}
