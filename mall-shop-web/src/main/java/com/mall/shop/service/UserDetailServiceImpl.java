package com.mall.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mall.pojo.TbSeller;
import com.mall.sellergoods.service.SellerService;
/**
 * ��֤��
 * @author Administrator
 *
 */

public class UserDetailServiceImpl implements UserDetailsService {
	
	@Reference
	private SellerService sellerService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("������UserDetailsServiceImpl,�û���="+username);
		//������ɫ�б�
		List<GrantedAuthority> grantAuths=new ArrayList();
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		//�õ��̼Ҷ���
		TbSeller seller = sellerService.findOne(username);
		System.out.println(seller);
		if(seller!=null){
			if(seller.getStatus().equals("1")){
				return new User(username,seller.getPassword(),grantAuths);
			}else{
				return null;
			}			
		}else{
			return null;
		}
	}
}
